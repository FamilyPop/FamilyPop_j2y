package com.j2y.familypop.activity.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lsh on 2016-04-29.
 */
class BitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource
{
    private final int[] mColors;

    public BitmapTextureAtlasSource(Bitmap pBitmap)
    {
        super(0, 0, pBitmap.getWidth(), pBitmap.getHeight());

        mColors = new int[mTextureWidth * mTextureHeight];


//        for(int y=0; y<mTextureHeight; ++y)
//        {
//            for( int x=0; x<mTextureWidth; ++x)
//            {
//                mColors[x+y*mTextureWidth] = pBitmap.getPixel(x,y);
//            }
//        }
        pBitmap.getPixels(mColors, 0, pBitmap.getWidth(), 0, 0, pBitmap.getWidth(), pBitmap.getHeight());
        //System.arraycopy(mColors, 0, pBitmap.getPixels();, );
    }


    @Override
    public IBitmapTextureAtlasSource deepCopy() {
        return new BitmapTextureAtlasSource(Bitmap.createBitmap(mColors, mTextureWidth, mTextureHeight, Bitmap.Config.ARGB_8888));
    }

    @Override
    public Bitmap onLoadBitmap(Bitmap.Config pBitmapConfig) {
        return Bitmap.createBitmap(mColors, mTextureWidth, mTextureHeight, Bitmap.Config.ARGB_8888);
    }
}
//_flash_sprites
class Sprite_flash
{
    private int _clientId = -1;
    private Sprite _sprite = null;
    private Sprite _photoFrame = null;

    public Sprite_flash(int clientId, Sprite sprite, Sprite photoFrame)
    {
        _clientId = clientId;
        _sprite = sprite;
        _photoFrame = photoFrame;
    }

    public Sprite Get_sprite(){return _sprite;}
    public Sprite Get_photoFrame(){return _photoFrame;}
    public int Get_clientID(){return _clientId;}
    public  void Release( Scene scene)
    {
        if( _sprite != null)    scene.detachChild(_sprite);
        if( _photoFrame != null )   scene.detachChild(_photoFrame);
    }
}

public class Manager_resource
{
    //====================================================================================================
    // enum
    //====================================================================================================
    public enum eType_atlas {
        ATLAS_DUMMY(0), ATLAS_SERVER(1), ATLAS_SPRITE(2);

        private int value;

