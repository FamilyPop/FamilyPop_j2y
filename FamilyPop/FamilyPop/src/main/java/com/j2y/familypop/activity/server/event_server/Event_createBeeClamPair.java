package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBee;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBeeClamPair;

/**
 * Created by lsh on 2016-08-04.
 */
public class Event_createBeeClamPair extends BaseEvent
{
    float _x;
    float _y;
    String _tileImageName;

    // Parse 하고 들어와야함.
    public Event_createBeeClamPair(float x, float y, String tileImageName)
    {
        super();

        _x = x;
        _y = y;

        _tileImageName = tileImageName;
        _eventType = Activity_serverMain_andEngine.event_createBee;
    }

    // todo: 이벤트 실행을 내부 함수로 변경하자
    public Actor_honeyBeeClamPair Create_honeyBee()
    {
        Actor_honeyBeeClamPair ret = null;
        ret = Activity_serverMain_andEngine.Instance.Create_honeybeeClamPair(_x, _y, _tileImageName);

        return ret;
    }

}
