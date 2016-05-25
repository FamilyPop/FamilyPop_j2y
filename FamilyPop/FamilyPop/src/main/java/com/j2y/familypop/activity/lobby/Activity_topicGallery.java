package com.j2y.familypop.activity.lobby;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;


import com.j2y.familypop.activity.BaseActivity;
import com.j2y.familypop.activity.manager.gallery.ImageInfo;
import com.j2y.familypop.activity.manager.gallery.PhotoGallery;
import com.nclab.familypop.R;

import java.util.ArrayList;


/**
 * Created by J2YSoft_Programer on 2016-04-28.
 */
public class Activity_topicGallery extends BaseActivity implements View.OnClickListener
{
    public enum eTopicButtons
    {
        NONE(-1),DAY(0),WEEK(1),MONTH(2),YEAR(3),MAX(4);

        private int value;
        eTopicButtons(int i)
        {
            value = i;
        }
        public int getValue()
        {
            return value;
        }
    }


    GridView _gridView;
    ArrayList<ImageInfo> _thumb_imageList;
    Context _context;
    PhotoGallery _photoGallery;

    ImageButton[] _imageButtons;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 초기화
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue_topic_gallery);


        _context = this;
        _thumb_imageList = new ArrayList<ImageInfo>();
        _gridView = (GridView) findViewById(R.id.grid_view);
        _photoGallery = new PhotoGallery(this, _gridView);
        _photoGallery.SetTopic(true);

        _imageButtons = new ImageButton[eTopicButtons.MAX.getValue()];

        _imageButtons[eTopicButtons.DAY.getValue()] = (ImageButton) findViewById(R.id.button_topic_day);
        _imageButtons[eTopicButtons.DAY.getValue()].setOnClickListener(this);
        _imageButtons[eTopicButtons.WEEK.getValue()] = (ImageButton) findViewById(R.id.button_topic_week);
        _imageButtons[eTopicButtons.WEEK.getValue()].setOnClickListener(this);
        _imageButtons[eTopicButtons.MONTH.getValue()] = (ImageButton) findViewById(R.id.button_topic_month);
        _imageButtons[eTopicButtons.MONTH.getValue()].setOnClickListener(this);
        _imageButtons[eTopicButtons.YEAR.getValue()] = (ImageButton) findViewById(R.id.button_topic_year);
        _imageButtons[eTopicButtons.YEAR.getValue()].setOnClickListener(this);


        FindMemoryRootImage();

//        ImageButton button = (ImageButton)findViewById(R.id.button_photo_select);
//        button.setOnClickListener(this);
    }

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
        switch (v.getId())
        {
            case R.id.button_topic_day:
                AllButtonsNotSelect();
                CheckTabMenu(_imageButtons[eTopicButtons.DAY.getValue()],true);
                break;
            case R.id.button_topic_week:
                AllButtonsNotSelect();
                CheckTabMenu(_imageButtons[eTopicButtons.WEEK.getValue()],true);
                break;
            case R.id.button_topic_month:
                AllButtonsNotSelect();
                CheckTabMenu(_imageButtons[eTopicButtons.MONTH.getValue()],true);
                break;
            case R.id.button_topic_year:
                AllButtonsNotSelect();
                CheckTabMenu(_imageButtons[eTopicButtons.YEAR.getValue()],true);
                break;
        }
    }

    private void CheckTabMenu(ImageButton imagebutton, boolean check)
    {
        if(check)
            imagebutton.setBackgroundResource(R.drawable.button_topic_select);
        else
            imagebutton.setBackgroundResource(R.drawable.button_topic_non);
    }

    private void AllButtonsNotSelect()
    {
        for(int i = 0; i < eTopicButtons.MAX.getValue(); i++)
        {
            CheckTabMenu(_imageButtons[i], false);
        }
    }

}
