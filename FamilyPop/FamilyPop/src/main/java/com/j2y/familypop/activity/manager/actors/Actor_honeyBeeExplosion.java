package com.j2y.familypop.activity.manager.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by lsh on 2016-06-27.
 */
public class Actor_honeyBeeExplosion extends BaseActor
{
    public Actor_honeyBeeExplosion(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);
        _actor_type = Manager_actor.eType_actor.ACTOR_HONEY_BEE_EXPLOSION;
        mBody.setActive(false);

        //((AnimatedSprite)mSprite).getCurrentTileIndex()
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
        if( !((AnimatedSprite)mSprite).isAnimationRunning() )
        {
            Activity_serverMain_andEngine main = Activity_serverMain_andEngine.Instance;
            main.OnEvent_deleteBeeExplosion(this);
            Actor_end();
            //Manager_actor.Instance.Destroy_honeyBeeExplosion(this);
        }

        return super.onUpdate(pSecondsElapsed);
    }


}