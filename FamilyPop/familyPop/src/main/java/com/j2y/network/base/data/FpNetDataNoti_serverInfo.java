package com.j2y.network.base.data;

import com.j2y.network.base.FpNetIncomingMessage;
import com.j2y.network.base.FpNetOutgoingMessage;

/**
 * Created by gmpguru on 2016-01-05.
 */
public class FpNetDataNoti_serverInfo extends FpNetData_base
{
    //  ������ �ó����� ����.
    public int _curScenario;
    //----------------------------------------------------------------
    // �޽��� �Ľ�
    @Override
    public void Parse(FpNetIncomingMessage inMsg)
    {
        super.Parse(inMsg);

        _curScenario = inMsg.ReadInt();
    }
    //----------------------------------------------------------------
    // �޽��� ��ŷ
    @Override
    public void Packing(FpNetOutgoingMessage outMsg)
    {
        super.Packing(outMsg);

        outMsg.Write(_curScenario);

    }
}
