package com.j2y.familypop.activity.manager.actors;

import com.badlogic.gdx.physics.box2d.Body;

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
    private long mActor_unique_number = -1;
    private int mColorId = -1;

    private float _spriteScale =0.0f; //꽃잎이 커진다~
    private float _maxSpriteScale = 2.3f; // 이만큼 커니다~
    public BaseActor(Body body, Sprite sprite, long uniqueNumber)
    {
        mBody = body;
        mSprite = sprite;
        mActor_unique_number = uniqueNumber;
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

    public  boolean onUpdate(float pSecondsElapsed)
    {
        // 점점 커지는 꽃잎
        if(mIsFlower)
        {
            IEntity temp = mSprite.getChildByIndex(0);
            if (temp != null) {
                if (_spriteScale <= _maxSpriteScale) {
                    _spriteScale += pSecondsElapsed * 1.0f;
                    mSprite.getChildByIndex(0).setScale(_spriteScale);
                }
            }
        }
        return false;
    }
}
