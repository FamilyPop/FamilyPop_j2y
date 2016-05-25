package com.j2y.familypop.activity;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_contents;
import com.j2y.familypop.activity.manager.Manager_resource;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.familypop.activity.manager.actors.Actor_good;
import com.j2y.familypop.activity.manager.actors.Actor_smile;
import com.j2y.familypop.activity.manager.actors.Actor_talk;
import com.j2y.familypop.activity.manager.actors.BaseActor;
import com.j2y.familypop.server.FpsTalkUser;
import com.j2y.network.server.FpNetFacade_server;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lsh on 2016-05-02.
 */
public class Activity_serverMain_andEngine extends SimpleBaseGameActivity implements IUpdateHandler ,IOnSceneTouchListener
{
    public static Activity_serverMain_andEngine Instance = null;
    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 480;

    // test
    Actor_attractor a1 = null;
    Actor_attractor a2 = null;
    Actor_attractor a3 = null;
    Actor_attractor a4 = null;



    // andengine
    private PhysicsWorld _physicsWorld = null;
    private Scene _scene;
    private Camera _camera;

    // manager

    private Manager_resource _manager_resource;
    private Manager_actor _manager_actor;
    private Manager_contents _manager_contents;

    // user pos
    private ArrayList<Vector2> _userStartPos;

    // image names
    private ArrayList<String> _userImageNames = null;
    // Info_regulation
    private Info_regulation info_regulation = null;
    public Info_regulation GetInfo_regulation(){return info_regulation;}
    //====================================================================================================
    // andengine init
    boolean _scheduleEngineStart;
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions)
    {
        Engine engine = new Engine(pEngineOptions);
        if(_scheduleEngineStart){
            engine.start();
            _scheduleEngineStart = !_scheduleEngineStart;
        }
        return engine;
    }

    @Override
    public synchronized void onResumeGame() {
        if(mEngine != null) {
            super.onResumeGame();
            _scheduleEngineStart = true;
        }
    }
    @Override
    protected void onCreateResources()
    {
        _manager_resource = new  Manager_resource(this);
    }
    @Override
    protected Scene onCreateScene()
    {
        mEngine.registerUpdateHandler(new FPSLogger());

        _scene = new Scene();
        _scene.setBackground(new Background(0, 0, 0));
        _scene.setOnSceneTouchListener(this);

        this._physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);

        _scene.registerUpdateHandler(_physicsWorld);
        _scene.registerUpdateHandler(this);

        // manager
        _manager_actor = new Manager_actor(_scene,_physicsWorld);
        _manager_contents = new Manager_contents(true);
        _manager_contents.Instance.Content_change(Manager_contents.eType_contents.CONTENTS_READY);



        // andEngein_test
        //a1 = create_attractor(_userStartPos.get(0).x, _userStartPos.get(0).y, "user-01.png");
        //a2 = create_attractor(_userStartPos.get(1).x, _userStartPos.get(1).y, "user-02.png");
        //a3 = create_attractor(_userStartPos.get(2).x, _userStartPos.get(2).y, "user-03.png");
        //a4 =create_attractor(_userStartPos.get(3).x, _userStartPos.get(3).y, "user-04.png");


        Activity_serverMain_andEngine.Instance.Init_attractor();

        return _scene;
    }
//    @Override
//    public Engine onCreateEngine(EngineOptions pEngineOptions)
//    {
//        Engine engine = new Engine(pEngineOptions);
//
//        return engine;
//    }
    @Override
    public EngineOptions onCreateEngineOptions()
    {
        EngineOptions ret = null;

        _camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
         ret = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), _camera);

        return ret;
    }
    // andEngine update
    @Override
    public  synchronized void onUpdate(float pSecondsElapsed)
    {
        // 컨텐츠 업데이트
        if( _manager_contents != null) _manager_contents.update();

        // talk
        ArrayList<BaseActor> talks = Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_TALK);
        for( BaseActor actor : talks){ actor.onUpdate(pSecondsElapsed); }

        // update smile
        ArrayList<BaseActor> smiles = Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_SMILE);
        for( BaseActor actor : smiles ){ actor.onUpdate(pSecondsElapsed);}

        // attractor
        ArrayList<BaseActor> attractors = Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_ATTRACTOR);
        for( BaseActor actor : attractors ){ actor.onUpdate(pSecondsElapsed); }

        // good
        ArrayList<BaseActor> good = Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_GOOD);
        for( BaseActor actor : good){actor.onUpdate(pSecondsElapsed);}

