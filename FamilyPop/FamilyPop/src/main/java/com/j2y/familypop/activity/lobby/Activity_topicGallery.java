package com.j2y.familypop.activity.lobby;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


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

    // topic keyword
    TextView _textview_keyword_top;
    TextView _textview_keyword_bottom;

    // radio button
    RadioGroup _radioGroup;
//    RadioButton _radioButton_0;
//    RadioButton _radioButton_1;
//    RadioButton _radioButton_2;
//    RadioButton _radioButton_3;

    RadioButton[] _radioButtons;
    ArrayList<Integer> _radioDeactives;
    ArrayList<Integer> _radioActives;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 초기화
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue_topic_gallery);

        //_radioButtons = new ArrayList<>();
        _radioButtons = new RadioButton[4];
        _radioDeactives = new ArrayList<Integer>();
        _radioActives = new ArrayList<Integer>();

        _context = this;
        _thumb_imageList = new ArrayList<ImageInfo>();
        _gridView = (GridView) findViewById(R.id.grid_view);
        _photoGallery = new PhotoGallery(this, _gridView, R.layout.gridview_item_topicimage);
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

        // keyword
        _textview_keyword_top = (TextView)findViewById(R.id.textView_topic_keyword_top);
        _textview_keyword_bottom = (TextView)findViewById(R.id.textView_topic_keyword_bottom);

        _radioGroup = (RadioGroup) findViewById(R.id.radiogroup_intersectionwith);

        _radioButtons[0] = (RadioButton)findViewById(R.id.radioButton_intersection_0);
        _radioButtons[1] = (RadioButton)findViewById(R.id.radioButton_intersection_1);
        _radioButtons[2] = (RadioButton)findViewById(R.id.radioButton_intersection_2);
        _radioButtons[3] = (RadioButton)findViewById(R.id.radioButton_intersection_3);



        // 라디오버튼 기본 이미지
        _radioDeactives.add(R.drawable.check_green);
        _radioDeactives.add(R.drawable.check_blue);
        _radioDeactives.add(R.drawable.check_people);
        _radioDeactives.add(R.drawable.check_pink);

        // 라디오버튼 선택 이미지
        _radioActives.add(R.drawable.check_green0);
        _radioActives.add(R.drawable.check_blue0);
        _radioActives.add(R.drawable.check_people0);
        _radioActives.add(R.drawable.check_pink0);


        //test 라디오 버튼 추가.
        deActive_all();
        active_radioButton(0);
        active_radioButton(1);

        _radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.radioButton_intersection_0:
                        setChecked(0);
                        break;
                    case R.id.radioButton_intersection_1:
                        //_radioButton_1.setChecked(true);
                        //_radioButtons.get(0).setBackgroundResource(R.drawable.image_bubble_orange_smile);
                        setChecked(1);
                        break;
                    case R.id.radioButton_intersection_2:
                        setChecked(2);
                        break;
                    case R.id.radioButton_intersection_3:
                        setChecked(3);
                        break;
                }
            }
        });
//        ImageButton button = (ImageButton)findViewById(R.id.button_photo_select);
//        button.setOnClickListener(this);
    }
    //리디오버튼
    private void deActive_all()
    {
        for( RadioButton rb : _radioButtons )
        {
            rb.setVisibility(View.GONE);

            //rb.setChecked(false);
        }
    }
    private void deActive_radioButton(int index)
    {
        setUnchecked_all();
        _radioButtons[index].setVisibility(View.GONE);
    }
    private void active_radioButton(int index)
    {
        setUnchecked_all();
        _radioButtons[index].setVisibility(View.VISIBLE);
    }

    private void setUnchecked_all()
    {
        boolean checked = false;
        for( int i=0; i<_radioButtons.length; ++i )
        {
            _radioButtons[i].setChecked(checked);
            _radioButtons[i].setButtonDrawable(_radioDeactives.get(i));
        }
    }
    private void setChecked(int index)
    {
        setUnchecked_all();
        boolean checked = true;

        _radioButtons[index].setChecked(checked);
        //_radioButtons[index].setBackgroundResource(R.drawable.check_green0);
        _radioButtons[index].setButtonDrawable(_radioActives.get(index));
    }
    //end 리디오버튼
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
