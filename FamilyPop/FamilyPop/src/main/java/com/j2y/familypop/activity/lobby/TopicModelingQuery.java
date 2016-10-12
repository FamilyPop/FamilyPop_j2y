package com.j2y.familypop.activity.lobby;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by KJW on 2016-04-25.
 * And modified by Jungi on 2016-08-27.
 * - change class name from DownloadWebpageTask to TopicModelingQuery
 */

public class TopicModelingQuery extends AsyncTask<String, Void, String> {
    String strEventType;
    Activity_topicGallery.QueryThread queryThread = null;

    private final String topicDelim = ",,,,,";
    private final String postDelim = "!#!#!#";

    public TopicModelingQuery(Activity_topicGallery.QueryThread queryThread, String eventType) {
        this.strEventType = eventType;
        this.queryThread = queryThread;

        Log.i("TopicModeling", "TopicModeling Query created with users selected: " + eventType);
    }

    @Override
    protected String doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String query_result) {
        ArrayList<String> keywords = new ArrayList<String> ();
        ArrayList<String> relatedPosts = new ArrayList<String> ();
        ArrayList<String> textInPost = new ArrayList<String> ();

        // Parse the result
        String[] sets = query_result.split("##########");
        String[] topicSet, postSet;

        //for (int i=0; i<sets.length; i++)
        //    Log.i("TopicModeling", sets[i]);
        //Log.i("TopicModeling", "length: " + query_result.length());

        //  Parse the keyword sets
        topicSet = sets[0].split("///");
        for (int i=0; i<topicSet.length; i++)
        {
            StringTokenizer topics = new StringTokenizer(topicSet[i], topicDelim);
            while (topics.hasMoreTokens())
                keywords.add(topics.nextToken());
        }

        //  Parse the related posts
        postSet = sets[1].split("///");
        for (int k=0; k<postSet.length; k++)
        {
            String[] posts = postSet[k].split(topicDelim);
            for (int i=0; i<posts.length; i++)
            {
                String[] tk = posts[i].split(postDelim);
                relatedPosts.add(tk[0]);
                textInPost.add(tk[1]);
            }
        }

        Log.i("TopicModeling", keywords.toString());
        Log.i("TopicModeling", relatedPosts.toString());
        Log.i("TopicModeling", textInPost.toString());

        queryThread.displayQueryResult(keywords, relatedPosts, textInPost);
    }

    /*Connection Module*/
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Get 65,535 characters of the retrieved web page content.
        int len = 4096;

        try {
            URL url = new URL(myurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            Log.i("TopicModeling", "Connection to Topic Modeling Server established.");

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            ContentValues values = new ContentValues();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            StringBuilder sb = new StringBuilder();

            sb.append(URLEncoder.encode("users", "UTF-8")); //"username"
            sb.append("=");
            sb.append(URLEncoder.encode(strEventType, "UTF-8")); //"username"

            /*if(!strEventQuery.equals("topic")) {
                sb.append("&");
                sb.append(URLEncoder.encode("text", "UTF-8")); //"username"
                sb.append("=");
                sb.append(URLEncoder.encode(strEventQuery, "UTF-8"));
            }*/

            writer.write(sb.toString());
            writer.flush();
            writer.close();

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            Log.i("TopicModeling", "Response from Topic Modeling Server received.");

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);

        String ret;
        String content = "";
        while ((ret = bufferedReader.readLine()) != null)
            content += ret;

        return content;
    }
}

