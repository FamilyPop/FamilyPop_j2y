package com.j2y.familypop.activity.manager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ImageReader;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.j2y.familypop.activity.manager.gallery.ImageInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by J2YSoft_Programer on 2016-05-02.
 */
public class Manager_photoGallery
{
    public static Manager_photoGallery Instance;

    public ArrayList<ImageInfo> _imageList;
    public ArrayList<Bitmap> _bitmap_file;

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
}


