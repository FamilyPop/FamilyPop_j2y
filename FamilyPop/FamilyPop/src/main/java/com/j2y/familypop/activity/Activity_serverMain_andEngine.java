package com.j2y.familypop.activity;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.facebook.internal.Utility;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_contents;
import com.j2y.familypop.activity.manager.Manager_resource;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.familypop.activity.manager.actors.Actor_good;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBee;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBeeClam;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBeeClamPair;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBeeExplosion;
import com.j2y.familypop.activity.manager.actors.Actor_smile;
import com.j2y.familypop.activity.manager.actors.Actor_talk;
import com.j2y.familypop.activity.manager.actors.BaseActor;
import com.j2y.familypop.activity.manager.states.State_ActorMove;
import com.j2y.familypop.activity.manager.states.State_ActorRotationAxis;
import com.j2y.familypop.activity.manager.states.State_machine;
import com.j2y.familypop.activity.server.event_server.BaseEvent;
import com.j2y.familypop.activity.server.event_server.Event_createBee;
import com.j2y.familypop.activity.server.event_server.Event_createBeeClam;
import com.j2y.familypop.activity.server.event_server.Event_createBeeClamPair;
import com.j2y.familypop.activity.server.event_server.Event_createBeeExplosion;
import com.j2y.familypop.activity.server.event_server.Event_createGood;
import com.j2y.familypop.activity.server.event_server.Event_createSmile;
import com.j2y.familypop.activity.server.event_server.Event_deleteBee;
import com.j2y.familypop.activity.server.event_server.Event_deleteBeeExplosion;
import com.j2y.familypop.activity.server.event_server.Event_mulCollider;
import com.j2y.familypop.activity.server.event_server.Event_serverClose;
import com.j2y.familypop.server.FpsRoot;
import com.j2y.familypop.server.FpsTalkUser;
import com.j2y.network.server.FpNetFacade_server;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationAtModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.math.MathUtils;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lsh on 2016-05-02.
 */
public class Activity_serverMain_andEngine extends SimpleBaseGameActivity implements IUpdateHandler, IOnSceneTouchListener {
    public static Activity_serverMain_andEngine Instance = null;
    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 480;

    // 서버 이벤트.
    public static final int event_createTalk = 0;
    public static final int event_deleteTalk = 1;
    public static final int event_createSmile = 2;
    public static final int event_deleteSmile = 3;
    public static final int event_createGood = 4;
    public static final int event_deleteGood = 5;
    public static final int event_createBee = 6;
    public static final int event_deleteBee = 7;
    public static final int event_shareImage = 8;
    public static final int event_shareImage_end = 9;

    public static final int event_createBeeExplosion = 10;
    public static final int event_deleteBeeExplosion = 11;

    public static final int event_createBeeClam = 12;
    public static final int event_deleteBeeClam = 13;

    public static final int event_createBeeClamPair = 14;
    public static final int event_deletebeeClamPair = 15;

    //ShareImage
    public static final int event_deleteShareImage = 97;

    public static final int event_mulCollider = 98;
    public static final int event_serverClose = 99;

    // test
//    Actor_attractor a1 = null;
//    Actor_attractor a2 = null;
//    Actor_attractor a3 = null;
//    Actor_attractor a4 = null;

    State_machine _testStateMachine = null;

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

    // Info_regulation
    private Info_regulation info_regulation = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public Info_regulation GetInfo_regulation() {
        return info_regulation;
    }

    public Scene Get_scene() {
        return _scene;
    }

    public PhysicsWorld Get_physicsWorld() {
        return _physicsWorld;
    }

    // message queue
    //PriorityBlockingQueue<BaseEvent> _eventQueue = null;
    ArrayList<BaseEvent> _eventQueue = null;

    // font
    private Font _font;
    private Text _text;
    //====================================================================================================
    // andengine init
    boolean _scheduleEngineStart;

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {

        Engine engine = new Engine(pEngineOptions);
        if (_scheduleEngineStart) {
            engine.start();
            _scheduleEngineStart = !_scheduleEngineStart;
        }

        //_eventQueue = new PriorityBlockingQueue<>();
        _eventQueue = new ArrayList<>();

        return engine;
    }

    @Override
    public synchronized void onResumeGame() {
        if (mEngine != null) {
            super.onResumeGame();
            _scheduleEngineStart = true;
        }
    }

    @Override
    protected void onCreateResources() {
        _manager_resource = new Manager_resource(this);
    }

    @Override
    protected Scene onCreateScene() {
        mEngine.registerUpdateHandler(new FPSLogger());

        _scene = new Scene();
        _scene.setBackground(new Background(0, 0, 0));
        _scene.setOnSceneTouchListener(this);

        this._physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);

        _scene.registerUpdateHandler(_physicsWorld);
        _scene.registerUpdateHandler(this);

