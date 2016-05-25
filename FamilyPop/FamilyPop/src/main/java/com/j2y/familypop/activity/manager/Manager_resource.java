package com.j2y.familypop.activity.manager;

import android.content.Context;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lsh on 2016-04-29.
 */


public class Manager_resource
{
    // enum
    public enum eType_atlas{
        ATLAS_DUMMY(0), ATLAS_SERVER(1);

        private int value;

        private eType_atlas (int value) {
            this.value = value;
        }

        public int getValue () {
            return value;
        }

    }
    public enum eImageIndex_color{
        COLOR_ERROR(-1), COLOR_ORANGE(0), COLOR_YELLOW_GREEN(1),  COLOR_PURPLE(2), COLOR_SKY_BLUE(3), COLOR_RED(4);
        private int value;
        private eImageIndex_color (int value) {
            this.value = value;
        }
        public int getValue () {
            return value;
        }
        public boolean Compare(int i){return value == i;}

        public static eImageIndex_color IntToImageColor(int v)
        {
            eImageIndex_color[] As = eImageIndex_color.values();
            for( int i=0; i<As.length; ++i)
            {
                if( As[i].Compare(v)){ return As[i]; }
            }
            return eImageIndex_color.COLOR_ERROR;
        }
    }
    public enum eImageIndex_event{
        EVENT_ERROR(-1), EVENT_SMILE(0), EVENT_STAR(1), EVENT_HEART(2), EVENT_FIRE(3);
        private int value;
        private eImageIndex_event (int value) {
            this.value = value;
        }
        public boolean Compare(int i){return value == i;}
        public int getValue () { return value; }

    }
    public enum eType_petal{
        PETAL_TALK(0), PETAL_SMILE(1), PETAL_GOOD(2);
        private int value;
        private eType_petal (int value) {
            this.value = value;
        }
        public int getValue () {
            return value;
        }
    }

    public static Manager_resource Instance = null;

  //private SimpleBaseGameActivity _gameActivity = null;
    private Context _gameActivity = null;
    private TextureManager _textureManager = null;
    private BitmapTextureAtlas _dummyTextureAtlas = null;
    private BitmapTextureAtlas _serverTextureAtlas = null;

    private HashMap<String, ITiledTextureRegion> _tiledTextures = null;
    //private HashMap<String, ITextureRegion> _textures = null;

    private final int ATLAS_WIDTH = 2048;
    private final int ATLAS_HEGIHT = 2048;

    private ArrayList<String> _userImageNames = null;
    private ArrayList<String> _eventImageNames = null;

    private ArrayList<String> _talkPetalNames = null;
    private ArrayList<String> _smilePetalNames = null;
    private ArrayList<String> _likePetalNames = null;


