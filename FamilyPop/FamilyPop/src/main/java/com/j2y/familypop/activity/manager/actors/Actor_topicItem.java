package com.j2y.familypop.activity.manager.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.j2y.familypop.activity.manager.Manager_actor;

import org.andengine.entity.sprite.Sprite;

/**
 * Created by lsh on 2016-08-31.
 */
// 움 이건 안쓰는걸로
public class Actor_topicItem extends BaseActor
{

    public Actor_topicItem(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);

        mIsFlower = false;
        _actor_type = Manager_actor.eType_actor.ACTOR_TOPICITEM;
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
    public synchronized boolean onUpdate(float pSecondsElapsed)
    {

        return super.onUpdate(pSecondsElapsed);
    }

}
