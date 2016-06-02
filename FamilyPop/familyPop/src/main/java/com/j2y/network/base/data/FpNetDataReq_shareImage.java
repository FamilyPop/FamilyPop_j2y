package com.j2y.network.base.data;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
// FpNetDataReq_shareImage
//
//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.j2y.network.base.FpNetIncomingMessage;
import com.j2y.network.base.FpNetOutgoingMessage;
import com.j2y.network.base.FpNetUtil;

import java.util.ArrayList;

public class FpNetDataReq_shareImage extends FpNetData_base
{
    private int _count_bitmap;
    private ArrayList<byte[]> _bitArrays;

    public FpNetDataReq_shareImage()
    {
        _count_bitmap = 0;
        _bitArrays = new ArrayList<>();
    }
    //----------------------------------------------------------------
    //
    public int Get_count(){return _count_bitmap;}
    public byte[] Get_bitArray(int index ){return _bitArrays.get(index);}
    public void Add_bitmap( Bitmap bitmap)
    {
        byte[] item = FpNetUtil.BitmapToByteArray(bitmap);

        _bitArrays.add(item);
        ++_count_bitmap;
    }
    //----------------------------------------------------------------
    // 메시지 파싱
    @Override
    public void Parse(FpNetIncomingMessage inMsg)
    {
        super.Parse(inMsg);

        _count_bitmap = inMsg.ReadInt();

        for(int i=0; i<_count_bitmap; ++i)
        {
            int length = inMsg.ReadInt();
            _bitArrays.add(inMsg.ReadByteArray(length));
        }
    }

    //----------------------------------------------------------------
    // 메시지 패킹
    @Override
    public void Packing(FpNetOutgoingMessage outMsg)
    {
        super.Packing(outMsg);

        outMsg.Write(_count_bitmap);
        for(byte[] data : _bitArrays)
        {
            outMsg.Write(data.length);
            outMsg.Write(data);
        }
    }
}