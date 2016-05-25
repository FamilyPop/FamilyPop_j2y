package com.j2y.familypop.activity.lobby;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.Activity_clientMain;
import com.j2y.familypop.activity.BaseActivity;
import com.j2y.familypop.activity.manager.Manager_contents;
import com.j2y.familypop.client.FpcScenarioDirectorProxy;
import com.j2y.network.base.FpNetConstants;
import com.j2y.network.client.FpNetFacade_client;
import com.nclab.familypop.R;

/**
 * Created by J2YSoft_Programer on 2016-05-17.
 */
public class Popup_dialogueMenu implements View.OnClickListener
{
    public static Popup_dialogueMenu Instance = null;
    String _name;

    View _view;

    //Top
    ImageView _photo;
    TextView  _profileName;
    PopupWindow _popupWindow;

    //Bottom

    //Bottom top
    ImageButton _status_button;
    ImageButton _account_button;
    ImageButton _setting_button;

    //Bottom mid

    ImageButton _sharing_pictures_button;
    ImageButton _sharing_keywords_button;
    ImageButton _flower_bomb_button;

    //Bottom bottom
    ImageButton _quit_dialogue_button;

    public Popup_dialogueMenu(View v, PopupWindow popup,String username)
    {

        _view = v;
        _popupWindow = popup;

        _profileName = (TextView) _view.findViewById(R.id.text_profile_name);

        _status_button = (ImageButton) _view.findViewById(R.id.button_dialog_status);
        _status_button.setOnClickListener(this);

        _account_button = (ImageButton) _view.findViewById(R.id.button_dialog_account);
        _account_button.setOnClickListener(this);

        _setting_button = (ImageButton) _view.findViewById(R.id.button_dialog_setting);
        _setting_button.setOnClickListener(this);

        _sharing_pictures_button = (ImageButton) _view.findViewById(R.id.button_dialog_sharing_pictures);
        _sharing_pictures_button.setOnClickListener(this);

        _sharing_keywords_button = (ImageButton) _view.findViewById(R.id.button_dialog_sharing_keywords);
        _sharing_keywords_button.setOnClickListener(this);

        _flower_bomb_button = (ImageButton) _view.findViewById(R.id.button_dialog_flower_bomb);
        _flower_bomb_button.setOnClickListener(this);

        _quit_dialogue_button = (ImageButton) _view.findViewById(R.id.button_dialog_quit_dialog);
        _quit_dialogue_button.setOnClickListener(this);

        ((Button) _view.findViewById(R.id.test_talk)).setOnClickListener(this);

        _profileName.setText(username);
    }



    private  boolean _temp_send_talk;
    @Override
    public void onClick(View v)
    {

        switch(v.getId())
        {
            case R.id.button_dialog_status :
                _profileName.setText("status");
                break;
            case R.id.button_dialog_account :
                _profileName.setText("account");
                break;
            case R.id.button_dialog_setting:
                _profileName.setText("setting");
                _popupWindow.dismiss();
                break;
            case R.id.button_dialog_sharing_pictures:
                _profileName.setText("pictures");
                onSharing_pictures();
                break;
            case R.id.button_dialog_sharing_keywords:
                _profileName.setText("keywords");
                break;
            case R.id.button_dialog_flower_bomb:
                _profileName.setText("flower");
                break;
            case R.id.button_dialog_quit_dialog:
                _profileName.setText("quit");
                break;
            case R.id.test_talk: onTalk();  break;

        }
        _popupWindow.dismiss();
    }

    private void onTalk()
    {
        if(!_temp_send_talk)
        {
            //_selectScenario = FpNetConstants.SCENARIO_RECORD;
            Activity_clientMain.Instance._selectScenario = Manager_contents.eType_contents.CONTENTS_TALK.getValue();
            FpNetFacade_client.Instance.SendPacket_req_changeScenario(Activity_clientMain.Instance._selectScenario);
            _temp_send_talk = true;
        }
    }
    private void onSharing_pictures()
    {
        MainActivity.Instance.startActivity(new Intent(MainActivity.Instance, Activity_photoGallery.class));
    }

//    private String get_user_name()
//    {
//        String name = Activity_talkHistory.Instance._userName;
//        return name;
//    }

  }
