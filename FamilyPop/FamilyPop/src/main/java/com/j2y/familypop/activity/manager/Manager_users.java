package com.j2y.familypop.activity.manager;

import com.j2y.familypop.server.FpsTalkUser;
import com.j2y.network.server.FpNetServer_client;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lsh on 2016-05-14.
 */
public class Manager_users {
    public static Manager_users Instance = null;
    private Lock _lock_user = new ReentrantLock();

    private int _colorTypeCount = 0;

    // net
    private HashMap<FpNetServer_client, FpsTalkUser> _talk_users = new HashMap<FpNetServer_client, FpsTalkUser>();

    public Manager_users() {
        Instance = this;

        _talk_users = new HashMap<FpNetServer_client, FpsTalkUser>();
    }
    //
    public HashMap<FpNetServer_client, FpsTalkUser> Get_talk_users(){return _talk_users;}
    //===============================================================================================
    // user add, releaase
    public boolean User_add(FpNetServer_client net_client) {
        boolean ret = false;
        try {
            _lock_user.lock();
            FpsTalkUser user = new FpsTalkUser(net_client);
            _talk_users.put(net_client, user);
        } finally
        {
            _lock_user.unlock();
        }
        return ret;
    }
    public void User_allRelease()
    {
        for( FpsTalkUser u : Get_talk_users().values())
        {
            u.User_release();
        }
        Get_talk_users().clear();
    }

    //===============================================================================================
    // find
    public FpsTalkUser FindTalkUser_byId(int clientId)
    {

        for (FpsTalkUser user : _talk_users.values()) {
            if (user._net_client._clientID == clientId)
                return user;
        }

        return null;
    }
    public synchronized boolean FindTalkUser_byId(int clientId, AtomicReference<FpsTalkUser> ref)
    {
        boolean ret = false;
        for (FpsTalkUser user : _talk_users.values())
        {
            if (user._net_client._clientID == clientId)
            {
                ref.set(user);
                ret = true;
                //return user;
            }
        }
        return ret;
    }

    public FpsTalkUser FindTalkUser_byName(String userName) {

        for (FpsTalkUser user : _talk_users.values()) {
            if (user._net_client._user_name.equals(userName))
                return user;
        }

        return null;

    }
    public FpsTalkUser GetTalkUser(FpNetServer_client net_client) {

        try {
            _lock_user.lock();
            if(!_talk_users.containsKey(net_client))
                return null;
            return _talk_users.get(net_client);
        }
        finally {
            _lock_user.unlock();
        }
    }

    public int GetColor_UniqueNumber()
    {
        int ret = _colorTypeCount;
        _colorTypeCount++;
        return ret;
    }


}