        private eType_atlas(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public enum eImageIndex_color {
        COLOR_ERROR(-1), COLOR_ORANGE(0), COLOR_YELLOW_GREEN(1), COLOR_PURPLE(2), COLOR_SKY_BLUE(3), COLOR_RED(4);
        private int value;

        private eImageIndex_color(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public boolean Compare(int i) {
            return value == i;
        }

        public static eImageIndex_color IntToImageColor(int v) {
            eImageIndex_color[] As = eImageIndex_color.values();
            for (int i = 0; i < As.length; ++i) {
                if (As[i].Compare(v)) {
                    return As[i];
                }
            }
            return eImageIndex_color.COLOR_ERROR;
        }
    }
    public enum eImageIndex_event {
        EVENT_ERROR(-1), EVENT_SMILE(0), EVENT_STAR(1), EVENT_HEART(2), EVENT_FIRE(3);
        private int value;

        private eImageIndex_event(int value) {
            this.value = value;
        }

        public boolean Compare(int i) {
            return value == i;
        }

        public int getValue() {
            return value;
        }
    }
    public enum eType_petal {
        PETAL_TALK(0), PETAL_SMILE(1), PETAL_GOOD(2);
        private int value;

        private eType_petal(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    //====================================================================================================
    //
    //====================================================================================================
    public static Manager_resource Instance = null;

    //private SimpleBaseGameActivity _gameActivity = null;
    private Context _gameActivity = null;
    private TextureManager _textureManager = null;
    private FontManager _fontManager = null;

    private BitmapTextureAtlas _dummyTextureAtlas = null;
    private BitmapTextureAtlas _serverTextureAtlas = null;
    private BitmapTextureAtlas _spriteTextureAtlas = null;


    private HashMap<String, ITiledTextureRegion> _tiledTextures = null;
    private HashMap<String, ITextureRegion> _spriteTextures = null;
    //private HashMap<String, ITextureRegion> _textures = null;

    private final int ATLAS_WIDTH = 2048;
    private final int ATLAS_HEGIHT = 2048;

    private ArrayList<String> _userImageNames = null;
    private ArrayList<String> _userLikeNames = null;
    private ArrayList<String> _eventImageNames = null;

    private ArrayList<String> _talkPetalNames = null;
    private ArrayList<String> _smilePetalNames = null;
    private ArrayList<String> _likePetalNames = null;


    //private CopyOnWriteArrayList<Sprite> _flash_sprites = null;
    private CopyOnWriteArrayList<Sprite_flash> _flash_sprites = null;

    private Font _font = null;


    //====================================================================================================
    // init
    //====================================================================================================
    public Manager_resource(Context gameActivity) {
        Instance = this;
        _gameActivity = gameActivity;
        _textureManager = ((BaseGameActivity) gameActivity).getTextureManager();
        _fontManager = ((BaseGameActivity)gameActivity).getFontManager();

        _flash_sprites = new CopyOnWriteArrayList<>();

        init_loadTexture();
        init_textureNames();
    }
    private void init_loadTexture() {
        _tiledTextures = new HashMap<String, ITiledTextureRegion>();
        _spriteTextures = new HashMap<>();

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        //_bitmapTextureAtlas= new BitmapTextureAtlas(_textureManager, 64, 64, TextureOptions.BILINEAR);
        // todo: 사이즈 자동으로 배치 되도록. 아틀라스 구분할 방법도 생각해 봐야한다.
        _dummyTextureAtlas = new BitmapTextureAtlas(_textureManager, ATLAS_WIDTH, ATLAS_HEGIHT);
        _serverTextureAtlas = new BitmapTextureAtlas(_textureManager, ATLAS_WIDTH, ATLAS_HEGIHT);
        _spriteTextureAtlas = new BitmapTextureAtlas(_textureManager, ATLAS_WIDTH, ATLAS_HEGIHT);


        //- texture load
        // todo : 문자열 따로 관리 해야함 (문자열 다빼불자)

        add_TiledTexture(eType_atlas.ATLAS_SERVER, "event_honeyBee", "FloPop_Server_Resources-25.png", 0, 0, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "event_honeyBee_explosion", "flare_bee.png", 128, 0, 6, 2);
        //꽃잎
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "talk_petal-01.png", "FloPop_Server_Resources-01.png", 0, 128);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "talk_petal-02.png", "FloPop_Server_Resources-02.png", 0, 256);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "talk_petal-03.png", "FloPop_Server_Resources-03.png", 0, 384);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "talk_petal-04.png", "FloPop_Server_Resources-04.png", 0, 512);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "talk_petal-05.png", "FloPop_Server_Resources-05.png", 0, 640);

        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "like_petal-01.png", "FloPop_Server_Resources-16.png", 0, 768);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "like_petal-02.png", "FloPop_Server_Resources-17.png", 0, 896);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "like_petal-03.png", "FloPop_Server_Resources-18.png", 0, 1024);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "like_petal-04.png", "FloPop_Server_Resources-19.png", 0, 1152);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "like_petal-05.png", "FloPop_Server_Resources-20.png", 0, 1280);

        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "smile_petal.png","FloPop_Server_Resources-22.png", 0, 1408);

        // user
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-01.png","FloPop_Server_Resources-06.png", 128, 34);   // 노랑
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-02.png","FloPop_Server_Resources-07.png", 128, 68);   // 녹색
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-03.png","FloPop_Server_Resources-08.png", 128, 102);  // 보라색
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-04.png","FloPop_Server_Resources-09.png", 128, 136);  // 파랑색
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-05.png","FloPop_Server_Resources-10.png", 128, 170);  // 빨간색

        //event
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_01.png","FloPop_Server_Resources-11.png", 162, 50);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_02.png","FloPop_Server_Resources-12.png", 162, 100);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_03.png","FloPop_Server_Resources-13.png", 162, 150);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_04.png","FloPop_Server_Resources-14.png", 162, 200);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_05.png","FloPop_Server_Resources-15.png", 162, 250);

        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "smile_01.png","FloPop_Server_Resources-21.png", 162, 300);

        // photoframe
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "photoframe_0.png","photoframe_0.png", 418, 0);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "photoframe_1.png","photoframe_1.png", 418, 256);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "photoframe_2.png","photoframe_2.png", 418, 512);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "photoframe_3.png","photoframe_3.png", 418, 768);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "photoframe_4.png","photoframe_4.png", 418, 1024);


        _serverTextureAtlas.load();
        _spriteTextureAtlas.load();
        //- end texture load


        // font
        FontFactory.setAssetBasePath("font/");
        _font = FontFactory.createFromAsset(_fontManager, _textureManager, 256, 256, TextureOptions.BILINEAR, _gameActivity.getAssets(), "LCD.ttf", 32f, true, Color.WHITE_ABGR_PACKED_INT);
        _font.load();
    }
    private void init_textureNames() {

        _userImageNames = new ArrayList<>();
        _userLikeNames = new ArrayList<>();
        _eventImageNames = new ArrayList<>();

        _talkPetalNames = new ArrayList<>();
        _smilePetalNames = new ArrayList<>();
        _likePetalNames = new ArrayList<>();

        // userImageNames
        _userImageNames.add("user-01.png");
        _userImageNames.add("user-02.png");
        _userImageNames.add("user-03.png");
        _userImageNames.add("user-04.png");
        _userImageNames.add("user-05.png");

        // userLikeImageNames
        _userLikeNames.add("user_like_01.png");
        _userLikeNames.add("user_like_02.png");
        _userLikeNames.add("user_like_03.png");
        _userLikeNames.add("user_like_04.png");
        _userLikeNames.add("user_like_05.png");
        // eventImageNames
        //_eventImageNames.add("smile_k-01-01.png");
        _eventImageNames.add("smile_01.png");
//        _eventImageNames.add("star_k-01-01.png");
//        _eventImageNames.add("heart_k-01-01.png");
//        _eventImageNames.add("fire_k-01-01.png");

        // talkImageNames
        _talkPetalNames.add("talk_petal-01.png");
        _talkPetalNames.add("talk_petal-02.png");
        _talkPetalNames.add("talk_petal-03.png");
        _talkPetalNames.add("talk_petal-04.png");
        _talkPetalNames.add("talk_petal-05.png");

        // smilePetalNames
        _smilePetalNames.add("smile_petal.png");
//        _smilePetalNames.add("smile_petal-02.png");
//        _smilePetalNames.add("smile_petal-03.png");
//        _smilePetalNames.add("smile_petal-04.png");
//        _smilePetalNames.add("smile_petal-05.png");

        // likePetalNames
        _likePetalNames.add("like_petal-01.png");
        _likePetalNames.add("like_petal-02.png");
        _likePetalNames.add("like_petal-03.png");
        _likePetalNames.add("like_petal-04.png");
        _likePetalNames.add("like_petal-05.png");
    }
    //====================================================================================================
    //
    //====================================================================================================
    private void add_TiledTexture(eType_atlas type, String key, String texName, int pTextureX, int pTextureY, int pTileColumns, int pTileRows) {

        switch (type) {
            case ATLAS_DUMMY:
                put_tiledTexture(key, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(_dummyTextureAtlas, _gameActivity, texName, pTextureX, pTextureY, pTileColumns, pTileRows));
                break;
            case ATLAS_SERVER: //같은 자료구조에 넣고있음.
                put_tiledTexture(key, BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(_serverTextureAtlas, _gameActivity, texName, pTextureX, pTextureY, pTileColumns, pTileRows));
                break;
        }
    }
    private void add_spriteTexture(eType_atlas type, String key, String texName, int pTextureX, int pTextureY){
        switch(type)
        {
            case ATLAS_SPRITE:
                put_spriteTexture(key, BitmapTextureAtlasTextureRegionFactory.createFromAsset(_spriteTextureAtlas, _gameActivity, texName, pTextureX, pTextureY));
                break;
        }
    }

    private void put_tiledTexture(String key, ITiledTextureRegion tiledtexture){ if (tiledtexture != null) _tiledTextures.put(key, tiledtexture); }
    private void put_spriteTexture(String key, ITextureRegion spritetexture){ if (spritetexture != null) _spriteTextures.put(key, spritetexture); }

    //====================================================================================================
    // get texture
    //====================================================================================================
    public CopyOnWriteArrayList<Sprite_flash> GetSprite_flash()
    {
        return _flash_sprites;
    }
    public ITiledTextureRegion GetTiledTexture(String key) {
        ITiledTextureRegion ret = null;

        if (_tiledTextures.containsKey(key)) {
            ret = _tiledTextures.get(key);
        } else {
            ret = _tiledTextures.get("error_null_tiledTextures.png");
        }
        return ret;
    }
    public ITextureRegion GetSpriteTexture(String key)
    {
        ITextureRegion ret = null;
        if( _spriteTextures.containsKey(key))
        {
            ret = _spriteTextures.get(key);
        }
        else
        {
            //todo : error 텍스쳐 없음.
        }
        return ret;
    }

    // get sprite
    public String Get_petalNames(eImageIndex_color color, eType_petal type_petal) {
        String ret = null;
        switch (type_petal) {
            case PETAL_TALK:
                ret = _talkPetalNames.get(color.getValue());
                break;
            case PETAL_GOOD:
                ret = _likePetalNames.get(color.getValue());
                break;
            case PETAL_SMILE:
                //ret = _smilePetalNames.get(color.getValue());
                ret = _smilePetalNames.get(0); // smilePetal 은 하나 밖에 없음.
                break;
        }
        return ret;
    }
    public String Get_userLike(eImageIndex_color color) {
        return _userLikeNames.get(color.getValue());
    }
    public String Get_userImage(eImageIndex_color color) {
        String ret = null;
        ret = _userImageNames.get(color.getValue());
        return ret;
    }
    public Font Get_font()
    {
        return _font;
    }
    //====================================================================================================
    // create
    //====================================================================================================
    // sprite
    private Sprite create_sprite(float posX, float posY, final SimpleBaseGameActivity gameActivity, final String path, final String fileName) throws IOException {

        Sprite ret = null;

        BitmapTexture bitmapTexture = new BitmapTexture(gameActivity.getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {

                return gameActivity.getAssets().open(path+fileName);
                //return null;
            }
        });
        bitmapTexture.load();

        TextureRegion textureRegion = TextureRegionFactory.extractFromTexture(bitmapTexture);
        ret = new Sprite(posX, posY, textureRegion, gameActivity.getVertexBufferObjectManager());

        return ret;
    }
    public Sprite Create_sprite(float posX, float posY, Scene scene, SimpleBaseGameActivity gameActivity, String path, String fileName) throws IOException {
        Sprite ret = null;

        ret = create_sprite(posX, posY,gameActivity, path, fileName);
        scene.attachChild(ret);

        return ret;
    }
    //todo: 이미지를 잠간 뿌리기 용으로 만들어야함 ( frame 그리는 기능은 빼는걸로 )
    public synchronized Sprite Create_flashSprite(int clientId,  float posX, float posY, Scene scene, SimpleBaseGameActivity gameActivity,Bitmap bitmap)
    {
        Sprite ret = null;

        // photo frame

        // COLOR_ERROR(-1), COLOR_ORANGE(0), COLOR_YELLOW_GREEN(1), COLOR_PURPLE(2), COLOR_SKY_BLUE(3), COLOR_RED(4); // 뭔가 엉킴.망.
        ITextureRegion photoFrame = null;
        switch(clientId)
        {
            case 0: photoFrame = GetSpriteTexture("photoframe_4.png"); break; // orange
            case 1: photoFrame = GetSpriteTexture("photoframe_0.png"); break; // red
            case 2: photoFrame = GetSpriteTexture("photoframe_1.png"); break; // green
            case 3: photoFrame = GetSpriteTexture("photoframe_2.png"); break; // purple
            case 4: photoFrame = GetSpriteTexture("photoframe_3.png"); break; // skybule
        }
        //ITextureRegion photoFrame = GetSpriteTexture("photoframe_0.png");
        Sprite photoFrameSprite = new Sprite(posX, posY, photoFrame, gameActivity.getVertexBufferObjectManager());
        photoFrameSprite.setZIndex(-110);
        scene.attachChild(photoFrameSprite);


        // photo
        BitmapTextureAtlasSource source = new BitmapTextureAtlasSource(bitmap);
        BitmapTextureAtlas texture = new BitmapTextureAtlas(gameActivity.getTextureManager(), 1024, 1024);
        texture.addTextureAtlasSource(source, 0, 0);
        texture.load();
        TextureRegion textureRegion = (TextureRegion)TextureRegionFactory.createFromSource(texture, source, 0, 0);

        ret = new Sprite(posX, posY, textureRegion, gameActivity.getVertexBufferObjectManager());
        ret.setZIndex(-100);
        scene.attachChild(ret);



        // todo : Sprite_flash 를 리턴 하는 쪽으로.
        Sprite_flash sprite_flash = new Sprite_flash(clientId, ret, photoFrameSprite);
        _flash_sprites.add(sprite_flash);

        return ret;
    }
    //====================================================================================================
    // release
    //====================================================================================================
    public static boolean flashSprittRelease = false;
    public static int deleteFlashSprite_clientId = -1; // -1 이면 전부 제거 한다.
    public IUpdateHandler ReleaseAll_flash_Sprites(final Scene scene)
    {
        return new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                if(flashSprittRelease == true )
                {
                    //for( Sprite sp : _flash_sprites)
                    for(Sprite_flash sp : _flash_sprites)
                    {
                        if( deleteFlashSprite_clientId == -1)
                        {
                            //scene.detachChild(sp);
                            sp.Release(scene);
                            _flash_sprites.remove(sp);
                        }
                        else
                        {
                            if( sp.Get_clientID() == deleteFlashSprite_clientId)
                            {
                                sp.Release(scene);
                                _flash_sprites.remove(sp);
                            }
                        }
                    }
                    ShareImage_reposition();
                    deleteFlashSprite_clientId = -1;
                    flashSprittRelease = false;
                }
            }
            @Override
            public void reset() {

            }
        };

        //_flash_sprites.clear();
    }
    public void ReleaseAll_sprite()
    {
        // box2d  에서 생성한 객체를 고려해서 제거 해줘야할듯.
        // todo : 만들어야함.

    }

    public void ReleaseAll_tileSprites()
    {
        // box2d  에서 생성한 객체를 고려해서 제거 해줘야할듯.
        for(ITiledTextureRegion tsp : _tiledTextures.values())
        {
            _textureManager.unloadTexture(tsp.getTexture());
        }
        _tiledTextures.clear();
    }

    public void ReleaseAll(Scene scene)
    {
        ReleaseAll_flash_Sprites(scene);
        ReleaseAll_sprite();
        ReleaseAll_tileSprites();
    }
    //====================================================================================================
    // util
    //====================================================================================================
    private void setResize_height(Sprite sprite, int heightSize)
    {
        int viewHeight = heightSize;
        float width = 0.0f;
        float height = 0.0f;

        width = sprite.getWidth();
        height = sprite.getHeight();

        float percente = (float)(height/100);
        float scale = (float)(viewHeight/percente);

        width *= (scale/100);
        height *= (scale/100);

        sprite.setWidth(width);
        sprite.setHeight(height);
    }
    public void ShareImage_reposition()
    {
        int imageCount = Manager_resource.Instance.GetSprite_flash().size();
        int cameraWidth = Activity_serverMain_andEngine.CAMERA_WIDTH;
        int cameraHeight = Activity_serverMain_andEngine.CAMERA_HEIGHT;
        float centerX = (cameraWidth / 2);
        float centerY = (cameraHeight / 2);

        float centerHalfX = centerX / 2;
        float centerHalfY = centerY / 2;

        float posX = 0.0f;
        float posY = 0.0f;

        //imageCount %= 9;

        if( imageCount == 1 )
        {
            Sprite_flash centerSprite = ((Sprite_flash) Manager_resource.Instance.GetSprite_flash().get(0));
            setResize_height(centerSprite.Get_sprite(), 256);
            posX = (cameraWidth / 2) - (centerSprite.Get_sprite().getWidth()/2);
            posY = (cameraHeight / 2) - (centerSprite.Get_sprite().getHeight()/2);
            centerSprite.Get_sprite().setPosition(posX, posY);


            centerSprite.Get_photoFrame().setWidth(centerSprite.Get_sprite().getWidth()+20);
            centerSprite.Get_photoFrame().setHeight(centerSprite.Get_sprite().getHeight()+20);
            centerSprite.Get_photoFrame().setPosition(posX-10, posY-10);
        }
        else if( imageCount <= 4)
        {

            float leftTopX = centerX - centerHalfX;
            float leftTopY = centerY - centerHalfY;

            float rightTopX = centerX + centerHalfX;
            float rightTopY = centerY - centerHalfY;

            float leftBottomX = centerX - centerHalfX;
            float leftBottomY = centerY + centerHalfY;

            float rightBottomX = centerX + centerHalfX;
            float rightBottomY = centerY + centerHalfY;

            int index = 0;
            for( Sprite_flash sprite_flash : Manager_resource.Instance.GetSprite_flash() )
            {
                setResize_height(sprite_flash.Get_sprite(), 120);

                switch (index) {
                    case 0:
                        posX = leftTopX - (sprite_flash.Get_sprite().getWidth()/ 2);
                        posY = leftTopY - (sprite_flash.Get_sprite().getHeight()/ 2);
                        break;
                    case 1:
                        posX = rightTopX - (sprite_flash.Get_sprite().getWidth()/ 2);
                        posY = rightTopY - (sprite_flash.Get_sprite().getHeight()/ 2);
                        break;
                    case 2:
                        posX = leftBottomX - (sprite_flash.Get_sprite().getWidth()/ 2);
                        posY = leftBottomY - (sprite_flash.Get_sprite().getHeight()/ 2);
                        break;
                    case 3:
                        posX = rightBottomX - (sprite_flash.Get_sprite().getWidth()/ 2);
                        posY = rightBottomY - (sprite_flash.Get_sprite().getHeight()/ 2);
                        break;
                }
                sprite_flash.Get_sprite().setPosition(posX, posY);
                sprite_flash.Get_photoFrame().setPosition(posX, posY);

                sprite_flash.Get_photoFrame().setWidth(sprite_flash.Get_sprite().getWidth()+20);
                sprite_flash.Get_photoFrame().setHeight(sprite_flash.Get_sprite().getHeight()+20);
                sprite_flash.Get_photoFrame().setPosition(posX-10, posY-10);

                index ++;
            }
        }
        else
        {
            int count_index = 0;
            final int count_tileX = 3;
            final int count_tileY = 3;
            for( Sprite_flash sprite_flash : Manager_resource.Instance.GetSprite_flash() )
            {
                count_index %= 9;
                setResize_height(sprite_flash.Get_sprite(), 95);

                float tileX = (cameraWidth/count_tileX);
                float tileY = (cameraHeight/count_tileY);

                posX = (count_index % count_tileX) * tileX;
                posY = ( count_index / count_tileX ) * tileY;

             //   posX += (sprite_flash.Get_sprite().getWidth()/4);
             //   posY += (sprite_flash.Get_sprite().getHeight()/4);

                sprite_flash.Get_sprite().setPosition(posX, posY);
                sprite_flash.Get_photoFrame().setPosition(posX, posY);

                sprite_flash.Get_photoFrame().setWidth(sprite_flash.Get_sprite().getWidth()+20);
                sprite_flash.Get_photoFrame().setHeight(sprite_flash.Get_sprite().getHeight()+20);
                sprite_flash.Get_photoFrame().setPosition(posX-10, posY-10);

                ++count_index;
            }
        }

    }
}

