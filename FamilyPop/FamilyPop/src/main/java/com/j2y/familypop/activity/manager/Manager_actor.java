package com.j2y.familypop.activity.manager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.familypop.activity.manager.actors.Actor_good;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBee;
import com.j2y.familypop.activity.manager.actors.Actor_smile;
import com.j2y.familypop.activity.manager.actors.Actor_talk;
import com.j2y.familypop.activity.manager.actors.BaseActor;
import com.j2y.familypop.server.FpsTalkUser;


import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

/**
 * Created by lsh on 2016-04-30.
 */
// ===========================================================
// class , enum
// ===========================================================
// Activity_serverMain_andEngine 에서 생성.
public class Manager_actor
{
    public class Info_actor
    {
        private SimpleBaseGameActivity _activity = null;
        public eType_actor type_actor = eType_actor.ACTOR_NON;
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        public BodyDef.BodyType type_body = BodyDef.BodyType.DynamicBody;
        public PhysicsWorld physicsWorld = null;
        //public eType_texture type_useTexture = eType_texture.TEXTURE;
        //public AnimatedSprite animatedsprite  = null;
        public Sprite sprite  = null;
        //    public Sprite sprite = null;
        public long actor_unique_number = -1;

        public long animate = 200; // animateSprite 에서만 사용.
    }

    public enum eType_actor
    {
        ACTOR_NON(-1), ACTOR_ATTRACTOR(0), ACTOR_TALK(1), ACTOR_BEE(2), ACTOR_SMILE(3), ACTOR_GOOD(4);

        private int value;

        private eType_actor (int value) {
            this.value = value;
        }

        public int getValue () {
            return value;
        }

    }

    public static Manager_actor Instance = null;
    private PhysicsWorld _physicsWorld = null;
    private Scene _scene = null;


    // ===========================================================
    //
    // ===========================================================
    private HashMap<eType_actor, ArrayList<BaseActor>> mActors = null;
    private long actor_unique_number = 100000;
    private boolean _isUpdate = true;


    // ===========================================================
    // methods
    // ===========================================================
    public Manager_actor(Scene scene, PhysicsWorld physicsWorld)
    {
        Instance = this;
        _physicsWorld = physicsWorld;
        _scene = scene;

        // 구분자 생성.
        mActors = new HashMap<eType_actor, ArrayList<BaseActor>>();

        // 각 타입별 액터 저장소 생성.
        mActors.put(eType_actor.ACTOR_NON, new ArrayList<BaseActor>());
        mActors.put(eType_actor.ACTOR_ATTRACTOR, new ArrayList<BaseActor>());
        mActors.put(eType_actor.ACTOR_TALK, new ArrayList<BaseActor>());
        mActors.put(eType_actor.ACTOR_BEE, new ArrayList<BaseActor>());
        mActors.put(eType_actor.ACTOR_SMILE, new ArrayList<BaseActor>());
        mActors.put(eType_actor.ACTOR_GOOD, new ArrayList<BaseActor>());
    }
    public void Manager_updateAll(float pSecondsElapsed)
    {
        if( Get_isUpdate()) return;

        for( ArrayList<BaseActor> actors : mActors.values())
        {
            for( BaseActor actor : actors)
            {
                actor.onUpdate(pSecondsElapsed);
            }
        }
    }
    public void Set_isUpdate(boolean isUpdate){_isUpdate = isUpdate;}
    public boolean Get_isUpdate(){return _isUpdate;}

