package com.j2y.familypop.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;

import android.widget.AbsListView;
import android.widget.AdapterView;

import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.Activity_clientMain;
import com.j2y.familypop.activity.BaseActivity;
import com.j2y.familypop.activity.manager.actors.BaseActor;
import com.j2y.familypop.activity.manager.gallery.ImageInfo;
import com.j2y.familypop.activity.manager.gallery.PhotoGallery;
import com.j2y.network.client.FpNetFacade_client;
import com.nclab.familypop.R;

import java.util.ArrayList;


/**
 * Created by J2YSoft_Programer on 2016-04-28.
 */
//extends BaseActivity
public class Activity_photoGallery  implements ListView.OnScrollListener, GridView.OnItemClickListener, View.OnClickListener
{
    GridView _gridView;
    ArrayList<ImageInfo> _thumb_imageList;
    Context _context;
    PhotoGallery _photoGallery;


    public void Active()
    {
        ((BaseActivity)_context).findViewById(R.id.llImageList).setVisibility(View.VISIBLE);
        _gridView.setVisibility(View.VISIBLE);
    }
    public void DeActive()
    {
        ((BaseActivity)_context).findViewById(R.id.llImageList).setVisibility(View.GONE);
        _gridView.setVisibility(View.GONE);
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 초기화
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public Activity_photoGallery(BaseActivity context, GridView gridView)
    {
        _context = context;
        _thumb_imageList = new ArrayList<ImageInfo>();
        _gridView = gridView;
                //(GridView) context.findViewById(R.id.grid_view);
        _photoGallery = new PhotoGallery(context, _gridView);

        FindMemoryRootImage();

        ImageButton button = (ImageButton)context.findViewById(R.id.button_photo_select);
        button.setOnClickListener(this);

        //gridView.setVisibility(View.GONE);
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dialogue_photo_gallery);
//
//        _context = this;
//        _thumb_imageList = new ArrayList<ImageInfo>();
//        _gridView = (GridView) findViewById(R.id.grid_view);
//        _photoGallery = new PhotoGallery(this, _gridView);
//
//
//        FindMemoryRootImage();
//
//        ImageButton button = (ImageButton)findViewById(R.id.button_photo_select);
//        button.setOnClickListener(this);
//    }

    //내장 메모리에서 이미지들을 검색해서 넣어줌.
    private void FindMemoryRootImage()
    {
        //Select 하고자 하는 컬럼
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};

        //쿼리 수행
        Cursor imageCursor = _context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);

        if(imageCursor != null && imageCursor.getCount() > 0)
        {
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

                _thumb_imageList.add(thumbImage);
            }
        }

        imageCursor.close();
        _photoGallery.SetImageList(_thumb_imageList);
    }
    @Override
    public void onClick(View v)
    {
        if( v.getId() == R.id.button_photo_select )
        {
            ArrayList<ImageInfo> checkedList = _photoGallery.GetImageList();
            MainActivity.Instance._photoManager.SetArrayList(checkedList);

            FpNetFacade_client.Instance.SendPacket_req_shareImage();
            //StartActivity(MainActivity.Instance, Activity_clientMain.class);
        }

        DeActive();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {

    }

}
