package com.j2y.familypop.activity.manager.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.j2y.familypop.activity.manager.Manager_actor;

import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by lsh on 2016-04-30.
 */
public class BaseActor
{
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
        {
//            double curTime = ((double) System.currentTimeMillis()) / 1000;
//            double speed = 1;
//            float sizeX = (float) (mSprite.getScaleX() + Math.sin(curTime * (speed)) * 0.005f);
//            float sizeY = (float) (mSprite.getScaleY() + Math.sin(curTime * (speed)) * 0.005f);
//            mSprite.setScale(sizeX, sizeY);
        }

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
}
