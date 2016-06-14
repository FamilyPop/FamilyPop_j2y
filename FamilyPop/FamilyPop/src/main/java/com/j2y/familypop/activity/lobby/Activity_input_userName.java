package com.j2y.familypop.activity.lobby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.j2y.engine.ColumnListView;
import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.Activity_clientMain;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.BaseActivity;
import com.j2y.familypop.client.FpcRoot;
import com.j2y.network.client.FpNetFacade_client;
import com.nclab.familypop.R;

import org.andengine.entity.sprite.ButtonSprite;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lsh on 2016-05-14.
 */
public class Activity_input_userName extends BaseActivity implements View.OnClickListener
{
    private EditText _user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialogue_start_client_input_id);

        _user_name = (EditText)findViewById(R.id.Text_userName);

        ((ImageButton)findViewById(R.id.ClientInputFaceBookButton)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.ClientInputNextButton)).setOnClickListener(this);


        SharedPreferences prefs = getSharedPreferences("FamilypopClient", MODE_PRIVATE);
        String text_username = prefs.getString("Username", "UserName");
        _user_name.setText(text_username);

        // server connect
        _image_servertoConnect = (ImageView)findViewById(R.id.image_connectToServer);
        _image_servertoConnectFail = (ImageView)findViewById(R.id.image_connect_to_server_fail);

        _image_servertoConnect.setVisibility(View.GONE);
        _image_servertoConnectFail.setVisibility(View.GONE);
    }
    @Override
    public void onDestroy()
    {
        Log.i("[J2Y]", "Activity_talkHistory:onDestroy");

        super.onDestroy();
    }
    @Override
    public void onClick(View v)
    {
        SharedPreferences prefs = getSharedPreferences("FamilypopClient", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        switch (v.getId())
        {
            case R.id.ClientInputNextButton:
                editor.putString("Username", _user_name.getText().toString());
                MainActivity.Instance._userName = _user_name.getText();


                //startActivity(new Intent(MainActivity.Instance, Activity_clientMain.class));
                FpcRoot.Instance.DisconnectServer();
                _connectFail = true;
                connectToServer();

                break;
            case R.id.ClientInputFaceBookButton:

                startActivity(new Intent(MainActivity.Instance, Activity_facebook_connected.class));

                break;
        }
    }
    // server 접속
    private boolean _onceClick_connectToServer = false;
    private long _connectedTime;
    private boolean _connectFail = false;
    public ImageView _image_servertoConnect = null;
    public ImageView _image_servertoConnectFail = null;

    private void connectToServer()
    {
        //save_client_information();

        if( MainActivity.Instance._virtualServer)
        {
            startActivity(new Intent(MainActivity.Instance, Activity_clientMain.class));
        }
        else
        {
            if( _onceClick_connectToServer == true) return;

            FpcRoot.Instance._user_name = MainActivity.Instance._userName.toString();   //_user_name.getText().toString();
            FpcRoot.Instance.ConnectToServer(MainActivity.Instance._serverIP.toString());

            _connectedTime = System.currentTimeMillis();
            _onceClick_connectToServer = true;

            _image_servertoConnect.setVisibility(View.VISIBLE);
            _image_servertoConnectFail.setVisibility(View.GONE);

            ChangeScenarioActivity();
        }
    }

    private Lock _lock_user = new ReentrantLock();
    public void ChangeScenarioActivity()
    {
        try
        {

            new Thread()
            {
                @Override
                public void run()
                {
                    while(true)
                    {
                        if(FpNetFacade_client.Instance.IsConnected() && FpNetFacade_client.Instance._recv_connected_message)
                        {

                            {
                                Log.i("[J2Y]", "userPosID" + FpcRoot.Instance._user_posid);
                                FpNetFacade_client.Instance.SendPacket_setUserInfo(MainActivity.Instance._userName.toString(), FpcRoot.Instance._bubble_color_type, FpcRoot.Instance._user_posid);

                                startActivity(new Intent(MainActivity.Instance, Activity_clientMain.class));

                                //connectToServer();

//                            //server state 시나리오 선택
                                //startActivity(new Intent(MainActivity.Instance, Activity_clientMain.class));
                                //FpNetFacade_client.Instance.SendPacket_req_changeScenario(MainActivity.Instance._curServerScenario);
                                //startActivity(new Intent(MainActivity.Instance, Activity_clientMain.class));
                                //FpNetFacade_client.Instance.SendPacket_req_changeScenario(MainActivity.Instance._curServerScenario.getValue());

                                _onceClick_connectToServer = false;

                                // _image_servertoConnectFail.setVisibility(View.GONE);
                                //  _image_servertoConnect.setVisibility(View.GONE);
                                //  _button_connectServer.setVisibility(View.GONE);

                                finish();
                                break;
                            }
                        }
                        else
                        {
                            //
                            long deltaTime = System.currentTimeMillis() - _connectedTime;
                            if(deltaTime > 5000) // 5초간 대기.
                            {
                                FpcRoot.Instance.DisconnectServer();
                                _connectFail = true;
                                //finish();
                                break;
                            }
                        }
                    }
                }
            }.start();
        }
        finally
        {
            //_image_servertoConnectFail.setVisibility(View.GONE);
            //_image_servertoConnect.setVisibility(View.GONE);
            //_button_connectServer.setVisibility(View.GONE);
        }


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if( _connectFail )
                {
                    _image_servertoConnect.setVisibility(View.GONE);
                    _image_servertoConnectFail.setVisibility(View.VISIBLE);
                    _connectFail = false;
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            _image_servertoConnect.setVisibility(View.GONE);
                            _image_servertoConnectFail.setVisibility(View.GONE);
                            _onceClick_connectToServer = false;
                            //finish();
                        }
                    }, 3000);
                }
            }
        },6000);
    }
}