    public Manager_resource(Context gameActivity){
        Instance = this;
        _gameActivity = gameActivity;
        _textureManager = ((BaseGameActivity)gameActivity).getTextureManager();

        init_loadTexture();
        init_textureNames();
    }
    //====================================================================================================
    // init
    //====================================================================================================
    private void init_loadTexture(){
        _tiledTextures = new HashMap<String, ITiledTextureRegion>();
        //_textures = new HashMap<String, ITextureRegion>();

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        //_bitmapTextureAtlas= new BitmapTextureAtlas(_textureManager, 64, 64, TextureOptions.BILINEAR);
        // todo: 사이즈 자동으로 배치 되도록. 아틀라스 구분할 방법도 생각해 봐야한다.

        _dummyTextureAtlas= new BitmapTextureAtlas(_textureManager, ATLAS_WIDTH, ATLAS_HEGIHT);
        _serverTextureAtlas = new BitmapTextureAtlas(_textureManager, ATLAS_WIDTH, ATLAS_HEGIHT);


        //- texture load
        // todo : 문자열 따로 관리 해야함 (문자열 다빼불자)

//        // dummy
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY, "face_box_tiled.png", "face_box_tiled.png", 0, 0, 2, 1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY, "face_circle_tiled.png",  "face_circle_tiled.png", 0, 32, 2, 1);
//
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY, "error_null_tiledTextures.png", "error_null_tiledTextures.png", 0, 64, 2, 1);
//
//        //attractor
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"user_icon_pressed_00.png", "user_icon_pressed_00.png", 0, 128, 2, 1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"user_icon_pressed_01.png", "user_icon_pressed_01.png", 0, 192, 2, 1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"user_icon_pressed_02.png", "user_icon_pressed_02.png", 0, 256, 2, 1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"user_icon_pressed_03.png", "user_icon_pressed_03.png", 0, 320, 2, 1);
//
//
//        // talk
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"image_bead_0.png", "image_bead_0.png" , 0, 384, 2,1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"image_bead_1.png", "image_bead_1.png" , 0, 448, 2,1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"image_bead_2.png", "image_bead_2.png" , 0, 512, 2,1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"image_bead_3.png", "image_bead_3.png" , 0, 576, 2,1);
//
//        // good event
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"fampop_gui_resource_0322-00.png", "fampop_gui_resource_0322-00.png", 128, 0, 2, 1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"fampop_gui_resource_0322-01.png", "fampop_gui_resource_0322-01.png", 128, 128, 2, 1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"fampop_gui_resource_0322-02.png", "fampop_gui_resource_0322-02.png", 128, 256, 2, 1);
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"fampop_gui_resource_0322-03.png", "fampop_gui_resource_0322-03.png", 128, 384, 2, 1);
//
//        // test image flower
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"image_flower.png", "image_flower.png", 512, 0, 2, 1);
//
//        // smile
//        add_TiledTexture(eType_atlas.ATLAS_DUMMY,"image_smile.png","image_smile.png", 640, 0, 2, 1);

        //ITextureRegion test = BitmapTextureAtlasTextureRegionFactory.createFromAsset(_bitmapTextureAtlas, _gameActivity, "user_icon_pressed_00.png", 0, 0);
        //public static TextureRegion createFromAsset(final BitmapTextureAtlas pBitmapTextureAtlas, final Context pContext, final String pAssetPath, final int pTextureX, final int pTextureY) {

