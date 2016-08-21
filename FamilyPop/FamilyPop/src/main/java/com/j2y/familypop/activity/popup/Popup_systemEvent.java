package com.j2y.familypop.activity.popup;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.j2y.familypop.activity.Activity_clientMain;
import com.j2y.familypop.client.FpcRoot;
import com.j2y.network.base.FpNetConstants;
import com.j2y.network.base.data.FpNetDataReq_setting_systemEvent;
import com.j2y.network.client.FpNetFacade_client;
import com.j2y.network.server.FpNetServer_client;
import com.nclab.familypop.R;

import org.andengine.input.touch.TouchEvent;

/**
 * Created by lsh on 2016-08-19.
 */
public class Popup_systemEvent implements View.OnClickListener
{

    PopupWindow _popupWindow;
    View _v;

    Button _checkBox_clam;
    Button _checkBox_pair;
    private boolean _clam_isChecked = true;
    private boolean _pair_isChecked = true;

    public Popup_systemEvent(View v, PopupWindow popupWindow)
    {
        _popupWindow = popupWindow;
        _v = v;

        //((EditText)v.findViewById(R.id.editText_regulation_buffer_count)).setText(String.valueOf(Activity_clientMain.Instance._buffer_count));
        //((EditText)v.findViewById(R.id.editText_regulation_smile_effect)).setText(String.valueOf(Activity_clientMain.Instance._smile_effect));
        _checkBox_clam = ((Button)v.findViewById(R.id.checkBox_systemEvent_clam));
        _checkBox_pair = ((Button)v.findViewById(R.id.checkBox_systemEvent_clamPair));
        _checkBox_clam.setOnClickListener(this);
        _checkBox_pair.setOnClickListener(this);

        ((Button)v.findViewById(R.id.button_systemEvent_send)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.button_systemEvent_cancel)).setOnClickListener(this);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_systemEvent_send:

                FpNetDataReq_setting_systemEvent reqPaket = new FpNetDataReq_setting_systemEvent();
                reqPaket.Set_clam(_clam_isChecked);
                reqPaket.Set_pair(_pair_isChecked);

                FpNetFacade_client.Instance.sendMessage(FpNetConstants.CSReq_systemEvent, reqPaket);
                _popupWindow.dismiss();
                break;

            case R.id.button_systemEvent_cancel: _popupWindow.dismiss();  break;

            case R.id.checkBox_systemEvent_clam:
                if( _clam_isChecked )
                {
                    _clam_isChecked = !_clam_isChecked; // false
                    _checkBox_clam.setText("Clam Off");
                }
                else
                {
                    _clam_isChecked = !_clam_isChecked; // true
                    _checkBox_clam.setText("Clam On");
                }

                break;
            case R.id.checkBox_systemEvent_clamPair:
                if( _pair_isChecked )
                {
                    _pair_isChecked = !_pair_isChecked; // false
                    _checkBox_pair.setText("Pair Off");
                }
                else
                {
                    _pair_isChecked = !_pair_isChecked; // true
                    _checkBox_pair.setText("Pair On");
                }

                break;
        }
        //_popupWindow.dismiss();
//        boolean checked = false;
//        switch(v.getId())
//        {
//            case R.id.checkBox_systemEvent_clam:
//                checked = ((CheckBox)v).isChecked();
//
//
//
//                break;
//            case R.id.checkBox_systemEvent_clamPair:
//                checked = ((CheckBox)v).isChecked();
//
//
//                break;
//        }
    }




}
