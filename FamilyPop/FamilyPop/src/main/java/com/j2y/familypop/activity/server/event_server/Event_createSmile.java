package com.j2y.familypop.activity.server.event_server;

import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_resource;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.familypop.activity.manager.actors.Actor_smile;
import com.j2y.familypop.server.FpsTalkUser;
import com.j2y.network.server.FpNetServer_client;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lsh on 2016-06-25.
 */
public class Event_createSmile extends BaseEvent
{
    HashMap<FpNetServer_client, FpsTalkUser> _user;

    public Event_createSmile(HashMap<FpNetServer_client, FpsTalkUser> user)
    {
        _user = user;

        _eventType = Activity_serverMain_andEngine.event_createSmile;
    }

    public void CreateSmile()
    {
        for (FpsTalkUser user : _user.values())
        {
            //String fileName = Manager_resource.Instance.Get_userImage(Manager_resource.eImageIndex_color.IntToImageColor(user._net_client._clientID));
            String fileName = Manager_resource.Instance.Get_petalNames(Manager_resource.eImageIndex_color.IntToImageColor(user._net_client._clientID), Manager_resource.eType_petal.PETAL_SMILE);
            Actor_attractor attractor = Manager_actor.Instance.Get_attractor(user._uid_attractor);

            Actor_smile actor = Activity_serverMain_andEngine.Instance.Create_smile( Activity_serverMain_andEngine.CAMERA_WIDTH / 2,
                                                                                     Activity_serverMain_andEngine.CAMERA_HEIGHT / 2, fileName, attractor);
            actor.Set_maxFlowerScale(1.3f);
        }
    }
}