        // manager
        _manager_actor = new Manager_actor(_scene, _physicsWorld);
        _manager_contents = new Manager_contents(true);
        _manager_contents.Instance.Content_change(Manager_contents.eType_contents.CONTENTS_READY);

//        // andEngein_test
//        //a1 = create_attractor(CAMERA_WIDTH/2 - 100, CAMERA_HEIGHT/2 - 100, "talk_petal-01.png");
//        a1 = create_attractor(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "talk_petal-01.png");
//        //a2 = create_attractor(_userStartPos.get(1).x, _userStartPos.get(1).y, "user-02.png");
//        a3 = create_attractor(_userStartPos.get(2).x, _userStartPos.get(2).y, "user-03.png");
//        a4 = create_attractor(_userStartPos.get(3).x, _userStartPos.get(3).y, "user-04.png");

        Activity_serverMain_andEngine.Instance.Init_attractor();


//        final RotationAtModifier rotMod = new RotationAtModifier(30,0,180,CAMERA_WIDTH/2,CAMERA_HEIGHT/2);
//        a1.Get_Sprite().registerEntityModifier(rotMod);

//        final RotationByModifier rotMod = new RotationByModifier(1, 360);
//        a1.Get_Sprite().registerEntityModifier(rotMod);

        //RotationModifier rotMod = new RotationModifier(1, 0, -360);
        //a1.Get_Sprite().registerEntityModifier(rotMod);

//        _testStateMachine = new State_machine();
//
//
//        _testStateMachine.Add_State(new State_ActorRotationAxis(a1, a3, a4,  2, true));
        //back
//        //_testStateMachine.Add_State(new State_ActorMove(a1, a3, 0.1f, 3));
//        _testStateMachine.Add_State(new State_ActorRotationAxis(a1, a3.Get_Body().getPosition(), 5, true));
//
//        //_testStateMachine.Add_State(new State_ActorMove(a1, a4, 0.1f, 3));
//        _testStateMachine.Add_State(new State_ActorRotationAxis(a1, a4.Get_Body().getPosition(), 5, false));
//
//        //_testStateMachine.Add_State(new State_ActorMove(a1, a3, 0.1f, 3));
//        _testStateMachine.Add_State(new State_ActorRotationAxis(a1, a3.Get_Body().getPosition(), 5, true));
//
//        //_testStateMachine.Add_State(new State_ActorMove(a1, a4, 0.1f, 3));
//        _testStateMachine.Add_State(new State_ActorRotationAxis(a1, a4.Get_Body().getPosition(), 5, false));

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
    public EngineOptions onCreateEngineOptions() {
        EngineOptions ret = null;

        _camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        ret = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), _camera);

        return ret;
    }

    //=====================================================================================================================================
    // andEngine update
