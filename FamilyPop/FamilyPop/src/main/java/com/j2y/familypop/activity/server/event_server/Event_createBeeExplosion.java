package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;

/**
 * Created by lsh on 2016-06-28.
 */
public class Event_createBeeExplosion extends BaseEvent
{
    float _x;
    float _y;
    String _tileImageName;

    public Event_createBeeExplosion(float x, float y, String tileImageName)
    {
        _x = x;
        _y = y;
        _tileImageName = tileImageName;
        _eventType = Activity_serverMain_andEngine.event_createBeeExplosion;
    }
    public void Create()
    {
        Activity_serverMain_andEngine main = Activity_serverMain_andEngine.Instance;
        main.Create_honeybeeExplosion(_x, _y, _tileImageName);
    }
}
