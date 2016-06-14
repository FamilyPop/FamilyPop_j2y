package com.j2y.familypop.activity.server;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.j2y.familypop.MainActivity;
import com.j2y.familypop.backup.Dialog_MessageBox_ok_cancel;
import com.j2y.familypop.server.FpsRoot;
import com.j2y.familypop.server.FpsTalkUser;
import com.nclab.familypop.R;
import com.nclab.sociophone.interfaces.MeasurementCallback;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
// Activity_serverStart
//
//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class Activity_serverStart extends Activity
{
    public static Activity_serverStart Instance = null;
    TextView _serverIP;
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Instance = this;
        super.onCreate(savedInstanceState);
        Log.i("[J2Y]", "Activity_serverStart:onCreate");

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 세로 고정
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 가로 고정
        //setContentView(R.layout.activity_dialogue_start_server);
        setContentView(R.layout.activity_dialogue_start_server_sample);


        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ipAddressStr = String.format("%d.%d.%d.%d",(ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        _serverIP = (TextView) findViewById(R.id.ServerIPText);
        _serverIP.setText(ipAddressStr);


        // maanager

        // net
        FpsRoot.Instance.CloseServer();
        FpsRoot.Instance.StartServer();
        //
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(Activity_serverMain.this, "Calibration started", Toast.LENGTH_SHORT).show();

                FpsRoot.Instance._socioPhone.measureSilenceVolThreshold(5000, new MeasurementCallback() {
                    @Override
                    public void done(int result) {
                        FpsRoot.Instance._socioPhone.setSilenceVolThreshold(result * 1.5);
                        //Toast.makeText(Activity_serverMain.this, "Clibration completed 1", Toast.LENGTH_SHORT).show();
                        Log.i("JeungminOh", "Calibration completed");
                    }
                });

                FpsRoot.Instance._socioPhone.measureSilenceVolVarThreshold(5000, new MeasurementCallback() {
                    @Override
                    public void done(int result) {
                        FpsRoot.Instance._socioPhone.setSilenceVolVarThreshold(result * 1.5);
                        //Toast.makeText(Activity_serverMain.this, "Clibration completed 2", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 5000);

        if(Build.VERSION.SDK_INT > 10){ StrictMode.enableDefaults();}

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }
    private void onClick_start()
    {
        Dialog_MessageBox_ok_cancel megBox = new Dialog_MessageBox_ok_cancel(this, "OK", "SKIP")
        {
            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);

                _content.setText("Would you like to renew the calibration?");
                _editText.setVisibility(View.INVISIBLE);

            }
            @Override
            public void onClick(View v)
            {
                super.onClick(v);

                switch (v.getId())
                {
                    case R.id.button_custom_dialog_ok: // ok

                        // ~�̸��극�̼� ����
                        startActivity(new Intent(MainActivity.Instance, Activity_serverCalibrationLocation.class));
                        finish();

                        break;
                    case R.id.button_custom_dialog_cancel: //skip

                        // ���� ����
                        //startActivity(new Intent(MainActivity.Instance, Activity_serverMain.class));

                        break;
                }

            }

        };

        megBox.show();

    }

//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    public void ChangeProcessingActivity()
//    {
//
//        new Thread()
//        {
//            @Override
//            public void run()
//            {
//                while(true)
//                {
//                    if(FpNetFacade_server.Instance.IsConnected())
//                    {
//                        startActivity(new Intent(MainActivity.Instance, Activity_serverMain.class));
//                        return;
//                    }
//                }
//            }
//        }.start();
//    }
}
