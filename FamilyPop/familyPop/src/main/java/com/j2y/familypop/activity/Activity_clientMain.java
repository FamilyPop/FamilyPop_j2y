package com.j2y.familypop.activity;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.lobby.Activity_talkHistory;
import com.j2y.familypop.activity.manager.contents.client.Contents_clientTalk;
import com.j2y.familypop.activity.manager.gallery.ImageInfo;
import com.j2y.familypop.activity.popup.Popup_dialogueMenu;
import com.j2y.familypop.activity.manager.Manager_contents;
import com.j2y.familypop.activity.manager.Manager_photoGallery;
import com.j2y.familypop.activity.popup.Popup_messageBox_shareImage;
import com.j2y.familypop.backup.Dialog_MessageBox_ok_cancel;
import com.j2y.familypop.client.FpcRoot;
import com.j2y.familypop.client.FpcScenarioDirectorProxy;
import com.j2y.familypop.client.FpcScenario_base;
import com.j2y.familypop.client.FpcTalkRecord;
//import com.j2y.familypop.server.FpsScenarioDirector;
import com.j2y.network.base.FpNetConstants;
import com.j2y.network.base.FpNetUtil;
import com.j2y.network.base.data.FpNetData_base;
import com.j2y.network.client.FpNetFacade_client;
import com.nclab.familypop.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import java.sql.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ToggleButton;

import org.andengine.util.math.MathUtils;

