package com.j2y.familypop.activity.manager.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.client.FpcRoot;
import com.j2y.familypop.server.FpsTalkUser;
import com.j2y.network.server.FpNetFacade_server;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.math.MathUtils;

/**
 * Created by lsh on 2016-08-04.
 */
public class Actor_honeyBeeClamPair extends BaseActor
{
    private BaseActor _target1 = null;
    private BaseActor _target2 = null;

    private BaseActor[] _targets = null;
    private int _targetsIndex = 0;

    private long    _connectedTime = 0;
    private int     _count_explosion = 2;

    public Actor_honeyBeeClamPair(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);

        //Set_target(Manager_actor.Instance.Get_randomAttractor());
        _connectedTime = System.currentTimeMillis();

        _count_explosion = MathUtils.random(2,4);

        _actor_type = Manager_actor.eType_actor.ACTOR_BEECLAMPAIR;
    }

    public void Set_target1(BaseActor targetActor)
    {
        _target1 = targetActor;
    }
    public void Set_target2(BaseActor targetActor) {
        _target2 = targetActor;
    }
    //========================================================================================================
    // override
    @Override
    public void init()
    {
        super.init();

        _targets = new BaseActor[2];
        _targets[0] = _target1;
        _targets[1] = _target2;

    }
    @Override
    public void release()
    {
        super.release();
    }
    @Override
    public synchronized boolean onUpdate(float pSecondsElapsed)
    {
        if( _target1 != null && _target2 != null)
        {
            Vector2 target = _targets[_targetsIndex].Get_Body().getPosition();
            Vector2 mover = mBody.getPosition();

            boolean isMove = MathUtils.distance(target.x, target.y, mover.x, mover.y) > 0.1f;

            if( isMove )
            {
                // move
                moveTo_target(pSecondsElapsed, target, 0.1f, 10 );
            }
            else
            {
                if( _count_explosion == 0)
                {
                    Activity_serverMain_andEngine main = Activity_serverMain_andEngine.Instance;
                    main.OnEvent_deleteHoneybee(this); // 벌을 제거.
                    //main.OnEvent_createBeeExplosion(mSprite.getX(), mSprite.getY(), "event_honeyBee_explosion"); // 폭파 이벤트 생성.

                    FpsTalkUser user = Manager_users.Instance.FindTalkUser_byId(_targets[_targetsIndex].Get_colorId());
                    FpsTalkUser user2 = Manager_users.Instance.FindTalkUser_byId(_target2.Get_colorId());

                    if( user.equals(user2))
                    {
                        FpNetFacade_server.Instance.Send_UserBang(user, true);
                    }
                    else
                    {
                        FpNetFacade_server.Instance.Send_UserBang(user, true);
                        FpNetFacade_server.Instance.Send_UserBang(user2, true);
                    }

                    Actor_end();
                }

                long deltaTime = System.currentTimeMillis() - _connectedTime;
                if(deltaTime > 500) // 0.5 초간 대기.
                {
                    FpcRoot.Instance.DisconnectServer();

                    _connectedTime = System.currentTimeMillis();
                    _targetsIndex = _targetsIndex + 1;
                    _targetsIndex %= 2;
                    _count_explosion--;
                    if( _count_explosion < 0 ) _count_explosion = 0;
                }

            }
        }
        return  super.onUpdate(pSecondsElapsed);
    }
    //========================================================================================================

}