//        // update good
//        ArrayList<BaseActor> good = mManagerActor.GetActorsList(eType_actor.ACTOR_GOOD);
//        int count_event = good.size();
//        for (int i = 0; i < count_event; ++i) {
//            ((Actor_good) good.get(i)).onUpdate(pSecondsElapsed);
//        }
//

    }

    @Override
    public void reset()
    {

    }
    //====================================================================================================
    // activity init
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Instance = this;

        _userStartPos = new ArrayList<Vector2>();
        _userStartPos.add(new Vector2(10, (CAMERA_HEIGHT/2) - 48));
        _userStartPos.add(new Vector2((CAMERA_WIDTH/2) - 24, 10));
        _userStartPos.add(new Vector2((CAMERA_WIDTH-10 ) - 96, (CAMERA_HEIGHT/2) - 48));
        _userStartPos.add(new Vector2((CAMERA_WIDTH / 2) - 24, (CAMERA_HEIGHT - 10) - 96));

        info_regulation = new Info_regulation();
    }
    public void Init_attractor()
    {
        if (_userImageNames == null )
        {
            _userImageNames = new ArrayList<String>();
            _userImageNames.add("user-01.png");
            _userImageNames.add("user-02.png");
            _userImageNames.add("user-03.png");
            _userImageNames.add("user-04.png");
        }
        Manager_users magUsers = Manager_users.Instance;

        int posCount = 0;
        for( FpsTalkUser user :  magUsers.Get_talk_users().values() )
        {
            user._uid_attractor = create_attractor(_userStartPos.get(posCount).x, _userStartPos.get(posCount).y, _userImageNames.get(posCount)).Get_UniqueNumber();
            posCount++;
        }
    }
    public void release_attractor()
    {
        Manager_users magUsers = Manager_users.Instance;
        Manager_actor magActor = Manager_actor.Instance;
        for( FpsTalkUser user :  magUsers.Get_talk_users().values() )
        {
            magActor.Destroy_attractor(user._uid_attractor);
        }

    }
    // activity exit
    //====================================================================================================
    // create actor
    //====================================================================================================
    private Actor_attractor create_attractor(float x, float y, String imageName)
    {
        AnimatedSprite face;
        //face = new AnimatedSprite(x, y, _manager_resource.GetTiledTexture(imageName), this.getVertexBufferObjectManager());
        //face.setScale(0.6f, 0.6f);
        face = create_animatedSprite(x, y, imageName);
        return _manager_actor.Create_attractor(_scene, _physicsWorld, face);
    }
    // talk
    private Actor_talk create_talk(float x, float y, String imageName, Actor_attractor attractor)
    {
        Actor_talk ret = null;
        AnimatedSprite face = create_animatedSprite(x, y, imageName);
        ret = _manager_actor.Create_talk(_scene, _physicsWorld, face, attractor);
        return ret;
    }
    public Actor_talk Create_talk(String imageName, String flowerName, Actor_attractor attractor)
    {
        float x = CAMERA_WIDTH /2 -24;
        float y = CAMERA_HEIGHT /2-48;

        //float x = CAMERA_WIDTH/2;
        //float y = CAMERA_HEIGHT/2;

        Actor_talk ret = null;
        AnimatedSprite face = null;
        AnimatedSprite flower = null;

        Vector2 newPos = new Vector2();
        newPos.x = attractor.Get_Sprite().getX() - x;
        newPos.y = attractor.Get_Sprite().getY() - y;
//
        newPos = newPos.nor();
        newPos.x *=70f;
        newPos.y *=70f;
//
        face = create_animatedSprite(x + newPos.x, y + newPos.y, imageName);
        flower = create_animatedSprite(0, 0, flowerName);
        face.setScale(0.3f, 0.3f);
        flower.setScale(0, 0);
        flower.setZIndex(-1);


        ret = Manager_actor.Instance.Create_talk(_scene, _physicsWorld, face, attractor);
        ret.Get_Sprite().attachChild(flower);

        return ret;
    }
    // smile
    public Actor_smile Create_smile(float x, float y, String flowerName, Actor_attractor attractor)
    {
        Actor_smile ret = null;

        AnimatedSprite face = null;
        AnimatedSprite flower = null;

        face = create_animatedSprite(x, y, "smile_k-01-01.png");
        flower = create_animatedSprite(0, 0, flowerName);

        face.setScale(0.6f, 0.6f);
        flower.setScale(0, 0);
        flower.setZIndex(-1);

        ret = Manager_actor.Instance.Create_smile(_scene, _physicsWorld, face, attractor);
        ret.Get_Sprite().attachChild(flower);


        return ret;
    }

    // good
    public Actor_good Create_good(float x, float y, String flowerName, Actor_attractor attractor)
    {
        Actor_good ret = null;

        AnimatedSprite face = null;
        AnimatedSprite flower = null;

        face = create_animatedSprite(x, y, "heart_k-01-01.png");
        flower = create_animatedSprite(0, 0,flowerName);

        face.setScale(0.6f, 0.6f);
        flower.setScale(0, 0);
        flower.setZIndex(-1);

        ret = Manager_actor.Instance.Create_good(_scene, _physicsWorld, face, attractor);
        ret.Get_Sprite().attachChild(flower);

        return ret;
    }
    public Actor_good Create_good(Actor_attractor from, Actor_attractor to)
    {
        Actor_good ret = null;

        Vector2 f = from.Get_Body().getPosition();
        f.x *= PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
        f.y *= PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

        FpsTalkUser fromUser =  null;

        for(FpsTalkUser user : Manager_users.Instance.Get_talk_users().values())
        {
            if( user._uid_attractor == from.Get_UniqueNumber() )
            {
                fromUser = user;
            }
        }

        String petalName = Manager_resource.Instance.Get_petalNames(Manager_resource.eImageIndex_color.IntToImageColor(fromUser._net_client._clientID), Manager_resource.eType_petal.PETAL_GOOD);
        Create_good(f.x, f.y, petalName, to);

        return ret;
    }
    public Actor_good Create_good(int clientIdFrom, int clientIdTo)
    {
        Actor_good ret = null;

        Actor_attractor from = get_attractorToClientId(clientIdFrom);
        Actor_attractor to = get_attractorToClientId(clientIdTo);

        FpsTalkUser fromUser = null;

        Vector2 f = from.Get_Body().getPosition();
        f.x *= PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
        f.y *= PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

        for(FpsTalkUser user : Manager_users.Instance.Get_talk_users().values()){
            if( user._uid_attractor == from.Get_UniqueNumber() ){ fromUser = user; }
        }

        String petalName = Manager_resource.Instance.Get_petalNames(Manager_resource.eImageIndex_color.IntToImageColor(fromUser._net_client._clientID), Manager_resource.eType_petal.PETAL_GOOD);
        Create_good(f.x, f.y, petalName, to);


        return ret;
    }
    private Actor_attractor get_attractorToClientId(int clientId)
    {
        Actor_attractor ret = null;

        for( FpsTalkUser user : Manager_users.Instance.Get_talk_users().values())
        {
            if( user._net_client._clientID == clientId )
            {
                for( BaseActor actor : Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_ATTRACTOR))
                {
                    if( actor.Get_UniqueNumber() == user._uid_attractor)
                    {
                        ret = (Actor_attractor)actor;
                        break;
                    }
                }
            }
        }

        return ret;
    }
    // sprite
    private AnimatedSprite create_animatedSprite(float x, float y, String imageName){
        AnimatedSprite face = null;
        face = new AnimatedSprite(x, y, _manager_resource.GetTiledTexture(imageName), this.getVertexBufferObjectManager());
        face.setScale(0.6f, 0.6f);
        return face;
    }
    //====================================================================================================
    // event
    //====================================================================================================
    public synchronized void OnEvent_smile()
    {
        for( FpsTalkUser user : Manager_users.Instance.Get_talk_users().values())
        {
            //String fileName = Manager_resource.Instance.Get_userImage(Manager_resource.eImageIndex_color.IntToImageColor(user._net_client._clientID));
            String fileName = Manager_resource.Instance.Get_petalNames(Manager_resource.eImageIndex_color.IntToImageColor(user._net_client._clientID), Manager_resource.eType_petal.PETAL_SMILE);
            Actor_attractor attractor = Manager_actor.Instance.Get_attractor(user._uid_attractor);
            Actor_smile actor = Create_smile(CAMERA_HEIGHT/2 - 24, CAMERA_HEIGHT/2 - 48,fileName,attractor);
            actor.Set_maxFlowerScale(1.3f);
        }
    }
    //====================================================================================================
    // bubble 컨트롤.
    //====================================================================================================
    public void MoveUserBubble_add(float x, float y, int userId)
    {
        FpsTalkUser user = Manager_users.Instance.FindTalkUser_byId(userId);
        Actor_attractor attractor = Manager_actor.Instance.Get_attractor(user._uid_attractor);
        attractor.Set_addPostion(x, y);
    }
    //====================================================================================================
    // system event
    //====================================================================================================
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {
        Log.i("[J2Y]","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        if( this._physicsWorld != null)
        {
            ArrayList<BaseActor> attractors = _manager_actor.GetActorsList(Manager_actor.eType_actor.ACTOR_ATTRACTOR);
            int count_attractor = attractors.size();
            for( int i=0; i<count_attractor; ++i)
            {
                ((Actor_attractor)attractors.get(i)).onTouch(pScene, pSceneTouchEvent);
            }
            FpNetFacade_server.Instance.Send_clientUpdate();
        }

        return true;
    }
    Actor_talk testbubble =null;
    @Override
    public synchronized boolean onKeyUp(int keyCode, KeyEvent event)
    {
        FpsTalkUser user = null;
        AtomicReference<FpsTalkUser> ref = new AtomicReference<>();
        Manager_users.Instance.FindTalkUser_byId(0, ref);
        user = ref.get();

        //FpsTalkUser user =   Manager_users.Instance.FindTalkUser_byId(0);
        if( user != null)
        {
            Actor_attractor attractor = Manager_actor.Instance.Get_attractor(user._uid_attractor);
            testbubble = Activity_serverMain_andEngine.Instance.Create_talk("user-01.png","talk_petal-01.png", attractor);
            testbubble.StartMover(0);
        }

        return false;
    }
    //
    //====================================================================================================
    // class s
    //====================================================================================================
    public class Info_regulation
    {
        public int _regulation_seekBar_0;
        public int _regulation_seekBar_1;
        public int _regulation_seekBar_2;
        public int _regulation_seekBar_3;
        public int _regulation_seekBar_smileEffect;
        public float _plusMoverRadius;

        public Info_regulation()
        {
            _regulation_seekBar_0 = 100000;
            _regulation_seekBar_1 = 1000000000;
            _regulation_seekBar_2 = 6;
            _regulation_seekBar_3 = 100;
            _regulation_seekBar_smileEffect = 10000;
            _plusMoverRadius = 0.1f; //0.1f
        }
    }
}
