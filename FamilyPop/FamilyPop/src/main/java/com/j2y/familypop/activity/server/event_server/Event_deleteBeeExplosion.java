package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.actors.Actor_honeyBeeExplosion;
import com.j2y.familypop.activity.manager.actors.BaseActor;

/**
 * Created by lsh on 2016-06-29.
 */
public class Event_deleteBeeExplosion extends BaseEvent
{
    BaseActor _actor;
    public Event_deleteBeeExplosion(BaseActor actor)
    {
        super();

        _actor = actor;
        _eventType = Activity_serverMain_andEngine.event_deleteBeeExplosion;
    }

    public void Delete()
    {
        Manager_actor.Instance.Destroy_honeyBeeExplosion((Actor_honeyBeeExplosion)_actor);
    }
}
