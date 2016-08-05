package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBee;
import com.j2y.familypop.activity.manager.actors.BaseActor;

/**
 * Created by lsh on 2016-06-29.
 */
//todo : 모든 액터가 사용할수 있다.. 클레스 이름을 변경 해서 사용하도록.
public class Event_deleteBee extends BaseEvent
{
    BaseActor _actor;
    public Event_deleteBee(BaseActor actor)
    {
        super();

        _actor = actor;
        _eventType = Activity_serverMain_andEngine.event_deleteBee;
    }

    public void Delete()
    {
        //Manager_actor.Instance.Destroy_honeyBee(this);
        Manager_actor.Instance.Destroy_honeyBee(_actor);
    }

}
