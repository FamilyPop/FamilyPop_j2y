package com.j2y.familypop.activity.manager.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.j2y.familypop.activity.manager.Manager_actor;

import org.andengine.entity.sprite.Sprite;
import org.andengine.util.math.MathUtils;

/**
 * Created by lsh on 2016-08-04.
 */
public class Actor_honeyBeeClamPair extends BaseActor
{
    private BaseActor _target = null;
    private long    _connectedTime = 0;
    private int     _count_explosion = 0;

    public Actor_honeyBeeClamPair(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);

        Set_target(Manager_actor.Instance.Get_randomAttractor());
        _connectedTime = System.currentTimeMillis();

        _count_explosion = MathUtils.random(3,5);

        _actor_type = Manager_actor.eType_actor.ACTOR_BEECLAMPAIR;
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

        return  super.onUpdate(pSecondsElapsed);
    }
    //========================================================================================================

}
