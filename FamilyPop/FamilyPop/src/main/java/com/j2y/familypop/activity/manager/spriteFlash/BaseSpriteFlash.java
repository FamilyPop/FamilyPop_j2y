package com.j2y.familypop.activity.manager.spriteFlash;

import org.andengine.entity.scene.Scene;

/**
 * Created by lsh on 2016-09-02.
 */
public class BaseSpriteFlash
{

    protected int _clientId = -1;
    public int _Type_flashSprite = -1;
    public BaseSpriteFlash(){}

    public  void Release( Scene scene)
    {

    }

    public int Get_clientID(){return _clientId;}
}
