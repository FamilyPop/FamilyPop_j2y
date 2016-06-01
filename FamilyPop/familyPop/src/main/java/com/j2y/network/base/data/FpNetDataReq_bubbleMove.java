package com.j2y.network.base.data;

import com.j2y.network.base.FpNetIncomingMessage;
import com.j2y.network.base.FpNetOutgoingMessage;
import com.j2y.network.server.FpNetServer_client;

/**
 * Created by gmpguru on 2016-01-27.
 */
public class FpNetDataReq_bubbleMove extends FpNetData_base
{
    public float _dirX;
    public float _dirY;
    public int _clientid;
    //----------------------------------------------------------------
    // �޽��� �Ľ�
    @Override
    public void Parse(FpNetIncomingMessage inMsg)
    {
        super.Parse(inMsg);
        _dirX = inMsg.ReadFloat();
        _dirY = inMsg.ReadFloat();
        _clientid = inMsg.ReadInt();

        //_clientid = ((FpNetServer_client)inMsg._obj )._clientID;
    }

    //----------------------------------------------------------------
    // �޽��� ��ŷ
    @Override
    public void Packing(FpNetOutgoingMessage outMsg)
    {
        super.Packing(outMsg);
        outMsg.Write(_dirX);
        outMsg.Write(_dirY);
        outMsg.Write(_clientid);

    }
}
