package com.j2y.familypop.activity.manager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ImageReader;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.j2y.familypop.activity.manager.contents.client.Contents_clientReady;
import com.j2y.familypop.activity.manager.gallery.ImageInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by J2YSoft_Programer on 2016-05-02.
 */
public class Manager_photoGallery
{
    public static Manager_photoGallery Instance;

    private ArrayList<ImageInfo> _imageList;
    private ArrayList<Bitmap> _bitmap_file;


    //================================================================================================
    // get
    public int Get_countImageList(){ return  _imageList.size(); }
    public int Get_countBitmap(){return _bitmap_file.size();}
    public void Release_lists()
    {
        _imageList = new ArrayList<>();
        _bitmap_file = new ArrayList<>();
    }
    public Bitmap Get_bitmap(int index)
    {
        Bitmap ret = null;
        if( Get_countBitmap() == 0) return ret;

        ret = _bitmap_file.get(index);

        return ret;
    }
    public ImageInfo Get_inageInfo(int index)
    {
        ImageInfo ret = null;
        if( Get_countImageList() == 0) return ret;

        ret = _imageList.get(index);

        return ret;
    }
    public Manager_photoGallery()
    {
        Instance = this;
        _imageList = new ArrayList<ImageInfo>();
        _bitmap_file = new ArrayList<Bitmap>();
    }
    public void SetArrayList(ArrayList<ImageInfo> imageList)
    {
        if(imageList.size() <= 0 )
            return;

        _imageList = ImageListSetup(imageList);
        _bitmap_file = ChangeToBitmapList(imageList);
    }

    private ArrayList<ImageInfo> ImageListSetup(ArrayList<ImageInfo> imageList)
    {
        ArrayList<ImageInfo> list = new ArrayList<ImageInfo>();

        for( int i = 0; i < imageList.size(); i++)
        {
            list.add(i, imageList.get(i));
        }

        return list;
    }

    private ArrayList<Bitmap> ChangeToBitmapList(ArrayList<ImageInfo> imageList)
    {
        ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();

        for(int i = 0; i < imageList.size(); i++)
        {
            bitmapList.add(imageList.get(i).GetBitmap());
        }

        return bitmapList;
    }

    public ArrayList<ImageInfo> GetAllImageInfo()
    {
        if(_imageList.size() <= 0)
            return null;

        //----------------------------------------------------------------
        ArrayList<ImageInfo> ret = new ArrayList<ImageInfo>();

        //return _imageList;
        return ret;
    }

    public ImageInfo GetItemImageInfo(int index)
    {
        if(index < 0)
            return null;

        return _imageList.get(index);
    }

    //내장 메모리에서 이미지들을 검색해서 넣어줌.
    //private ArrayList<ImageInfo> _imageList_Memory;
    public ArrayList<ImageInfo> FindMemoryRootImage(Context context)
    {
        ArrayList<ImageInfo> ret = null;
        //Select 하고자 하는 컬럼
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};

        //쿼리 수행
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);

        if(imageCursor != null && imageCursor.getCount() > 0)
        {
            ret = new ArrayList<>();
            //컬럼 인덱스
            int imageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int imageDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //이름 추가
            int imageNameCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);

            //커서에서 이미지의 ID와 경로명을 가져와서 Thumb이미지 모델 클래스를 생성하여 리스트에 더해줌
            while(imageCursor.moveToNext())
            {
                ImageInfo thumbImage = new ImageInfo();

                thumbImage.SetId(imageCursor.getString(imageIDCol));
                thumbImage.SetData(imageCursor.getString(imageDataCol));
                thumbImage.SetCheckedState(false);  //check 상태 기본값 false
                thumbImage.SetTopic(imageCursor.getString(imageNameCol));

                //_imageList_Memory.add(thumbImage);
                ret.add(thumbImage);
            }
        }

        imageCursor.close();
        //_photoGallery.SetImageList(_imageList_Memory);
        return ret;
    }

}


