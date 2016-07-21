package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;

/**
 * Created by lsh on 2016-07-19.
 */
public class Event_mulCollider extends  BaseEvent
{
    public float _mulRadius = 1;
    public Manager_actor.eType_actor _type_actor = Manager_actor.eType_actor.ACTOR_NON;
    public Event_mulCollider()
    {
        _eventType = Activity_serverMain_andEngine.event_mulCollider;
    }

    public void MulCollider()
    {
        if( _type_actor == Manager_actor.eType_actor.ACTOR_NON) return;
        Manager_actor.Instance.Mul_collider(_type_actor, _mulRadius);
    }
}

/*
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

 */