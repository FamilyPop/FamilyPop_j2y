package com.j2y.network.server.packet;

import android.util.Log;

import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.server.FpsTalkUser;
import com.j2y.network.base.FpNetIncomingMessage;
import com.j2y.network.base.FpNetMessageCallBack;
import com.j2y.network.server.FpNetFacade_server;
import com.j2y.network.server.FpNetServer_client;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lsh on 2016-05-15.
 */
public class PacketListener_connect
{
    FpNetFacade_server _net_server;
    public PacketListener_connect(FpNetFacade_server net_server)
    {
        _net_server = net_server;
    }
    public FpNetMessageCallBack onConnected = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
            Log.i("[J2Y]", "[Network] 클라 연결");
            _net_server.AddClient(inMsg._socket);

            int clientIndex = _net_server._clients.size()-1;
            _net_server.Send_ServerState(_net_server.GetClientByIndex((clientIndex)));

            int clientId = FpNetServer_client._index-1;

            FpsTalkUser taget = null;
            AtomicReference<FpsTalkUser> ref = new AtomicReference<>();
            Manager_users.Instance.FindTalkUser_byId(clientId, ref);
            taget = ref.get();

            _net_server.Send_connect_clientId(taget);
        }
    };
}
