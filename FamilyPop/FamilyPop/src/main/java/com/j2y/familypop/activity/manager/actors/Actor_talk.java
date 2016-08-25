package com.j2y.familypop.activity.manager.actors;

import android.os.Build;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.j2y.familypop.activity.manager.Manager_actor;

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

    //public int _flowerColor = -1;

    public int _startTalkID = -1;   // 자신의 스피커 아이디.
    public int _answerID = -1;      // 자신의 앞전에 말한 사람. // 꽃잎 색.(자신과 이야기한 사람의 색) 데이터 전송할때도 사용하고있다 정리가 필요.

    public Actor_talk(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);

        Effect_balloon(true);
        mBody.setActive(false);
        _actor_type = Manager_actor.eType_actor.ACTOR_TALK;
    }

    public int GetStart_time(){ return _start_time; }
    public void SetStart_time(int start_time){ _start_time = start_time;}
    public int GetEnd_time(){ return _end_time; }
    public void SetEnd_time(int end_time){ _end_time = end_time;}

    public void Set_addScale(float addScale){ _addTalkScale = addScale; }
    public void Set_maxScale(float maxScale){ _maxTalkScale = maxScale; }

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
        // attractor 로 이동.
        if( mIsMover )
        {
            if (mAttractor == null) return false;
            Vector2 v = new Vector2((mAttractor.mBody.getPosition().x - mBody.getPosition().x) * _addTalkScale,
                    (mAttractor.mBody.getPosition().y - mBody.getPosition().y) * _addTalkScale);
            mBody.applyForce(v, mBody.getWorldCenter());
            mBody.applyTorque(0.0001f);
        }
        // end attractor 로 이동
        return super.onUpdate(pSecondsElapsed);
    }
    public void SetAttractor(Actor_attractor attractor){ mAttractor = attractor; }
    public void Set_plusScale(float pSecondsElapsed)
    {
        // TODO: 2016-06-09 충돌 박스 사이즈 조절
        if( mSprite.getScaleX() < _maxTalkScale && mSprite.getScaleY() < _maxTalkScale)
        {
            mOriginalScale.x += (_addTalkScale * (0.1f * pSecondsElapsed));
            mOriginalScale.y += (_addTalkScale * (0.1f * pSecondsElapsed));
            mSprite.setScale(mOriginalScale.x, mOriginalScale.y);

            mBody.getFixtureList().get(0).getShape().setRadius(mBody.getFixtureList().get(0).getShape().getRadius() + (_addTalkScale *  (0.1f * pSecondsElapsed)));
            mOriginalRadius = mBody.getFixtureList().get(0).getShape().getRadius();
        }
    }
    public float Get_Scale(){return mSprite.getScaleX();}
    public void StartMover(int record_end_time)
    {
        mIsMover = true;
        mIsFlower = true;
        _end_time = record_end_time;
        mBody.setActive(true);
        //mBody.getFixtureList().get(0).getShape().setRadius(_addTalkScale);
//        _radius = 0.0f;
    }
    public void Effect_balloon(boolean act)
    {
        mIsBalloon = act;
    }
}
