package com.j2y.familypop;

import android.app.ActivityManager;
import android.content.Intent;

import com.facebook.share.widget.MessageDialog;
import com.j2y.familypop.activity.Activity_clientMain;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.lobby.Activity_title;
import com.j2y.familypop.activity.manager.Manager_contents;
import com.j2y.familypop.activity.manager.Manager_photoGallery;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.client.FpcLocalization_Client;
import com.j2y.familypop.client.FpcRoot;
import com.j2y.familypop.client.FpcTalkRecord;
import com.j2y.familypop.server.FpsRoot;

import com.j2y.network.base.FpNetConstants;

import com.nclab.familypop.R;
import com.nclab.sociophone.SocioPhone;
import com.nclab.sociophone.handler.SignalHandler;
import com.nclab.sociophone.interfaces.DisplayInterface;
import com.nclab.sociophone.interfaces.EventDataListener;
import com.nclab.sociophone.interfaces.TurnDataListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Contacts;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
//import com.facebook.FacebookSdk;
//import com.facebook.LoggingBehavior;

import static android.provider.Contacts.Settings.*;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
// MainActivity
//
//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class MainActivity extends Activity
{
	public static MainActivity Instance;
	public FpsRoot _fpsRoot;
	public FpcRoot _fpcRoot;
    public FpcLocalization_Client _localization;
    public Editable _serverIP;
    public Editable _userName;
    public boolean _serverActivityStart = false;

    public double _calibration_width_length;
    public double _calibration_height_length;

	public boolean _virtualServer;

    public SharedPreferences _familypopSetting;

    //debug
    public String _deviceRole;
    public float _deviceRotation = 0.0f;
    public float _rotationAngle = 90.0f;

    // # localization locator 역할용 클라이언트.


    //client init info
    public boolean _ready;
    public Manager_contents.eType_contents _curServerScenario;  // server -> client

    //Manager
    public Manager_photoGallery _photoManager;
    public Manager_users _manager_users;
    //------------------------------------------------------------------------------------------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("[J2Y]", "MainActivity:onCreate");

//        com.facebook.Settings.getApplicationSignature()
//        Setting.getApplicationSignature(this);
        //Settings.getApplicationSignature(this);
        // manager
        _manager_users = new Manager_users();
        _photoManager = new Manager_photoGallery();

        _familypopSetting = getSharedPreferences("familypopSetting",0);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

        Instance = this;
		_virtualServer = false;

		startActivity(new Intent(this, Activity_title.class));
        //startActivity(new Intent(this, Activity_serverMain_andEngine.class));

		_fpsRoot = new FpsRoot();
		_fpcRoot = new FpcRoot();
        _fpcRoot.Initialize(this);

		// test record save
		_fpcRoot._talk_records.clear();
        FpcTalkRecord recorddata = test_recordData();
		_fpcRoot._talk_records.add(recorddata);

        Log.i("[J2Y]", "ThreadID:[Root]" + (int) Thread.currentThread().getId());


        SharedPreferences.Editor editor  = MainActivity.Instance._familypopSetting.edit();
        editor.clear();

        // calibration
        _calibration_width_length = 130;
        _calibration_height_length = 90;

        //client init info
        //_ready = false;
        _curServerScenario = Manager_contents.eType_contents.CONTENTS_NOT;//FpNetConstants.SCENARIO_NONE;

        FacebookSdk.sdkInitialize(getApplicationContext());

        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);

            for(Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("[J2Y]", "MainActivity:onDestroy");
        super.onDestroy();
    }



    private FpcTalkRecord test_recordData() {
        FpcTalkRecord recorddata = new FpcTalkRecord();
        recorddata._name        = "testName1234";
        recorddata._filename    = "testfilename1234";
        recorddata._startTime   = 1;
        recorddata._endTime     = 10;

        recorddata.AddBubble(2, 9, 0, 0, 30, R.color.red, R.color.red);
        recorddata.AddBubble(2, 9, 100, 100, 50, R.color.red, R.color.red);
        recorddata.AddBubble(2, 9, 150, 200, 100, R.color.red, R.color.red);
        recorddata.AddBubble(2, 9, 200, 150, 130, R.color.red, R.color.red);

        recorddata.AddSmileEvent(2, 0, R.drawable.scroll_smilepoint1);
        recorddata.AddSmileEvent(5, 0, R.drawable.scroll_smilepoint1);

        _fpcRoot._talk_records.add(recorddata);
        //

        recorddata = new FpcTalkRecord();
        recorddata._name = "testName";
        recorddata._filename = "testfilename";
        recorddata._startTime = 1;
        recorddata._endTime = 10;

        recorddata.AddBubble(2, 9, 0, 150, 30, R.color.red, R.color.red);
        recorddata.AddBubble(2, 9, 150, 150, 60, R.color.red, R.color.red);
        recorddata.AddBubble(2, 9, 200, 150, 120, R.color.red, R.color.red);
        recorddata.AddBubble(2, 9, 250, 150, 150, R.color.red, R.color.red);
        recorddata.AddBubble(2, 9, 300, 150, 200, R.color.red, R.color.red);

        _fpcRoot._talk_records.add(recorddata);
        //
        recorddata = new FpcTalkRecord();
        recorddata._name = "testName5678";
        recorddata._filename = "testfilename5678";
        recorddata._startTime = 1;
        recorddata._endTime = 10;

        recorddata.AddBubble(2, 9, 200, 0, 300, R.color.red, R.color.red);
        return recorddata;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------
    //return : 키가 있으면 : 키에 해당하는 값 ,  키가 없으면 : "null"
    //value  : 가 null 이 아니면 해당 키에 데이터를 넣음.
    public String familypopSettingValue(String key, String value )
    {
        String ret = "null";

        String str = MainActivity.Instance._familypopSetting.getString(key, "null");
        if( value != null)
        {
            SharedPreferences.Editor editor  = MainActivity.Instance._familypopSetting.edit();
            editor.putString(key, value );

            //editor.apply();
            editor.commit();

            //전체 제거 : editor.clear();
            //부분 제거 : editor.remove(key);
            ret = value;
        }
        else{ ret = str; }

        return ret;
    }
    public int GetFamilypopSettingValue_int(String key)
    {
        int ret = MainActivity.Instance._familypopSetting.getInt(key, 0);

        return ret;
    }


    public void SetFamilypopSettingValue_int(String key, int value)
    {

        SharedPreferences.Editor editor  = MainActivity.Instance._familypopSetting.edit();
        editor.putInt(key, value);
        editor.commit();

        //int ret = MainActivity.Instance._familypopSetting.getInt(key, 0);

//        if( value != 0)
//        {
//            SharedPreferences.Editor editor  = MainActivity.Instance._familypopSetting.edit();
//            editor.putInt(key, value);
//            editor.commit();
//            ret = value;
//        }
        //return ret;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 유틸
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public static void Sleep(long time)
    {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static long _debug_timecount;
    private static String _debug_name;
    public static void Debug_begin_timecount(String name)
    {
//        _debug_timecount = 0;
//        _debug_name = name;
//        _debug_timecount = System.currentTimeMillis();
//
//        Log.i("[SI]", _debug_name + "_Start timecount : " + _debug_timecount);
    }
    public static void Debug_end_timecount()
    {
//        long end = System.currentTimeMillis();
//
//        Log.i("[SI]", _debug_name + "_End timecount : " + _debug_timecount);
//        Log.i("[SI]", _debug_name + "_"+(end -_debug_timecount )  +" milliseconds");
    }
}
