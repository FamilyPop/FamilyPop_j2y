package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_contents;
import com.j2y.familypop.activity.manager.Manager_users;

/**
 * Created by lsh on 2016-06-25.
 */
public class Event_serverClose extends BaseEvent
{
    public Event_serverClose()
    {
        _eventType = Activity_serverMain_andEngine.event_serverClose;
    }

    public void CloseToServer()
    {
        //FpsRoot.Instance.CloseServer();
        //System.exit(0);
        //MainActivity.Instance.finishFromChild(this);
       MainActivity.Instance._serverActivityStart = false;

        // 액터를 전부 제거 한다.
        Activity_serverMain_andEngine.Instance.release_talk();
        Activity_serverMain_andEngine.Instance.release_smile();
        Activity_serverMain_andEngine.Instance.release_good();
        Activity_serverMain_andEngine.Instance.release_bee();
        Activity_serverMain_andEngine.Instance.release_attractor();
        // 유저를 전부 제거 disconnect 한다.
        Manager_users.Instance.User_allRelease();
        Manager_actor.Instance = null;

        Manager_contents.Instance.Release_All();
        Manager_contents.Instance = null;
        // todo : 마저 제거 해버리자.
        /*
            private Manager_resource _manager_resource;
            private Manager_contents _manager_contents;
         */


        Activity_serverMain_andEngine.Instance.Get_scene().detachChildren();
        Activity_serverMain_andEngine.Instance.Get_scene().clearEntityModifiers();
        Activity_serverMain_andEngine.Instance.Get_scene().clearTouchAreas();
        Activity_serverMain_andEngine.Instance.Get_scene().clearUpdateHandlers();
    }
}
