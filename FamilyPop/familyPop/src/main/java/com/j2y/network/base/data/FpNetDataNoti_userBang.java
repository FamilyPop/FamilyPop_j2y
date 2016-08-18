package com.j2y.network.base.data;

import com.j2y.network.base.FpNetIncomingMessage;
import com.j2y.network.base.FpNetOutgoingMessage;

/**
 * Created by lsh on 2016-08-18.
 */
public class FpNetDataNoti_userBang extends FpNetData_base
{

    private final int ask_doNotShareImage = 0;
    private final int ask_shareImage = 1;

    int _askShareImage = ask_doNotShareImage;

    public void Ask_doNotshareImage()
    {
        _askShareImage = ask_doNotShareImage;
    }
    public void Ask_shareImage()
    {
        _askShareImage = ask_shareImage;
    }
    public void Ask_ShareImage(boolean askShareImage)
    {
        if( askShareImage)
        {
            Ask_shareImage();
        }
        else
        {
            Ask_doNotshareImage();
        }
    }
    public boolean GetAsk_ShareImage()
    {
        boolean ret = false;

        switch (_askShareImage)
        {
            case ask_shareImage: ret = true; break;
            case ask_doNotShareImage: ret = false; break;
        }

        return ret;
    }


    //public int _curScenario;
    //----------------------------------------------------------------

    @Override
    public void Parse(FpNetIncomingMessage inMsg)
    {
        super.Parse(inMsg);
        _askShareImage = inMsg.ReadInt();

    //    _curScenario = inMsg.ReadInt();
    }
    //----------------------------------------------------------------

    @Override
    public void Packing(FpNetOutgoingMessage outMsg)
    {
        super.Packing(outMsg);

        outMsg.Write(_askShareImage);
      //  outMsg.Write(_curScenario);

    }
}


