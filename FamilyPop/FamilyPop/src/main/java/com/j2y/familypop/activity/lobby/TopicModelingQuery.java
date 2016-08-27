package com.j2y.familypop.activity.lobby;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.CheckBox;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * Created by KJW on 2016-04-25.
 * And modified by Jungi on 2016-08-27.
 * - change class name from DownloadWebpageTask to TopicModelingQuery
 */

public class TopicModelingQuery extends AsyncTask<String, Void, String> {
    String strEventType;
    String strEventQuery;

    public TopicModelingQuery(String eventType, String eventQuery) {
        this.strEventType = eventType;
        this.strEventQuery = eventQuery;
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
    protected void onPostExecute(String result) {
    }

        /*Connection Module*/

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);

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

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}

class QueryThread extends Thread
{
    private String userSelected;
    QueryThread(HashSet<String> selected)
    {
        userSelected = "";
        Iterator<String> iter = selected.iterator();
        while(iter.hasNext())
        {
            if (userSelected != "")
                userSelected += ",";
            userSelected += iter.next();
        }
        Log.i("TopicModeling", "Selected users: " + userSelected);
    }

    public void run()
    {
        try {
            String query_result = new TopicModelingQuery(userSelected, "").execute("http://143.248.139.91:5000").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}