//import org.jbox2d.common.Vec3;


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
// Activity_clientMain
//
//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class Activity_clientMain extends BaseActivity implements OnClickListener , SensorEventListener, SeekBar.OnSeekBarChangeListener//, RadioGroup.OnCheckedChangeListener
{

    public static Activity_clientMain Instance;
    public int _selectScenario;

    private ImageButton _button_home;
    private ImageButton _button_feature;
    private Button _button_feature_imageFind;
    private Button _button_feature_quitdialogue;
    private Button _button_feature_savedialogue;

    private Button _button_client_mode_view_clearBubble;
    private ImageButton _button_rotation;

    // text info
    private boolean _plugVisibleInfo;
    private TextView _text_date;
    private TextView _text_name;
    public TextView _text_user;
    public TextView _text_color_pos;
    public LinearLayout _layout_roomInfo;


    // 버블
    private ImageButton _button_redbubble;
    private TouchMove _touchMove;

    private TextView _text_voiceAmplitude;
    public TextView _text_voiceAmplitudeAverage;
    private ImageButton _shared_image;

    private ImageView _image_smilebubble;
    private ImageView _image_boomChosen;
    private ImageView _image_bombRunning;
    public   boolean _temp_send_talk;

    public FrameLayout _layout_bubbleImage;

    // 센서
    private boolean _accelerStart = false;
    //private Vec3 _lastPos;
    private float _lastPosX;
    private float _lastPosY;
    private float _lastPosZ;
    private long _accelerDelayTime;
    private Sensor _accelerormeterSensor;
    private Sensor _oriSensor;
    private boolean _sensor_plug;

    // Regulation
    public LinearLayout _layout_regulation;
    private Button _button_feature_regulation;
    private Button _button_regulation_send;
    private Button _button_regulation_cancel;
    private Button _button_regulation_clearBubble;

    private SeekBar _seekBar_regulation_0;
    private SeekBar _seekBar_regulation_1;
    private SeekBar _seekBar_regulation_2;
    private SeekBar _seekBar_regulation_3;
    private SeekBar _seekBar_voice_hold;
    private SeekBar _seekBar_regulation_smileEffect;
    private SeekBar _seekBar_plus_bubble_size;

    // regulation
    public int _buffer_count = 6;   // 서버로감
    public int _smile_effect = 10000;   // 서버로감
    public int _voice_hold = 5000;     // 서버로감

    // tlak mode setting
    public float _attractorMoveSpeed = 1.0f;
    public float _flowerPlusSize = 1.1f;    // 서버로감
    public float _flowerMaxSize = 1.5f;     // 서버로감
    public float _flowerMinSize = 0.3f;     // 서버로감
    public float _flowerGoodSize = 1.0f;    // 서버로감
    public float _flowerSmileSize = 1.0f;   // 서버로감
    public float _talkDelayTime = 10.0f;     // 서버로감

    public float _colliderTalkSize  = 1.0f; // 서버로감
    public float _colliderSmileSize = 1.0f; // 서버로감
    public float _colliderGoodSize  = 1.0f; // 서버로감

    private TextView _textView_regulation_0;
    private TextView _textView_regulation_1;
    private TextView _textView_regulation_2;
    private TextView _textView_regulation_3;
    private TextView _textView_voice_hold;
    private TextView _textView_regulation_smileEffect;
    private TextView _textView_plus_bubble_size;

    private ToggleButton _toggleButton_voiceProcessingMode;

    // tic tac toe ui
    public FrameLayout _layout_ttt;
    private Button _button_tictactoe;

    private Button _button_ttt_top_left;
    private Button _button_ttt_top_top;
    private Button _button_ttt_top_right;

    private Button _button_ttt_middle_left;
    private Button _button_ttt_middle_top;
    private Button _button_ttt_middle_right;

    private Button _button_ttt_bottom_left;
    private Button _button_ttt_bottom_top;
    private Button _button_ttt_bottom_right;

    private Button _button_ttt_style_o;
    private Button _button_ttt_style_x;
    private Button _button_ttt_exit;

    private Button _button_ttt_localization;

    private Button _button_ttt_throwback;

    private ImageView _imageview_ttt_winner;

    public int _ttt_style;
    public Button _button_ttt_Style;

    private ImageView _button_userLike;

    //
    Dialog_MessageBox_ok_cancel _messageBox_instruction;
    Dialog_MessageBox_ok_cancel _messageBox_exit_familybomb;

    // joystick
    public JoyStick _joystick;

    // client pos button
    private ImageButton _button_clientpos_pink_left;
    private ImageButton _button_clientpos_pink_top;
    private ImageButton _button_clientpos_pink_right;
    private ImageButton _button_clientpos_pink_bottom;

    private ImageButton _button_topmenu_bomb;
    private ImageButton _button_topmenu_keyword;
    private ImageButton _button_topmenu_sharephotos;

    // pictures view
    private FrameLayout _layout_photoView;

    private ImageButton   _image_center;
    private ImageButton   _image_leftTop;
    private ImageButton   _image_rightTop;
    private ImageButton   _image_leftBottom;
    private ImageButton   _image_rightBottom;


    // connect server
    public ImageButton _button_connectServer;
    public ImageView _image_servertoConnect = null;
    public ImageView _image_servertoConnectFail = null;

    public RelativeLayout _photoView = null;
    public Activity_photoGallery _photoGallery = null;
    //public Activity_photoGallery _activity_photoGallery = null;

    // manager
    Manager_contents _manager_contents = null;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 초기화/종료
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    RelativeLayout _layout_joystick;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        Log.i("[J2Y]", "Activity_clientMain:onCreate");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //setContentView(R.layout.activity_client);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_client_mode);


        Instance = this;
        _plugVisibleInfo = false;

        init_guiWidgets();

        _selectScenario = FpNetConstants.SCENARIO_NONE;

        // Scenario
        for(FpcScenario_base scenario : FpcRoot.Instance._scenarioDirectorProxy._scenarios)
        {
            if(scenario != null)
                scenario.OnCreate(this);
        }

        _net_quit_request = false;
        _temp_send_talk = false;

        init_sensor(this);

        if(Build.VERSION.SDK_INT >  10)
        {
            StrictMode.enableDefaults();
        }
        // # localization  클라이언트용 접속.
        //FpcRoot.Instance.InitLocalization();

        //deactive_interactionView();
        //allDeactive_targetImage();

//        // joystick 생성
//        _layout_joystick = (RelativeLayout)findViewById(R.id.image_sticklayout);
//        Resources res = getResources();
//        Drawable drawble = null;
//        drawble = res.getDrawable(R.drawable.image_stick_red);
//
//        //js = new Joystick(getApplicationContext() , layout_joystick, R.drawable.image_stick_yellow);
//        _joystick = new JoyStick(getApplicationContext() , _layout_joystick, drawble);
//
//        _joystick.setStickSize(250, 250);
//        _joystick.setLayoutSize(800, 800);
//        //js.setLayoutAlpha(150);
//        //js.setStickAlpha(100);
//        _joystick.setOffset(125);
//        //js.setMinimumDistance(50);
//
//        _joystick.draw(800 / 2, 800 / 2);
//        // touch event
//        _layout_joystick.setOnTouchListener(new View.OnTouchListener()
//        {
//            public boolean onTouch(View arg0, MotionEvent arg1)
//            {
//                _joystick.drawStick(arg1);
//                return true;
//            }
//        });

        _manager_contents = new Manager_contents(false);

        _manager_contents.Content_change(Manager_contents.eType_contents.CONTENTS_READY);

        // create update root
        SystemClock.sleep(50);
        FpcTalkRecord talk = FpcRoot.Instance.NewTalkRecord();
        talk._startTime = System.currentTimeMillis();
        Activity_clientMain.Instance.NewVoiceAmplitudeTask();

        //        //MainActivity.Instance._socioPhone.startRecord(0, "temp");
//        SystemClock.sleep(50);
//        //Log.i("[J2Y]", "[SocioPhone] startRecord ");
//
//        FpcTalkRecord talk = FpcRoot.Instance.NewTalkRecord();
//        talk._startTime = System.currentTimeMillis();
//
//        Activity_clientMain.Instance.NewVoiceAmplitudeTask();

        // photo sharing
        Manager_photoGallery photoGallery = Manager_photoGallery.Instance;

        // connect server
        //connectToServer();
	}
     @Override
    protected void onDestroy()
    {
        super.onDestroy();

        //kookm0614
        request_exitRoom();
        FpcRoot.Instance._socioPhone.stopRecord();

        Log.i("[J2Y]", "Activity_clientMain:onDestroy");
        if(_task_voiceAmplitude != null)
        {
            _task_voiceAmplitude.cancel(true);
            _task_voiceAmplitude = null;
        }
        FpcRoot.Instance.DisconnectServer();

        Instance = null;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    public void SaveTalkRecord()
    {
        // 대화 정보 기록하기
        FpcTalkRecord talk_record = FpcRoot.Instance._selected_talk_record;
        if(talk_record != null)
        {
            talk_record._endTime = System.currentTimeMillis();

            //talk_record._name = "test_save";
            if(!talk_record._list_added)
                FpcRoot.Instance.AddTalkRecord(talk_record);
            FpcRoot.Instance.SaveTalkRecords();
        }
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // GUI 이벤트
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // GetSoundAmplitue()
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // [이벤트] 버튼 클릭
    private int _device_rotationCount = 0;
//    PopupWindow popupWindow = null;
//    Popup_dialogueMenu popup_dialogueMenu = null;
    @Override
	public void onClick(View view) 
	{
        // 토크 버튼 중복 방지.
        if( FpcScenarioDirectorProxy.Instance._activeScenarioType != FpNetConstants.SCENARIO_RECORD){ _temp_send_talk = false; }

//        if(view.getId() == R.id.button_client_featuremenu_sharephotos || view.getId() == R.id.button_client_dialogue_topmenu_sharephotos  )
//        {
//            //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//            intent.setType("image/*");             									// 모든 이미지
//            //intent.putExtra("crop", "false");    									// Crop기능 활성화
//
//
//            //intent.putExtra(MediaStore.EXTRA_OUTPUT,  getTempUri());     				// 임시파일 생성
//            //intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); 	// 포맷방식
//
//            startActivityForResult(intent, 0);
//            active_featureMenu(false);
//
//            /*
//             Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");             									// 모든 이미지
//            intent.putExtra("crop", "true");        									// Crop기능 활성화
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());     				// 임시파일 생성
//            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); 	// 포맷방식
//
//            startActivityForResult(intent, 0);
//             */
//        }
		//scenario select
		switch(view.getId())
		{
			case R.id.button_client_featuremenu_familybomb:

                OnClock_bomb_instruction();
                active_featureMenu(false);
				break;

			case R.id.button_client_featuremenu_talk:

                if(FpcScenarioDirectorProxy.Instance._activeScenarioType == FpNetConstants.SCENARIO_RECORD ||
                        FpcScenarioDirectorProxy.Instance._activeScenarioType == FpNetConstants.SCENARIO_GAME) break;
                if(!_temp_send_talk)
                {
                    //_selectScenario = FpNetConstants.SCENARIO_RECORD;
                    _selectScenario = Manager_contents.eType_contents.CONTENTS_TALK.getValue();
                    FpNetFacade_client.Instance.SendPacket_req_changeScenario(_selectScenario);
                    _temp_send_talk = true;
                }
                active_featureMenu(false);
                break;

            case R.id.button_client_dialogue_topmenu_feature:
                //active_featureMenu(true);
                View popupview = getLayoutInflater().inflate(R.layout.popup_topmenu_dialog,null);
                PopupWindow popupWindow = new PopupWindow(popupview);

                popupWindow.setWindowLayoutMode(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //팝업 터치 가능
                popupWindow.setTouchable(true);
                //팝업 외부 터치 가능(외부 터치시 나갈 수 있게)
                popupWindow.setOutsideTouchable(true);
                //외부터치 인식을 위한 추가 설정 : 미 설정시 외부는 null로 생각하고 터치 인식 X
                popupWindow.setBackgroundDrawable(new BitmapDrawable());

                //팝업 생성
                popupWindow.showAtLocation(popupview, Gravity.LEFT, 0, 0);
                Popup_dialogueMenu popup_dialogueMenu = new Popup_dialogueMenu(popupview, popupWindow,FpcRoot.Instance._user_name);

                break;
            case R.id.button_client_featuremenu_quitdialogue:
                onClick_Quitdialogue(false, true, true);
                active_featureMenu(false);
                break;
            case R.id.button_client_featuremenu_smile_event:
                FpNetFacade_client.Instance.sendMessage(FpNetConstants.CSReq_smileEvent, new FpNetData_base());
                active_featureMenu(false);
                break;
            case R.id.button_client_featuremenu_savedialogue:
                onClick_Quitdialogue(false, false, true);
                active_featureMenu(false);
                break;
            case R.id.button_client_featuremenu_bubbleclear:
                FpNetFacade_client.Instance.SendPacket_req_clearBubble();
                active_featureMenu(false);
                break;
            case R.id.button_client_redbubble:
                onClick_Bubble();
                break;
            // regulation  거품 설정??.
            case R.id.button_client_featuremenu_regulation:
                _layout_regulation.setVisibility(View.VISIBLE);

                //_layout_roomInfo.setVisibility(View.GONE);
                _layout_bubbleImage.setVisibility(View.GONE);

                active_featureMenu(false);
                _layout_joystick.setVisibility(View.GONE);
                break;
            case R.id.button_client_mode_regulation_send: // 설정 값을 서버로 보냄

//                int s0 =   _seekBar_regulation_0.getProgress();
//                int s1 =   _seekBar_regulation_1.getProgress();
//                int s2 =   _seekBar_regulation_2.getProgress();
//                int s3 =   _seekBar_regulation_3.getProgress();
//                int voice_hold = _seekBar_voice_hold.getProgress();
//                int mode =  _toggleButton_voiceProcessingMode.isChecked() == true ? 1 : 0; // 0 이면 j2y 모드
//                int smileEffect = _seekBar_regulation_smileEffect.getProgress();
//                int plusBubbleSize = _seekBar_plus_bubble_size.getProgress();
//
//                FpNetFacade_client.Instance.SendPacket_req_regulation_info(s0, s1, s2, s3, voice_hold, mode, smileEffect, plusBubbleSize);

                break;

            case R.id.button_client_mode_regulation_cancel: // 취소함
                _layout_regulation.setVisibility(View.INVISIBLE);

                //_layout_roomInfo.setVisibility(View.VISIBLE);
                //_layout_bubbleImage.setVisibility(View.VISIBLE);
                _layout_joystick.setVisibility(View.VISIBLE);
                break;

            case R.id.button_client_mode_view_clearBubble:
            case R.id.button_client_mode_clearBubble:
                FpNetFacade_client.Instance.SendPacket_req_clearBubble();
                break;
            case R.id.imagebutton_rotation:
                _device_rotationCount++;
                _button_rotation.setRotation(_button_rotation.getRotation() + (MainActivity.Instance._rotationAngle));
                MainActivity.Instance._deviceRotation = _button_rotation.getRotation();

                break;
            case R.id.toggleButton_voice_processing_mode:

                break;
            //end regulation

            // tic tac toe
            case R.id.button_client_featuremenu_tictactoe:

                // back 16.01.11
//                _selectScenario = FpNetConstants.SCENARIO_TIC_TAC_TOE;
//                FpNetFacade_client.Instance.SendPacket_req_start_Tic_Tac_Toe();
//                _layout_regulation.setVisibility(View.INVISIBLE);
//                _layout_roomInfo.setVisibility(View.GONE);
//                _layout_bubbleImage.setVisibility(View.GONE);

                FpNetFacade_client.Instance.SendPacket_req_start_Tic_Tac_Toe();
                _joystick.Deactive();
                active_featureMenu(false);
                break;

            case R.id.button_ttt_top_left: onClick_ttt(_button_ttt_top_left, 0); break;
            case R.id.button_ttt_top_top: onClick_ttt(_button_ttt_top_top, 1);break;
            case R.id.button_ttt_top_right: onClick_ttt(_button_ttt_top_right, 2);break;

            case R.id.button_ttt_middle_left: onClick_ttt(_button_ttt_middle_left, 3);break;
            case R.id.button_ttt_middle_top: onClick_ttt(_button_ttt_middle_top, 4);break;
            case R.id.button_ttt_middle_right: onClick_ttt(_button_ttt_middle_right, 5);break;

            case R.id.button_ttt_bottom_left: onClick_ttt(_button_ttt_bottom_left, 6);break;
            case R.id.button_ttt_bottom_top: onClick_ttt(_button_ttt_bottom_top, 7);break;
            case R.id.button_ttt_bottom_right: onClick_ttt(_button_ttt_bottom_right, 8);break;

            case R.id.button_ttt_style_o: _ttt_style = 0; break;
            case R.id.button_ttt_style_x: _ttt_style = 1; break;
            case R.id.button_ttt_exit:
                FpNetFacade_client.Instance.SendPacket_req_end_Tic_Tac_Toe();

                //_layout_roomInfo.setVisibility(View.VISIBLE);
                _layout_bubbleImage.setVisibility(View.VISIBLE);
                _joystick.Active();
                break;
            case R.id.button_ttt_localization:
            case R.id.button_ttt_type:

                //localization
                _button_ttt_localization.setEnabled(false); // 클릭 무효화
                _button_ttt_Style.setEnabled(false);

                Handler h = new Handler();
                h.postDelayed(new button_ttt_localization_handler(), 5000); // 3초지연.

                //서버에게 스타일 전송. ( 버튼 누르면 일단 무조건 날림.)
                FpNetFacade_client.Instance.SendPacket_req_selectIndex_TicTacToe(_ttt_style);
                //Toast.makeText(this, "localization_3 seconds delay ", Toast.LENGTH_SHORT).show();
                //1 초후 Localization 을 시도한다.
                h.postDelayed(new button_ttt_requestLocalization_handler(), 1000); //
                //FpcRoot.Instance._localization._client.requestLocalization();

                //throwback
                //_button_ttt_throwback.setVisibility(View.VISIBLE);

                //Handler hThrowback = new Handler();
                //hThrowback.postDelayed(new button_ttt_throwback_handler(), 8000); // 5 초후  INVISIBLE
                break;

            case R.id.button_ttt_throwback:

                FpNetFacade_client.Instance.SendPacket_req_throwback_Tic_Tac_Toe();

                break;
            //end tic tac toe

            // client pos
            case R.id.imageButton_clientpos_pink_left:
                _device_rotationCount++;
                MainActivity.Instance._deviceRotation = 90;//, _button_rotation.getRotation();
                active_clientPos(R.id.imageButton_clientpos_pink_right);
                break;
            case R.id.imageButton_clientpos_pink_top:
                _device_rotationCount++;
                MainActivity.Instance._deviceRotation = 90;// _button_rotation.getRotation();
                active_clientPos(R.id.imageButton_clientpos_pink_left);
                break;
            case R.id.imageButton_clientpos_pink_right:
                _device_rotationCount++;
                MainActivity.Instance._deviceRotation = 270;//_button_rotation.getRotation();
                active_clientPos(R.id.imageButton_clientpos_pink_bottom);
                break;
            case R.id.imageButton_clientpos_pink_bottom:
                _device_rotationCount++;
                MainActivity.Instance._deviceRotation = 270;//_button_rotation.getRotation();
                active_clientPos(R.id.imageButton_clientpos_pink_top);
                break;
            // end client pos

            // top menu
            case R.id.button_client_dialogue_topmenu_sharephotos:
                _photoGallery = new Activity_photoGallery(this, (GridView)findViewById(R.id.grid_view));
                _photoGallery.Active();

                //Activity_clientMain.Instance._photoGallery.Active();
                break;
            case R.id.button_client_dialogue_topmenu_keyword:

                break;
            case R.id.button_client_dialogue_topmenu_bomb:
                //OnClock_bomb_instruction();
                FpNetFacade_client.Instance.SendPacket_req_startGame();
                break;
            case R.id.button_connectServer:
                //connectToServer();
                //FpcRoot.Instance._socioPhone.RegisterQuery();

                //kookm0614
                //FpcRoot.Instance._socioPhone.RegisterQuery();

                Activity_clientMain.Instance._selectScenario = Manager_contents.eType_contents.CONTENTS_TALK.getValue();
                FpNetFacade_client.Instance.SendPacket_req_changeScenario(Activity_clientMain.Instance._selectScenario);
                _button_connectServer.setVisibility(View.GONE);
                break;
		}
        //_joystick.onClick(view);
	}
    // client pos
    private void active_clientPos( int id )
    {
        _button_clientpos_pink_left.setVisibility(View.GONE);
        _button_clientpos_pink_top.setVisibility(View.GONE);
        _button_clientpos_pink_right.setVisibility(View.GONE);
        _button_clientpos_pink_bottom.setVisibility(View.GONE);

        switch (id)
        {
            case R.id.imageButton_clientpos_pink_left:
                _button_clientpos_pink_left.setVisibility(View.VISIBLE);
                break;
            case R.id.imageButton_clientpos_pink_top:
                _button_clientpos_pink_top.setVisibility(View.VISIBLE);
                break;
            case R.id.imageButton_clientpos_pink_right:
                _button_clientpos_pink_right.setVisibility(View.VISIBLE);
                break;
            case R.id.imageButton_clientpos_pink_bottom:
                _button_clientpos_pink_bottom.setVisibility(View.VISIBLE);
                break;
        }
    }

    // _button_ttt_localization 지연용
    class button_ttt_localization_handler implements Runnable
    {
        public void run()
        {
            _button_ttt_localization.setEnabled(true); // 클릭 유효화
            _button_ttt_Style.setEnabled(true);
        }
    }
    class button_ttt_throwback_handler implements Runnable
    {
        public void run()
        {
            _button_ttt_throwback.setVisibility(View.GONE);
        }
    }
    class button_ttt_requestLocalization_handler implements Runnable
    {
        public void run()
        {
            FpcRoot.Instance._localization._client.requestLocalization();
        }
    }
    private void OnClock_bomb_instruction()
    {
        _messageBox_instruction = new Dialog_MessageBox_ok_cancel(this)
        {
            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);

                _editText.setVisibility(View.GONE);
                _content.setText("Shake and place your smartphone on the table.");
                _ok.setText("OK");
                _cancel.setText("CANCEL");
            }
            @Override
            public void onClick(View v)
            {
                super.onClick(v);
                switch (v.getId())
                {
                    case R.id.button_custom_dialog_ok:

//                        _selectScenario = FpNetConstants.SCENARIO_GAME;
//                        FpNetFacade_client.Instance.SendPacket_req_changeScenario(_selectScenario);
                        _sensor_plug = true;


                        //OnClock_bomb_exit_familybomb();
                        //setContentView(R.layout.activity_game);
                        cancel();
                        break;
                    case R.id.button_popupmessagebox_cancel:
                        cancel();
                        break;
                }
            }
        };
        _messageBox_instruction.show();
    }

    private void OnClock_bomb_exit_familybomb()
    {
        _messageBox_exit_familybomb = new Dialog_MessageBox_ok_cancel(this)
        {
            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);

                _editText.setVisibility(View.GONE);
                _content.setText("Do you want to quit family bomb?");
                _ok.setText("OK");
                _cancel.setText("CANCEL");
            }
            @Override
            public void onClick(View v)
            {
                super.onClick(v);
                switch (v.getId())
                {
                    case R.id.button_custom_dialog_ok:

                        cancel();
                        break;
                    case R.id.button_popupmessagebox_cancel:

                        cancel();
                        break;
                }
            }
        };
        _messageBox_exit_familybomb.show();
    }

    //seekbar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        switch ( seekBar.getId() )
        {
            case R.id.seekBar_regulation_0:
                //((TextView) findViewById(R.id.textView_regulation_0)).setText(seekBar.getProgress());
                _textView_regulation_0.setText("VolThreshold : "+String.valueOf(progress));
                break;
            case R.id.seekBar_regulation_1:
                //((TextView) findViewById(R.id.textView_regulation_1)).setText(seekBar.getProgress());
                _textView_regulation_1.setText("VolVarThreshold : "+String.valueOf(progress));
                break;
            case R.id.seekBar_regulation_2:
                //((TextView) findViewById(R.id.textView_regulation_2)).setText(seekBar.getProgress());
                _textView_regulation_2.setText("bufferSize : "+String.valueOf(progress));
                break;
            case R.id.seekBar_regulation_3:
                //((TextView) findViewById(R.id.textView_regulation_2)).setText(seekBar.getProgress());
                _textView_regulation_3.setText("bubbleMaxSize : "+String.valueOf(progress));
                break;
            case R.id.seekBar_voice_hold:
                _textView_voice_hold.setText("voiceHold : "+String.valueOf(progress));
                break;
            case R.id.seekBar_regulation_smileEffect:
                _textView_regulation_smileEffect.setText("smileEffect : "+String.valueOf(progress));
                break;
            case R.id.seekBar_bubble_plussize:
                _textView_plus_bubble_size.setText("plusSize : "+String.valueOf(progress));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    // tic tac toe
    private void onClick_ttt(Button tile, int index)
    {
        tile.setBackgroundResource(R.drawable.image_ttt_o_red);

        FpNetFacade_client.Instance.SendPacket_req_selectIndex_TicTacToe(_ttt_style);
    }
    public void Init_TicTacToe()
    {
        _button_ttt_top_left.setBackgroundResource(R.drawable.image_ttt_empty_red);
        _button_ttt_top_top.setBackgroundResource(R.drawable.image_ttt_empty_red);
        _button_ttt_top_right.setBackgroundResource(R.drawable.image_ttt_empty_red);

        _button_ttt_middle_left.setBackgroundResource(R.drawable.image_ttt_empty_red);
        _button_ttt_middle_top.setBackgroundResource(R.drawable.image_ttt_empty_red);
        _button_ttt_middle_right.setBackgroundResource(R.drawable.image_ttt_empty_red);

        _button_ttt_bottom_left.setBackgroundResource(R.drawable.image_ttt_empty_red);
        _button_ttt_bottom_top.setBackgroundResource(R.drawable.image_ttt_empty_red);
        _button_ttt_bottom_right.setBackgroundResource(R.drawable.image_ttt_empty_red);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // [이벤트] 터치
    public boolean onTouchEvent(MotionEvent e)
    {
        RelativeLayout menu = (RelativeLayout) findViewById(R.id.layout_dialogue_menu_feature);
        menu.setVisibility(View.INVISIBLE);



        return super.onTouchEvent(e);
    }

    //ArrayList<View> _touchViews;
    ArrayList<Interaction_Target> _touchViews;
    boolean _interaction = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent e)
    {
        //_joystick.drawStick(e);
        if( _interaction )
        {
            int eventAction = e.getAction();

            switch(eventAction)
            {
                case MotionEvent.ACTION_DOWN:
//                Toast.makeText(MainActivity.Instance, "ACTION_DOWN", Toast.LENGTH_SHORT).show();

                    Rect rect = new Rect();
                    _button_userLike.getGlobalVisibleRect(rect);

                    float posX = rect.left;
                    float posY = rect.top;
                    float widht = rect.width();
                    float height = rect.height();

                    if( posX < e.getX() && (posX+widht) > e.getX()  &&
                            posY < e.getY() && (posY+height) > e.getY() )
                    {
                        _effectButton = true;
                        //Toast.makeText(MainActivity.Instance, "ACTION_DOWN", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
//                Toast.makeText(MainActivity.Instance, "ACTION_MOVE", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP:
//                Toast.makeText(MainActivity.Instance, "ACTION_UP", Toast.LENGTH_SHORT).show();
                    //int id =  collision_chack(e.getX(), e.getY());
                    Interaction_Target target = collision_chack(e.getX(), e.getY());
                    if( target != null)
                    {
                        FpNetFacade_client.Instance.SendPacket_req_userInteraction(target._clientId, FpcRoot.Instance._clientId);
                    }
                    _effectButton = false;
                    //_joystick.Action_up();

                    break;
            }
        }
        return super.dispatchTouchEvent(e);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 네트워크
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 방 나가기 요청
    private void request_exitRoom()
    {
        Log.i("[J2Y]", "Request exit room");
        // todo: 방 정보 입력 팝업
        if(FpcRoot.Instance._scenarioDirectorProxy._activeScenario != null)
            FpcRoot.Instance._scenarioDirectorProxy._activeScenario.OnDeactivated();

        FpNetFacade_client.Instance.sendMessage(FpNetConstants.CSReq_exitRoom, new FpNetData_base());
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 서버에서 방 나가기 요청
    public void OnEventSC_exitRoom() {

        Log.i("[J2Y]", "Exit room");

        if(_net_quit_request) {
            ExitRoom();
        }
        else {
            // 방 정보 입력 팝업
            onClick_Quitdialogue(true, true, false);
        }
    }
    public void ExitRoom()
    {
        //SaveTalkRecord();

        startActivity(new Intent(MainActivity.Instance, Activity_talkHistory.class));
        finish();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // [이벤트] 네트워크
    public void OnInteractionEventOccured(int speakerID, int eventType, long timestamp)
	{
		FpcScenario_base activeScenario = FpcRoot.Instance._scenarioDirectorProxy.GetActiveScenario();
		
		if(activeScenario != null)
			activeScenario.OnInteractionEventOccured(speakerID, eventType, timestamp);
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------
	public void OnDisplayMessageArrived(int type, String message) 
	{
		FpcScenario_base activeScenario = FpcRoot.Instance._scenarioDirectorProxy.GetActiveScenario();
		
		if(activeScenario != null)
			activeScenario.OnDisplayMessageArrived(type, message);
	}


    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // [이벤트] 화자 인식
    public void OnTurnDataReceived(int[] speakerID)
	{
		FpcScenario_base activeScenario = FpcRoot.Instance._scenarioDirectorProxy.GetActiveScenario();
		
		if(activeScenario != null)
			activeScenario.OnTurnDataReceived(speakerID);
	}

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 스마일 이벤트
    public void OnEventSC_smileEvent(int eventTime) {

        Log.i("[J2Y]", "OnEventSC_smileEvent:" + eventTime);

        // todo: 스마일 이미지 그리기 On/Off
        _button_redbubble.setVisibility(View.GONE);
        _image_smilebubble.setVisibility(View.VISIBLE);


        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run() {
                _button_redbubble.setVisibility(View.GONE);
                _image_smilebubble.setVisibility(View.INVISIBLE);
            }
        }, 5000);

    }

    private  ImageInfo _bang_selectImage;
    public void OnEventSC_bang(boolean ask_ShareImage)
    {
//        _button_redbubble.setVisibility(View.GONE);
//        _image_boomChosen.setVisibility(View.VISIBLE);
//
//        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
//            @Override
//            public void run() {
//                _button_redbubble.setVisibility(View.GONE);
//                _image_boomChosen.setVisibility(View.INVISIBLE);
//            }
//        }, 5000);


        Manager_photoGallery mag = Manager_photoGallery.Instance;

        _bang_selectImage = null;
        if( mag != null)
        {
            ArrayList<ImageInfo> images =  mag.FindMemoryRootImage(this);
            ArrayList<ImageInfo> arrayList = new ArrayList<>();

            int imageIndex = MathUtils.random(0, images.size()-1);
            _bang_selectImage = images.get(imageIndex);
            arrayList.add(_bang_selectImage);
            mag.SetArrayList(arrayList);
        }



        if( ask_ShareImage )
        {

            Popup_messageBox_shareImage test = new Popup_messageBox_shareImage(this)
            {
                @Override
                protected void onCreate(Bundle savedInstanceState)
                {
                    super.onCreate(savedInstanceState);
                    this._imageView.setImageBitmap(_bang_selectImage.GetBitmap());

                    //_content.setText("share image?");

                }
                @Override
                public void onClick(View v)
                {
                    super.onClick(v);

                    switch(v.getId())
                    {
                        case R.id.button_popupmessagebox_ok:
                            FpNetFacade_client.Instance.SendPacket_req_shareImage();
                            cancel();

                            break;
                        case R.id.button_popupmessagebox_cancel:

                            cancel();
                            break;

                    }


                }
            };

            test.show();

        }
        else
        {
            FpNetFacade_client.Instance.SendPacket_req_shareImage();
        }

        // back
//        // bang
//        Manager_photoGallery mag = Manager_photoGallery.Instance;
//
//        if( mag != null)
//        {
//            ArrayList<ImageInfo> images =  mag.FindMemoryRootImage(this);
//            ArrayList<ImageInfo> arrayList = new ArrayList<>();
//
//            int imageIndex = MathUtils.random(0, images.size()-1);
//            ImageInfo selectImage = images.get(imageIndex);
//            arrayList.add(selectImage);
//
//            mag.SetArrayList(arrayList);
//            FpNetFacade_client.Instance.SendPacket_req_shareImage();
//        }
    }
    public void OnEventSC_win_Tic_Tac_toe()
    {
        _button_redbubble.setVisibility(View.GONE);

        _imageview_ttt_winner.setBackgroundResource(R.drawable.image_ttt_youwin);
        _imageview_ttt_winner.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run()
            {
                _button_redbubble.setVisibility(View.GONE);
                _imageview_ttt_winner.setVisibility(View.INVISIBLE);
            }
        }, 5000);
    }
    public void OnEventSC_lose_Tic_Tac_Toe()
    {
        _button_redbubble.setVisibility(View.GONE);

        _imageview_ttt_winner.setBackgroundResource(R.drawable.image_ttt_youlose);
        _imageview_ttt_winner.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run()
            {
                _button_redbubble.setVisibility(View.GONE);
                _imageview_ttt_winner.setVisibility(View.INVISIBLE);
            }
        }, 5000);

    }
    public void OnEventSC_bombRunning()
    {
        _image_bombRunning.setVisibility(View.VISIBLE);

//        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
//            @Override
//            public void run() {
//
//                _image_bombRunning.setVisibility(View.INVISIBLE);
//            }
//        }, 5000);
    }
    public void OnEventSC_endBomb()
    {
        _image_bombRunning.setVisibility(View.INVISIBLE);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 음성 파동 출력
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public VoiceAmplitudeTask _task_voiceAmplitude = new VoiceAmplitudeTask();
    private float _bubble_size = 350f;
    private float _voiceAmpAvg = 0f;
    private int _voiceAvgCount = 0;

    public VoiceAmplitudeTask NewVoiceAmplitudeTask()
    {

        _task_voiceAmplitude = new VoiceAmplitudeTask();
        _task_voiceAmplitude.execute();
        return _task_voiceAmplitude;
    }

    public class VoiceAmplitudeTask extends AsyncTask<Void, Void, Void>
    {
        public VoiceAmplitudeTask() { }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            if(FpcRoot.Instance._socioPhone != null && Manager_contents.Instance != null)
            {
                // add 160517
                Manager_contents.Instance.update(0.0f); // todo: 사이클 타임 구해서 넣어주자.

//                // 버블이동 패킷 을 서버로 보낸다.
//                if( _touchMove._actionDown )
//                {
//                    Log.i("[J2Y]", "_isClick");
//
//                    if( _device_rotationCount % 2 == 0 )
//                    {
//                        _touchMove._touchDirVectorRotation = MainActivity.Instance._deviceRotation;
//                    }
//                    else
//                    {
//                        _touchMove._touchDirVectorRotation = MainActivity.Instance._deviceRotation * -1;
//                    }
//
//                    FpNetFacade_client.Instance.SendPacket_req_userInput_bubbleMove(_touchMove._normalX, -_touchMove._normalY);
//                }
//

            }
            super.onProgressUpdate(values);
        }
        @Override
        protected Void doInBackground(Void... arg0) {

            while(!isCancelled()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
                publishProgress();
            }
            return null;
        }

    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 이미지 공유
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 외장메모리에 임시 이미지 파일을 생성하여 그 파일의 경로를 반환
    private Uri getTempUri() 
    {
//    	// Check SDCard Mount
//    	 String status = Environment.getExternalStorageState();
//        if (!status.equals(Environment.MEDIA_MOUNTED))
//            return null;
//
//        // Create Temp JPG File
//        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//        try
//        {
//        	boolean res = f.createNewFile();      // 외장메모리에 temp.jpg 파일 생성
//
//            if(res == true)
//                return Uri.fromFile(f);
//        }
//        catch (IOException e)
//        {
//            //e.printStackTrace();
//            return null;
//        }
//
//        // return temp Uri
//        return Uri.fromFile(f);

    	 String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED))
            return null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return Uri.fromFile(file);
        }
        return null;
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 다시 액티비티로 복귀하였을때 이미지를 셋팅
    protected void onActivityResult(int requestCode, int resultCode, Intent imageData) 
    {
        super.onActivityResult(requestCode, resultCode, imageData);
 
        switch (requestCode) 
        {
	        case 0:
	            if (resultCode == RESULT_OK) 
	            {
	                if (imageData != null)
                    {
//	                    String filePath = Environment.getExternalStorageDirectory() + "/temp.jpg";
//	                    System.out.println("path" + filePath); // logCat으로 경로확인.
//
//                        // temp.jpg파일을 Bitmap으로 디코딩한다.
//	                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
//
//                        // 임시
//                        FpNetFacade_client.Instance.SendPacket_req_shareImage(selectedImage);


//                         //Bitmap bitImage= MediaStore.Images.Media.getBitmap( getContentResolver(), imageData.getData());
//                        // uri를 절대경로 변경하기 위해
//                        Cursor c = getContentResolver().query(imageData.getData(), null, null, null, null);
//                        if( c.moveToNext() );
//                        {
//                            String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
//                            Uri uri = Uri.fromFile(new File(path));
//
//                            Bitmap bmp = BitmapFactory.decodeFile(uri.getPath());
//                            FpNetFacade_client.Instance.SendPacket_req_shareImage(bmp);
//                        }
//                        c.close();

                        try
                        {
                            Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), imageData.getData());
                            //FpNetFacade_client.Instance.SendPacket_req_shareImage(image_bitmap);
                            //FpNetFacade_client.Instance.SendPacket_req_shareImage();

                            Dialog_MessageBox_ok_cancel msgbox = new Dialog_MessageBox_ok_cancel(Activity_clientMain.Instance)
                            {
                                @Override
                                protected void onCreate(Bundle savedInstanceState)
                                {
                                    super.onCreate(savedInstanceState);

                                    // "Do you want to share a ramdom picture?"
                                    _content.setText("Do you want to share a picture?");
                                    _editText.setVisibility(View.GONE);

                                }
                                @Override
                                public void onClick(View v)
                                {
                                    super.onClick(v);
                                    switch (v.getId())
                                    {
                                        case R.id.button_custom_dialog_ok:
                                            FpNetFacade_client.Instance.SendPacket_req_shareImage();
                                            cancel();
                                            break;
                                        case R.id.button_popupmessagebox_cancel:
                                            cancel();
                                            break;
                                    }
                                }
                            };
                            msgbox.show();




                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
	            }
	            break;
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 공유 이미지를 설정한다.
    public void SetupSharedImage(int index , byte[] image)
    {
        ImageButton inButton = null;
        switch(index)
        {
            // left top
            case 0: inButton = _image_leftTop;   break;
            // right top
            case 1: inButton = _image_rightTop; break;
            // left buttom
            case 2: inButton = _image_leftBottom; break;
            // right buttom
            case 3: inButton = _image_rightBottom; break;
            // center
            case 4: inButton = _image_center; break;
        }
        if(inButton != null )
        {
            if(null == image)
            {
                //Activity_clientMain.Instance._photoView.setVisibility(View.INVISIBLE);
                SetupSharedImage(inButton,(Bitmap)null);
            }
            else
            {
                //Activity_clientMain.Instance._photoView.setVisibility(View.VISIBLE);
                Bitmap selectedImage = FpNetUtil.ByteArrayToBitmap(image);
                SetupSharedImage(inButton,selectedImage);
            }
        }
    }
    public void SetupSharedImage(ImageButton imageButton,  Bitmap selectedImage)
    {

        if(null == selectedImage)
        {
            //_shared_image.setVisibility(View.INVISIBLE);
            _layout_photoView.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.GONE);
        }
        else
        {
            // temp.jpg파일을 이미지뷰에 씌운다.
//            _shared_image.setVisibility(View.VISIBLE);
//            _shared_image.setImageBitmap(selectedImage);
//            _shared_image.setOnClickListener(new OnClickListener();

            _layout_photoView.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setImageBitmap(selectedImage);
            imageButton.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Dialog_MessageBox_ok_cancel megBox = new Dialog_MessageBox_ok_cancel(Activity_clientMain.Instance, "Ok", "Cancel")
                    {
                        @Override
                        protected void onCreate(Bundle savedInstanceState)
                        {
                            super.onCreate(savedInstanceState);

                            _content.setText("Do you want to stop sharing photo?");
                            _editText.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onClick(View v)
                        {
                            super.onClick(v);

                            switch (v.getId()) {
                                case R.id.button_custom_dialog_ok: // ok
                                    //_shared_image.setVisibility(View.INVISIBLE);
                                    all_deActive();
                                    Manager_photoGallery.Instance.Release_lists();
                                    FpNetFacade_client.Instance.SendPacket_req_shareImage();
                                    cancel();
                                    break;
                                case R.id.button_popupmessagebox_cancel: //skip
                                    cancel();
                                    break;
                            }
                        }
                    };
                    megBox.show();
                }
            });
        }
    }

    private void all_deActive()
    {
        _image_leftTop.setVisibility(View.GONE);
        // right top
        _image_rightTop.setVisibility(View.GONE);
        // left buttom
        _image_leftBottom.setVisibility(View.GONE);
        // right buttom
        _image_rightBottom.setVisibility(View.GONE);
        // center
        _image_center.setVisibility(View.GONE);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // GUI Widgets
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    private void init_guiWidgets()
    {
        // layout
        _layout_bubbleImage = (FrameLayout) findViewById(R.id.layout_image_view); //layout_image_view
        _imageview_ttt_winner = (ImageView) findViewById(R.id.imageview_win_tic_tac_toe);
        _ttt_style = 0;

        ((Button) findViewById(R.id.button_client_featuremenu_familybomb)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_client_featuremenu_talk)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_client_featuremenu_smile_event)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_client_featuremenu_savedialogue)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_client_featuremenu_bubbleclear)).setOnClickListener(this);

        _button_redbubble = (ImageButton) findViewById(R.id.button_client_redbubble);
        _button_redbubble.setOnClickListener(this);

        Rect rect = new Rect();
