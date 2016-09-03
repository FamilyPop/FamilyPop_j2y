package com.j2y.familypop.activity.manager.spriteFlash;

import com.j2y.familypop.activity.manager.Manager_resource;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

/**
 * Created by lsh on 2016-09-02.
 */
public class Sprite_flash_topic  extends BaseSpriteFlash
{
    private int _clientId = -1;
    private Sprite _topicImage = null;
    private Text _topicText = null;

    public Sprite_flash_topic(int clientId, Sprite topicImage, Text topictext)
    {
        super();

        _Type_flashSprite = Manager_resource.Instance.TYPE_FLASH_SPRITE_TOPIC;

        _clientId = clientId;
        _topicImage = topicImage;
        _topicText = topictext;

    }

    public Sprite Get_topicImage() {return _topicImage;}
    public Text Get_topicText(){return _topicText;}
    public int Get_clientID(){return _clientId;}

    public void Release(Scene scene)
    {
        if( _topicText != null) scene.detachChild(_topicText);
        if( _topicImage != null) scene.detachChild(_topicImage);
    }

}