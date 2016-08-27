package com.j2y.familypop.activity.manager.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by J2YSoft_Programer on 2016-05-03.
 */
public class ImageInfo
{
    private String _id;
    private String _data;
    private String _topic;
    private boolean _checked_state;

    public String getId()
    {
        return _id;
    }

    public  void SetId(String id)
    {
        _id = id;
    }

    public String GetData()
    {
        return _data;
    }

    public void SetData(String data)
    {
        _data = data;
    }

    public boolean GetCheckedState()
    {
        return _checked_state;
    }

    public  void SetCheckedState(boolean check)
    {
        _checked_state = check;
    }

    public Bitmap GetBitmap()
    {
        return ChangeToBitmap(_data);
    }

    public void SetTopic(String topic) { _topic = topic; }
    public String GetTopic() { return _topic; }

    private Bitmap ChangeToBitmap(String path)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        return bitmap;
    }


}