//        _button_redbubble.getGlobalVisibleRect(rect);
//        _touchMove = new TouchMove(_button_redbubble,
//                                    rect.centerX(),
//                                    rect.centerY());

        _touchMove = new TouchMove(_button_redbubble,
                rect.centerX(),
                rect.centerY() );

        _touchMove._move = false;

        _image_smilebubble = (ImageView) findViewById(R.id.image_createMode_smile);
        _image_boomChosen = (ImageView) findViewById(R.id.image_message_boom_you_were_chosen);
        _image_bombRunning = (ImageView) findViewById(R.id.image_message_bomb_running);


        // todo: 이미지 컬러 변경 ( 조이스틱 컬러 변경 추가.. )
//        _button_redbubble
        _text_color_pos = ((TextView) findViewById(R.id.text_client_color));
        _text_color_pos.setText("Color:" + FpcRoot.Instance._bubble_color_type + " , userPos: " + FpcRoot.Instance._user_posid);

        // joystick
        _layout_joystick = (RelativeLayout) findViewById(R.id.image_sticklayout);

        Resources res = getResources();
        Drawable drawble = null;
        Drawable drawbleRotation = null;
        Drawable drawbleStickLayout = null;

        //_layout_joystick.setRotation(50);

        switch(FpcRoot.Instance._bubble_color_type)
        {
            // orange
            case 0:
                //_button_redbubble.setBackgroundResource(R.drawable.image_bead_4);
                //drawble = res.getDrawable(R.drawable.image_stick_pink);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_20x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_pink_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_orange);
                break;
            // green
            case 1:
                //_button_redbubble.setBackgroundResource(R.drawable.image_bead_0);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_40x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_red_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_green);
                break;
            // purple
            case 2:
               // _button_redbubble.setBackgroundResource(R.drawable.image_bead_2);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_310x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_yellow_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_purple);
                break;
            // blue
            case 3:
               // _button_redbubble.setBackgroundResource(R.drawable.image_bead_1);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_10x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_green_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_skybule);
                break;
            // red
            case 4:
                //_button_redbubble.setBackgroundResource(R.drawable.image_bead_5);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_10x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_green_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_phthalogreen);
                break;
            // yellow
            case 5:
                //_button_redbubble.setBackgroundResource(R.drawable.image_bead_3);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_310x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_green_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_blue);
                break;
            case 6:
               // _button_redbubble.setBackgroundResource(R.drawable.image_bead_6);
                drawble = res.getDrawable(R.drawable.image_stick_green);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_green_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_green);
                break;
        }


        _layout_joystick.setClickable(true);
        //_layout_joystick.setBackground(drawbleStickLayout);
        findViewById(R.id.imageView_stickRound).setBackground(drawbleStickLayout);
        _joystick = new JoyStick(getApplicationContext() , _layout_joystick, drawble);
        _joystick.setStickSize(500, 500);
        _joystick.setLayoutSize(900, 900);
        //js.setLayoutAlpha(150);
        //js.setStickAlpha(100);
        _joystick.setOffset(250);
        //js.setMinimumDistance(50);

        _joystick.draw(900 / 2, 900 / 2);
        // touch event
        _layout_joystick.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                _joystick.drawStick(arg1);
                //_layout_joystick.invalidate();

                return true;
            }
        });

        // 현재 날자와 시간을 구한다.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);
        Date currentTime = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(currentTime);
        //

        _button_client_mode_view_clearBubble = (Button) findViewById(R.id.button_client_mode_view_clearBubble);
        _button_client_mode_view_clearBubble.setOnClickListener(this);

        _layout_roomInfo = (LinearLayout) findViewById(R.id.layout_client_mode_room_info);
        _text_date = (TextView) findViewById(R.id.text_client_date);
        _text_date.setText(time);
        _text_name = (TextView) findViewById(R.id.text_client_name);
        _text_user = (TextView) findViewById(R.id.text_client_users);
        _text_voiceAmplitude = (TextView) findViewById(R.id.text_voice_amplitude);
        _text_voiceAmplitudeAverage = (TextView) findViewById(R.id.text_voice_amplitude_average);

        //top menu
        _button_home = (ImageButton) findViewById(R.id.button_client_dialogue_topmenu_home);
        _button_home.setOnClickListener(this);

        _button_feature = (ImageButton) findViewById(R.id.button_client_dialogue_topmenu_feature);
        _button_feature.setOnClickListener(this);

        // feature menu
        _button_feature_imageFind = (Button) findViewById(R.id.button_client_featuremenu_sharephotos);
        _button_feature_imageFind.setOnClickListener(this);
        _button_feature_quitdialogue = (Button) findViewById(R.id.button_client_featuremenu_quitdialogue);
        _button_feature_quitdialogue.setOnClickListener(this);

        _shared_image = (ImageButton) findViewById(R.id.button_shared_image);
        //_shared_image.setOnClickListener(this);

        // regulation;
        _layout_regulation = (LinearLayout) findViewById(R.id.layout_client_mode_bubble_regulation);

        _button_feature_regulation = (Button) findViewById(R.id.button_client_featuremenu_regulation);
        _button_feature_regulation.setOnClickListener(this);
        _button_regulation_send = (Button) findViewById(R.id.button_client_mode_regulation_send) ;
        _button_regulation_send.setOnClickListener(this);
        _button_regulation_cancel = (Button) findViewById(R.id.button_client_mode_regulation_cancel);
        _button_regulation_cancel.setOnClickListener(this);
        _button_regulation_clearBubble = (Button) findViewById(R.id.button_client_mode_clearBubble);
        _button_regulation_clearBubble.setOnClickListener(this);
        _button_rotation = (ImageButton) findViewById(R.id.imagebutton_rotation);
        _button_rotation.setOnClickListener(this);
        // 회전 버튼 이미지 변경.
        //drawble = res.getDrawable(R.drawable.image_clientpos_pink_top);
        //_button_rotation.setImageDrawable(drawbleRotation);

        _seekBar_regulation_0 = (SeekBar) findViewById(R.id.seekBar_regulation_0);
        _seekBar_regulation_1 = (SeekBar) findViewById(R.id.seekBar_regulation_1);
        _seekBar_regulation_2 = (SeekBar) findViewById(R.id.seekBar_regulation_2);
        _seekBar_regulation_3 = (SeekBar) findViewById(R.id.seekBar_regulation_3);
        _seekBar_voice_hold = (SeekBar) findViewById(R.id.seekBar_voice_hold);
        _seekBar_regulation_smileEffect = (SeekBar) findViewById(R.id.seekBar_regulation_smileEffect);
        _seekBar_plus_bubble_size = (SeekBar) findViewById(R.id.seekBar_bubble_plussize);

        _seekBar_regulation_0.setMax(200000);           //??
        _seekBar_regulation_1.setMax(2000000000);       //??
        _seekBar_regulation_2.setMax(30);               // 버퍼개수
        _seekBar_regulation_3.setMax(200);              // 최대사이즈
        _seekBar_voice_hold.setMax(5000);               // 소리크기 무시 최대치.
        _seekBar_regulation_smileEffect.setMax(50000);       // 스마일 효과 발생 보이스 크기.
        _seekBar_plus_bubble_size.setMax(10);            // 버블 커지는속도

        _seekBar_regulation_0.setProgress(5000);
        _seekBar_regulation_1.setProgress(5000);
        _seekBar_regulation_2.setProgress(6);
        _seekBar_regulation_3.setProgress(100);
        _seekBar_voice_hold.setProgress(1400);
        _seekBar_regulation_smileEffect.setProgress(10000);
        _seekBar_plus_bubble_size.setProgress(2);

        // regulation
        _buffer_count   = 6;
        _smile_effect   = 10000;
        _voice_hold     = 5000;

        // tlak mode setting
        //_attractorMoveSpeed;
        _flowerPlusSize = 1.1f;
        _flowerMaxSize = 1.5f;

        _seekBar_regulation_0.setOnSeekBarChangeListener(this);
        _seekBar_regulation_1.setOnSeekBarChangeListener(this);
        _seekBar_regulation_2.setOnSeekBarChangeListener(this);
        _seekBar_regulation_3.setOnSeekBarChangeListener(this);
        _seekBar_voice_hold.setOnSeekBarChangeListener(this);
        _seekBar_regulation_smileEffect.setOnSeekBarChangeListener(this);
        _seekBar_plus_bubble_size.setOnSeekBarChangeListener(this);

