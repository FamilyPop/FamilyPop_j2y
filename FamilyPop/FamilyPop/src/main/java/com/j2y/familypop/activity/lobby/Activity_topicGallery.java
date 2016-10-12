package com.j2y.familypop.activity.lobby;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.j2y.familypop.activity.BaseActivity;
import com.j2y.familypop.activity.manager.gallery.ImageInfo;
import com.j2y.familypop.activity.manager.gallery.PhotoGallery;
import com.j2y.network.base.data.FpNetDataNoti_clientUpdate;
import com.j2y.network.client.FpNetFacade_client;
import com.nclab.familypop.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

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


    CheckBox _checkbox_orange;
    CheckBox _checkbox_green;
    CheckBox _checkbox_blue;
    CheckBox _checkbox_purple;
    //CheckBox _checkbox_red;

    // keeping the Information of selected users in "Intersection With"
    HashSet<String> userSelected = new HashSet<String>();

    Button _topic_send = null;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 초기화
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialogue_topic_gallery);


        //send button
        _topic_send = (Button)findViewById(R.id.button_topic_send);

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

        //FindMemoryRootImage();

        // keyword
        _textview_keyword_top = (TextView)findViewById(R.id.textView_topic_keyword_top);
        _textview_keyword_bottom = (TextView)findViewById(R.id.textView_topic_keyword_bottom);

        // 접속 한 유저 만큼 checked box 도출하기.
        ArrayList<FpNetDataNoti_clientUpdate.clientInfo> _userInfos = FpNetFacade_client.Instance.GetClientsInfos();

        //checked box
        _checkbox_orange = (CheckBox)findViewById(R.id.checkBox_intersection_0);
        _checkbox_orange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    userSelected.add ("User_A");
                else
                    userSelected.remove("User_A");

                sendQueryToTopicModelingServer ();
            }
        });

        _checkbox_green = (CheckBox)findViewById(R.id.checkBox_intersection_1);
        _checkbox_green.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    userSelected.add ("User_B");
                else
                    userSelected.remove("User_B");

                sendQueryToTopicModelingServer();
            }
        });

        _checkbox_purple = (CheckBox)findViewById(R.id.checkBox_intersection_2);
        _checkbox_purple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    userSelected.add ("User_C");
                else
                    userSelected.remove("User_C");

                sendQueryToTopicModelingServer();
            }
        });

        _checkbox_blue = (CheckBox)findViewById(R.id.checkBox_intersection_3);
        _checkbox_blue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        // checked box 전부 gone
        _checkbox_orange.setVisibility(View.GONE);
        _checkbox_purple.setVisibility(View.GONE);
        _checkbox_blue.setVisibility(View.GONE);
        _checkbox_green.setVisibility(View.GONE);

        //COLOR_ERROR(-1), COLOR_ORANGE(0), COLOR_YELLOW_GREEN(1), COLOR_PURPLE(2), COLOR_SKY_BLUE(3), COLOR_RED(4); // # color 이걸 기준. // common 으로 manager 을 통해서 빼내자.
        if( _userInfos != null )
        {
            for( FpNetDataNoti_clientUpdate.clientInfo uinfo : _userInfos )
            {
                switch(uinfo._clientId) // 컬러는 무조건 0 이 들어온다. ( 사용 안하는듯.)
                {
                    case 0: _checkbox_orange.setVisibility(View.VISIBLE);   break;
                    case 1: _checkbox_green.setVisibility(View.VISIBLE);   break;
                    case 2: _checkbox_purple.setVisibility(View.VISIBLE);   break;
                    case 3: _checkbox_blue.setVisibility(View.VISIBLE);   break;
                }
            }
        }
    }

    // create a thread that sends a request to TopicModeling server
    // - Jungi
    private void sendQueryToTopicModelingServer()
    {
        new QueryThread(userSelected).run();
    }

    private void DisplayTopicModelingContents(
            ArrayList<String> keywords, ArrayList<String> relatedPosts, ArrayList<String> textInPost)
    {
        //  keywords, relatedPosts, and textInPost must have the same length!
        assert(keywords.size() == relatedPosts.size() && relatedPosts.size() == textInPost.size());

        //  내부 저장소에 있는 사진들 중에서 관련있는 사진을 선택
        ArrayList<String> posts = SelectTopicModelingPosts(relatedPosts);

        _thumb_imageList.clear();
        for (int i=0; i<keywords.size(); i++)
        {
            if ((i % 3) == 0)
            {
                ImageInfo topicSet = new ImageInfo();
                topicSet.SetKeyword(keywords.get(i) + ", " + keywords.get(i+1) + ", " + keywords.get(i+2));
                _thumb_imageList.add(topicSet);
            }
            ImageInfo post = new ImageInfo();
            post.SetData(posts.get(i));
            post.SetTopic(textInPost.get(i));
            _thumb_imageList.add(post);
        }
        _photoGallery.SetImageList(_thumb_imageList);
    }

    private ArrayList<String> SelectTopicModelingPosts(ArrayList<String> relatedPosts)
    {
        ArrayList<String> posts = new ArrayList<>();

        //Select 하고자 하는 컬럼
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};

        //쿼리 수행
        Cursor imageCursor = _context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);

        if (imageCursor != null && imageCursor.getCount() > 0)
        {
            for (int i=0;i <relatedPosts.size(); i++)
            {
                boolean found = false;
                imageCursor.moveToFirst();

                int imageDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                while (imageCursor.moveToNext())
                {
                    //  사진 이름으로 관련 포스트 사진을 찾는다.
                    if (imageCursor.getString(imageDataCol).contains(relatedPosts.get(i)))
                    {
                        Log.i("TopicModeling", imageCursor.getString(imageDataCol));
                        posts.add(imageCursor.getString(imageDataCol));
                        found = true;
                    }
                }

                if (!found)
                    posts.add ("");
            }
        }

        assert(relatedPosts.size() == posts.size());

        return posts;
    }
    // - Jungi

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
            int count = 0;
            while(imageCursor.moveToNext())
            {
                ImageInfo thumbImage = new ImageInfo();

                if (count % 3 == 0)
                {
                    ImageInfo keyword = new ImageInfo();
                    keyword.SetKeyword("keyword");
                    _thumb_imageList.add(keyword);
                }
                thumbImage.SetId(imageCursor.getString(imageIDCol));
                thumbImage.SetData(imageCursor.getString(imageDataCol));
                thumbImage.SetCheckedState(false);  //check 상태 기본값 false
                thumbImage.SetTopic(imageCursor.getString(imageNameCol));
                _thumb_imageList.add(thumbImage);

                count++;
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
            case R.id.button_topic_send:
                //=====================================================================================


                //=====================================================================================
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

    class QueryThread extends Thread
    {
        private String userSelected;
        private final String topicDelim = ",,,,,";

        QueryThread(HashSet<String> selected)
        {
            userSelected = "";
            Iterator<String> iter = selected.iterator();
            while(iter.hasNext())
            {
                if (userSelected != "")
                    userSelected += topicDelim;
                userSelected += iter.next();
            }
            Log.i("TopicModeling", "Selected users: " + userSelected);
        }

        public void run()
        {
            ArrayList<String> keywords = new ArrayList<String> ();
            ArrayList<String> relatedPosts = new ArrayList<String> ();
            ArrayList<String> textInPost = new ArrayList<String> ();

            //  아무 사용자도 선택되지 않았을 때, 빈 ArrayList를 이용한다.
            if (userSelected == "")
            {
                DisplayTopicModelingContents(keywords, relatedPosts, textInPost);
                return;
            }

            AsyncTask<String, Void, String> query_task = new TopicModelingQuery(this, userSelected);
            query_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://143.248.139.91:5000");

            return;
        }

        public void displayQueryResult (ArrayList<String> keywords, ArrayList<String> relatedPosts, ArrayList<String> textInPost) {
            DisplayTopicModelingContents(keywords, relatedPosts, textInPost);
        }
    }
}
