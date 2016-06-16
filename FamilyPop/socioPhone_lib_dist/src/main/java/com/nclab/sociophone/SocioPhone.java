package com.nclab.sociophone;

import android.bluetooth.BluetoothA2dp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

import com.nclab.sociophone.handler.DisplayHandler;
import com.nclab.sociophone.handler.SignalHandler;
import com.nclab.sociophone.interfaces.DisplayInterface;
import com.nclab.sociophone.interfaces.EventDataListener;
import com.nclab.sociophone.interfaces.MeasurementCallback;
import com.nclab.sociophone.interfaces.TurnDataListener;
import com.nclab.sociophone.network.NetworkManager;
import com.nclab.sociophone.processors.SoundProcessManager;
import com.nclab.sociophone.processors.VolumeWindow;
import com.nclab.sociophone.record.AudioRecorderService;
import com.nclab.sociophone.record.RecordProcessThread;

/**
 * SocioPhone
 *
 * @author Chanyou
 */
public class SocioPhone
{
    public static  SocioPhone Instance;

    private SignalHandler sHandler;
    private DisplayHandler dHandler;
    private NetworkManager mNetworkManager;
    private AudioManager mAudioManager;
    public Context mContext;
    private boolean isOrderMode = true, isAllowingOverlap = false, isUsingBluetoothHeadset = false;
    private RecordProcessThread recordThread;
    private SoundProcessManager processManager;
    public TurnDataListener turnInterface;
    public EventDataListener eventDataInterface;
    public DisplayInterface displayInterface;
    public static boolean isServer = false;
    private int myId = 0;
    private long _record_start_time = 0;
    private ContextAPI CAPI;
    private Intent audioRecordIntent;
    private AudioRecorderService _audioRecordService;

    /**
     * @param context          The context of an activity
     * @param ti               A class to get the turn data
     * @param di               A class to get the misc data
     * @param bluetoothHeadset Set true if you want to use bluetooth headset (NOT STABLE!)
     */
    public SocioPhone(Context context, TurnDataListener ti, DisplayInterface di, EventDataListener ei, boolean bluetoothHeadset)
    {
        mContext = context;

        turnInterface = ti;
        displayInterface = di;
        eventDataInterface = ei;
        sHandler = new SignalHandler(this, mNetworkManager);
        dHandler = new DisplayHandler(this);
        mNetworkManager = new NetworkManager(sHandler, dHandler);
        sHandler.setNetworkManager(mNetworkManager);

        if (!bluetoothHeadset) {
            //recordThread = new RecordProcessThread(sHandler, true);
        } else {
            mContext.registerReceiver(btHeadsetStatusRecevicer, new IntentFilter(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED));

            mContext.registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    int state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
                    //Log.d("MYTAG", "Audio SCO state: " + state);
                    if (AudioManager.SCO_AUDIO_STATE_CONNECTED == state) {
                        mContext.unregisterReceiver(this);
                        //Toast.makeText(mContext, "Bluetooth Headset Ready", Toast.LENGTH_SHORT).show();
                    }
                }

            }, new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED));
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.startBluetoothSco();
            isUsingBluetoothHeadset = true;

        }

        CAPI = new ContextAPI(mContext);


        Instance = this;
    }

    /**
     * @param mode true : volume order mode, false : volume topography mode
     */
    public void setVolumeOrderMode(boolean mode) {
        isOrderMode = mode;
    }

    public void setAllowOverlap(boolean mode) {
        isAllowingOverlap = mode;
    }


    /**
     * Connect to server
     *
     * @param targetIP target IP if it attemps to connect via TCP
     */
    public void connectToServer(String targetIP) {
        mNetworkManager.connect(targetIP);
    }

    /**
     * Open server socket
     */
    public void openServer() {
        isServer = true;
        mNetworkManager.openServer();

    }

    /**
     * Start recording. It will be started 3 sec later.
     *
     * @return Return true if it is working.
     */
    public boolean startRecord() {
        if (isServer) {
            startRecord(0, "temp");
            return true;
        }
        return false;
    }



    /**
     * (Don't use this)
     * TODO : Hide this method from users
     *
     * @param time
     */
    public void startRecord(long time) {
        startRecord(time, "");
    }

    /**
     * (Don't use this)
     * <p/>
     * TODO : Hide this method from users<br>
     * <p/>
     * Start recording!
     * Recording will be started at 3 sec later after server requested
     *
     * @param time     Start time of record.
     * @param filename Filename of the recorded file.
     */

