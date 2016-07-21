package com.j2y.network.base.data;

import com.j2y.network.base.FpNetIncomingMessage;
import com.j2y.network.base.FpNetOutgoingMessage;

/**
 * Created by gmpguru on 2015-07-01.
 */
public class FpNetDataReq_regulation_info extends FpNetData_base
{
    // back
//    public int _seekBar_0;
//    public int _seekBar_1;
//    public int _seekBar_2;
//    public int _seekBar_3;
//    public int _seekBar_voice_hold;
//    public int _voiceProcessingMode; // 무조건 j2y 모드
//    public int _seekBar_regulation_smileEffect;
//    public int _seekBar_bubble_plusSIze;


    // regulation
    public int _buffer_count;
    public int _smile_effect;
    public int _voice_hold;

    // tlak mode setting
    public float _flowerPlusSize;
    public float _flowerMaxSize;
    public float _flowerMinSize;
    public float _flowerGoodSize;
    public float _flowerSmileSize;

    public float _colliderTalkSize  = 1.0f;
    public float _colliderSmileSize = 1.0f;
    public float _colliderGoodSize  = 1.0f;

    //----------------------------------------------------------------
    // 메시지 파싱
    @Override
    public void Parse(FpNetIncomingMessage inMsg)
    {
        super.Parse(inMsg);

        _buffer_count   = inMsg.ReadInt();
        _smile_effect   = inMsg.ReadInt();
        _voice_hold     = inMsg.ReadInt();

        _flowerPlusSize     = inMsg.ReadFloat();
        _flowerMaxSize      = inMsg.ReadFloat();
        _flowerMinSize      = inMsg.ReadFloat();
        _flowerGoodSize     = inMsg.ReadFloat();
        _flowerSmileSize    = inMsg.ReadFloat();

        _colliderTalkSize   = inMsg.ReadFloat();
        _colliderSmileSize  = inMsg.ReadFloat();
        _colliderGoodSize   = inMsg.ReadFloat();

        // back
//        _seekBar_0 = inMsg.ReadInt();
//        _seekBar_1 = inMsg.ReadInt();
//        _seekBar_2 = inMsg.ReadInt();
//        _seekBar_3 = inMsg.ReadInt();
//        _seekBar_voice_hold = inMsg.ReadInt();
//        _voiceProcessingMode = inMsg.ReadInt();
//        _seekBar_regulation_smileEffect = inMsg.ReadInt();
//        _seekBar_bubble_plusSIze = inMsg.ReadInt();

    }

    //----------------------------------------------------------------
    // 메시지 패킹
    @Override
    public void Packing(FpNetOutgoingMessage outMsg)
    {
        super.Packing(outMsg);

        outMsg.Write(_buffer_count);
        outMsg.Write(_smile_effect);
        outMsg.Write(_voice_hold);

        outMsg.Write(_flowerPlusSize);
        outMsg.Write(_flowerMaxSize);
        outMsg.Write(_flowerMinSize);
        outMsg.Write(_flowerGoodSize);
        outMsg.Write(_flowerSmileSize);

        outMsg.Write(_colliderTalkSize);
        outMsg.Write(_colliderSmileSize);
        outMsg.Write(_colliderGoodSize);

// back
//        outMsg.Write(_seekBar_0);
//        outMsg.Write(_seekBar_1);
//        outMsg.Write(_seekBar_2);
//        outMsg.Write(_seekBar_3);
//        outMsg.Write(_seekBar_voice_hold);
//        outMsg.Write(_voiceProcessingMode);
//        outMsg.Write(_seekBar_regulation_smileEffect);
//        outMsg.Write(_seekBar_bubble_plusSIze);
    }
}
