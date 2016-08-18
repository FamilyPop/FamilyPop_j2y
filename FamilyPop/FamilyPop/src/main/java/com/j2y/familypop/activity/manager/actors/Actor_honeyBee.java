package com.j2y.familypop.activity.manager.actors;

import android.os.Handler;
import android.view.View;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.client.FpcRoot;
import com.j2y.familypop.server.FpsTalkUser;
import com.j2y.network.client.FpNetFacade_client;
import com.j2y.network.server.FpNetFacade_server;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.math.MathUtils;

import java.util.ArrayList;

/**
 * Created by lsh on 2016-05-31.
 */
public class Actor_honeyBee extends BaseActor {

    private BaseActor _target = null;
    private long    _connectedTime = 0;
    private int     _count_explosion = 0;

//    private ArrayList<Actor_attractor> actor_attractors = null;



    public Actor_honeyBee(Body body, Sprite sprite, long uniqueNumber) {
        super(body, sprite, uniqueNumber);
        mIsFlower = false;
        //mBody.setFixedRotation(true);

        Set_target(Manager_actor.Instance.Get_randomAttractor());
        _connectedTime = System.currentTimeMillis();

        _count_explosion = MathUtils.random(3,5);

        _actor_type = Manager_actor.eType_actor.ACTOR_BEE;
    }

    public void Set_target(BaseActor targetActor) {
        _target = targetActor;
    }

    //========================================================================================================
    // override
    @Override
    public void init()
    {
        super.init();
    }
    @Override
    public void release()
    {
        super.release();
    }
    @Override
    public synchronized boolean onUpdate(float pSecondsElapsed)
    {
        if (_target != null)
        {
            // look ak
//            Vector2 vTarget = _target.Get_Body().getPosition();
//            Vector2 vStart = mBody.getPosition();
//            float bugAngle = (float) Math.atan2((vTarget.x - vStart.x), -(vTarget.y - vStart.y));

            Vector2 target = _target.mBody.getPosition();
            Vector2 mover = mBody.getPosition();

            boolean isMove = MathUtils.distance(target.x, target.y, mover.x, mover.y) > 0.1f;

            if(isMove)
            {
                // move
                moveTo_target(pSecondsElapsed, target, 0.1f, 10 );
//                Vector2 v = new Vector2((_target.mBody.getPosition().x - mBody.getPosition().x), (_target.mBody.getPosition().y - mBody.getPosition().y));
//                v.nor();
//                v.x *= (pSecondsElapsed *  10f);
//                v.y *= (pSecondsElapsed *  10f);
//
//                v.x += mBody.getPosition().x;
//                v.y += mBody.getPosition().y;
//
//                mBody.setTransform(v, bugAngle);

                // end move
            }
            else
            {
                // explosion 조건이 만족하면 폭발 한다.
                if (_count_explosion == 0)
                {
                    // event
                    Activity_serverMain_andEngine main = Activity_serverMain_andEngine.Instance;
                    main.OnEvent_deleteHoneybee(this); // 벌을 제거.
                    main.OnEvent_createBeeExplosion(mSprite.getX(), mSprite.getY(), "event_honeyBee_explosion"); // 폭파 이벤트 생성.

                    FpsTalkUser user = Manager_users.Instance.FindTalkUser_byId(_target.Get_colorId());
                    FpNetFacade_server.Instance.Send_UserBang(user, false);

                    Actor_end();
                    //Activity_serverMain_andEngine.Instance.Create_honeybeeExplosion(mSprite.getX(), mSprite.getY(), "event_honeyBee_explosion");
                    //Manager_actor.Instance.Destroy_honeyBee(this);
                }
            }


            long deltaTime = System.currentTimeMillis() - _connectedTime;
            if(deltaTime > 3000) // 3초간 대기.
            {
                FpcRoot.Instance.DisconnectServer();
                Set_target(Manager_actor.Instance.Get_randomAttractor());
                _connectedTime = System.currentTimeMillis();

                _count_explosion--;
                if( _count_explosion < 0 ) _count_explosion = 0;
            }
        }
        return super.onUpdate(pSecondsElapsed);
    }
}
