package com.j2y.familypop.activity.popup;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.j2y.familypop.activity.Activity_clientMain;
import com.j2y.network.client.FpNetFacade_client;
import com.nclab.familypop.R;

import org.andengine.entity.sprite.ButtonSprite;

/**
 * Created by lsh on 2016-06-13.
 */
public class Popup_settingTalk_regulation implements View.OnClickListener
{
    PopupWindow _popupWindow;
    View _v;

    public Popup_settingTalk_regulation(View v, PopupWindow popupWindow)
    {
        _v = v;
        _popupWindow = popupWindow;
        ((Button)v.findViewById(R.id.button_regulation_setting_send)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.button_regulation_setting_cancel)).setOnClickListener(this);

        ((EditText)v.findViewById(R.id.editText_regulation_buffer_count)).setText(String.valueOf(Activity_clientMain.Instance._buffer_count));
        ((EditText)v.findViewById(R.id.editText_regulation_smile_effect)).setText(String.valueOf(Activity_clientMain.Instance._smile_effect));
        ((EditText)v.findViewById(R.id.editText_regulation_voice_hold)).setText(String.valueOf(Activity_clientMain.Instance._voice_hold));
        ((EditText)v.findViewById(R.id.editText_regulation_attractorSpeed)).setText(String.valueOf(Activity_clientMain.Instance._attractorMoveSpeed));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_regulation_setting_send: onSend(); break;
            case R.id.button_regulation_setting_cancel: onCencal(); break;
        }
        _popupWindow.dismiss();
    }

    //=================================================================================================
    //
    private void onSend()
    {
        Activity_clientMain main = Activity_clientMain.Instance;

        main._buffer_count  = Integer.parseInt(((EditText)_v.findViewById(R.id.editText_regulation_buffer_count)).getText().toString());
        main._smile_effect  = Integer.parseInt(((EditText)_v.findViewById(R.id.editText_regulation_smile_effect)).getText().toString());
        main._voice_hold    = Integer.parseInt(((EditText)_v.findViewById(R.id.editText_regulation_voice_hold)).getText().toString());
        main._attractorMoveSpeed = Float.valueOf(((EditText)_v.findViewById(R.id.editText_regulation_attractorSpeed)).getText().toString());


        FpNetFacade_client.Instance.SendPacket_req_regulation_info();
    }
    private void onCencal()
    {
        _popupWindow.dismiss();
    }
}