//        _seekBar_regulation_0.incrementProgressBy(1);
//        _seekBar_regulation_1.incrementProgressBy(1);
//        _seekBar_regulation_2.incrementProgressBy(1);

        _textView_regulation_0 = (TextView) findViewById(R.id.textView_regulation_0);
        _textView_regulation_1 = (TextView) findViewById(R.id.textView_regulation_1);
        _textView_regulation_2 = (TextView) findViewById(R.id.textView_regulation_2);
        _textView_regulation_3 = (TextView) findViewById(R.id.textView_regulation_3);
        _textView_voice_hold = (TextView) findViewById(R.id.textView__voice_hold);
        _textView_regulation_smileEffect = (TextView) findViewById(R.id.textView_regulation_smileEffect);
        _textView_plus_bubble_size = (TextView) findViewById(R.id.textView_bubble_plussize);

        _textView_regulation_0.setText("VolThreshold : "+String.valueOf( _seekBar_regulation_0.getProgress()));
        _textView_regulation_1.setText("VolVarThreshold : "+String.valueOf(_seekBar_regulation_1.getProgress()));
        _textView_regulation_2.setText("bufferSize : "+String.valueOf(_seekBar_regulation_2.getProgress()));
        _textView_regulation_3.setText("bubbleMaxSize : "+String.valueOf(_seekBar_regulation_3.getProgress()));
        _textView_voice_hold.setText("voiceHold : " + String.valueOf(_seekBar_voice_hold.getProgress()));
        _textView_regulation_smileEffect.setText("smileEffect : " + String.valueOf(_seekBar_regulation_smileEffect.getProgress()));
        _textView_plus_bubble_size.setText("plusSize : " + String.valueOf(_seekBar_plus_bubble_size.getProgress()));

        _toggleButton_voiceProcessingMode = (ToggleButton) findViewById(R.id.toggleButton_voice_processing_mode);
        _toggleButton_voiceProcessingMode.setChecked(false);
        _toggleButton_voiceProcessingMode.setOnClickListener(this);

        // tic tac toe

        _button_tictactoe = (Button) findViewById(R.id.button_client_featuremenu_tictactoe);
        _button_tictactoe.setOnClickListener(this);

        _layout_ttt = (FrameLayout) findViewById(R.id.layout_tic_tac_toe);

        _button_ttt_top_left = (Button) findViewById(R.id.button_ttt_top_left);
        _button_ttt_top_top = (Button) findViewById(R.id.button_ttt_top_top);
        _button_ttt_top_right = (Button) findViewById(R.id.button_ttt_top_right);

        _button_ttt_middle_left = (Button) findViewById(R.id.button_ttt_middle_left);
        _button_ttt_middle_top = (Button) findViewById(R.id.button_ttt_middle_top);
        _button_ttt_middle_right = (Button) findViewById(R.id.button_ttt_middle_right);

        _button_ttt_bottom_left = (Button) findViewById(R.id.button_ttt_bottom_left);
        _button_ttt_bottom_top = (Button) findViewById(R.id.button_ttt_bottom_top);
        _button_ttt_bottom_right = (Button) findViewById(R.id.button_ttt_bottom_right);

        _button_ttt_style_o = (Button) findViewById(R.id.button_ttt_style_o);
        _button_ttt_style_x = (Button) findViewById(R.id.button_ttt_style_x);
        _button_ttt_exit = (Button) findViewById(R.id.button_ttt_exit);
        _button_ttt_localization = (Button) findViewById(R.id.button_ttt_localization);
        _button_ttt_throwback = (Button)findViewById(R.id.button_ttt_throwback);
        _button_ttt_Style = (Button) findViewById(R.id.button_ttt_type);

        _button_ttt_top_left.setOnClickListener(this);
        _button_ttt_top_top.setOnClickListener(this);
        _button_ttt_top_right.setOnClickListener(this);

        _button_ttt_middle_left.setOnClickListener(this);
        _button_ttt_middle_top.setOnClickListener(this);
        _button_ttt_middle_right.setOnClickListener(this);

        _button_ttt_bottom_left.setOnClickListener(this);
        _button_ttt_bottom_top.setOnClickListener(this);
        _button_ttt_bottom_right.setOnClickListener(this);

        _button_ttt_style_o.setOnClickListener(this);
        _button_ttt_style_x.setOnClickListener(this);
        _button_ttt_exit.setOnClickListener(this);
        _button_ttt_localization.setOnClickListener(this);
        _button_ttt_throwback.setOnClickListener(this);
        _button_ttt_Style.setOnClickListener(this);


        // photo view
        _layout_photoView = (FrameLayout)findViewById(R.id.layout_photoView);
        _image_leftTop = (ImageButton)findViewById(R.id.imageView_photo_leftTop);
        _image_rightTop = (ImageButton)findViewById(R.id.imageView_photo_rightTop);
        _image_leftBottom = (ImageButton)findViewById(R.id.imageView_photo_leftBottom);
        _image_rightBottom = (ImageButton)findViewById(R.id.imageView_photo_rightBottom);
        _image_center = (ImageButton)findViewById(R.id.imageView_photo_center);

        // user message
        _button_userLike = (ImageView) findViewById(R.id.imageview_user_interaction_like);
        //_button_userLike.setOnClickListener(this);

        // touch
        _touchViews = new ArrayList<Interaction_Target>();
        ((Button)findViewById(R.id.imagebutton_interaction)).setOnClickListener(this);
        ((Button)findViewById(R.id.imagebutton_interaction_exit)).setOnClickListener(this);

        // client pos

        _button_clientpos_pink_left = (ImageButton)findViewById(R.id.imageButton_clientpos_pink_left);
        _button_clientpos_pink_top = (ImageButton)findViewById(R.id.imageButton_clientpos_pink_top);
        _button_clientpos_pink_right = (ImageButton)findViewById(R.id.imageButton_clientpos_pink_right);
        _button_clientpos_pink_bottom = (ImageButton)findViewById(R.id.imageButton_clientpos_pink_bottom);

        _button_clientpos_pink_left.setOnClickListener(this);
        _button_clientpos_pink_top.setOnClickListener(this);
        _button_clientpos_pink_right.setOnClickListener(this);
        _button_clientpos_pink_bottom.setOnClickListener(this);

        //active_clientPos(R.id.imageButton_clientpos_pink_top);

        // topmenu button
        _button_topmenu_sharephotos = (ImageButton)findViewById(R.id.button_client_dialogue_topmenu_sharephotos);
        _button_topmenu_sharephotos.setOnClickListener(this);
        _button_topmenu_keyword = (ImageButton)findViewById(R.id.button_client_dialogue_topmenu_keyword);
        _button_topmenu_keyword.setOnClickListener(this);
        _button_topmenu_bomb = (ImageButton)findViewById(R.id.button_client_dialogue_topmenu_bomb);
        _button_topmenu_bomb.setOnClickListener(this);

        // server event
        _button_connectServer = (ImageButton)findViewById(R.id.button_connectServer);
        _button_connectServer.setOnClickListener(this);

        _image_servertoConnect = (ImageView)findViewById(R.id.image_connectServer);
        _image_servertoConnectFail = (ImageView)findViewById(R.id.image_connectFail);

        // photo view
        //_photoView = (RelativeLayout)findViewById(R.id.llImageList);
        _photoGallery = new Activity_photoGallery(this, (GridView)findViewById(R.id.grid_view));
    }

    // todo : 같은 일을 하는 부분가 있음. 분리 해야함.
    public void Set_StyleJoyStick(int clientId)
    {
        // COLOR_ERROR(-1), COLOR_ORANGE(0), COLOR_YELLOW_GREEN(1),  COLOR_PURPLE(2), COLOR_SKY_BLUE(3), COLOR_RED(4);
        _layout_joystick = (RelativeLayout) findViewById(R.id.image_sticklayout);
        Resources res = getResources();
        Drawable drawble = null;
        Drawable drawbleRotation = null;
        Drawable drawbleStickLayout = null;

        switch(clientId)
        {
            // orange
            case 0:
                //_button_redbubble.setBackgroundResource(R.drawable.image_bead_4);
                //drawble = res.getDrawable(R.drawable.image_stick_pink);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_20x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_pink_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_orange);
                break;
            // green
            case 1:
                // _button_redbubble.setBackgroundResource(R.drawable.image_bead_2);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_40x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_yellow_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_green);
                break;
            // purple
            case 2:
                // _button_redbubble.setBackgroundResource(R.drawable.image_bead_1);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_310x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_green_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_purple);
                break;
            // sky bule
            case 3:
                //_button_redbubble.setBackgroundResource(R.drawable.image_bead_5);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_10x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_green_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_skybule);
                break;
            // red
            case 4:
                //_button_redbubble.setBackgroundResource(R.drawable.image_bead_0);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_00x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_red_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_red);
                break;
            // yellow
            case 5:
                //_button_redbubble.setBackgroundResource(R.drawable.image_bead_3);
                drawble = res.getDrawable(R.drawable.gui_0621_reso_jog_user_310x4);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_green_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_blue);
                break;
            case 6:
                // _button_redbubble.setBackgroundResource(R.drawable.image_bead_6);
                drawble = res.getDrawable(R.drawable.image_stick_green);
                drawbleRotation = res.getDrawable(R.drawable.image_clientpos_green_top);
                drawbleStickLayout = res.getDrawable(R.drawable.image_sticklayout_green);
                break;
        }

        //_layout_joystick.setBackground(drawbleStickLayout);
        findViewById(R.id.imageView_stickRound).setBackground(drawbleStickLayout);
        _joystick.Set_stick(drawble);
        _joystick.setStickSize(500, 500);
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    private void active_featureMenu(boolean active)
    {
        RelativeLayout menu = (RelativeLayout) findViewById(R.id.layout_dialogue_menu_feature);
        if(active )
        {
            menu.setVisibility(View.VISIBLE);
            //_layout_joystick.setVisibility(View.GONE);
        }
        else
        {
            menu.setVisibility(View.INVISIBLE);
            //_layout_joystick.setVisibility(View.VISIBLE);
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    int _bubbleClickCount =1;
    private void onClick_Bubble()
    {
        if( _bubbleClickCount > 2) _bubbleClickCount = 0;

        switch (_bubbleClickCount)
        {
            case 0:
                _text_date.setVisibility(View.VISIBLE);
                _text_name.setVisibility(View.VISIBLE);
                _text_user.setVisibility(View.VISIBLE);
                _text_color_pos.setVisibility(View.VISIBLE);
                _text_voiceAmplitude.setVisibility(View.VISIBLE);
                _text_voiceAmplitudeAverage.setVisibility(View.VISIBLE);
                break;
            case 1:
                _text_user.setVisibility(View.INVISIBLE);
                _text_voiceAmplitude.setVisibility(View.INVISIBLE);
                _text_voiceAmplitudeAverage.setVisibility(View.INVISIBLE);
                _text_color_pos.setVisibility(View.INVISIBLE);
                break;
            case 2:
                _text_date.setVisibility(View.INVISIBLE);
                _text_name.setVisibility(View.INVISIBLE);
                break;

        }
//
//        if( _plugVisibleInfo)
//        {
//            _layout_roomInfo.setVisibility(View.VISIBLE);
//            _plugVisibleInfo = false;
//        }
//        else
//        {
//            _layout_roomInfo.setVisibility(View.GONE);
//            _plugVisibleInfo = true;
//        }

        _bubbleClickCount++;
        RelativeLayout menu = (RelativeLayout) findViewById(R.id.layout_dialogue_menu_feature);
        menu.setVisibility(View.INVISIBLE);
    }
    private boolean _net_quit_request;

    public void onClick_Quitdialogue(final boolean force_exit, final boolean exit_talk, final boolean net_request)
    {
        _net_quit_request = net_request;
        FpNetFacade_client.Instance.SendPacket_req_talk_record_info();
        Dialog_MessageBox_ok_cancel msgbox = new Dialog_MessageBox_ok_cancel(this)
        {
            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                //_content.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                //String str  = _editText.getText().toString();
            }
            @Override
            public void onClick(View v)
            {
                super.onClick(v);
                switch (v.getId())
                {
                    case R.id.button_custom_dialog_ok:
                    {
                        // 위에서 이미 진행.( 정보를 받아옴 )
                        //FpNetFacade_client.Instance.SendPacket_req_talk_record_info();

                        SaveTalkRecord();

                        ((Contents_clientTalk)Manager_contents.Instance.GetCurrentContents()).TalkRecordSave( _editText.getText().toString(),
                                                                                                              FpcRoot.Instance._socioPhone.GetWavFileName(),
                                                                                                              FpcRoot.Instance._bubble_color_type );
                        if(exit_talk)
                        {
                            if(net_request)
                                request_exitRoom();
                            else
                                ExitRoom();

                            finish();
                        }
                        else
                        {
                            cancel();
                        }
                    }
                        break;
                    case R.id.button_popupmessagebox_cancel:

//                        if(exit_talk)
//                        {
//                            if(net_request)
//                                request_exitRoom();
//                            else
//                                ExitRoom();
//
//                            finish();
//                        }
//                        else
//                        {
//                            cancel();
//                        }

                        //---------------------------
                        if(force_exit) {

                            startActivity(new Intent(MainActivity.Instance, Activity_talkHistory.class));
                            finish();
                        }
                        else
                        {

                            // 저장 해논 데이터를 날린다.
                            FpcTalkRecord recordData = FpcRoot.Instance._selected_talk_record;
                            FpcRoot.Instance.RemoveTalkRecord(recordData);
                            FpcRoot.Instance.SaveTalkRecords();
                            recordData = null;

                            // 위에 종료 로직 복사
                            if(exit_talk)
                            {
                                if(net_request)
                                    request_exitRoom();
                                else
                                    ExitRoom();

                                finish();
                            }
                            else
                            {
                                cancel();
                            }
                            // end 위에 종료 로직 복사

                            cancel(); //back
                        }
                        break;
                }
            }
        };
        msgbox.show();

        // back
//        if(exit_talk)
//        {
//            if(net_request)
//                request_exitRoom();
//            else
//                ExitRoom();
//
//            finish();
//        }
//        else
//        {
//            //cancel();
//        }

    }
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //super.onKeyDown(keyCode, event);
        boolean ret = false;
        _photoGallery.DeActive();
        _photoGallery = null;

        return ret;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 센서
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private void init_sensor(Activity activity)
    {
        _sensor_plug = false;
        _accelerDelayTime = 0;
        //_lastPos = new Vec3(0, 0, 0);

        SensorManager sensorManager = (SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);
        _accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _oriSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if (_accelerormeterSensor != null)
            sensorManager.registerListener(this, _accelerormeterSensor, SensorManager.SENSOR_DELAY_GAME);

        if (_oriSensor != null)
            sensorManager.registerListener(this, _oriSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {

        if( !_sensor_plug ) return;

        switch (event.sensor.getType())
        {
            // ACCELEROMETER 가속도 센서
            case Sensor.TYPE_ACCELEROMETER:
                long currentTime = System.currentTimeMillis();
                long gabOfTime = (currentTime - _accelerDelayTime);
                if (gabOfTime > 100)
                {
                    _accelerDelayTime = currentTime;

//                    double speed = Math.abs(event.values[0] + event.values[1] + event.values[2] - _lastPos.x - _lastPos.y - _lastPos.z) / gabOfTime * 10000;
//
//                    if (speed > 1100)
//                    {
//                        _accelerStart = true;
//                    }
//
//                    _lastPos.x = event.values[0];
//                    _lastPos.y = event.values[1];
//                    _lastPos.z = event.values[2];
                }
                break;


            // ORIENTATION 회전센서
            case Sensor.TYPE_ORIENTATION:
                if(_accelerStart == false)
                    break;

                if(Math.abs((int)event.values[1]) <= 10 && Math.abs((int)event.values[2]) <= 10)
                {
                    _accelerStart = false;
                    _sensor_plug = false;

                    FpNetFacade_client.Instance.SendPacket_req_startGame();

                    //_selectScenario = FpNetConstants.SCENARIO_GAME;
                    FpNetFacade_client.Instance.SendPacket_req_changeScenario(FpNetConstants.SCENARIO_GAME);
                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    //-----------------------------------------------------------------------------------------------------------------------------
    // Interaction
    private int _collisionView_ID;
    private boolean _effectButton;

    private Interaction_Target collision_chack(float x, float y)
    {
        Interaction_Target ret =null;
        for( Interaction_Target v : _touchViews)
        {
            Rect rect = new Rect();
            v._targetImage.getGlobalVisibleRect(rect);

            float posX = rect.left;  // 위치 + left 마진
            float posY = rect.top;
            int destWidth  = rect.width();
            int destHeight = rect.height();

            if( posX < x && (posX+destWidth) > x  &&
                posY < y && (posY+destHeight) > y )
            {
                ret = v;
//                Toast.makeText(MainActivity.Instance, "collision_chack : " + ret, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return ret;
    }

    public void add_touchViewObject(Interaction_Target v)
    {
        _touchViews.add(v);
    }
    public void clear_touchViewObject()
    {
        _touchViews.clear();
    }
    public void create_popupWindow()
    {


    }


    private int GetCollision_viewID()
    {
        return _collisionView_ID;
    }
    // server 접속
    private boolean _onceClick_connectToServer = false;
    private long _connectedTime;
    private boolean _connectFail = false;

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

//                            //server state 시나리오 선택
                                //startActivity(new Intent(MainActivity.Instance, Activity_clientMain.class));
                                //FpNetFacade_client.Instance.SendPacket_req_changeScenario(MainActivity.Instance._curServerScenario);
                                //startActivity(new Intent(MainActivity.Instance, Activity_clientMain.class));
                                //FpNetFacade_client.Instance.SendPacket_req_changeScenario(MainActivity.Instance._curServerScenario.getValue());

                                _onceClick_connectToServer = false;

                                // _image_servertoConnectFail.setVisibility(View.GONE);
                                //  _image_servertoConnect.setVisibility(View.GONE);
                                //  _button_connectServer.setVisibility(View.GONE);

                                //finish();
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
             _image_servertoConnectFail.setVisibility(View.GONE);
              _image_servertoConnect.setVisibility(View.GONE);
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
    // end server 접속
    //private void

}


