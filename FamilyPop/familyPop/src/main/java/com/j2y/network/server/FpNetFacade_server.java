package com.j2y.network.server;

import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.j2y.familypop.activity.Activity_clientMain;
//import com.j2y.familypop.activity.Activity_serverMain;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.Vector2;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_contents;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.familypop.server.FpsRoot;
//import com.j2y.familypop.server.FpsScenarioDirector;
import com.j2y.familypop.server.FpsTalkUser;
//import com.j2y.familypop.server.render.FpsBubble;
//import com.j2y.familypop.server.render.FpsGameBomb;
import com.j2y.network.base.FpNetConstants;
import com.j2y.network.base.FpNetFacade_base;
import com.j2y.network.base.data.*;
import com.j2y.network.client.FpNetFacade_client;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsConnectorManager;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.debug.Debug;

//import org.jbox2d.common.Vec2;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
// FpNetFacade_server
//
//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class FpNetFacade_server extends FpNetFacade_base
{
	public static FpNetFacade_server Instance;
	
	private FpTCPAccepter _accepter;
	private ServerSocket _serverSocket;
    private FpNetServer_packetHandler _packet_handler;
    //


	public ArrayList<FpNetServer_client> _clients = new ArrayList<FpNetServer_client>();

    //private HashMap<int, FpNetServer_client> _clientsTest;

	//------------------------------------------------------------------------------------------------------------------------------------------------------
	public FpNetFacade_server()
	{
        Instance = this;
        _packet_handler = new FpNetServer_packetHandler(this);
	}
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 서버 네트워크
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    //------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
	public boolean IsConnected() 
	{
		if(_serverSocket == null)
			return false;
		
		return _serverSocket.isBound();
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	public void StartServer(int port) 
	{
        if( _serverSocket != null) return;
		try
        {
			_serverSocket = new ServerSocket(port);
			_serverSocket.setReuseAddress(true);
			
			_accepter = new FpTCPAccepter(_serverSocket, _messageHandler);
			_accepter.start();
			
		}
        catch (IOException e)
        {
			e.printStackTrace();

			try {
				_serverSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		}
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------
    public void CloseServer()
    {
        Log.i("[J2Y]", "FpNetFacade_server:CloseServer");
		try{
            send_quitRoom();

			_serverSocket.close();
			_accepter.destroy();

            for(FpNetServer_client client : _clients)
                client.Disconnect();

            _clients.clear();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    private void send_quitRoom()
    {
        Log.i("[J2Y]", "FpNetFacade_server:send_quitRoom");
        BroadcastPacket(FpNetConstants.SCNoti_quitRoom);
        SystemClock.sleep(50); // 기다려야 하나??

        //if(Activity_serverMain.Instance != null)
        //     Activity_serverMain.Instance.CloseRoom();
        FpNetServer_client._index = 0; // 임시
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 게임 상태
    public void Send_UserBang(FpsTalkUser user)
    {
        Log.i("[J2Y]", "FpNetFacade_server:Send_UserBang");
        user._net_client.SendPacket(FpNetConstants.SCReq_userBang, new FpNetData_base());
    }
    public void Send_BombRunning()
    {
        Log.i("[J2Y]", "FpNetFacade_server:Send_BombRunning");
        BroadcastPacket(FpNetConstants.SCNoti_bombRunning);
    }
    public void Send_endBomb()
    {
        Log.i("[J2Y]", "FpNetFacade_server:Send_endBomb");
        BroadcastPacket(FpNetConstants.SCNoti_endBomb);
    }
    //틱텍토
    public void Send_UserWin_TicTacToe(FpsTalkUser user)
    {
        Log.i("[J2Y]", "FpNetFacade_server:Send_UserWin_TicTacToe");
        user._net_client.SendPacket(FpNetConstants.SCReq_winUser_Tic_Tac_Toe, new FpNetData_base());

        for( int i=0; i<_clients.size()-1; i++)
        {
            if( user._net_client._clientID != _clients.get(i)._clientID)
            {
                _clients.get(i).SendPacket(FpNetConstants.SCReq_loseUser_Tic_Tac_Toe, new FpNetData_base());
            }
        }
    }
    public void Send_Start_TicTacToe()
    {
        for(int i=0; i<_clients.size(); i++ )//FpNetServer_client client : _clients)
        {
            if( i == _clients.size()-1)
            {
                FpNetDataReq_TicTacToe_Start outMsg = new FpNetDataReq_TicTacToe_Start();
                outMsg._style = 0;
                _clients.get(i).SendPacket(FpNetConstants.SCNoti_Start_Tic_Tac_Toe, outMsg);
            }
            else
            {
                FpNetDataReq_TicTacToe_Start outMsg = new FpNetDataReq_TicTacToe_Start();
                outMsg._style = (i % 2) + 1;
                _clients.get(i).SendPacket(FpNetConstants.SCNoti_Start_Tic_Tac_Toe, outMsg);
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    public void Send_talk_record_info()
    {

        Log.i("[J2Y]", "FpNetFacade_server:send_talk_record_info");

        //if(FpsScenarioDirector.Instance.GetActiveScenarioType() == FpNetConstants.SCENARIO_RECORD)
        {
            for(FpNetServer_client client : _clients)
            {
                FpNetDataRes_recordInfoList outMsg = new FpNetDataRes_recordInfoList();

                // todo: 메인쓰레드, 렌더링쓰레드 충돌남
                //FpsTalkUser talk_user = Activity_serverMain.Instance.GetTalkUser(client);
               // if(null == talk_user)
                //    continue;

//                outMsg._attractor._x = talk_user._attractor.GetPosition().x;
//                outMsg._attractor._y = talk_user._attractor.GetPosition().y;
//                outMsg._attractor._color = client._clientID;
//
//                for(FpsBubble bubble : talk_user._bubble)
//                {
//                    Vec2 pos = bubble.GetPosition();
//                    //Log.i("[J2Y]", String.format("[NetServer]:%f,%f", pos.x, pos.y));
//                    outMsg.AddRecordData(bubble._start_time, bubble._end_time, pos.x, pos.y, bubble._rad, bubble._colorId);
//                }

               // outMsg._smile_events.addAll(talk_user._smile_events);

                client.SendPacket(FpNetConstants.SCRes_TalkRecordInfo, outMsg);
            }
        }
        SystemClock.sleep(50); // 기다려야 하나??
    }

    public void Send_clientUpdate()
    {

        FpNetDataNoti_clientUpdate outMsg = new FpNetDataNoti_clientUpdate();

        if( Manager_actor.Instance != null ) // 첫번째는 업그래이드 안해줘도 되니..그냥 넘어가는 쪽으로..
        {
            for(FpsTalkUser user : Manager_users.Instance.Get_talk_users().values())
            {
                Actor_attractor attractor = Manager_actor.Instance.Get_attractor(user._uid_attractor);

                com.badlogic.gdx.math.Vector2 vec2 = new com.badlogic.gdx.math.Vector2( attractor.Get_Body().getPosition().x * PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
                                                                                        attractor.Get_Body().getPosition().y * PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT);

                float width     = Activity_serverMain_andEngine.CAMERA_WIDTH;
                float height    = Activity_serverMain_andEngine.CAMERA_HEIGHT;

                float x = vec2.x - ((width/2));
                float y = vec2.y - ((height/2));

                double dv = Math.sqrt(x * x + y * y );

                x /= dv;
                y /= dv;

                outMsg.AddClientData(x, y, user._bubble_color_type, user._net_client._clientID);

                Log.i("[POS]", "x: " + x + "_y: "+y);
            }

            BroadcastPacket(FpNetConstants.SCNoti_clientUpdate, outMsg);
        }

    }
    public void Send_connect_clientId(FpsTalkUser user)
    {
        //user._net_client.SendPacket(FpNetConstants.SCReq_userBang, new FpNetData_base());
        FpNetDataReq_connectId data = new FpNetDataReq_connectId();
        data._clientId = user._net_client._clientID;
        //서버에서 컬러 타입을 정해서 넘겨준다.
        data._colorType = Manager_users.Instance.GetColor_UniqueNumber();
        user._net_client.SendPacket(FpNetConstants.SCReq_connect_clientId, data);
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 모든 클라 패킷 보내기
    public void BroadcastPacket(int msgID, FpNetData_base outPacket)
    {
//        FpNetOutgoingMessage outMsg = new FpNetOutgoingMessage();
//        //outMsg.Write(msgID);
//        outPacket.Packing(outMsg);
//
//        FpPacketData packetData = new FpPacketData();
//        packetData._header = new FpPacketHeader();
//        packetData._header._size = outMsg.GetPacketSize();
//        packetData._header._type = msgID;
//        packetData._data = outMsg.GetPacketToByte();
//
//        for(FpNetServer_client clinet : _clients)
//        {
//            clinet.SendPacket(packetData);
//        }

        for(FpNetServer_client client : _clients)
        {
            client.SendPacket(msgID, outPacket);
        }
    }


    public void BroadcastPacket(int msgID)
    {
        BroadcastPacket(msgID, new FpNetData_base());
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 클라이언트 관리
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void AddClient(Socket socket)
    {
        FpNetServer_client client = new FpNetServer_client(socket, _messageHandler);
        _clients.add(client);

        //
        if( Manager_users.Instance != null ) Manager_users.Instance.User_add(client);
    }
    public void RemoveClient(FpNetServer_client client)
    {
        // todo: 클라이언트 한명만 나가기
        if(Activity_serverMain_andEngine.Instance != null)
            FpNetFacade_server.Instance.Send_talk_record_info();

        // 현재 버전은 서버 종료하기
        FpsRoot.Instance.CloseServer();

        if(Activity_serverMain_andEngine.Instance != null) {
            Activity_serverMain_andEngine.Instance.CloseServer();
        }
    }

    public FpNetServer_client GetClientByIndex(int index)
    {
        if((index < 0) || (index >= _clients.size()))
            return null;
        return _clients.get(index);
    }

    // 서버의 정보를 클라이언트에게 전송한다.
    public void Send_ServerState( FpNetServer_client client )
    {
//        // test
//        FpNetDataNoti_serverInfo outMsg = new FpNetDataNoti_serverInfo();
//        outMsg._curScenario = FpsScenarioDirector.Instance.GetActiveScenarioType();
//        client.SendPacket(FpNetConstants.SCNoti_getServerState, outMsg);

    }
}
