package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.network.base.data.FpNetData_userInteraction;

/**
 * Created by lsh on 2016-06-24.
 */
public class Event_createGood extends BaseEvent
{
    private FpNetData_userInteraction _data;

    // Parse 하고 들어와야함.
    public Event_createGood(FpNetData_userInteraction data)
    {
        super();
        _data = data;
        _eventType = Activity_serverMain_andEngine.event_createGood;
    }

    public FpNetData_userInteraction Get_data(){return _data;}
}
