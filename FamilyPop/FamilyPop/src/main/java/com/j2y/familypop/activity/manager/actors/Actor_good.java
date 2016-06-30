package com.j2y.familypop.activity.manager.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.j2y.familypop.activity.manager.Manager_actor;

import org.andengine.entity.sprite.Sprite;

/**
 * Created by lsh on 2016-05-05.
 */
public class Actor_good extends BaseActor
{
    Actor_attractor mAttractor = null;

    public Actor_good(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);
        mIsFlower = true;
        _actor_type = Manager_actor.eType_actor.ACTOR_GOOD;
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


        if (mAttractor == null) return false;
        Vector2 v = new Vector2((mAttractor.mBody.getPosition().x - mBody.getPosition().x) * 1.5f,
                (mAttractor.mBody.getPosition().y - mBody.getPosition().y) * 1.5f);
        mBody.applyForce(v, mBody.getWorldCenter());

        return  super.onUpdate(pSecondsElapsed);
    }
    public void SetAttractor(Actor_attractor attractor){ mAttractor = attractor; }
}