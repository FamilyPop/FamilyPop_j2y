package com.j2y.familypop.activity.popup;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.j2y.familypop.activity.Activity_clientMain;
import com.nclab.familypop.R;

/**
 * Created by lsh on 2016-06-13.
 */
public class Popup_settingTalk implements View.OnClickListener
{

    PopupWindow _popupWindow;

    public Popup_settingTalk(View v, PopupWindow popupWindow)
    {
        _popupWindow = popupWindow;
        ((Button)(v.findViewById(R.id.button_talkModeSetting_flower))).setOnClickListener(this);
        ((Button)(v.findViewById(R.id.button_talkModeSetting_regulation))).setOnClickListener(this);
        ((Button)(v.findViewById(R.id.button_talkModeSetting_flowerCollider))).setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_talkModeSetting_flower: onSettingFlower(); break;
            case R.id.button_talkModeSetting_regulation: onSettingRegulation(); break;
            case R.id.button_talkModeSetting_flowerCollider: onSettingFlowerCollider(); break;
        }
        _popupWindow.dismiss();
    }

    private void onSettingFlower()
    {
        Activity_clientMain main = Activity_clientMain.Instance;
        if( main != null)
        {
            View popupView = main.getLayoutInflater().inflate(R.layout.popup_setting_flower, null);
            PopupWindow popupWindow = new PopupWindow(popupView);

            popupWindow.setWindowLayoutMode(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            //Popup_settingTalk popupSetting = new Popup_settingTalk(popupView, popupWindow);
            Popup_settingTalk_flower popup_settingTalk_flower = new Popup_settingTalk_flower(popupView, popupWindow);
        }
    }
    private void onSettingFlowerCollider()
    {
        Activity_clientMain main = Activity_clientMain.Instance;

        if( main !=  null )
        {
            View popupView = main.getLayoutInflater().inflate(R.layout.popup_setting_flowercollider, null);
            PopupWindow popupWindow = new PopupWindow(popupView);

            popupWindow.setWindowLayoutMode(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            Popup_settingTalk_flowerCollider popup_settingTalk_flowerCollider = new Popup_settingTalk_flowerCollider(popupView, popupWindow);
        }
    }
    private void onSettingRegulation()
    {
        Activity_clientMain main = Activity_clientMain.Instance;
        if( main != null)
        {
            View popupView = main.getLayoutInflater().inflate(R.layout.popup_setting_regulation, null);
            PopupWindow popupWindow = new PopupWindow(popupView);

            popupWindow.setWindowLayoutMode(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            //Popup_settingTalk popupSetting = new Popup_settingTalk(popupView, popupWindow);
            Popup_settingTalk_regulation popup_settingTalk_regulation = new Popup_settingTalk_regulation(popupView, popupWindow);
        }
    }
}
