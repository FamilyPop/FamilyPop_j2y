package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBee;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBeeClam;

/**
 * Created by lsh on 2016-08-04.
 */
public class Event_createBeeClam extends BaseEvent
{
    float _x;
    float _y;
    String _tileImageName;

    // Parse 하고 들어와야함.
    public Event_createBeeClam(float x, float y, String tileImageName)
    {
        super();

        _x = x;
        _y = y;

        _tileImageName = tileImageName;
        _eventType = Activity_serverMain_andEngine.event_createBee;
    }

    public Actor_honeyBeeClam Create_honeyBee()
    {
        Actor_honeyBeeClam ret = null;
        ret = Activity_serverMain_andEngine.Instance.Create_honeybeeClam(_x, _y, _tileImageName);

        return ret;
    }

}
