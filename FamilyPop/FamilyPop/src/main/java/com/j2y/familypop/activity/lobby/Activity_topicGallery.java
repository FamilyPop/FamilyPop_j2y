package com.j2y.familypop.activity.lobby;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.j2y.familypop.activity.BaseActivity;
import com.j2y.familypop.activity.manager.gallery.ImageInfo;
import com.j2y.familypop.activity.manager.gallery.PhotoGallery;
import com.j2y.network.base.data.FpNetDataNoti_clientUpdate;
import com.j2y.network.client.FpNetFacade_client;
import com.nclab.familypop.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.j2y.familypop.activity.topicInterface;

/**
 * Created by J2YSoft_Programer on 2016-04-28.
 */
public class Activity_topicGallery extends BaseActivity implements /*View.OnClickListener,*/ topicInterface {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Activity_topicGallery Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public enum eTopicButtons {
        NONE(-1), DAY(0), WEEK(1), MONTH(2), YEAR(3), MAX(4);

        private int value;

        eTopicButtons(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }
    }

    GridView _gridView;
    ArrayList<ImageInfo> _thumb_imageList;
    Context _context;
    PhotoGallery _photoGallery;

    // topic keyword
    TextView _textview_keyword_top;
    TextView _textview_keyword_bottom;

    CheckBox _checkbox_orange;
    CheckBox _checkbox_green;
    CheckBox _checkbox_blue;
    CheckBox _checkbox_purple;

    // keeping the Information of selected users in "Intersection With"
    HashSet<String> userSelected = new HashSet<String>();

