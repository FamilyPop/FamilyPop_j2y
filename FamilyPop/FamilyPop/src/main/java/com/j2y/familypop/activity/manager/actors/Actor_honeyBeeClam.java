package com.j2y.familypop.activity.manager.actors;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.server.FpsTalkUser;
import com.j2y.network.server.FpNetFacade_server;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.math.MathUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by lsh on 2016-08-04.
 */
public class Actor_honeyBeeClam extends BaseActor
{
    BaseActor _targetActor = null;
    float _distance = 0.0f;
    float _deleteTimeSec = 10;

    private boolean _rotDir = false;

    public Actor_honeyBeeClam(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);

        _actor_type = Manager_actor.eType_actor.ACTOR_BEECLAM;
    }
    public void Set_target(BaseActor targetActor) {
        _targetActor = targetActor;
    }
    //========================================================================================================
    // override
    @Override
    public void init()
    {
        super.init();

        Vector2 moverPos = Get_Body().getPosition();
        Vector2 targetPos = _targetActor.Get_Body().getPosition();
        _distance =  MathUtils.distance(moverPos.x, moverPos.y, targetPos.x, targetPos.y);

        int temp = (int)Math.random() * 2;

        switch (temp)
        {
            case 0: _rotDir = false; break;
            case 1: _rotDir = true; break;
        }


    }
    @Override
    public void release()
    {
        super.release();
    }
    @Override
    public synchronized boolean onUpdate(float pSecondsElapsed)
    {
        //public boolean rotation_axis(float pSecondsElapsed, Vector2 target, float radius, float rotationSpeed, boolean rotationDirection)
        if( _distance > 3){ _distance -= (pSecondsElapsed * 2f); }
        rotation_axis(pSecondsElapsed, _targetActor.Get_Body().getPosition(), _distance, 5, _rotDir);

        _deleteTimeSec -= pSecondsElapsed;
        Log.i("[SecondsElapsed]", ""+_deleteTimeSec);
        if( _deleteTimeSec <= 0)
        {
            Activity_serverMain_andEngine main = Activity_serverMain_andEngine.Instance;
            main.OnEvent_deleteHoneybee(this); // 벌을 제거.
            main.OnEvent_createBeeExplosion(mSprite.getX(), mSprite.getY(), "event_honeyBee_explosion"); // 폭파 이벤트 생성.

            // 그냥 랜덤 사진 가져오기.
            FpsTalkUser user = Manager_users.Instance.FindTalkUser_byId(_targetActor.Get_colorId());
            FpNetFacade_server.Instance.Send_UserBang(user,true);
            //
            Actor_end();
        }

        return  super.onUpdate(pSecondsElapsed);
    }
    //========================================================================================================
}
