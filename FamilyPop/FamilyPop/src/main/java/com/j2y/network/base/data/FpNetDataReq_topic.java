package com.j2y.network.base.data;

import android.graphics.Bitmap;

import com.j2y.network.base.FpNetIncomingMessage;
import com.j2y.network.base.FpNetOutgoingMessage;
import com.j2y.network.base.FpNetUtil;

import java.util.ArrayList;

/**
 * Created by lsh on 2016-09-01.
 */
public class FpNetDataReq_topic extends FpNetData_base
{
    public int _clientId = -1;
    private byte[] _bitArray;
    private String _text;

    //private int _count_bitmap;
//    private ArrayList<byte[]> _bitArrays;


    public FpNetDataReq_topic()
    {
        //_count_bitmap = 0;
        //_bitArrays = new ArrayList<>();
    }
    //----------------------------------------------------------------
    //
    //public int Get_count(){return _count_bitmap;}
    //public byte[] Get_bitArray(int index ){return _bitArrays.get(index);}
    public byte[] Get_bitArray( ){return _bitArray;}
    public void Add_bitmap( Bitmap bitmap)
    {
        byte[] item = FpNetUtil.BitmapToByteArray(bitmap);
        _bitArray = item;
        //++_count_bitmap;
    }
    public void Add_Text(String text)
    {
        _text = text;
    }
    public String Get_Text(){return _text;}
    //----------------------------------------------------------------
    // 메시지 파싱
    @Override
    public void Parse(FpNetIncomingMessage inMsg)
    {
        super.Parse(inMsg);

        _clientId = inMsg.ReadInt();
        int length = inMsg.ReadInt();
        _bitArray = inMsg.ReadByteArray(length);
        _text = inMsg.ReadString();

//        _count_bitmap = inMsg.ReadInt();

//        for(int i=0; i<_count_bitmap; ++i)
//        {
//            int length = inMsg.ReadInt();
//            _bitArrays.add(inMsg.ReadByteArray(length));
//        }
    }

    //----------------------------------------------------------------
    // 메시지 패킹
    @Override
    public void Packing(FpNetOutgoingMessage outMsg)
    {
        super.Packing(outMsg);

        outMsg.Write(_clientId);
        outMsg.Write(_bitArray.length);
        outMsg.Write(_bitArray);
        outMsg.Write(_text);
//      outMsg.Write(_count_bitmap);
//        for(byte[] data : _bitArrays)
//        {
//            outMsg.Write(data.length);
//            outMsg.Write(data);
//        }
    }
}
