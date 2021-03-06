//package com.j2y.familypop.activity;
//
//import android.content.ContentValues;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.StrictMode;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.WindowManager;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.Reader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//import processing.core.PImage;
//
//import com.badlogic.gdx.math.*;
//import com.badlogic.gdx.math.Vector2;
//import com.j2y.familypop.activity.manager.Manager_actor;
//import com.j2y.familypop.activity.manager.Manager_resource;
//import com.j2y.familypop.server.FpsRoot;
////import com.j2y.familypop.server.FpsScenarioDirector;
//import com.j2y.familypop.server.FpsScenario_base;
////import com.j2y.familypop.server.FpsScenario_record;
//import com.j2y.familypop.server.FpsTalkUser;
//import com.j2y.familypop.server.FpsTicTacToe;
//import com.j2y.network.base.FpNetConstants;
//import com.j2y.network.server.FpNetFacade_server;
//import com.j2y.network.server.FpNetServer_client;
//import com.nclab.sociophone.interfaces.MeasurementCallback;
//
//
//
//import cps.mobilemaestro.library.MMServer;
//
//// box2d
//import org.andengine.engine.handler.IUpdateHandler;
//import org.andengine.engine.options.EngineOptions;
//import org.andengine.entity.scene.IOnSceneTouchListener;
//import org.andengine.entity.scene.Scene;
//import org.andengine.entity.scene.background.Background;
//import org.andengine.entity.util.FPSLogger;
//import org.andengine.extension.physics.box2d.PhysicsWorld;
//import org.andengine.input.sensor.acceleration.AccelerationData;
//import org.andengine.input.sensor.acceleration.IAccelerationListener;
//import org.andengine.input.touch.TouchEvent;
//import org.andengine.ui.activity.SimpleBaseGameActivity;
//
////++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
////
//// Activity_serverMain
////
////
////++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
////public class Activity_serverMain extends PApplet
//public class Activity_serverMain extends SimpleBaseGameActivity implements IAccelerationListener, IOnSceneTouchListener, IUpdateHandler
//{
//	public static Activity_serverMain Instance;
//    //public Box2DProcessing _box2d; back 160509
//
//
//    public HashMap<FpNetServer_client, FpsTalkUser> _talk_users = new HashMap<FpNetServer_client, FpsTalkUser>();
//    private Lock _lock_user = new ReentrantLock();
//
//    // ===========================================================
//    // andengine
//    // ===========================================================
//    public PhysicsWorld _physicsWorld;
//    public Scene _scene;
//    // ===========================================================
//    // Managers
//    // ===========================================================
//    private Manager_resource    _managerResource;
//    private Manager_actor       _managerActor;
//
//
//    // ===========================================================
//    // Managers
//    // ===========================================================
//    Info_regulation info_regulation = null;
//
//    // 스마일 이벤트
//    private boolean _smile_event;
//    private long _smile_event_time;
//    //private PImage _smile_image = null;
//
//    private boolean _bomb_runningMsg_event;
//    private long    _bomb_runningMsg_event_time;
//    //private PImage _bomb_runningMsg_image = null;
//
//    //tictactoe event
//    public boolean  _isNotLocatorDevice = false;
//    public long     _isNotLocatorDevice_time;
//    //private PImage  _image_isNotLocatorDevice;
//
//    //private PImage _ttt_image_winner = null; //image_winner.png
//    private boolean _winner_event = false;
//
//    //private PImage _ttt_image_winner_o = null;
//    //private PImage _ttt_image_winner_x = null;
//    private boolean _onEvent_winner_o = false;
//    private boolean _onEvent_winner_x = false;
//
//    //public PImage _image_server_righttop;
//
//    //public PImage _testImage;
//
//    // tic tac toe
////    public enum eTictactoeImageIndex
////    {   RED_EMPTY(0), RED_O(1), RED_X(2);
////
////        private int value;
////        eTictactoeImageIndex(int i)
////        {
////            value = i;
////        }
////        public int getValue()
////        {
////            return value;
////        }
////    }
//    public boolean _tictactoe_runningMsg_event;
////
////    public int[] _tileIndex;
////    private ArrayList< PImage > _ttt_images;
////    private PImage _tictactoe_grid;
//
//    //public FpsTicTacToe _tictactoe;
//    public int _ttt_style;
//    public boolean _netEvent_tttSytleSuccess = false;
//
//    // # localization  locator 에서 받은 정보를 저장.
//    public double _locX;
//    public double _locY;
//    //regulation
//    // FpsScenario_record 에서 직접 가져다 쓰고 있음.
//
//
//    // topic
//    public TopicRenderer _topic;
//
//    public void Initialization_tictactoe()
//    {
////        if( _tictactoe != null)
////        {
////            _tictactoe.Initialization();
////            _ttt_prvUser = null;
////            _prvStyle = 0;
////            _ttt_userIndex=0;
////        }
//    }
//    // familybomb 메세지
//
//    private MMServer server = null;
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    // 초기화/종료
//    //
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    // [메인쓰레드]
//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        Instance = this;
//
//        //PApplet.
//        // 사이즈 변경됨
////        WindowManager.LayoutParams params = getWindow().getAttributes();
////        params.x = -100;
////        params.height = 500;
////        params.width = 1000;
////        params.y = -50;
//        //this.getWindow().setAttributes(params);
//
//
//        //_server = new MMServer(getApplicationContext(), null);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        //requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //setContentView(R.layout.activity_server_mode);
//
//        Log.i("[J2Y]", "Activity_serverMain:onCreate");
//        Log.i("[J2Y]", "ThreadID:[Activity_serverMain:onCreate]" + (int) Thread.currentThread().getId());
//
//        _locX = 0;
//        _locY = 0;
//
//        FpsRoot.Instance.StartServer();
//
//        info_regulation = new Info_regulation();
//
//        //FpsRoot.Instance._socioPhone.setSilenceVolThreshold(_regulation_seekBar_0);
//        //FpsRoot.Instance._socioPhone.setSilenceVolVarThreshold(_regulation_seekBar_1);
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Toast.makeText(Activity_serverMain.this, "Calibration started", Toast.LENGTH_SHORT).show();
//
//                FpsRoot.Instance._socioPhone.measureSilenceVolThreshold(5000, new MeasurementCallback() {
//                    @Override
//                    public void done(int result) {
//                        FpsRoot.Instance._socioPhone.setSilenceVolThreshold(result * 1.5);
//                        //Toast.makeText(Activity_serverMain.this, "Clibration completed 1", Toast.LENGTH_SHORT).show();
//                        Log.i("JeungminOh", "Calibration completed");
//                    }
//                });
//
//                FpsRoot.Instance._socioPhone.measureSilenceVolVarThreshold(5000, new MeasurementCallback() {
//                    @Override
//                    public void done(int result) {
//                        FpsRoot.Instance._socioPhone.setSilenceVolVarThreshold(result * 1.5);
//                        //Toast.makeText(Activity_serverMain.this, "Clibration completed 2", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }, 5000);
//
//        if(Build.VERSION.SDK_INT >  10)
//        {
//            StrictMode.enableDefaults();
//        }
//
//        // - text to image test -
//
////        final DownloadWebpageTask task = new DownloadWebpageTask();
////        task.execute("http://143.248.135.84:2080");
////
////        _topic = new TopicRenderer();
////
////
////        new Thread (new Runnable()
////        {
////            @Override
////            public void run()
////            {
////                while (true)
////                {
////                    if( task._webText != "" ) break;
////                }
////
////
////                _topic.Add_text(Instance, Instance, task._webText);
////                task._webText = "";
////            }
////        }).start();
//    }
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    // [렌더링 쓰레드??]
//    //@Override
//	public void setup()
//	{
//        Log.i("[J2Y]", "Activity_serverMain:setup");
//		//size(1920, 1080);
//
//        //boolean bSleep = true;
//		//_box2d = new Box2DProcessing(this);
//        //_box2d.createWorld(new Vec2(0, 0), false, false);
//
//        //_box2d.world.setSleepingAllowed(false);
//        //_box2d.world.setSubStepping(false);
//        //_box2d.world.setAllowSleep(false);
//
////		_box2d.setGravity(0, 0);
////        _box2d.setContinuousPhysics(false);
////        _box2d.setWarmStarting(false);
//
//        //requestImageMax = 1;
//
//        //_smile_image = this.loadImage("image_bubble_grey_smile.png");
//        //_bomb_runningMsg_image = this.loadImage("image_bomb_is_running.png");
//        //_ttt_image_winner = this.loadImage("image_winner.png");
//
//        //_ttt_image_winner_o = this.loadImage("widget_ttt_server_owin.png");
//        //_ttt_image_winner_x = this.loadImage("widget_ttt_server_xwin.png");
//
//        //_image_server_righttop = this.loadImage("image_server_righttop.png");
//
//        // is not locator device
//        //_image_isNotLocatorDevice = this.loadImage("image_isnot_locator_device.png");
//
//        //tic tac toe.
//        _tictactoe_runningMsg_event = false;
////        //_ttt_image_empty_red = this.loadImage("image_empty_red.png");
////        _ttt_images = new ArrayList<PImage>();
////
////        _tileIndex = new int[]
////                { eTictactoeImageIndex.RED_EMPTY.getValue(),eTictactoeImageIndex.RED_EMPTY.getValue(),eTictactoeImageIndex.RED_EMPTY.getValue(),
////                  eTictactoeImageIndex.RED_EMPTY.getValue(),eTictactoeImageIndex.RED_EMPTY.getValue(),eTictactoeImageIndex.RED_EMPTY.getValue(),
////                  eTictactoeImageIndex.RED_EMPTY.getValue(),eTictactoeImageIndex.RED_EMPTY.getValue(),eTictactoeImageIndex.RED_EMPTY.getValue() };
////
////        _ttt_images.add(this.loadImage("image_empty_red.png")); // 0  RED_EMPTY
////        _ttt_images.add(this.loadImage("image_ttt_o_red.png")); // 1  RED_O
////        _ttt_images.add(this.loadImage("image_ttt_x_red.png")); // 2  RED_X
////        _tictactoe_grid = loadImage("image_ttt_grid.png");      // grid
//
//        //_tictactoe = new FpsTicTacToe();
//        //_tictactoe.Start(this, 84, 52);
//
//       //_tictactoe.TicTacToe_tileChange(0.6f, 0.4f, FpsTicTacToe.eTictactoeImageIndex.O);
//
//        for(FpsScenario_base scenario : FpsRoot.Instance._scenarioDirector._scenarios)
//        {
//            if(scenario != null)
//                scenario.OnSetup(_physicsWorld);
//                //scenario.OnSetup(this, _box2d);
//        }
//        //FpsRoot.Instance._scenarioDirector.ChangeScenario(FpNetConstants.SCENARIO_GAME);
//        Log.i("[J2Y]", "ThreadID:[Activity_serverMain:setup]" + (int) Thread.currentThread().getId());
//
//
//
//	}
//
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    // [메인쓰레드]
//    private boolean _call_exit = false;
//    //@Override
//    public void exit()
//    {
//        Log.i("[J2Y]", "Activity_serverMain:exit");
//        Log.i("[J2Y]", "ThreadID:[Activity_serverMain:exit]" + (int) Thread.currentThread().getId());
//
//        try
//        {
//            _lock_user.lock();
//
//            if (!_call_exit)
//            {
//                _call_exit = true;
//
//                FpNetFacade_server.Instance.Send_talk_record_info();
//
//                ClearTalkUsers();
//                FpsRoot.Instance.CloseServer();
//            }
//
//            Instance = null;
//            //super.exit();
//
//            finish();
//        }
//        finally
//        {
//            _lock_user.unlock();
//        }
//    }
//    public void CloseServer()
//    {
//        Log.i("[J2Y]", "Activity_serverMain:CloseServer");
//        finish(); // exit 호출함
//    }
//
//
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    // 그리기
//    //
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    // [렌더링 쓰레드]
//    //@Override
//	public void draw()
//	{
////		//background(0);
////        try
////        {
////            _lock_user.lock();
////
//////            int drawCount = 100;
//////            int addHeight = _image_server_righttop.height;
//////            int addWidth = 0;//_image_server_righttop.width;
//////
//////            for( int i=0; i<drawCount; ++i)
//////            {
//////                if( addWidth > this.width)
//////                {
//////                    addWidth = 0;
//////
//////                    addHeight+=_image_server_righttop.height;
//////                    addHeight +=1;
//////                }
//////                this.image(_image_server_righttop,addWidth ,addHeight);
//////                addWidth +=_image_server_righttop.width;
//////                addWidth += 1;
//////                //this.image(_image_server_righttop, this.width - _image_server_righttop.width, _image_server_righttop.height);
//////            }
////
////
////            // 서버 오른쪽 위에 흰박스.
////            //this.image(_image_server_righttop, this.width - _image_server_righttop.width, _image_server_righttop.height);
////
////            //this.image(_testImage, this.width/2, this.height/2);
////            _topic.draw(this);
////
////            if( FpsRoot.Instance._exitServer) return;
////
////            // tic tac toe
////            if( _tictactoe._tictactoe_runningMsg_event )
////            {
////                _tictactoe.Draw(this, _box2d);
////
////                if( _onEvent_winner_o)
////                {
////                    this.image(_ttt_image_winner_o, (this.width - _ttt_image_winner_o.width) / 2 , (this.height - _ttt_image_winner_o.height)/2);
////                }
////                if( _onEvent_winner_x)
////                {
////                    this.image(_ttt_image_winner_x, (this.width - _ttt_image_winner_x.width) / 2 , (this.height - _ttt_image_winner_x.height)/2);
////                }
////            }
////            else
////            {
////                for (FpsTalkUser user : _talk_users.values())
////                {
////                    //if (user._attractor == null)
////                    if( user._user_posid != -1)
////                        user.CreateAttractor(_box2d, this.width, this.height);
////                }
////
////                FpsScenario_base activeScenario = FpsRoot.Instance._scenarioDirector.GetActiveScenario();
////
////                if (activeScenario != null)
////                    activeScenario.OnDraw();
////
////                // 스마일 이벤트
////                if (_smile_event)
////                {
////                    long deltaTime = System.currentTimeMillis() - _smile_event_time;
////                    if (deltaTime > 5000)
////                        _smile_event = false;
////
////                    this.image(_smile_image, (this.width - _smile_image.width) / 2, (this.height - _smile_image.height) / 2);
////                }
////                // 로케이터 없음
////                if(_isNotLocatorDevice)
////                {
////                    long deltaTime = System.currentTimeMillis() - _isNotLocatorDevice_time;
////                    if(deltaTime > 5000)
////                        _isNotLocatorDevice = false;
////
////                    this.image(_image_isNotLocatorDevice, (this.width - _image_isNotLocatorDevice.width) / 2, (this.height - _image_isNotLocatorDevice.height) / 2);
////                }
////                if( _bomb_runningMsg_event )
////                {
////                    long deltaTime = System.currentTimeMillis() - _bomb_runningMsg_event_time;
////                    if (deltaTime > 3000)
////                        _bomb_runningMsg_event = false;
////
////                    this.image(_bomb_runningMsg_image, (this.width - _smile_image.width) / 2, (this.height - _smile_image.height) / 2);
////                }
////
////                if(_winner_event)
////                {
////                    this.image(_ttt_image_winner, (this.width - _ttt_image_winner.width) / 2 , (this.height - _ttt_image_winner.height)/2);
////                }
////
////            }
////            //-------------------------------------------------------------------------------------------------------------------------------
////            // debug
//////            this.text("" + _regulation_seekBar_0, (this.width - _smile_image.width) / 2, 100);
//////            this.text("" + _regulation_seekBar_1, (this.width - _smile_image.width) / 2, 200);
//////            this.text("" + _regulation_seekBar_2, (this.width - _smile_image.width) / 2, 300);
//////            this.text("" + _regulation_seekBar_3, (this.width - _smile_image.width) / 2, 400);
//////
//////            // # localization  locator 에서 받은 정보를 화면에 출력.
//////            this.text("" + _locX, (this.width - _smile_image.width) / 2, 500);
//////            this.text("" + _locY, (this.width - _smile_image.width) / 2, 600);
////
////            //this.text("" + _regulation_seekBar_smileEffect, 0, this.height - 50);
////            //this.text("" + _regulation_seekBar_1, 300, this.height - 50);
////            //this.text("" + _regulation_seekBar_2, 600, this.height - 50);
////            //this.text("" + _regulation_seekBar_3, 900, this.height - 50);
////
////            // # localization  locator 에서 받은 정보를 화면에 출력.
////            //this.text("" + _locX, 1200, this.height - 50);
////            //this.text("" + _locY, 1500, this.height - 50);
////
////            //
//////            int y=0;
//////            for( int i=0 ; i<FpsRoot.Instance._speaker_voice.length; i++)
//////            {
//////                this.text("" + FpsRoot.Instance._speaker_voice[i], 150, y += 50);
//////            }
////            //end debug ----------------------------------------------------------------------------------------------------------------------
////        } finally
////        {
////            _lock_user.unlock();
////        }
//        //smooth();
//	}
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    // 네트워크 이벤트
//    //
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//	public void OnInteractionEventOccured(int speakerID, int eventType, long timestamp)
//	{
//		FpsScenario_base activeScenario = FpsRoot.Instance._scenarioDirector.GetActiveScenario();
//
//		if(activeScenario != null)
//			activeScenario.OnInteractionEventOccured(speakerID, eventType, timestamp);
//	}
//
//	//------------------------------------------------------------------------------------------------------------------------------------------------------
//	public void OnDisplayMessageArrived(int type, String message)
//	{
//		FpsScenario_base activeScenario = FpsRoot.Instance._scenarioDirector.GetActiveScenario();
//
//		if(activeScenario != null)
//			activeScenario.OnDisplayMessageArrived(type, message);
//	}
//
//	//------------------------------------------------------------------------------------------------------------------------------------------------------
//    // 메인쓰레드
//    public void OnTurnDataReceived(int speakerID)
//	{
//        FpsScenario_base activeScenario = FpsRoot.Instance._scenarioDirector.GetActiveScenario();
//
//        if(activeScenario != null)
//            activeScenario.OnTurnDataReceived(speakerID);
//	}
//
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    // 메인쓰레드
//    public void OnEvent_smile(int time)
//    {
//        try {
//            _lock_user.lock();
//            _smile_event = true;
//            _smile_event_time = System.currentTimeMillis();
//
//            FpsScenario_base activeScenario = FpsRoot.Instance._scenarioDirector.GetActiveScenario();
//
//            if(activeScenario != null)
//                activeScenario.Create_smile_bubble();
//        }
//        finally {
//            _lock_user.unlock();
//        }
//    }
//
//    public void OnEvent_bomb_runningMsg(int time)
//    {
//
//        try {
//            _lock_user.lock();
//            _bomb_runningMsg_event = true;
//            _bomb_runningMsg_event_time = System.currentTimeMillis();
//
//            //FpsScenario_base activeScenario = FpsRoot.Instance._scenarioDirector.GetActiveScenario();
//
////            if(activeScenario != null)
////                activeScenario.Create_smile_bubble();
//        }
//        finally
//        {
//            _lock_user.unlock();
//        }
//
//    }
//
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    // 유저 관리
//    //
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    // 메인쓰레드
//    public void AddTalkUser(FpNetServer_client net_client) {
//
//        try {
//            _lock_user.lock();
//
//            FpsTalkUser user = new FpsTalkUser(net_client);
//
//            _talk_users.put(net_client, user);
//        }
//        finally {
//            _lock_user.unlock();
//        }
//    }
//
//    public FpsTalkUser GetTalkUser(FpNetServer_client net_client) {
//
//        try {
//            _lock_user.lock();
//            if(!_talk_users.containsKey(net_client))
//                return null;
//            return _talk_users.get(net_client);
//        }
//        finally {
//            _lock_user.unlock();
//        }
//    }
//
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    // 렌더링 쓰레드
//    public FpsTalkUser FindTalkUser_byId(int clientId) {
//
//        for (FpsTalkUser user : Activity_serverMain.Instance._talk_users.values())
//        {
//            if (user._net_client._clientID == clientId)
//                return user;
//        }
//
//        return null;
//    }
//    public FpsTalkUser FindTalkUser_byName(String userName)
//    {
//
//        for( FpsTalkUser user : Activity_serverMain.Instance._talk_users.values())
//        {
//            if( user._net_client._user_name.equals(userName))
//            return user;
//        }
//
//        return null;
//
//    }
////    // 메인쓰레드
////    public void RemoveTalkUser(FpNetServer_client net_client) {
////
////        // todo: _handler_rendering.sendMessage(_handler_rendering.obtainMessage(messageid_add_user, net_client));
////        if(!_talk_users.containsKey(net_client))
////            return;
////        _talk_users.remove(net_client);
////    }
//
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    // 렌더링 쓰레드
//    public void ClearTalkUsers() {
//
//        try {
//            _lock_user.lock();
//            _talk_users.clear();
//        }
//        finally {
//            _lock_user.unlock();
//        }
//
//        //_handler_rendering.sendMessage(_handler_rendering.obtainMessage(messageid_clear_user));
//    }
//
//    public int GetTalkUserCount() {
//        return _talk_users.size();
//    }
//
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    // 틱텍토
//    //
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    private FpNetServer_client _ttt_prvUser = null;
//    private int _prvStyle; //test
//    private int _ttt_userIndex=0;
//    public void TicTacToe_tileChange(int index, FpsTicTacToe.eTictactoeImageIndex style , FpNetServer_client user)
//    {
////        if( _ttt_prvUser == user ){return;} //같은 유저가
////
////        _tictactoe.TicTacToe_tileChange(index, style);
////        _ttt_prvUser = user;
//    }
//    public void TicTacToe_tileChange(String userName )
//    {
//
////        //무조건 처음 접속한 유저가 먼저.
//////        _ttt_prvUser = FindTalkUser_byId(_ttt_userIndex)._net_client;
//////        if( _ttt_prvUser._clientID != _ttt_userIndex) return;
////
////        //test
////        FpsTicTacToe.eTictactoeImageIndex ttt_style = _tictactoe._curStyle;
////        if(ttt_style == FpsTicTacToe.eTictactoeImageIndex.EMPTY) return;
////        //if(ttt_style == FpsTicTacToe.eTictactoeImageIndex.EMPTY){ttt_style = FpsTicTacToe.eTictactoeImageIndex.O; }
////
////         //ttt_style = ttt_style == FpsTicTacToe.eTictactoeImageIndex.O ? FpsTicTacToe.eTictactoeImageIndex.O : FpsTicTacToe.eTictactoeImageIndex.X;
////
////        _tictactoe.TicTacToe_tileChange( (float)_locX , (float)_locY, ttt_style);
////
////        if( _tictactoe.TicTacToe_checkSuccessful( _tictactoe._prvStyle))
////        {
////
////            FpNetFacade_server.Instance.BroadcastPacket(FpNetConstants.SCNoti_end_Tic_Tac_Toe);
////            //FpNetFacade_server.Instance.Send_UserWin_TicTacToe(FindTalkUser_byId(_ttt_userIndex));
////            FpNetFacade_server.Instance.Send_UserWin_TicTacToe(FindTalkUser_byName(userName));
////
////            //_winner_event = true;
////            Handler hThrowback = new Handler();
////            hThrowback.postDelayed(new ttt_winner_handler(),3000);
////
////            switch (_tictactoe._curStyle.getValue())
////            {
////                case 1: //o
////                    _onEvent_winner_o = true;
////                    break;
////                case 2://x
////                    _onEvent_winner_x = true;
////                    break;
////            }
////        }
////        _ttt_userIndex++;
////        if( _ttt_userIndex >= _talk_users.size()){_ttt_userIndex = 0;}
//    }
////    class ttt_winner_handler implements Runnable
////    {
////        public void run()
////        {
////            _winner_event = false;
////            _onEvent_winner_o = false;
////            _onEvent_winner_x = false;
////            Activity_serverMain.Instance._tictactoe._tictactoe_runningMsg_event = false;
////        }
////    }
//    //
//    public void ClearBubble()
//    {
//        try
//        {
//            _lock_user.lock();
////            FpsScenario_record record =  (FpsScenario_record) FpsScenarioDirector.Instance.GetActiveScenario();
////            record.ClaerBubble();
//        }
//        finally
//        {
//            _lock_user.unlock();
//        }
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event)
//    {
//       // if(FpsScenarioDirector.Instance._activeScenarioType != FpNetConstants.SCENARIO_RECORD) return false;
//        //Toast.makeText(Activity_serverMain.Instance, "dispatchTouchEvent", Toast.LENGTH_SHORT).show();
//        float x = event.getX();                              // get x/y coords of touch event
//        float y = event.getY();
////
//        int action = event.getActionMasked();          // get code for action
//        //pressure = event.getPressure();                // get pressure and size
//        //pointerSize = event.getSize();
////
//        switch (action) {                              // let us know which action code shows up
//            case MotionEvent.ACTION_DOWN:
//                //touchEvent = "DOWN";
//                //Toast.makeText(Activity_serverMain.Instance, "ACTION_DOWN x : "+ x +"x : " + y  , Toast.LENGTH_SHORT).show();
//
////                //사용자동그라미 클릭하면 거품 생성됨.
////                if(FpsScenarioDirector.Instance._activeScenarioType != FpNetConstants.SCENARIO_NONE && FpsScenarioDirector.Instance.GetActiveScenario() != null)
////                {
////                    FpsTalkUser tUser = CheckSelectedUserButtble(x, y, 100f);
////                    if( tUser != null)
////                    {
////                        int userId = tUser._net_client._clientID;
////                        ((FpsScenario_record) FpsScenarioDirector.Instance.GetActiveScenario()).OnTurnDataReceived(userId + 2);
////                    }
////                }
//                //moveUserBubble(x, y);
//                break;
//            case MotionEvent.ACTION_UP:
//                //touchEvent = "UP";
//                //pressure = pointerSize = 0.0;                // when up, set pressure/size to 0
//                //Toast.makeText(Activity_serverMain.Instance, "ACTION_UP x : " + x + "y : " + y, Toast.LENGTH_SHORT).show();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //touchEvent = "MOVE";
//                //  Toast.makeText(Activity_serverMain.Instance, "MOVE x : \"+ x +\"x : \" + y", Toast.LENGTH_SHORT).show();
//                FpsTalkUser tUser = CheckSelectedUserButtble(x, y, 100f);
//                if( tUser != null)
//                {
//                    int userId = tUser._net_client._clientID;
//                    if (userId >= 0)
//                        moveUserBubble(x, y, userId);
//                }
//                break;
//            default:
//                //Toast.makeText(Activity_serverMain.Instance, "dispatchTouchEvent", Toast.LENGTH_SHORT).show();
//                //touchEvent = "OTHER (CODE " + action + ")";  // default text on other event
//        }
//
//        return super.dispatchTouchEvent(event);        // pass data along when done!
//    }
//
//    private FpsTalkUser CheckSelectedUserButtble(float tx, float ty, float range)
//    {
//        //Vec2 worldPos = _box2d.coordPixelsToWorld(tx, ty);
//        //Vec2 scnPos = new Vec2(tx, ty);
//        Vector2 scnPos = new Vector2(tx, ty);
//
//        for (FpsTalkUser user : _talk_users.values())
//        {
//            if (user._attractor == null)
//                continue;
//
//            //float dist = scnPos.sub(user._attractor.GetPosition()).length();
//            float dist = scnPos.sub(user._attractor.GetAttractorPos()).len();
//            if(dist < range)
//                return user;
//                //return user._net_client._clientID;
//        }
//        return null;
//    }
//    private void moveUserBubble(float x, float y, int userId)
//    {
////        Vec2 worldPos = _box2d.coordPixelsToWorld(x, y);
////        for (FpsTalkUser user : _talk_users.values())
////        {
////            if (user._attractor == null)
////                continue;
////            if(userId == user._net_client._clientID)
////                user._attractor.body.setTransform(worldPos, 0f);
////        }
//    }
//
//    public void MoveUserBubble_add(float x, float y, int userId)
//    {
////        Vec2 worldPos;// = _box2d.coordPixelsToWorld(x, y);
////        for (FpsTalkUser user : _talk_users.values())
////        {
////            if (user._attractor == null)
////                continue;
////            if(userId == user._net_client._clientID)
////            {
////                worldPos = user._attractor.body.getPosition();
////                worldPos.x += x;
////                worldPos.y += y;
////
////                user._attractor.body.setTransform(worldPos, 0f);
////            }
////        }
//    }
//
//    //HTTP TEST
//    private String downloadUrl(String myurl) throws IOException
//    {
//        InputStream is = null;
//        // Only display the first 500 characters of the retrieved
//        // web page content.
//        int len = 15;
//
//        try {
//            Log.d("kookm", myurl);
//            URL url = new URL(myurl);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(10000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
//
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//
//            ContentValues values = new ContentValues();
//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            StringBuilder sb = new StringBuilder();
//            sb.append(URLEncoder.encode("username", "UTF-8"));
//            sb.append("=");
//            sb.append(URLEncoder.encode("J2y", "UTF-8"));
//            writer.write(sb.toString());
//            writer.flush();
//            writer.close();
//
//
//            // Starts the query
//            conn.connect();
//            Log.d("kookm", "success");
//            int response = conn.getResponseCode();
//            Log.d("kookm" ,"The response is: " + response);
//            is = conn.getInputStream();
//
//            // Convert the InputStream into a string
//            String contentAsString = readIt(is, len);
//            Log.d("kookm", contentAsString);
//            return contentAsString;
//
//            // Makes sure that the InputStream is closed after the app is
//            // finished using it.
//        }
//        finally
//        {
//            if (is != null)
//            {
//                is.close();
//            }
//        }
//    }
//    public class DownloadWebpageTask extends AsyncTask<String, Void, String>
//    {
//        String _webText = "";
//        @Override
//        protected String doInBackground(String... urls)
//        {
//
//            // params comes from the execute() call: params[0] is the url.
//            try
//            {
//                _webText = downloadUrl(urls[0]);
//                return _webText;
//            }
//            catch (IOException e)
//            {
//                return "Unable to retrieve web page. URL may be invalid.";
//            }
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result)
//        {
//        }
//    }
//    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException
//    {
//        Reader reader = null;
//        reader = new InputStreamReader(stream, "UTF-8");
//        char[] buffer = new char[len];
//        reader.read(buffer);
//        return new String(buffer);
//    }
//
//
//
//    //=====================================================================================================================================================
//    // andengine
//    @Override
//    protected void onCreateResources()
//    {
//        _managerResource = new Manager_resource(this);
//    }
//
//    @Override
//    protected Scene onCreateScene()
//    {
//        this.mEngine.registerUpdateHandler(new FPSLogger());
//
//        this._scene = new Scene();
//        this._scene.setBackground(new Background(0, 0, 0));
//        this._scene.setOnSceneTouchListener(this);
//
//        this._physicsWorld = new PhysicsWorld(new com.badlogic.gdx.math.Vector2(0, 0), false);
//
//        this._scene.registerUpdateHandler(this._physicsWorld);
//        this._scene.registerUpdateHandler(this);
//
//        return null;
//    }
//
//    @Override
//    public EngineOptions onCreateEngineOptions()
//    {
//
//        return null;
//    }
//    @Override
//    public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
//
//    }
//
//    @Override
//    public void onAccelerationChanged(AccelerationData pAccelerationData) {
//
//    }
//
//    @Override
//    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
//        return false;
//    }
//    @Override
//    public void onUpdate(float pSecondsElapsed) {
//
//    }
//
//    @Override
//    public void reset()
//    {
//        exit();
//    }
//
//    // end andengine
//    //=====================================================================================================================================================
//};
//// regulation info
//class Info_regulation
//{
//    public int _regulation_seekBar_0;
//    public int _regulation_seekBar_1;
//    public int _regulation_seekBar_2;
//    public int _regulation_seekBar_3;
//    public int _regulation_seekBar_smileEffect;
//    public float _plusMoverRadius;
//
//    public Info_regulation()
//    {
//        _regulation_seekBar_0 = 100000;
//        _regulation_seekBar_1 = 1000000000;
//        _regulation_seekBar_2 = 6;
//        _regulation_seekBar_3 = 100;
//        _regulation_seekBar_smileEffect = 10000;
//        _plusMoverRadius = 2f; //0.1f
//    }
//}