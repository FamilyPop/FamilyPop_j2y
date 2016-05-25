package com.j2y.familypop.activity.manager.actors;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;


import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.input.touch.TouchEvent;

/**
 * Created by lsh on 2016-04-30.
 */
public class Actor_attractor extends BaseActor
{


    public Actor_attractor(Body body, Sprite sprite, long uniqueNumber)
    {
        super(body, sprite, uniqueNumber);
        //body.getUserData()
    }


    public boolean onUpdate(float pSecondsElapsed)
    {
        super.onUpdate(pSecondsElapsed);
        //_sprite.setPosition(0, 0);

        return false;
    }

    //=================================================================================================
    // touch event
    private boolean onTouch_Up(final Scene pScene, final TouchEvent pSceneTouchEvent)
    {
        return false;
    }
    private boolean onTouck_Down(final Scene pScene, final TouchEvent pSceneTouchEvent)
    {

        return false;
    }
    private boolean onTouch_Move(final Scene pScene, final TouchEvent pSceneTouchEvent)
    {

        float radius = mBody.getFixtureList().get(0).getShape().getRadius();

        float sw = mBody.getPosition().x;
        float sh = mBody.getPosition().y;

        float tW = pSceneTouchEvent.getX() / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
        float tH = pSceneTouchEvent.getY() / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;


        if( (sw - radius) < tW  &&  (sw + radius ) > tW)
        {
            if( (sh - radius) < tH && (sh + radius) > tH)
            {
                mBody.setTransform(tW, tH, 0);
            }
        }
        return false;
    }
    public boolean Set_addPostion( float dirx, float diry)
    {
        boolean ret = false;

        float posX = dirx / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
        float posY = diry / PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;

        posX += mBody.getPosition().x;
        posY += mBody.getPosition().y;
        mBody.setTransform(posX, posY, 0);

        return ret;
    }
    private boolean getTouch_attractorImpact(float touchX, float touchY)
    {
        return false;
    }

    public boolean onTouch(final Scene pScene, final TouchEvent pSceneTouchEvent)
    {
        int eventaction = pSceneTouchEvent.getAction();

        // touch
        switch(eventaction)
        {
            case MotionEvent.ACTION_DOWN: onTouck_Down(pScene, pSceneTouchEvent); break;
            case MotionEvent.ACTION_MOVE: onTouch_Move(pScene, pSceneTouchEvent);break;
            case MotionEvent.ACTION_UP:  onTouch_Up(pScene, pSceneTouchEvent); break;
        }
        return false;
    }
    // end touch move event
    //private
}