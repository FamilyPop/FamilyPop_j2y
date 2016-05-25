package com.j2y.familypop.activity.manager.contents.client;

import android.util.Log;

import com.j2y.familypop.activity.Activity_clientMain;
import com.j2y.familypop.activity.JoyStick;
import com.j2y.familypop.activity.Vector2;
import com.j2y.familypop.activity.manager.contents.BaseContents;
import com.j2y.familypop.client.FpcRoot;
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

        _joystick = Activity_clientMain.Instance._joystick;
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

                FpNetFacade_client.Instance.SendPacket_req_userInput_bubbleMove(n.x, -n.y);
            }
        }
        return super.update();
    }
    @Override
    public void release()
    {

    }
}
