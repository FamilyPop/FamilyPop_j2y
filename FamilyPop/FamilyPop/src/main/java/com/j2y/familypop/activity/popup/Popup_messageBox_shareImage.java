package com.j2y.familypop.activity.popup;

import android.app.Activity;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nclab.familypop.R;

/**
 * Created by lsh on 2016-08-20.
 */
public class Popup_messageBox_shareImage extends Dialog implements android.view.View.OnClickListener
{
    public Activity _activity;

    public Button _ok;
    public Button _cancel;
    public TextView _content;
    public ImageView _imageView;
    //public EditText _editText;

    private String _okTXT;
    private String _cancelTXT;

    public Popup_messageBox_shareImage(Activity activity)
    {
        //Do you want to share a      ramdom picture?
        super(activity,R.style.Theme_Dialog);

        _okTXT = new String("YES");
        _cancelTXT = new String("NO");

        _activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.custom_dialog);
        setContentView(R.layout.popup_messagebox_shareimage);

        _ok = (Button) findViewById(R.id.button_popupmessagebox_ok);
        //_ok.setBackground(R.style.Theme_Dialog);

        _cancel = (Button) findViewById(R.id.button_popupmessagebox_cancel);
        _content = (TextView) findViewById(R.id.txt_popupmessagebox_content);

        _imageView = (ImageView)findViewById(R.id.imageView_popupmessagebox_shareimage);
        //_editText = (EditText) findViewById(R.id.editText_messageBox_ok_cancel);

        _ok.setText(_okTXT);
        _cancel.setText(_cancelTXT);

        _ok.setOnClickListener(this);
        _cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
