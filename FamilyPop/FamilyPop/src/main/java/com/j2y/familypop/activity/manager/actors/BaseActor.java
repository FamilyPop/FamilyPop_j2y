package com.j2y.familypop.activity.manager.actors;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.facebook.internal.Utility;
import com.j2y.familypop.activity.manager.Manager_actor;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import java.util.Vector;

/**
 * Created by lsh on 2016-04-30.
 */

public class BaseActor
{
    protected float mOriginalRadius = 0.0f;
    protected Body mBody = null;
    protected Sprite mSprite;
    protected boolean mIsFlower = false;
    protected boolean mIsBalloon = false;
    protected Vector2 mOriginalScale;
    protected Manager_actor.eType_actor _actor_type = Manager_actor.eType_actor.ACTOR_NON;

    private long mActor_unique_number = -1;
    private int mColorId = -1;  // colorid == clientid
    private boolean mInit;
    private boolean mEnd;

    private float _spriteScale =0.0f; //꽃잎이 커진다~
    private float _maxSpriteScale = 2.3f; // 이만큼 커니다~

    public BaseActor(Body body, Sprite sprite, long uniqueNumber)
    {
        mBody = body;
        mSprite = sprite;
        mActor_unique_number = uniqueNumber;
        mOriginalScale = new Vector2(mSprite.getScaleX(), mSprite.getScaleY());

        mInit = false;
        mEnd = false;

        //꽃잎 커지는거 막는담!
        //mSprite.getChildByIndex(0).setScale(_spriteScale);
    }

    public Body Get_Body()
    {
        return mBody;
    }
    public Sprite Get_Sprite()
    {
        return mSprite;
    }
    public long Get_UniqueNumber(){return mActor_unique_number == -1 ? -1 : mActor_unique_number;}
    public void Set_UniqueNumber(long unique_number){ mActor_unique_number = unique_number; }
    public void Set_maxFlowerScale(float maxScale){_maxSpriteScale = maxScale;}
    public int Get_colorId(){return mColorId;}
    public void Set_colorId(int colorId){mColorId = colorId;}
    public Manager_actor.eType_actor Get_ActorType(){return _actor_type;}

    public void init()
    {

    }
    public  boolean onUpdate(float pSecondsElapsed)
    {
        //if( mIsBalloon )
//        {
//            double curTime = ((double) System.currentTimeMillis()) / 1000;
//            double speed = 1;
//            float sizeX = (float) (mSprite.getScaleX() + Math.sin(curTime * (speed)) * 0.005f);
//            float sizeY = (float) (mSprite.getScaleY() + Math.sin(curTime * (speed)) * 0.005f);
//            mSprite.setScale(sizeX, sizeY);
//        }
        // 점점 커지는 꽃잎
//        if(mIsFlower)
//        {
//            IEntity temp = mSprite.getChildByIndex(0);
//            if (temp != null) {
//                if (_spriteScale <= _maxSpriteScale) {
//                    _spriteScale += pSecondsElapsed * 0.3f;
//                    mSprite.getChildByIndex(0).setScale(_spriteScale);
//                }
//            }
//        }

        return mEnd;
    }
    public void release()
    {
        mInit = false;
        mEnd = false;
    }

    public void Actor_end(){ mEnd = true; }
    public void Init_scuccess(){ mInit = true; }
    public boolean Get_InitState(){return mInit;}

    //mBody.getFixtureList().get(0).getShape().setRadius(mBody.getFixtureList().get(0).getShape().getRadius() + (_addTalkScale * 0.001f));
    public void Set_collider(float radius)
    {
        if( mBody == null) return;
        mBody.getFixtureList().get(0).getShape().setRadius(radius);
    }

    public void Mul_collider(float radius)
    {
        if( mBody == null) return;
        if( mOriginalRadius <= 0.0f){ mOriginalRadius = mBody.getFixtureList().get(0).getShape().getRadius(); }

        float r;
        r = radius * mOriginalRadius;

        mBody.getFixtureList().get(0).getShape().setRadius(r);
    }

//    org.andengine.entity.modifier.RotationModifier
//    org.andengine.entity.modifier.RotationAtModifier
//    org.andengine.entity.modifier.RotationByModifier
    //

    //===================================================================================================================================
    // movement
//    protected boolean lookAt(float duration, float pSecondsElapsed, Vector2 target)
//    {
//        boolean ret = false;
//
//        float angleDeg = getTargetAngle(
//                mSprite.getX(),
//                mSprite.getY(),
//                mSprite.getRotation(), target.x, target.y);
//
//        RotationModifier rotMod = new RotationModifier( duration,   mSprite.getRotation(),
//                                                                    mSprite.getRotation() + angleDeg);
//        mSprite.registerEntityModifier(rotMod);
//        ret = true;
//
//        return ret;
//    }
    //protected boolean moveTo_target(float pSecondsElapsed , float moveSpeed, Vector2 target)
    public boolean moveTo_target(float pSecondsElapsed , Vector2 target, float distance,  float moveSpeed)
    {
        boolean ret = false;

        // look ak
        Vector2 vStart = mBody.getPosition();
        float bugAngle = (float) Math.atan2((target.x - vStart.x), - (target.y - vStart.y));

        Vector2 mover = mBody.getPosition();

        ret = MathUtils.distance(target.x, target.y, mover.x, mover.y) <= distance;

        // move
        Vector2 v = new Vector2((target.x - mBody.getPosition().x), (target.y - mBody.getPosition().y));
        v.nor();
        v.x *= (pSecondsElapsed *  moveSpeed);
        v.y *= (pSecondsElapsed *  moveSpeed);

        v.x += mBody.getPosition().x;
        v.y += mBody.getPosition().y;

        mBody.setTransform(v, bugAngle);

        //ret = true;
        return ret;
    }
    // 이름이 미묘 하네..
    // false 왼쪽, true 오른쪽
    public float _rotation_axis_angle = 0.0f;
    public boolean rotation_axis(float pSecondsElapsed, Vector2 target, float radius, float rotationSpeed, boolean rotationDirection)
    {
        boolean ret = false;

        if( rotationDirection)
        {
            _rotation_axis_angle += ((2.0f * Math.PI / 10) * pSecondsElapsed * (rotationSpeed));
            if( _rotation_axis_angle >= (2.0f*Math.PI)) _rotation_axis_angle -= (2.0f*Math.PI);
        }
        else
        {
            _rotation_axis_angle -= ((2.0f * Math.PI / 10) * pSecondsElapsed * (rotationSpeed));
            if( _rotation_axis_angle <= (2.0f*Math.PI) *-1) _rotation_axis_angle += (2.0f*Math.PI);
        }

        float x = (float)Math.cos( _rotation_axis_angle) * ((1) - radius);
        float y = (float)Math.sin( _rotation_axis_angle) * ((1) - radius);

        x += target.x;
        y += target.y;

        mBody.setTransform( x , y , 0);

        return ret;
    }

    private float getTargetAngle(float startX, float startY, float startAngle, float targetX, float targetY) {

        float angleRad = 0.0f;

        float dX = targetX - startX;
        float dY = targetY - startY;

        float cos = (float) Math.cos(Math.toRadians(-startAngle));
        float sin = (float) Math.sin(Math.toRadians(-startAngle));

        float RotateddX = ((dX * cos) - (dY * sin));
        float RotateddY = ((dX * sin) + (dY * cos));

        angleRad = (float) Math.atan2(RotateddY, RotateddX);

        float angleDeg = (float) Math.toDegrees(angleRad);

        return angleDeg;
    }
}
