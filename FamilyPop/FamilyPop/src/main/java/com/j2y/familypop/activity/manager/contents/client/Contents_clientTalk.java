package com.j2y.familypop.activity.manager.contents.client;

import android.util.Log;
import android.view.View;

import com.j2y.familypop.activity.Activity_clientMain;
import com.j2y.familypop.activity.JoyStick;
import com.j2y.familypop.activity.Vector2;
import com.j2y.familypop.activity.manager.contents.BaseContents;
import com.j2y.familypop.client.FpcRoot;
import com.j2y.familypop.client.FpcTalkRecord;
import com.j2y.familypop.server.FpsRoot;
import com.j2y.network.client.FpNetFacade_client;

/**
 * Created by lsh on 2016-05-17.
 */
public class Contents_clientTalk extends BaseContents
{
    private int _voiceAvgCount;
    private float _voiceAmpAvg;
    private JoyStick _joystick = null;

    @Override
    public void init()
    {
        super.init();

        //kookm0616
        FpcRoot.Instance._socioPhone.RegisterQuery();

        _joystick = Activity_clientMain.Instance._joystick;
      //  _clientId = FpcRoot.Instance._clientId;
        Activity_clientMain.Instance._button_connectServer.setVisibility(View.GONE);

    }
    @Override
    public  synchronized boolean update()
    {
        //Log.e("[J2Y]", "talk_clientUpdate");
        double amplitude = FpcRoot.Instance._socioPhone.GetSoundAmplitue();
        Log.i("[J2Y]", " amplitude :  "+amplitude);
        ++_voiceAvgCount;
        _voiceAmpAvg += amplitude;
        if(_voiceAvgCount >= 5) {

            _voiceAmpAvg /= (float)_voiceAvgCount;
            //_text_voiceAmplitudeAverage.setText("Voice:" + (int)_voiceAmpAvg);
            _voiceAvgCount = 0;
            FpNetFacade_client.Instance.SendPacket_familyTalk_voice(_voiceAmpAvg);
        }
        // joystick
        if( _joystick != null)
        {
            if(_joystick.gettouchState())
            {
                Vector2 v2 = new Vector2(_joystick.getX(),_joystick.getY());
                Vector2 n = v2.nor();
                n.x *= (Activity_clientMain.Instance._attractorMoveSpeed * (_joystick.Get_centerDistance() * 0.01f));
                n.y *= (Activity_clientMain.Instance._attractorMoveSpeed * (_joystick.Get_centerDistance() * 0.01f));

                FpNetFacade_client.Instance.SendPacket_req_userInput_bubbleMove(n.x, n.y,FpcRoot.Instance._clientId);
            }
        }
        return super.update();
    }
    @Override
    public void release()
    {

    }

    public void TalkRecordSave(String name, String filename, int bubble_color_type)
    {
        // TalkRecordSave
        // record save
        FpcTalkRecord talk_record = FpcRoot.Instance._selected_talk_record;
        if(talk_record != null)
        {
//            talk_record._name = _editText.getText().toString();
//            talk_record._filename = FpcRoot.Instance._socioPhone.GetWavFileName();
//            talk_record._bubble_color_type = FpcRoot.Instance._bubble_color_type;

            talk_record._name = name;
            talk_record._filename = filename;
            talk_record._bubble_color_type = bubble_color_type;
            //debug
            //Toast.makeText(Activity_clientMain.Instance, FpcRoot.Instance._socioPhone.GetWavFileName(), Toast.LENGTH_SHORT).show();
            Log.i("[J2Y]", String.format("[from][to]:%s , %s", talk_record._filename, FpcRoot.Instance._socioPhone.GetWavFileName()));
        }

        FpNetFacade_client.Instance.SendPacket_req_talk_record_info();

        SaveTalkRecord();

//        if(exit_talk)
//        {
//            if(net_request)
//                request_exitRoom();
//            else
//                ExitRoom();
//
//            finish();
//        }
//        else
//        {
//            cancel();
//        }
    }
    private void SaveTalkRecord()
    {
        // 대화 정보 기록하기
        FpcTalkRecord talk_record = FpcRoot.Instance._selected_talk_record;
        if(talk_record != null)
        {
            talk_record._endTime = System.currentTimeMillis();

            //talk_record._name = "test_save";
            if(!talk_record._list_added)
                FpcRoot.Instance.AddTalkRecord(talk_record);
            FpcRoot.Instance.SaveTalkRecords();
        }
    }
}
