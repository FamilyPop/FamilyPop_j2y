package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.actors.BaseActor;

/**
 * Created by lsh on 2016-09-07.
 */
public class Event_deleteTalk extends BaseEvent
{

    public Event_deleteTalk()
    {
        super();
        _eventType = Activity_serverMain_andEngine.event_deleteTalk;
    }

    public void Delete()
    {
        //Manager_actor.Instance.Destroy_honeyBee(this);
        //Manager_actor.Instance.Destroy_honeyBee(_actor);
        Activity_serverMain_andEngine.Instance.release_talk();
    }

}