    ArrayList<String> keywords;
    ArrayList<String> relatedPosts;
    ArrayList<String> textInPost;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 초기화
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialogue_topic_gallery);

        _context = this;
        _thumb_imageList = new ArrayList<ImageInfo>();
        _gridView = (GridView) findViewById(R.id.grid_view);
        _photoGallery = new PhotoGallery(this, _gridView, R.layout.gridview_item_topicimage, true);
        _photoGallery.SetTopic(true);

        // keyword
        _textview_keyword_top = (TextView) findViewById(R.id.textView_topic_keyword_top);
        _textview_keyword_bottom = (TextView) findViewById(R.id.textView_topic_keyword_bottom);

        // 접속 한 유저 만큼 checked box 도출하기.
        ArrayList<FpNetDataNoti_clientUpdate.clientInfo> _userInfos = FpNetFacade_client.Instance.GetClientsInfos();

        //checked box
        _checkbox_orange = (CheckBox) findViewById(R.id.checkBox_intersection_0);
        _checkbox_orange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    userSelected.add("User_A");
                else
                    userSelected.remove("User_A");

                sendQueryToTopicModelingServer();
            }
        });

        _checkbox_green = (CheckBox) findViewById(R.id.checkBox_intersection_1);
        _checkbox_green.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    userSelected.add("User_B");
                else
                    userSelected.remove("User_B");

                sendQueryToTopicModelingServer();
            }
        });

        _checkbox_purple = (CheckBox) findViewById(R.id.checkBox_intersection_2);
        _checkbox_purple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    userSelected.add("User_C");
                else
                    userSelected.remove("User_C");

                sendQueryToTopicModelingServer();
            }
        });

        _checkbox_blue = (CheckBox) findViewById(R.id.checkBox_intersection_3);
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
        if (_userInfos != null) {
            for (FpNetDataNoti_clientUpdate.clientInfo uinfo : _userInfos) {
                switch (uinfo._clientId) // 컬러는 무조건 0 이 들어온다. ( 사용 안하는듯.)
                {
                    case 0:
                        _checkbox_orange.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        _checkbox_green.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        _checkbox_purple.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        _checkbox_blue.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void shareKeywords (int id) {
        FpNetFacade_client.Instance.SendPacket_req_topic(
                10, _photoGallery.GetImageList().get(id).GetBitmap(), "");
    }

    // create a thread that sends a request to TopicModeling server
    // - Jungi
    private void sendQueryToTopicModelingServer() {
        new QueryThread(userSelected).run();
    }

    private void DisplayTopicModelingContents(
            ArrayList<String> keywords, ArrayList<String> relatedPosts, ArrayList<String> textInPost) {
        //  keywords, relatedPosts, and textInPost must have the same length!
        assert (keywords.size() == relatedPosts.size() && relatedPosts.size() == textInPost.size());

        new CursorThread().run();
    }

    private ArrayList<String> SelectTopicModelingPosts(ArrayList<String> relatedPosts) {
        ArrayList<String> posts = new ArrayList<>();

        //Select 하고자 하는 컬럼
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME};

        //쿼리 수행
        Cursor imageCursor = _context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

        if (imageCursor != null && imageCursor.getCount() > 0) {
            for (int i = 0; i < relatedPosts.size(); i++) {
                boolean found = false;
                imageCursor.moveToFirst();

                int imageDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                while (imageCursor.moveToNext()) {
                    //  사진 이름으로 관련 포스트 사진을 찾는다.
                    if (imageCursor.getString(imageDataCol).contains(relatedPosts.get(i))) {
                        //Log.i("TopicModeling", imageCursor.getString(imageDataCol));
                        posts.add(imageCursor.getString(imageDataCol));
                        found = true;
                    }
                }

                if (!found)
                    posts.add("");
            }
        }

        imageCursor.close();

        assert (relatedPosts.size() == posts.size());

        return posts;
    }
    // - Jungi

    class QueryThread extends Thread {
        private String userSelected;
        private final String topicDelim = ",,,,,";

        QueryThread(HashSet<String> selected) {
            userSelected = "";
            Iterator<String> iter = selected.iterator();
            while (iter.hasNext()) {
                if (userSelected != "")
                    userSelected += topicDelim;
                userSelected += iter.next();
            }
            Log.i("TopicModeling", "Selected users: " + userSelected);
        }

        public void run() {
            keywords = new ArrayList<String>();
            relatedPosts = new ArrayList<String>();
            textInPost = new ArrayList<String>();

            //  아무 사용자도 선택되지 않았을 때, 빈 ArrayList를 이용한다.
            if (userSelected == "") {
                DisplayTopicModelingContents(keywords, relatedPosts, textInPost);
                return;
            }

            AsyncTask<String, Void, String> query_task = new TopicModelingQuery(this, userSelected);
            query_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://143.248.139.91:5000");
            //query_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://192.168.0.50:5000");

            return;
        }

        public void displayQueryResult(ArrayList<String> _keywords, ArrayList<String> _relatedPosts, ArrayList<String> _textInPost) {
            keywords.addAll(_keywords);
            relatedPosts.addAll(_relatedPosts);
            textInPost.addAll(_textInPost);

            _keywords.clear();
            _relatedPosts.clear();
            _textInPost.clear();

            assert (_keywords.isEmpty() && !keywords.isEmpty());
            assert (_relatedPosts.isEmpty() && !relatedPosts.isEmpty());
            assert (_textInPost.isEmpty() && !textInPost.isEmpty());

            DisplayTopicModelingContents(keywords, relatedPosts, textInPost);
        }
    }

    class CursorThread extends Thread {
        CursorThread() {
        }

        public void run() {
            //  내부 저장소에 있는 사진들 중에서 관련있는 사진을 선택
            ArrayList<String> posts = SelectTopicModelingPosts(relatedPosts);

            _thumb_imageList.clear();
            for (int i = 0; i < keywords.size(); i++) {
                if ((i % 3) == 0) {
                    ImageInfo topicSet = new ImageInfo();
                    topicSet.SetKeyword(keywords.get(i) + ", " + keywords.get(i + 1) + ", " + keywords.get(i + 2));
                    _thumb_imageList.add(topicSet);
                }
                ImageInfo post = new ImageInfo();
                post.SetData(posts.get(i));
                post.SetTopic(textInPost.get(i));
                _thumb_imageList.add(post);
            }
            _photoGallery.SetImageList(_thumb_imageList);
        }
    }
}