//kookm0614
    public void RegisterQuery(){
        boolean isSucceeded = CAPI.registerQuery("GetVolume 50 50 0");
    }

    public void startRecord(long time, String filename)
    {
        Log.i("[J2Y]", "[SocioPhone] startRecord ");


        long ctime;
        if (isServer)
        {
            SocioPhoneConstants.id = 1;

            //Initialize process Manager
            processManager = new SoundProcessManager(sHandler, mNetworkManager.getNumOfUser() + 1, isOrderMode, isAllowingOverlap);

            // Send start record signal : start time (currenttime + 3sec)
            ctime = System.currentTimeMillis() + 3000;
            displayInterface.onDisplayMessageArrived(0, "TS Server: " + System.currentTimeMillis());
            sendMessage("/1:" + ctime + "," + filename);

        }
        else
        {
            ctime = time;
            displayInterface.onDisplayMessageArrived(0, "TS Client: " + (System.currentTimeMillis() + SocioPhoneConstants.deviceTimeOffset));
        }

        recordThread = new RecordProcessThread(sHandler, true, filename, mContext);
        recordThread.setCheckPoint(ctime);
        recordThread.start_record(); // get volume


        // Start Record  ----------------- 6/9
        _record_start_time = System.currentTimeMillis();
        // FIXME start AudioRecorderService. It registers rec query
        Log.i("JSIM", "start audioRecorderService");

        audioRecordIntent = new Intent("com.nclab.sociophone.record.AudioRecorderService");
        _audioRecordService = new AudioRecorderService();
        //audioRecordIntent.setClass(mContext, AudioRecorderService.class);
        audioRecordIntent.setClass(mContext, _audioRecordService.getClass());
        audioRecordIntent.putExtra("sociophone request", "startService");
        mContext.startService(audioRecordIntent);   // start audio record

        // ---------------- 6/9
    }

    /**
     * Stop recording!
     * Recording will be immediately stopped
     */
    public void stopRecord()
    {
        Log.i("[J2Y]", "[SocioPhone] stopRecord ");

        if (isServer) {
            // Send stop record signal
            sendMessage("/2:");
        }
        if (recordThread != null)
        {
            // [J2Y]
            recordThread.stopRecord();

        }
        CAPI.deregisterQuery("GetVolume");
        // FIXME stop AudioRecorderService. It deregisters rec query
        audioRecordIntent = new Intent("com.nclab.sociophone.record.AudioRecorderService");

        audioRecordIntent.setClass(mContext, AudioRecorderService.class);
        //audioRecordIntent.putExtra("sociophone request", "stopService");
        mContext.stopService(audioRecordIntent);
    }
    public void recordRelease()
    {
        if (recordThread != null)
        {
            // [J2Y]
            recordThread.stopRecord();
            recordThread = null;
        }
    }
    /**
     * @param isServer true if this device is server
     */
    public void setIsServer(boolean isServer) {
        SocioPhone.isServer = isServer;
    }

    /*
     * Get my speakerID
     */
    public int getMyId() {
        return SocioPhoneConstants.id;
    }

    public String GetFilePath() {
        return recordThread.GetFilePath();
    }

    public String GetWavFileName()
    {
        return (_audioRecordService != null) ? _audioRecordService.getWaveFilePath() : "";
        //return (recordThread != null) ? recordThread.GetWavFileName() : "";
    }

    public double GetSoundAmplitue() {
        return (recordThread != null) ? recordThread.GetSoundAmplitue() : 0.0;
    }

    public long GetRecordTime()
    {
        return (System.currentTimeMillis() - _record_start_time);
    }

    /**
     * Send message to the opposite devices
     *
     * @param message
     */
    private void sendMessage(String message) {
        if (isServer)
            mNetworkManager.sendToClients(message);
        else
            mNetworkManager.sendToServer(message);
    }


    /**
     * Set network mode
     * :: Please do not call this after any connection is established. This framework doesn't handle that case. It could make a bug!
     *
     * @param wifi True if you wish to use wifi
     */
    public void setNetworkMode(boolean wifi) {
        mNetworkManager.setWifiMode(wifi);
    }

    /**
     * Only for server device!
     * TODO : Hide this method from users
     *
     * @param window volume window
     * @param from   where is it from
     */

    public void onDataReceived(VolumeWindow window, int from) {
        // Push it into soundProcessManager
        //Log.d("MYTAG", "from : " + from +", "+ window.power);
        processManager.addData(from, window.power, window.timestamp);

    }

    /**
     * (Only for overlap mode)
     *
     * @param idx       index of device
     * @param threshold new volume threshold of device
     */

    public void setThresholdValue_O(int idx, double threshold) {
        if (processManager != null)
            processManager.thresholds[idx] = threshold;
    }

    /**
     * (Only for overlap mode)
     *
     * @param thresholdRatio
     */
    public void setDThresholdValue(double thresholdRatio)
    {
        if (processManager != null)
            processManager.dThresholdRatio = thresholdRatio;
    }

    /**
     * 15_07_02 메일 2015-06-29 자 함수 추가
     */
    /**
     * (For volume order mode)
     *
     * @param threshold  threshold value (Default : 100000) seekBar 0
     */
    public void setSilenceVolThreshold(double threshold)
    {
        if(processManager != null) {
            //processManager.volThreshold = threshold;
            processManager.volThreshold = threshold * 1.50;
        }
    }
    /**
     * (For volume order mode)
     *
     * @param threshold threshold value (Default : 1,000,000,000) seekBar 1
     */
    public void setSilenceVolVarThreshold(double threshold)
    {
        if(processManager != null) {
            //processManager.varianceThreshold = threshold;
            processManager.varianceThreshold = threshold * 1.50;
        }
    }

    // end 15_07_02
    public void reset() {

    }

    public void destroy()
    {
        mNetworkManager.destroy();
        if (recordThread != null) {
            recordThread.stopRecord();
        }
        if (isUsingBluetoothHeadset) {
            mContext.unregisterReceiver(btHeadsetStatusRecevicer);
            mAudioManager.stopBluetoothSco();
        }
    }

    BroadcastReceiver btHeadsetStatusRecevicer = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int status = arg1.getIntExtra(BluetoothA2dp.EXTRA_STATE, 0);
            if (status == BluetoothA2dp.STATE_CONNECTED) {

                mAudioManager.startBluetoothSco();
            } else if (status == BluetoothA2dp.STATE_DISCONNECTED) {
                mAudioManager.stopBluetoothSco();
            }
        }

    };

    // Addition by Jeungmin Oh starts
    public void measureSilenceVolThreshold(int timeInMs, MeasurementCallback callback){
        if(processManager != null) {
            processManager.measureSilenceVol(timeInMs, callback);
            Log.i("Calibration", "measureSilenceVol()");
        } else {
            Log.i("Calibration", "ProcessManager is null");
        }

    }

    public void measureSilenceVolVarThreshold(int timeInMs, MeasurementCallback callback) {
        if (processManager != null) {
            processManager.measureSilenceVolVar(timeInMs, callback);
            Log.i("Calibration", "measureSilenceVolVar()");

        } else {
            Log.i("Calibration", "ProcessManager is null");
        }
    }

    // Addition by Jeungmin Oh ends

}
