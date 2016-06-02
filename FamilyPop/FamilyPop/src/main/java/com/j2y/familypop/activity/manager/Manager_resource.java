package com.j2y.familypop.activity.manager;

import android.content.Context;
import android.graphics.Bitmap;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

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

public class Manager_resource {
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


    private ArrayList<Sprite> _flash_sprites = null;
    //====================================================================================================
    // init
    //====================================================================================================
    public Manager_resource(Context gameActivity) {
        Instance = this;
        _gameActivity = gameActivity;
        _textureManager = ((BaseGameActivity) gameActivity).getTextureManager();

        _flash_sprites = new ArrayList<>();

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
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-01.png","FloPop_Server_Resources-06.png", 128, 34);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-02.png","FloPop_Server_Resources-07.png", 128, 68);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-03.png","FloPop_Server_Resources-08.png", 128, 102);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-04.png","FloPop_Server_Resources-09.png", 128, 136);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user-05.png","FloPop_Server_Resources-10.png", 128, 170);

        //event
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_01.png","FloPop_Server_Resources-11.png", 162, 50);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_02.png","FloPop_Server_Resources-12.png", 162, 100);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_03.png","FloPop_Server_Resources-13.png", 162, 150);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_04.png","FloPop_Server_Resources-14.png", 162, 200);
        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "user_like_05.png","FloPop_Server_Resources-15.png", 162, 250);

        add_spriteTexture(eType_atlas.ATLAS_SPRITE, "smile_01.png","FloPop_Server_Resources-21.png", 162, 300);


        _serverTextureAtlas.load();
        _spriteTextureAtlas.load();
        //- end texture load


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
    public synchronized Sprite Create_sprite(float posX, float posY, Scene scene, SimpleBaseGameActivity gameActivity,Bitmap bitmap)
    {
        Sprite ret = null;

        BitmapTextureAtlasSource source = new BitmapTextureAtlasSource(bitmap);
        BitmapTextureAtlas texture = new BitmapTextureAtlas(gameActivity.getTextureManager(), 1024, 1024);
        texture.addTextureAtlasSource(source, 0, 0);
        texture.load();
        TextureRegion textureRegion = (TextureRegion)TextureRegionFactory.createFromSource(texture, source, 0, 0);

        ret = new Sprite(posX, posY, textureRegion, gameActivity.getVertexBufferObjectManager());
        ret.setZIndex(-100);
        scene.attachChild(ret);

        _flash_sprites.add(ret);

        return ret;
    }
    public void ReleaseAll_sprites(Scene scene)
    {
        for( Sprite sp : _flash_sprites)
        {
            scene.detachChild(sp);
        }
        _flash_sprites.clear();
    }
}

