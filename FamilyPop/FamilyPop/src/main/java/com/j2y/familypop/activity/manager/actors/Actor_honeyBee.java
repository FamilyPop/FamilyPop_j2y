package com.j2y.familypop.activity.manager.actors;

import android.os.Handler;
import android.view.View;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.client.FpcRoot;

import org.andengine.entity.sprite.Sprite;

import java.util.ArrayList;

/**
 * Created by lsh on 2016-05-31.
 */
public class Actor_honeyBee extends BaseActor {

    private BaseActor _target = null;
    private long _connectedTime = 0;
//    private ArrayList<Actor_attractor> actor_attractors = null;



    public Actor_honeyBee(Body body, Sprite sprite, long uniqueNumber) {
        super(body, sprite, uniqueNumber);
        mIsFlower = false;
        //mBody.setFixedRotation(true);

        Set_target(Manager_actor.Instance.Get_randomAttractor());
        _connectedTime = System.currentTimeMillis();
    }

    public void Set_target(BaseActor targetActor) {
        _target = targetActor;
    }

    @Override
    public boolean onUpdate(float pSecondsElapsed)
    {

        if (_target != null) {

            // look ak
            Vector2 vTarget = _target.Get_Body().getPosition();
            Vector2 vStart = mBody.getPosition();
            float bugAngle = (float) Math.atan2((vTarget.x - vStart.x),
                    -(vTarget.y - vStart.y));



            // move
            Vector2 v = new Vector2((_target.mBody.getPosition().x - mBody.getPosition().x),
                    (_target.mBody.getPosition().y - mBody.getPosition().y));

            v.nor();
            v.x *= (pSecondsElapsed *  10f);
            v.y *= (pSecondsElapsed *  10f);

            v.x += mBody.getPosition().x;
            v.y += mBody.getPosition().y;

            mBody.setTransform(v, bugAngle);


            long deltaTime = System.currentTimeMillis() - _connectedTime;
            if(deltaTime > 5000) // 5초간 대기.
            {
                FpcRoot.Instance.DisconnectServer();
                Set_target(Manager_actor.Instance.Get_randomAttractor());
                _connectedTime = System.currentTimeMillis();
            }
        }
        return super.onUpdate(pSecondsElapsed);
    }

}
