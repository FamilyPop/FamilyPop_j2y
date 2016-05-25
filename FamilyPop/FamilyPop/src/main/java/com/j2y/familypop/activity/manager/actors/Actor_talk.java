package com.j2y.familypop.activity.manager.actors;

import android.os.Build;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by lsh on 2016-05-01.
 */
public class Actor_talk  extends BaseActor
{
    Actor_attractor mAttractor = null;
    private boolean mIsMover = false;

    private int _start_time = 0;
    private int _end_time = 0;

    public Actor_talk(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);
    }
    @Override
    public boolean onUpdate(float pSecondsElapsed)
    {
        super.onUpdate(pSecondsElapsed);

        // attractor 로 이동.
        if( mIsMover ) {
            if (mAttractor == null) return false;
            Vector2 v = new Vector2((mAttractor.mBody.getPosition().x - mBody.getPosition().x) * 1.1f,
                    (mAttractor.mBody.getPosition().y - mBody.getPosition().y) * 1.1f);
            mBody.applyForce(v, mBody.getWorldCenter());
        }
        // end attractor 로 이동
        return false;
    }
    public void SetAttractor(Actor_attractor attractor){ mAttractor = attractor; }
    public void Set_plusScale(float x, float y)
    {
        if( mSprite.getScaleX() < 1f && mSprite.getScaleY() < 1f)
        {
            mSprite.setScale(mSprite.getScaleX() + x, mSprite.getScaleY() + y);
        }
    }
    public void StartMover(int record_end_time)
    {
        mIsMover = true;
        mIsFlower = true;
        _end_time = record_end_time;
    }
}
