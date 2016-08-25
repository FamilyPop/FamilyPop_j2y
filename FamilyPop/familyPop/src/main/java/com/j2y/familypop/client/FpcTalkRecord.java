package com.j2y.familypop.client;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class FpcTalkRecord implements Serializable
{

    private static final long serialVersionUID = 101L;

    public String _name;
    public String _filename;
    public int _bubble_color_type;
    public long _startTime;
    public long _endTime;
    public boolean _list_added;

    public float _x;
    public float _y;

    public ArrayList<Bubble> _bubbles = new ArrayList<Bubble>();
    public ArrayList<SmileEvent> _smiles = new ArrayList<SmileEvent>();


    public ArrayList<Bubble> CopyBubbles()
    {
        ArrayList<Bubble> ret = new ArrayList<>();

        for( Bubble b : _bubbles)
        {
            Bubble bubble = new Bubble();
            bubble._startTime = b._startTime;
            bubble._endTime = b._endTime;
            bubble._radius = b._radius;
            bubble._x = b._x;
            bubble._y = b._y;
            bubble._color = b._color;
            bubble._flowerColor = b._flowerColor;

            ret.add(bubble);
        }

        return ret;
    }



    public class  Bubble implements Serializable
    {

        private static final long serialVersionUID = 101L;

        public long _startTime;
        public long _endTime;
        public float _radius;
        public float _x;
        public float _y;
        public int _color;
        public int _flowerColor = -2;
    }

    public class  SmileEvent implements Serializable
    {

        private static final long serialVersionUID = 100L;

        public long _startTime;
        int _color;
        int _drawableImage;
    }


    //------------------------------------------------------------------------------------------------------------------------------------------------------
    public void AddBubble(long startTime, long endTime, float x, float y, float rad, int color, int flowerColor)
    {
        Bubble bubble = new Bubble();
        bubble._startTime = startTime;
        bubble._endTime = endTime;
        bubble._x = x;
        bubble._y = y;
        bubble._radius = rad;
        bubble._color = color;
        bubble._flowerColor = flowerColor;
        _bubbles.add(bubble);
    }
    public void AddSmileEvent(long startTiem, int color, int drawableImage)
    {
        SmileEvent smile = new SmileEvent();
        smile._startTime = startTiem;
        smile._color = color;
        smile._drawableImage = drawableImage;
        _smiles.add(smile);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    public int GetBubbleCount()
    {
        return _bubbles.size();
    }


}


