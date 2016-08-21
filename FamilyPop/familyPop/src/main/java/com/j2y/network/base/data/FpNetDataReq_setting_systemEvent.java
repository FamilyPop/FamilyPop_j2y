package com.j2y.network.base.data;

import com.j2y.network.base.FpNetIncomingMessage;
import com.j2y.network.base.FpNetOutgoingMessage;

/**
 * Created by lsh on 2016-08-19.
 */
public class FpNetDataReq_setting_systemEvent extends FpNetData_base
{
    private final int TRUE = 1;
    private final int FALSE = 0;

    private int _clam;
    private int _pair;

    public void Set_clam(boolean checked)
    {
        _clam = checked == true ? TRUE : FALSE;
    }
    public void Set_pair(boolean checked)
    {
        _pair = checked == true ? TRUE : FALSE;
    }
    public boolean Get_clam()
    {
        boolean ret = false;
        if( _clam == TRUE) ret = true;
        return ret;
    }
    public boolean Get_pair()
    {
        boolean ret = false;
        if( _pair == TRUE) ret = true;
        return ret;
    }
    //----------------------------------------------------------------
    // 메시지 파싱
    @Override
    public void Parse(FpNetIncomingMessage inMsg)
    {
        super.Parse(inMsg);

        _clam = inMsg.ReadInt();
        _pair = inMsg.ReadInt();
    }

    //----------------------------------------------------------------
    // 메시지 패킹
    @Override
    public void Packing(FpNetOutgoingMessage outMsg)
    {
        super.Packing(outMsg);

        outMsg.Write(_clam);
        outMsg.Write(_pair);
    }
}