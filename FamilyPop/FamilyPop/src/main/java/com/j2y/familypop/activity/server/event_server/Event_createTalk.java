package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.network.base.data.FpNetData_userInteraction;

/**
 * Created by lsh on 2016-06-25.
 */
// 사용안함. Contents_talk 에서 직접 생성중.
// engine update 주기에서 생성해서 안전하다고 생각 됨.
public class Event_createTalk extends BaseEvent
{

    String _imageName;
    String _flowerName;
    int _preSpeaker;
    Actor_attractor _attractor;

    // Parse 하고 들어와야함.
    public Event_createTalk(String imageName, String flowerName, int preSpeaker, Actor_attractor attractor)
    {
        super();

        _imageName = imageName;
        _flowerName = flowerName;
        _preSpeaker = preSpeaker;
        _attractor = attractor;

        _eventType = Activity_serverMain_andEngine.event_createTalk;
    }
}