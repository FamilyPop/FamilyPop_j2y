package com.j2y.familypop.activity.lobby;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nclab.familypop.R;

/**
 * Created by J2YSoft_Programer on 2016-05-17.
 */
public class Popup_dialogue_historyMenu implements View.OnClickListener
{
    String _name;

    View _view;

    //Top
    ImageView _photo;
    TextView  _profileName;

    //Bottom

    //Bottom top
    ImageButton _status_button;
    ImageButton _account_button;
    ImageButton _setting_button;

    //Bottom mid

    ImageButton _sharing_pictures_button;
    ImageButton _sharing_keywords_button;

    public Popup_dialogue_historyMenu(View v, String username)
    {

        _view = v;

        _profileName = (TextView) _view.findViewById(R.id.text_profile_name);

        _status_button = (ImageButton) _view.findViewById(R.id.button_dh_status);
        _status_button.setOnClickListener(this);

        _account_button = (ImageButton) _view.findViewById(R.id.button_dh_account);
        _account_button.setOnClickListener(this);

        _setting_button = (ImageButton) _view.findViewById(R.id.button_dh_setting);
        _setting_button.setOnClickListener(this);

        _sharing_pictures_button = (ImageButton) _view.findViewById(R.id.button_dh_sharing_pictures);
        _sharing_pictures_button.setOnClickListener(this);

        _sharing_keywords_button = (ImageButton) _view.findViewById(R.id.button_dh_sharing_keywords);
        _sharing_keywords_button.setOnClickListener(this);

        _profileName.setText(username);
    }



    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.button_dh_status :
                _profileName.setText("status");
                break;
            case R.id.button_dh_account :
                _profileName.setText("account");
                break;
            case R.id.button_dh_setting:
                _profileName.setText("setting");
                break;
            case R.id.button_dh_sharing_pictures:
                _profileName.setText("pictures");
                break;
            case R.id.button_dh_sharing_keywords:
                _profileName.setText("keywords");
                break;

        }
    }

//    private String get_user_name()
//    {
//        String name = Activity_talkHistory.Instance._userName;
//        return name;
//    }

  }