//    float _test_sin = 0.0f;
//    private final int a1move= 0;
//    private final int a1curve= 1;
//    private boolean testboolean = true;
//    private int _testState = a1move;
//    private double _testUpdateCount = 1;



    @Override
    public synchronized void onUpdate(float pSecondsElapsed) {
        // 컨텐츠 업데이트
//        if (_manager_contents == null) return;
//        if (Manager_actor.Instance == null) return;
//        if (FpsRoot.Instance._exitServer) return;

        event_surveillant();

        _manager_contents.update();

        if (_manager_actor != null) _manager_actor.Update(pSecondsElapsed);


    }
    @Override
    public void reset() {

    }

    //====================================================================================================
    // activity init
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance = this;

        _userStartPos = new ArrayList<Vector2>();
        _userStartPos.add(new Vector2(10, (CAMERA_HEIGHT / 2) - 48));
        _userStartPos.add(new Vector2((CAMERA_WIDTH / 2) - 24, 10));
        _userStartPos.add(new Vector2((CAMERA_WIDTH - 10) - 96, (CAMERA_HEIGHT / 2) - 48));
        _userStartPos.add(new Vector2((CAMERA_WIDTH / 2) - 24, (CAMERA_HEIGHT - 10) - 96));

        info_regulation = new Info_regulation();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void Init_attractor() {

        Manager_users magUsers = Manager_users.Instance;

        //int posCount = 0;
        for (FpsTalkUser user : magUsers.Get_talk_users().values()) {
            Actor_attractor attractor = create_attractor(_userStartPos.get(user._net_client._clientID).x, _userStartPos.get(user._net_client._clientID).y,
                    Manager_resource.Instance.Get_userImage(Manager_resource.eImageIndex_color.IntToImageColor(user._net_client._clientID)));
            user._uid_attractor = attractor.Get_UniqueNumber();
            attractor.Set_colorId(user._net_client._clientID);
            //posCount++;
        }

        draw_ipAddress(FpsRoot.Instance._serverIP);
    }

    //===================================================================================================
    // release actor
    //ACTOR_NON(-1), ACTOR_ATTRACTOR(0), ACTOR_TALK(1), ACTOR_BEE(2), ACTOR_SMILE(3), ACTOR_GOOD(4);
    //Manager_users 가 살아 있어야함.
    public void release_attractor() {
        Manager_users magUsers = Manager_users.Instance;
        Manager_actor magActor = Manager_actor.Instance;

        if (magUsers == null || magActor == null) return;
        for (FpsTalkUser user : magUsers.Get_talk_users().values()) {
            magActor.Destroy_attractor(user._uid_attractor);
        }
    }

    public void release_talk() {
        Manager_actor magActor = Manager_actor.Instance;

        if (magActor == null) return;

        CopyOnWriteArrayList<BaseActor> talks = magActor.GetActorsList(Manager_actor.eType_actor.ACTOR_TALK);
        int count = talks.size();

        while (count != 0) {
            magActor.Destroy_talk((Actor_talk) talks.get(0));
            count--;
        }

    }

    public void release_smile() {
        Manager_actor magActor = Manager_actor.Instance;
        if (magActor == null) return;

        CopyOnWriteArrayList<BaseActor> smile = magActor.GetActorsList(Manager_actor.eType_actor.ACTOR_SMILE);
        int count = smile.size();

        while (count != 0) {
            magActor.Destroy_smile((Actor_smile) smile.get(0));
            count--;
        }
    }

    public void release_good() {
        Manager_actor magActor = Manager_actor.Instance;
        if (magActor == null) return;

        CopyOnWriteArrayList<BaseActor> good = magActor.GetActorsList(Manager_actor.eType_actor.ACTOR_GOOD);
        int count = good.size();

        while (count != 0) {
            synchronized (good) {
                magActor.Destroy_good((Actor_good) good.get(0));
                count--;
            }
        }
    }

    public void release_bee() {
        Manager_actor magActor = Manager_actor.Instance;
        if (magActor == null) return;

        CopyOnWriteArrayList<BaseActor> bee = magActor.GetActorsList(Manager_actor.eType_actor.ACTOR_BEE);
        int count = bee.size();

        while (count != 0) {
            magActor.Destroy_honeyBee((Actor_honeyBee) bee.get(0));
            count--;
        }
    }

    // activity exit
    //====================================================================================================
    // create actor
    //====================================================================================================
    // Actor_honeyBee
    private Actor_honeyBee create_honeybee(float x, float y, String tileImageName) {
        Actor_honeyBee ret = null;

        AnimatedSprite face;
        Manager_resource manager_resource = Manager_resource.Instance;
        Manager_actor manager_actor = Manager_actor.Instance;

        face = new AnimatedSprite(x, y, manager_resource.GetTiledTexture(tileImageName), this.getVertexBufferObjectManager());
        ret = manager_actor.Create_honeyBee(_scene, _physicsWorld, face);

        return ret;
    }
    private Actor_honeyBeeClam create_honeybeeClam(float x, float y, String tileImageName)
    {
        Actor_honeyBeeClam ret = null;

        AnimatedSprite face;
        Manager_resource manager_resource = Manager_resource.Instance;
        Manager_actor manager_actor = Manager_actor.Instance;

        face = new AnimatedSprite(x, y, manager_resource.GetTiledTexture(tileImageName), this.getVertexBufferObjectManager());
        ret = manager_actor.Create_honeyBeeClam(_scene, _physicsWorld, face);

        return ret;
    }
    private Actor_honeyBeeClamPair create_honeybeeClamPair(float x, float y, String tileImageName)
    {
        Actor_honeyBeeClamPair ret = null;

        AnimatedSprite face;
        Manager_resource manager_resource = Manager_resource.Instance;
        Manager_actor manager_actor = Manager_actor.Instance;

        face = new AnimatedSprite(x, y, manager_resource.GetTiledTexture(tileImageName), this.getVertexBufferObjectManager());
        ret = manager_actor.Create_honeyBeeClamPair(_scene, _physicsWorld, face);

        return ret;
    }

    public Actor_honeyBee Create_honeybee(float x, float y, String tileImageName) {
        return create_honeybee(x, y, tileImageName);
    }
    public Actor_honeyBeeClam Create_honeybeeClam(float x, float y, String tileImageName)
    {return create_honeybeeClam(x, y, tileImageName) ;}
    public Actor_honeyBeeClamPair Create_honeybeeClamPair(float x, float y, String tileImageName)
    {return create_honeybeeClamPair(x, y, tileImageName); }


    // Actor_honeyBeeExplosion
    private Actor_honeyBeeExplosion create_honeybeeExplosion(float x, float y, String tileImageName) {
        Actor_honeyBeeExplosion ret = null;

        AnimatedSprite face;
        Manager_resource manager_resource = Manager_resource.Instance;
        Manager_actor manager_actor = Manager_actor.Instance;

        ITiledTextureRegion textureRegion = manager_resource.GetTiledTexture(tileImageName);
        face = new AnimatedSprite(x - (textureRegion.getWidth() / 2) + 27, y - (textureRegion.getHeight() / 2) + 27, textureRegion, this.getVertexBufferObjectManager());
        face.setScale(1.0f);
        ret = manager_actor.Create_honeyBeeExplosion(_scene, _physicsWorld, face);

        return ret;
    }

    public Actor_honeyBeeExplosion Create_honeybeeExplosion(float x, float y, String tileImageName) {
        return create_honeybeeExplosion(x, y, tileImageName);
    }

    // attarctor
    private Actor_attractor create_attractor(float x, float y, String imageName) {
        //AnimatedSprite face;
        //face = new AnimatedSprite(x, y, _manager_resource.GetTiledTexture(imageName), this.getVertexBufferObjectManager());
        //face.setScale(0.6f, 0.6f);
        //face = create_animatedSprite(x, y, imageName);

        Sprite face = null;
        face = create_sprite(x, y, imageName);
        return _manager_actor.Create_attractor(_scene, _physicsWorld, face);
    }

    // talk
    private Actor_talk create_talk(float x, float y, String imageName, Actor_attractor attractor) {
        Actor_talk ret = null;
        //AnimatedSprite face = create_animatedSprite(x, y, imageName);
        //ret = _manager_actor.Create_talk(_scene, _physicsWorld, face, attractor);

        Sprite face = create_sprite(x, y, imageName);
        ret = _manager_actor.Create_talk(_scene, _physicsWorld, face, attractor);
        return ret;
    }

    public Actor_talk Create_talk(String imageName, String flowerName, Actor_attractor attractor) {
        float x = CAMERA_WIDTH / 2 - 24;
        float y = CAMERA_HEIGHT / 2 - 48;

        //float x = CAMERA_WIDTH/2;
        //float y = CAMERA_HEIGHT/2;

        Actor_talk ret = null;
        //AnimatedSprite face = null;
        //AnimatedSprite flower = null;
        Sprite face = null;
        Sprite flower = null;

        Vector2 newPos = new Vector2();
        newPos.x = attractor.Get_Sprite().getX() - x;
        newPos.y = attractor.Get_Sprite().getY() - y;
//
        newPos = newPos.nor();
        newPos.x *= 70f;
        newPos.y *= 70f;
//
        //face = create_animatedSprite(x + newPos.x, y + newPos.y, imageName);
        //flower = create_animatedSprite(0, 0, flowerName);

        face = create_sprite(x + newPos.x, y + newPos.y, imageName);
        flower = create_sprite(0, 0, flowerName);
        flower.setPosition((-flower.getWidth() / 2) + (face.getWidth() / 2),
                (-flower.getHeight() / 2) + (face.getHeight() / 2));

        //face.setScale(0.6f, 0.6f);
        face.setScale(info_regulation._flowerMinSize);
        flower.setScale(0.7f, 0.7f);
        flower.setZIndex(-1);

        ret = Manager_actor.Instance.Create_talk(_scene, _physicsWorld, face, attractor);
        ret.Get_Sprite().attachChild(flower);
        ret.Set_maxFlowerScale(0.7f);

        ret.Set_addScale(info_regulation._flowerPlusSize);
        ret.Set_maxScale(info_regulation._flowerMaxSize);


        return ret;
    }

    // smile
    public Actor_smile Create_smile(float x, float y, String flowerName, Actor_attractor attractor) {
        Actor_smile ret = null;

//        AnimatedSprite face = null;
//        AnimatedSprite flower = null;
//
//        face = create_animatedSprite(x, y, "smile_k-01-01.png");
//        flower = create_animatedSprite(0, 0, flowerName);

        Sprite face = null;
        Sprite flower = null;

        face = create_sprite(x, y, "smile_01.png");
        flower = create_sprite(0, 0, flowerName);
        flower.setPosition((-flower.getWidth() / 2) + (face.getWidth() / 2),
                (-flower.getHeight() / 2) + (face.getHeight() / 2));

        //face.setScale(0.6f, 0.6f);
        float faceScale = 0.6f;
        float flowerScale = 0.7f;


        face.setScale(faceScale);
        //flower.setScale(0.7f, 0.7f);
        flower.setScale(flowerScale);
        flower.setZIndex(-1);

        ret = Manager_actor.Instance.Create_smile(_scene, _physicsWorld, face, attractor);
        ret.Get_Sprite().attachChild(flower);

        ret.Get_Sprite().setScale(faceScale * info_regulation._flowerSmileSize);

        ret.Mul_collider(info_regulation._colliderSmileSize);

        return ret;
    }

    // good
    public Actor_good Create_good(float x, float y, String faceName, String flowerName, Actor_attractor attractor) {
        Actor_good ret = null;

//        AnimatedSprite face = null;
//        AnimatedSprite flower = null;
//
//        face = create_animatedSprite(x, y, "heart_k-01-01.png");
//        flower = create_animatedSprite(0, 0,flowerName);

        Sprite face = null;
        Sprite flower = null;

        face = create_sprite(x, y, faceName);
        flower = create_sprite(0, 0, flowerName);
        flower.setPosition((-flower.getWidth() / 2) + (face.getWidth() / 2),
                (-flower.getHeight() / 2) + (face.getHeight() / 2));

        //face.setScale(1f, 1f);
        float faceScale = 1.0f;
        float flowerScale = 0.7f;
        face.setScale(faceScale);
        //flower.setScale(0.7f, 0.7f);
        flower.setScale(flowerScale);
        flower.setZIndex(-1);

        ret = Manager_actor.Instance.Create_good(_scene, _physicsWorld, face, attractor);
        ret.Get_Sprite().attachChild(flower);
        ret.Get_Sprite().setScale(faceScale * GetInfo_regulation()._flowerGoodSize);
        //ret.Set_maxFlowerScale(0.7f);

        ret.Mul_collider(GetInfo_regulation()._colliderGoodSize);

        return ret;
    }

    public Actor_good Create_good(Actor_attractor from, Actor_attractor to) {
        Actor_good ret = null;

        Vector2 f = from.Get_Body().getPosition();
        f.x *= PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
        f.y *= PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

        FpsTalkUser fromUser = null;

        for (FpsTalkUser user : Manager_users.Instance.Get_talk_users().values()) {
            if (user._uid_attractor == from.Get_UniqueNumber()) {
                fromUser = user;
            }
        }

        String goodName = Manager_resource.Instance.Get_userLike(Manager_resource.eImageIndex_color.IntToImageColor(fromUser._net_client._clientID));
        String petalName = Manager_resource.Instance.Get_petalNames(Manager_resource.eImageIndex_color.IntToImageColor(fromUser._net_client._clientID), Manager_resource.eType_petal.PETAL_GOOD);
        ret = Create_good(f.x, f.y, goodName, petalName, to);

        return ret;
    }

    public Actor_good Create_good(int clientIdFrom, int clientIdTo) {
        Actor_good ret = null;

        Actor_attractor from = get_attractorToClientId(clientIdFrom);
        Actor_attractor to = get_attractorToClientId(clientIdTo);

        FpsTalkUser fromUser = null;

        Vector2 f = from.Get_Body().getPosition();
        f.x *= PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
        f.y *= PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

        for (FpsTalkUser user : Manager_users.Instance.Get_talk_users().values()) {
            if (user._uid_attractor == from.Get_UniqueNumber()) {
                fromUser = user;
            }
        }

        String goodName = Manager_resource.Instance.Get_userLike(Manager_resource.eImageIndex_color.IntToImageColor(fromUser._net_client._clientID));
        String petalName = Manager_resource.Instance.Get_petalNames(Manager_resource.eImageIndex_color.IntToImageColor(fromUser._net_client._clientID), Manager_resource.eType_petal.PETAL_GOOD);
        ret = Create_good(f.x, f.y, goodName, petalName, to);

        return ret;
    }

    private Actor_attractor get_attractorToClientId(int clientId) {
        Actor_attractor ret = null;

        for (FpsTalkUser user : Manager_users.Instance.Get_talk_users().values()) {
            if (user._net_client._clientID == clientId) {
                for (BaseActor actor : Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_ATTRACTOR)) {
                    if (actor.Get_UniqueNumber() == user._uid_attractor) {
                        ret = (Actor_attractor) actor;
                        break;
                    }
                }
            }
        }

        return ret;
    }

    // sprite
    private AnimatedSprite create_animatedSprite(float x, float y, String imageName) {
        AnimatedSprite face = null;
        face = new AnimatedSprite(x, y, _manager_resource.GetTiledTexture(imageName), this.getVertexBufferObjectManager());
        face.setScale(0.6f, 0.6f);
        return face;
    }

    private Sprite create_sprite(float x, float y, String imageName) {
        Sprite face = null;

        ITextureRegion spriteTexture = Manager_resource.Instance.GetSpriteTexture(imageName);
        face = new Sprite(x, y, spriteTexture, this.getVertexBufferObjectManager());
        face.setScale(1.5f, 1.5f);
        return face;
    }

    private synchronized void IteratorUpdate_Actor(CopyOnWriteArrayList<BaseActor> actors, float pSecondsElapsed) {

//        // synchronized update
//        List<BaseActor> sActor = Collections.synchronizedList(actors);
//        synchronized (sActor)
//        {
//            Iterator<BaseActor> iterator = sActor.listIterator();
//            while (iterator.hasNext())
//            {
//                ((BaseActor)iterator.next()).onUpdate(pSecondsElapsed);
//            }
//        }


        // interator update
//        Iterator<BaseActor> iterator = actors.listIterator();
//        while(iterator.hasNext())
//        {
//            ((BaseActor)iterator.next()).onUpdate(pSecondsElapsed);
//        }

//        Iterator<BaseActor> iterator = actors.listIterator();
//
//        for( Iterator<BaseActor> iter = actors.listIterator(); iter.hasNext();)
//        {
//            ((BaseActor)iter.next()).onUpdate(pSecondsElapsed);
//        }

        for (BaseActor a : actors) {
            a.onUpdate(pSecondsElapsed);
        }
    }

    //====================================================================================================
    // event
    //====================================================================================================
    public void Add_event(BaseEvent event) throws InterruptedException {
        _eventQueue.add(event);
    }

    public void OnEvent_smile() {
//        for (FpsTalkUser user : Manager_users.Instance.Get_talk_users().values()) {
//            //String fileName = Manager_resource.Instance.Get_userImage(Manager_resource.eImageIndex_color.IntToImageColor(user._net_client._clientID));
//            String fileName = Manager_resource.Instance.Get_petalNames(Manager_resource.eImageIndex_color.IntToImageColor(user._net_client._clientID), Manager_resource.eType_petal.PETAL_SMILE);
//            Actor_attractor attractor = Manager_actor.Instance.Get_attractor(user._uid_attractor);
//
//            Actor_smile actor = Create_smile(CAMERA_HEIGHT / 2 - 24, CAMERA_HEIGHT / 2 - 48, fileName, attractor);
//            actor.Set_maxFlowerScale(1.3f);
//        }
        Event_createSmile event = new Event_createSmile(Manager_users.Instance.Get_talk_users());
        try {
            Add_event(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //public Actor_honeyBee OnEvent_honeybee()
    public void OnEvent_honeybee() {
        //return create_honeybee(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "event_honeyBee");
        Event_createBee event = new Event_createBee(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "event_honeyBee");
        try {
            Add_event(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void OnEvent_honeybeeClam()
    {
        Event_createBeeClam event = new Event_createBeeClam(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "event_honeyBee");
        try {
            Add_event(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void OnEvent_honeybeeClamPair()
    {
        Event_createBeeClamPair event = new Event_createBeeClamPair(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "event_honeyBee");
        try {
            Add_event(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void OnEvent_deleteHoneybee(BaseActor actor) {
        Event_deleteBee event = new Event_deleteBee(actor);
        try {
            Add_event(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void OnEvent_createBeeExplosion(float x, float y, String tileName) {
        Event_createBeeExplosion event = new Event_createBeeExplosion(x, y, tileName);
        try {
            Add_event(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void OnEvent_deleteBeeExplosion(BaseActor actor) {
        Event_deleteBeeExplosion event = new Event_deleteBeeExplosion(actor);
        try {
            Add_event(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void OnEvent_shareimage(int clientId, int posIndex, Bitmap bitmap) // posIndex 사용 안함.
    {
//        // -1 일때 가운데 출력
//        float centerX = (CAMERA_WIDTH / 2) - (bitmap.getWidth() / 2);
//        float centerY = (CAMERA_HEIGHT / 2) - (bitmap.getHeight() / 2);
//
//        float centerHalfX = centerX / 2;
//        float centerHalfY = centerY / 2;
//
//        float leftTopX = centerX - centerHalfX;
//        float leftTopY = centerY - centerHalfY;
//
//        float rightTopX = centerX + centerHalfX;
//        float rightTopY = centerY - centerHalfY;
//
//        float leftBottomX = centerX - centerHalfX;
//        float leftBottomY = centerY + centerHalfY;
//
//        float rightBottomX = centerX + centerHalfX;
//        float rightBottomY = centerY + centerHalfY;
//
//        float posX = 0;
//        float posY = 0;
//
//        if (posIndex == -1)
//        {
//            posX = centerX;
//            posY = centerY;
//        }
//        else
//        {
//            switch (posIndex)
//            {
//                case 0:
//                    posX = leftTopX;
//                    posY = leftTopY;
//                    break;
//                case 1:
//                    posX = rightTopX;
//                    posY = rightTopY;
//                    break;
//                case 2:
//                    posX = leftBottomX;
//                    posY = leftBottomY;
//                    break;
//                case 3:
//                    posX = rightBottomX;
//                    posY = rightBottomY;
//                    break;
//            }
//        }
//        Manager_resource.Instance.Create_flashSprite(clientId, posX, posY, _scene, this, bitmap);


        // 화면을 쪼개서 적당히 배치 하자.
        float centerX = (CAMERA_WIDTH / 2) - (bitmap.getWidth() / 2);
        float centerY = (CAMERA_HEIGHT / 2) - (bitmap.getHeight() / 2);

        int countX = 5;
        int countY = 5;

        int tileX = CAMERA_WIDTH / countX;
        int tileY = CAMERA_HEIGHT / countY;

        int tileCenterX = tileX / 2;
        int tileCenterY = tileY / 2;

        int posX = 0;
        int posY = 0;

        int index = Manager_resource.Instance.GetSprite_flash().size() + (posIndex % 1);
//        if (posIndex == -1)
//        {
//            posX = (int)centerX;
//            posY = (int)centerY;
//        }
//        else
        {
            posX = (index % countX) * tileX;
            posY = (index / countX) * tileY;
        }
        Manager_resource.Instance.Create_flashSprite(clientId, posX, posY, _scene, this, bitmap);
    }

    public void OnEvent_mulCollider(Manager_actor.eType_actor type, float radius) {
//        //return create_honeybee(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "event_honeyBee");
//        Event_createBee event = new Event_createBee(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "event_honeyBee");
//        try {
//            Add_event(event);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Event_mulCollider event = new Event_mulCollider();
        event._type_actor = type;
        event._mulRadius = radius;

        try {
            Add_event(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // todo: map 에다가 다 넣어버리자.
    private void event_surveillant() {
        if (_eventQueue.size() == 0) return;

        try {
            //BaseEvent data = _eventQueue.take();
            BaseEvent data = _eventQueue.get(0);

            switch (data.Get_eventType()) {
                // talk
                case event_createTalk:
                    // 사용안함. Contents_talk 에서 직접 생성중.
                    // engine update 주기에서 생성해서 안전하다고 생각 됨.
                    break;
                case event_deleteTalk:
                    break;

                // smile
                case event_createSmile:
                    ((Event_createSmile) data).CreateSmile();
                    break;
                case event_deleteSmile:
                    break;

                // good
                case event_createGood:
                    Create_good(((Event_createGood) data).Get_data()._send_client_id, ((Event_createGood) data).Get_data()._clientid);
                    break;
                case event_deleteGood:
                    break;

                // bee
                case event_createBee:
                    create_honeybee(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "event_honeyBee");
                    break;
                case event_deleteBee:
                    ((Event_deleteBee) data).Delete();
                    break;

                case event_createBeeClam:
                    create_honeybeeClam(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "event_honeyBee");
                    break;
                case event_deleteBeeClam:
                    ((Event_deleteBee) data).Delete();
                    break;

                case event_createBeeClamPair:
                    create_honeybeeClamPair(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, "event_honeyBee");
                    break;
                case event_deletebeeClamPair:
                    ((Event_deleteBee) data).Delete();
                    break;


                // beeExplosion
                case event_createBeeExplosion:
                    ((Event_createBeeExplosion) data).Create();
                    break;
                case event_deleteBeeExplosion:
                    ((Event_deleteBeeExplosion) data).Delete();
                    break;

                // shareImage
                case event_shareImage:
                    break;
                case event_shareImage_end:
                    break;

                // system
                case event_serverClose:
                    ((Event_serverClose) data).CloseToServer();
                    break;
                case event_mulCollider:
                    ((Event_mulCollider) data).MulCollider();
                    break;
            }

            _eventQueue.remove(0);

        } catch (Exception e) {

        }
    }


    //====================================================================================================
    // bubble 컨트롤.
    //====================================================================================================
    public void MoveUserBubble_add(float x, float y, int userId) {
        FpsTalkUser user = Manager_users.Instance.FindTalkUser_byId(userId);
        Actor_attractor attractor = Manager_actor.Instance.Get_attractor(user._uid_attractor);
        attractor.Set_addPostion(x, y);
    }

    //====================================================================================================
    // system event
    //====================================================================================================
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

        if (this._physicsWorld != null) {
            CopyOnWriteArrayList<BaseActor> attractors = _manager_actor.GetActorsList(Manager_actor.eType_actor.ACTOR_ATTRACTOR);
            int count_attractor = attractors.size();
            for (int i = 0; i < count_attractor; ++i) {
                ((Actor_attractor) attractors.get(i)).onTouch(pScene, pSceneTouchEvent);
            }
            FpNetFacade_server.Instance.Send_clientUpdate();
        }

        return true;
    }

    Actor_talk testbubble = null;

    @Override
    public synchronized boolean onKeyUp(int keyCode, KeyEvent event) {
        FpsTalkUser user = null;
        AtomicReference<FpsTalkUser> ref = new AtomicReference<>();
        Manager_users.Instance.FindTalkUser_byId(0, ref);
        user = ref.get();

        //FpsTalkUser user =   Manager_users.Instance.FindTalkUser_byId(0);
        if (user != null) {
            Actor_attractor attractor = Manager_actor.Instance.Get_attractor(user._uid_attractor);
            testbubble = Activity_serverMain_andEngine.Instance.Create_talk("user-01.png", "talk_petal-01.png", attractor);
            testbubble.StartMover(0);

        }

        return false;
    }

    public void CloseServer() throws InterruptedException {

//        //FpsRoot.Instance.CloseServer();
//        //System.exit(0);
//        //MainActivity.Instance.finishFromChild(this);
//        Instance = null;
//        MainActivity.Instance._serverActivityStart = false;
//
//        // 액터를 전부 제거 한다.
////        release_talk();
////        release_smile();
////        release_good();
////        release_bee();
////        release_attractor();
//        // 유저를 전부 제거 disconnect 한다.
//        Manager_users.Instance.User_allRelease();
//        Manager_actor.Instance = null;
//
//        _manager_contents.Release_All();
//        _manager_contents = null;
//        // todo : 마저 제거 해버리자.
//        /*
//            private Manager_resource _manager_resource;
//            private Manager_contents _manager_contents;
//         */
//
//        Thread.sleep(1000);
//        _scene.detachChildren();
//        _scene.clearEntityModifiers();
//        _scene.clearTouchAreas();
//        _scene.clearUpdateHandlers();

        Event_serverClose event = new Event_serverClose();
        //_eventQueue.add(event);
        Add_event(event);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Activity_serverMain_andEngine Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.j2y.familypop.activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Activity_serverMain_andEngine Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.j2y.familypop.activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }

    //kookm0614
    //
//    @Override
//    public void onDestroy()
//    {
//        //FpsRoot.Instance._socioPhone.destroy();
//        super.onDestroy();
//    }
    //
    //====================================================================================================
    // text draw -andengine 흐름상에서만 사용해야한다.-
    //====================================================================================================
    public Text _draw_ipAddress = null;

    public void draw_ipAddress(String text) {
        release_text(_draw_ipAddress);
        _draw_ipAddress = new Text((CAMERA_WIDTH / 2) - (text.length() * 2), CAMERA_HEIGHT / 2, _manager_resource.Get_font(), text, getVertexBufferObjectManager());
        _scene.attachChild(_draw_ipAddress);
    }

    public void release_text(Text text) {
        if (text != null) {
            _scene.detachChild(text);
            _scene.unregisterTouchArea(text);
            text = null;
        }
    }

    //====================================================================================================
    // ShareImage_reposition
    //====================================================================================================
    public void ShareImage_reposition() {
        Manager_resource.Instance.ShareImage_reposition();
    }

    //====================================================================================================
    // test
    //====================================================================================================
    public float getTargetAngle(float startX, float startY, float startAngle, float targetX, float targetY) {

        float angleRad = 0.0f;

        float dX = targetX - startX;
        float dY = targetY - startY;

        float cos = (float) Math.cos(Math.toRadians(-startAngle));
        float sin = (float) Math.sin(Math.toRadians(-startAngle));

        float RotateddX = ((dX * cos) - (dY * sin));
        float RotateddY = ((dX * sin) + (dY * cos));

        angleRad = (float) Math.atan2(RotateddY, RotateddX);

        float angleDeg = (float) Math.toDegrees(angleRad);

        return angleDeg;
    }

    //====================================================================================================
    // class s
    //====================================================================================================
    public class Info_regulation {
        public int _regulation_seekBar_0;
        public int _regulation_seekBar_1;
        public int _regulation_seekBar_2;
        public int _regulation_seekBar_3;
        public int _regulation_seekBar_smileEffect;
        public float _plusMoverRadius; // 사용 안함.

        // regulation
        public int _buffer_count;
        public int _smile_effect;
        public int _voice_hold;

        // flower
        //public float _attractorMoveSpeed;
        public float _flowerPlusSize;
        public float _flowerMaxSize;
        public float _flowerMinSize;
        public float _flowerGoodSize;
        public float _flowerSmileSize;
        // flower collider
        public float _colliderTalkSize;
        public float _colliderSmileSize;
        public float _colliderGoodSize;


        public Info_regulation() {
            _regulation_seekBar_0 = 100000;
            _regulation_seekBar_1 = 1000000000;
            _regulation_seekBar_2 = 6;
            _regulation_seekBar_3 = 100;
            _regulation_seekBar_smileEffect = 10000;
            _plusMoverRadius = 0.1f; //0.1f

            _buffer_count = 6;
            _smile_effect = 10000;
            _voice_hold = 5000;

            _flowerMinSize = 0.3f;
            _flowerMaxSize = 1.5f;
            _flowerPlusSize = 1.1f;
            _flowerGoodSize = 1.0f;
            _flowerSmileSize = 1.0f;

            _colliderTalkSize = 1.0f;
            _colliderSmileSize = 1.0f;
            _colliderGoodSize = 1.0f;
        }
    }
}

