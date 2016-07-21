package com.j2y.familypop.activity.popup;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.j2y.familypop.activity.Activity_clientMain;
import com.j2y.network.client.FpNetFacade_client;
import com.nclab.familypop.R;

/**
 * Created by lsh on 2016-07-18.
 */
public class Popup_settingTalk_flowerCollider implements View.OnClickListener
{
    PopupWindow _popupWindow;
    View _v;

    public Popup_settingTalk_flowerCollider(View v, PopupWindow popupWindow)
    {
        _v = v;
        _popupWindow = popupWindow;
        ((Button)v.findViewById(R.id.button_flowercollider_talk_send)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.button_flowercollider_talk_cancel)).setOnClickListener(this);

        ((EditText)v.findViewById(R.id.editText_collider_talk)).setText(String.valueOf(Activity_clientMain.Instance._colliderTalkSize));
        ((EditText)v.findViewById(R.id.editText_collider_smile)).setText(String.valueOf(Activity_clientMain.Instance._colliderSmileSize));
        ((EditText)v.findViewById(R.id.editText_collider_good)).setText(String.valueOf(Activity_clientMain.Instance._colliderGoodSize));

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_flowercollider_talk_send: onSend(); break;
            case R.id.button_flowercollider_talk_cancel: onCancel(); break;
        }
        _popupWindow.dismiss();
    }

    //=======================================================================================================
    //
    private void onSend()
    {
//        Activity_clientMain main = Activity_clientMain.Instance;
//
//        main._buffer_count  = Integer.parseInt(((EditText)_v.findViewById(R.id.editText_regulation_buffer_count)).getText().toString());
//        main._smile_effect  = Integer.parseInt(((EditText)_v.findViewById(R.id.editText_regulation_smile_effect)).getText().toString());
//        main._voice_hold    = Integer.parseInt(((EditText)_v.findViewById(R.id.editText_regulation_voice_hold)).getText().toString());
        Activity_clientMain main = Activity_clientMain.Instance;

//        main._flowerPlusSize = Float.valueOf(((EditText)_v.findViewById(R.id.editText_flower_talk_addScale)).getText().toString());
//        main._flowerMaxSize = Float.valueOf(((EditText)_v.findViewById(R.id.editText_flower_talk_maxScale)).getText().toString());
//        main._flowerMinSize = Float.valueOf(((EditText)_v.findViewById(R.id.editText_flower_talk_minScale)).getText().toString());
//        main._flowerGoodSize = Float.valueOf(((EditText)_v.findViewById(R.id.editText_flower_talk_goodScale)).getText().toString());
//        main._flowerSmileSize = Float.valueOf(((EditText)_v.findViewById(R.id.editText_flower_talk_smileScale)).getText().toString());

        main._colliderGoodSize  = Float.valueOf(((EditText)_v.findViewById(R.id.editText_collider_good)).getText().toString());
        main._colliderSmileSize = Float.valueOf(((EditText)_v.findViewById(R.id.editText_collider_smile)).getText().toString());
        main._colliderTalkSize  = Float.valueOf(((EditText)_v.findViewById(R.id.editText_collider_talk)).getText().toString());

        FpNetFacade_client.Instance.SendPacket_req_regulation_info();
    }
    private void onCancel()
    {
        _popupWindow.dismiss();
    }
}
