package com.j2y.familypop.activity.manager.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

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
    }

    @Override
    public boolean onUpdate(float pSecondsElapsed)
    {
        super.onUpdate(pSecondsElapsed);

        if (mAttractor == null) return false;
        Vector2 v = new Vector2((mAttractor.mBody.getPosition().x - mBody.getPosition().x) * 1.5f,
                (mAttractor.mBody.getPosition().y - mBody.getPosition().y) * 1.5f);
        mBody.applyForce(v, mBody.getWorldCenter());

        return false;
    }
    public void SetAttractor(Actor_attractor attractor){ mAttractor = attractor; }
}