package com.j2y.familypop.activity.manager.spriteFlash;

import com.j2y.familypop.activity.manager.Manager_resource;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by lsh on 2016-09-02.
 */
public class Sprite_flash extends BaseSpriteFlash
{

    private Sprite _sprite = null;
    private Sprite _photoFrame = null;

    public Sprite_flash(int clientId, Sprite sprite, Sprite photoFrame)
    {
        super();

        _Type_flashSprite = Manager_resource.Instance.TYPE_FLASH_SPRITE_SHAREIMAGE;
        _clientId = clientId;
        _sprite = sprite;
        _photoFrame = photoFrame;
    }

    public Sprite Get_sprite(){return _sprite;}
    public Sprite Get_photoFrame(){return _photoFrame;}


    @Override
    public  void Release( Scene scene)
    {
        super.Release(scene);
        if( _sprite != null)    scene.detachChild(_sprite);
        if( _photoFrame != null )   scene.detachChild(_photoFrame);
    }
}
