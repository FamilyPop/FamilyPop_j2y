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
    private float _addTalkScale = 1.1f;
    private float _maxTalkScale = 1.5f;


    public Actor_talk(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);

        Effect_balloon(true);
    }

    public int GetStart_time(){ return _start_time; }
    public void SetStart_time(int start_time){ _start_time = start_time;}
    public int GetEnd_time(){ return _end_time; }
    public void SetEnd_time(int end_time){ _end_time = end_time;}

    public void Set_addScale(float addScale){ _addTalkScale = addScale; }
    public void Set_maxScale(float maxScale){ _maxTalkScale = maxScale; }


    @Override
    public synchronized boolean onUpdate(float pSecondsElapsed)
    {
        super.onUpdate(pSecondsElapsed);

        // attractor 로 이동.
        if( mIsMover ) {
            if (mAttractor == null) return false;
            Vector2 v = new Vector2((mAttractor.mBody.getPosition().x - mBody.getPosition().x) * _addTalkScale,
                    (mAttractor.mBody.getPosition().y - mBody.getPosition().y) * _addTalkScale);
            mBody.applyForce(v, mBody.getWorldCenter());
        }
        // end attractor 로 이동
        return false;
    }
    public void SetAttractor(Actor_attractor attractor){ mAttractor = attractor; }
    public void Set_plusScale()
    {
        // TODO: 2016-06-09 충돌 박스 사이즈 조절
        if( mSprite.getScaleX() < _maxTalkScale && mSprite.getScaleY() < _maxTalkScale)
        {
            mOriginalScale.x += (_addTalkScale * 0.001f);
            mOriginalScale.y += (_addTalkScale * 0.001f);
            mSprite.setScale(mOriginalScale.x, mOriginalScale.y);

           mBody.getFixtureList().get(0).getShape().setRadius(mBody.getFixtureList().get(0).getShape().getRadius() + (_addTalkScale * 0.001f));
        }
    }
    public float Get_Scale(){return mSprite.getScaleX();}
    public void StartMover(int record_end_time)
    {
        mIsMover = true;
        mIsFlower = true;
        _end_time = record_end_time;
    }
    public void Effect_balloon(boolean act)
    {
        mIsBalloon = act;
    }
}