//        _dummyTextureAtlas.load();

        // server (main)
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "fire_k-01-01.png", "fire_k-01-01.png",      0, 0, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "heart_k-01-01.png", "heart_k-01-01.png",    0, 96, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "smile_k-01-01.png", "smile_k-01-01.png",    0, 192, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "star_k-01-01.png", "star_k-01-01.png",      0, 288, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "like_petal-01.png", "like_petal-01.png",    0, 384, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "like_petal-02.png", "like_petal-02.png",    0, 480, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "like_petal-03.png", "like_petal-03.png",    0, 576, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "like_petal-04.png", "like_petal-04.png",    0, 672, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "like_petal-05.png", "like_petal-05.png",    0, 768, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "smile_petal-01.png", "smile_petal-01.png",  0, 864, 2,1);

        add_TiledTexture(eType_atlas.ATLAS_SERVER, "smile_petal-04.png", "smile_petal-04.png",  192, 96, 2, 1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "smile_petal-05.png", "smile_petal-05.png",  192, 192, 2, 1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "talk_petal-01.png", "talk_petal-01.png",    192, 288, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "talk_petal-02.png", "talk_petal-02.png",    192, 384, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "talk_petal-03.png", "talk_petal-03.png",    192, 480, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "talk_petal-04.png", "talk_petal-04.png",    192, 576, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "talk_petal-05.png", "talk_petal-05.png",    192, 672, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "user-01.png", "user-01.png",                192, 768, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "user-02.png", "user-02.png",                192, 864, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "user-03.png", "user-03.png",                192, 960, 2,1);

        add_TiledTexture(eType_atlas.ATLAS_SERVER, "user-04.png", "user-04.png",                384, 0, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "user-05.png", "user-05.png",                384, 96, 2,1);

        add_TiledTexture(eType_atlas.ATLAS_SERVER, "smile_petal-02.png", "smile_petal-02.png",  384, 192, 2,1);
        add_TiledTexture(eType_atlas.ATLAS_SERVER, "smile_petal-03.png", "smile_petal-03.png",  384, 288, 2,1);


        _serverTextureAtlas.load();
        //- end texture load



    }
    private void init_textureNames(){

        _userImageNames = new ArrayList<>();
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
        // eventImageNames
        _eventImageNames.add("smile_k-01-01.png");
        _eventImageNames.add("star_k-01-01.png");
        _eventImageNames.add("heart_k-01-01.png");
        _eventImageNames.add("fire_k-01-01.png");

        // talkImageNames
        _talkPetalNames.add("talk_petal-01.png");
        _talkPetalNames.add("talk_petal-02.png");
        _talkPetalNames.add("talk_petal-03.png");
        _talkPetalNames.add("talk_petal-04.png");
        _talkPetalNames.add("talk_petal-05.png");

        // smilePetalNames
        _smilePetalNames.add("smile_petal-01.png");
        _smilePetalNames.add("smile_petal-02.png");
        _smilePetalNames.add("smile_petal-03.png");
        _smilePetalNames.add("smile_petal-04.png");
        _smilePetalNames.add("smile_petal-05.png");

        // likePetalNames
        _likePetalNames.add("like_petal-01.png");
        _likePetalNames.add("like_petal-02.png");
        _likePetalNames.add("like_petal-03.png");
        _likePetalNames.add("like_petal-04.png");
        _likePetalNames.add("like_petal-05.png");
    }

    private void add_TiledTexture(eType_atlas type, String key, String texName, int pTextureX, int pTextureY, int pTileColumns, int pTileRows)
    {

        ITiledTextureRegion ret = null;
        switch(type)
        {
            case ATLAS_DUMMY: ret = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(_dummyTextureAtlas, _gameActivity, texName, pTextureX, pTextureY, pTileColumns, pTileRows); break;
            case ATLAS_SERVER: ret = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(_serverTextureAtlas, _gameActivity, texName, pTextureX, pTextureY, pTileColumns, pTileRows); break;

        }
        if( ret != null)  _tiledTextures.put(key, ret);
    }

    //    private void add_Texture(String key, String texName, int pTextureX, int pTextureY)
//    {
//        ITextureRegion ret = BitmapTextureAtlasTextureRegionFactory.createFromAsset(_bitmapTextureAtlas, _gameActivity, texName, pTextureX, pTextureY);
//        _textures.put(key, ret);
//    }
    //====================================================================================================
    // get texture
    //====================================================================================================
    public ITiledTextureRegion GetTiledTexture(String key)
    {
        ITiledTextureRegion ret = null;

        if( _tiledTextures.containsKey(key) ){ ret = _tiledTextures.get(key); }
        else{ ret = _tiledTextures.get("error_null_tiledTextures.png"); }
        return ret;
    }
//    public ITextureRegion GetTexture(String key)
//    {
//        ITextureRegion ret = null;
//
//        if( _textures.containsKey(key) ){ ret = _textures.get(key); }
//        else
//        {
//            // todo : 에러 이미지 만들어야함.
//            //ret = _textures.get("error_null_tiledTextures.png");
//        }
//
//        return ret;
//    }
    // todo : sprite 생성 함수 만들기.
    //public

    //====================================================================================================
    // get image name
    //====================================================================================================
    public String Get_petalNames(eImageIndex_color color, eType_petal type_petal)
    {
        String ret = null;
        switch(type_petal)
        {
            case PETAL_TALK:    ret = _talkPetalNames.get(color.getValue());  break;
            case PETAL_GOOD:    ret = _likePetalNames.get(color.getValue());   break;
            case PETAL_SMILE:   ret = _smilePetalNames.get(color.getValue());  break;
        }
        return ret;
    }
    public String Get_userImage(eImageIndex_color color)
    {
        String ret = null;
        ret = _userImageNames.get(color.getValue());
        return ret;
    }
}