    private long getUniqueNumber()
    {
        long ret = actor_unique_number;
        ++actor_unique_number;
        return ret;
    }
    private Body createCircleBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyDef.BodyType pBodyType, final FixtureDef pFixtureDef, float multiplyRadius)
    {
        final float[] sceneCenterCoordinates = pAreaShape.getSceneCenterCoordinates();
        final float centerX = sceneCenterCoordinates[Constants.VERTEX_INDEX_X];
        final float centerY = sceneCenterCoordinates[Constants.VERTEX_INDEX_Y];
        return PhysicsFactory.createCircleBody(pPhysicsWorld, centerX, centerY, pAreaShape.getWidthScaled() * multiplyRadius, pAreaShape.getRotation(), pBodyType, pFixtureDef, PIXEL_TO_METER_RATIO_DEFAULT);
    }
    // ===========================================================
    // get
    // ===========================================================
    public ArrayList<BaseActor> GetActorsList(eType_actor type)
    {
        //return (ArrayList<BaseActor>)mActors.get(type).clone();
        return mActors.get(type);
    }
    public HashMap<eType_actor, ArrayList<BaseActor>> GetActors()
    {
        return mActors;
    }
    // ===========================================================
    // create actor
    // ===========================================================
    public synchronized BaseActor Create_actor(Info_actor info)
    {
        BaseActor ret = null;
        if( info.actor_unique_number == -1 ){ info.actor_unique_number = getUniqueNumber(); }
        // 생성

        final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1.0f, 0.01f, 0.8f); // 밀도 ,탄성, 마찰력

        //info.animatedsprite.setWidth( info.animatedsprite.getWidthScaled() * 1.5f);

        switch(info.type_actor)
        {
            case ACTOR_ATTRACTOR:
                ret = new Actor_attractor (createCircleBody(info.physicsWorld, info.sprite, info.type_body, objectFixtureDef, 0.5f), info.sprite, info.actor_unique_number);
                break;
            case ACTOR_TALK:
                ret = new Actor_talk (createCircleBody(info.physicsWorld, info.sprite, info.type_body, objectFixtureDef, 1.0f), info.sprite, info.actor_unique_number);
                break;
            case ACTOR_BEE:
                ret = new Actor_honeyBee(createCircleBody(info.physicsWorld, info.sprite, info.type_body, objectFixtureDef, 0.5f), info.sprite, info.actor_unique_number);
                break;
            case ACTOR_GOOD:
                ret = new Actor_good(createCircleBody(info.physicsWorld, info.sprite, info.type_body, objectFixtureDef, 0.8f), info.sprite,info.actor_unique_number);
                break;
            case ACTOR_SMILE:
                ret = new Actor_smile(createCircleBody(info.physicsWorld, info.sprite, info.type_body, objectFixtureDef, 1.0f), info.sprite,info.actor_unique_number);
                break;
        }

        //info.sprite.animate(info.animate, 1);
        //info.animatedsprite.attachChild();
        //info.animatedsprite.getChildByIndex(0).setZIndex();
        //info.animatedsprite.setZIndex();

        List<BaseActor> listActor = Collections.synchronizedList(mActors.get(info.type_actor));
        synchronized (listActor)
        {
            listActor.add(ret);
        }

        //mActors.get(info.type_actor).add(ret);

        return ret;
    }
    // todo : 이벤트와 bee 생성 함수도 만들어야 함 (Create_bee , Create_event)
    public Actor_honeyBee Create_honeyBee(Scene scene, PhysicsWorld physicsWorld, AnimatedSprite sprite)
    {
        Actor_honeyBee ret = null;

        Info_actor actorInfo = new Info_actor();
        actorInfo.type_actor = eType_actor.ACTOR_BEE;
        actorInfo.scaleX = 1;
        actorInfo.scaleY = 1;
        actorInfo.type_body = BodyDef.BodyType.StaticBody;
        actorInfo.physicsWorld = physicsWorld;
        actorInfo.sprite = sprite;

        ret = (Actor_honeyBee)Create_actor(actorInfo);

        ((AnimatedSprite)ret.Get_Sprite()).animate(100, true);
        if( ret.Get_Body() != null)
        {
            scene.attachChild(sprite);
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, ret.Get_Body(), true, true));
        }


        return ret;
    }
    public Actor_attractor Create_attractor(Scene scene, PhysicsWorld physicsWorld, Sprite sprite)
    {
        Actor_attractor ret = null;

        Info_actor actorInfo = new Info_actor();
        actorInfo.type_actor = eType_actor.ACTOR_ATTRACTOR;
        actorInfo.scaleX = 1;
        actorInfo.scaleY = 1;
        actorInfo.type_body = BodyDef.BodyType.StaticBody;
        actorInfo.physicsWorld = physicsWorld;
        actorInfo.sprite = sprite;

        ret = (Actor_attractor)Create_actor(actorInfo);

        if( ret.Get_Body() != null)
        {
            scene.attachChild(sprite);
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, ret.Get_Body(), true, true));
        }
        return ret;
    }
    public Actor_talk Create_talk( Scene scene, PhysicsWorld physicsWorld, Sprite sprite, Actor_attractor actor_attractor)
    {
        Actor_talk ret = null;

        Info_actor actorInfo = new Info_actor();

        actorInfo.type_actor = eType_actor.ACTOR_TALK;
        actorInfo.scaleX = 1;
        actorInfo.scaleY = 1;
        actorInfo.type_body = BodyDef.BodyType.DynamicBody;
        actorInfo.physicsWorld = physicsWorld;
        actorInfo.sprite = sprite;

        ret = (Actor_talk)Create_actor(actorInfo);
        ret.SetAttractor(actor_attractor);



        if( ret.Get_Body() != null)
        {
            scene.attachChild(sprite);
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, ret.Get_Body(), true, true));
        }

        return ret;
    }
    public Actor_good Create_good(Scene scene, PhysicsWorld physicsWorld, Sprite sprite, Actor_attractor actor_attractor)
    {
        Actor_good ret = null;

        Info_actor actorInfo = new Info_actor();

        actorInfo.type_actor = eType_actor.ACTOR_GOOD;
        actorInfo.scaleX = 1;
        actorInfo.scaleY = 1;
        actorInfo.type_body = BodyDef.BodyType.DynamicBody;
        actorInfo.physicsWorld = physicsWorld;
        actorInfo.sprite = sprite;

        ret = (Actor_good)Create_actor(actorInfo);
        ret.SetAttractor(actor_attractor);

        if( ret.Get_Body() != null)
        {
            scene.attachChild(sprite);
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, ret.Get_Body(), true, true));
        }

        return ret;
    }
    public Actor_smile Create_smile(Scene scene, PhysicsWorld physicsWorld, Sprite sprite, Actor_attractor actor_attractor)
    {
        Actor_smile ret = null;

        Info_actor actorInfo = new Info_actor();

        actorInfo.type_actor = eType_actor.ACTOR_SMILE;
        actorInfo.scaleX = 1;
        actorInfo.scaleY = 1;
        actorInfo.type_body = BodyDef.BodyType.DynamicBody;
        actorInfo.physicsWorld = physicsWorld;
        actorInfo.sprite = sprite;

        ret = (Actor_smile)Create_actor(actorInfo);
        ret.SetAttractor(actor_attractor);

        if( ret.Get_Body() != null)
        {
            scene.attachChild(sprite);
            physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, ret.Get_Body(), true, true));
        }

        return ret;
    }
    // random get attractor
    public Actor_attractor Get_randomAttractor()
    {
        Random random = new Random();
        int index =  Math.abs(random.nextInt() % mActors.get(eType_actor.ACTOR_ATTRACTOR).size());

        return (Actor_attractor)mActors.get(eType_actor.ACTOR_ATTRACTOR).get(index);
    }
    // get attractor
    public Actor_attractor Get_attractor(long actor_unique_number)
    {
        Actor_attractor ret = null;

        ArrayList<BaseActor> attractors = mActors.get(eType_actor.ACTOR_ATTRACTOR);
        int count_attractor = attractors.size();

        for( int i=0; i<count_attractor; ++i)
        {
            if( attractors.get(i).Get_UniqueNumber() == actor_unique_number )
            {
                ret = (Actor_attractor)attractors.get(i);
                break;
            }
        }

        return ret;
    }

    // ===========================================================
    // destroy actor
    // ===========================================================
    public boolean Destroy_attractor(long actor_unique_number)
    {
        boolean ret = false;

        int count_attractor = mActors.get(eType_actor.ACTOR_ATTRACTOR).size();
        Body select_body = null;
        Sprite select_sprite = null;
        int delect_index = -1;

        for( int i=0; i<count_attractor; ++i)
        {
            if( actor_unique_number == mActors.get(eType_actor.ACTOR_ATTRACTOR).get(i).Get_UniqueNumber())
            {
                select_body = mActors.get(eType_actor.ACTOR_ATTRACTOR).get(i).Get_Body();
                select_sprite = mActors.get(eType_actor.ACTOR_ATTRACTOR).get(i).Get_Sprite();
                delect_index = i;
                break;
            }
        }

        if( select_body != null && select_sprite != null && delect_index != -1)
        {
            final PhysicsConnector physicsConnector = _physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(select_sprite);

            _physicsWorld.unregisterPhysicsConnector(physicsConnector);
            //select_body.setActive(false);
            _physicsWorld.destroyBody(select_body);
            _scene.detachChild(select_sprite);

            mActors.get(eType_actor.ACTOR_ATTRACTOR).remove(delect_index);
            ret = true;
        }

        return ret;
    }
    //=========================================================================================================================================
    // add child sprite
    public void AttachChild(BaseActor actor, Sprite attachSprite)
    {
        actor.Get_Sprite().attachChild(attachSprite);
    }
}