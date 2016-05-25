package com.j2y.familypop.activity.lobby;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;
import com.j2y.familypop.activity.BaseActivity;
import com.nclab.familypop.R;

/**
 * Created by J2YSoft_Programer on 2016-05-17.
 */
public class Popup_historyMenu implements View.OnClickListener
{
    String _name;

    Activity _activity;
    View     _view;
    Layout   _layout;

    //Top
    ImageView _photo;
    TextView  _profileName;

    //Bottom
    ImageButton _status_button;
    ImageButton _account_button;
    ImageButton _setting_button;

    public Popup_historyMenu(View v, String profile_name)
    {
        _view      = v;

        //레이아웃 셋팅
        _profileName = (TextView)_view.findViewById(R.id.text_profile_name);
        _status_button = (ImageButton) _view.findViewById(R.id.button_popup_status);
        _status_button.setOnClickListener(this);
        _account_button = (ImageButton) _view.findViewById(R.id.button_popup_account);
        _account_button.setOnClickListener(this);
        _setting_button = (ImageButton) _view.findViewById(R.id.button_popup_setting);
        _setting_button.setOnClickListener(this);

        _profileName.setText(profile_name);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.button_popup_status :
                _profileName.setText("status");
                break;
            case R.id.button_popup_account :
                _profileName.setText("account");
                break;
            case R.id.button_popup_setting:
                _profileName.setText("setting");
                break;
        }
    }


    private void createDialog() {
        //Title바 없에기
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //LayoutParams
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //팝업 외부 뿌연 효과
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //뿌연 효과 정도
//        layoutParams.dimAmount = 0.7f;
        //위치
//        layoutParams.gravity = Gravity.LEFT;
//        getWindow().setAttributes(layoutParams);
    }



  }
