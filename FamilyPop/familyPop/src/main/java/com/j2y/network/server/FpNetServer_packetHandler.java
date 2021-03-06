package com.j2y.network.server;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.j2y.familypop.MainActivity;
//import com.j2y.familypop.activity.Activity_serverMain;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_contents;
import com.j2y.familypop.activity.manager.Manager_resource;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.familypop.activity.manager.actors.Actor_talk;
import com.j2y.familypop.activity.manager.actors.BaseActor;
import com.j2y.familypop.activity.server.Activity_serverCalibration;
import com.j2y.familypop.activity.server.Activity_serverCalibrationLocation;
import com.j2y.familypop.activity.server.Activity_serverStart;
import com.j2y.familypop.activity.server.event_server.BaseEvent;
import com.j2y.familypop.activity.server.event_server.Event_createGood;
import com.j2y.familypop.activity.server.event_server.Event_deleteTalk;
import com.j2y.familypop.client.FpcRoot;
import com.j2y.familypop.server.FpsRoot;
//import com.j2y.familypop.server.FpsScenarioDirector;
//import com.j2y.familypop.server.FpsScenario_game;
//import com.j2y.familypop.server.FpsScenario_record;
import com.j2y.familypop.server.FpsTalkUser;
import com.j2y.familypop.server.FpsTicTacToe;
//import com.j2y.familypop.server.render.FpsBubble;
import com.j2y.network.base.FpNetConstants;
import com.j2y.network.base.FpNetFacade_base;
import com.j2y.network.base.FpNetIncomingMessage;
import com.j2y.network.base.FpNetMessageCallBack;
import com.j2y.network.base.FpNetUtil;
import com.j2y.network.base.data.FpNetDataNoti_changeScenario;
import com.j2y.network.base.data.FpNetDataNoti_roomInfo;
import com.j2y.network.base.data.FpNetDataReq_TicTacToe_index;
import com.j2y.network.base.data.FpNetDataReq_bubbleMove;
import com.j2y.network.base.data.FpNetDataReq_changeScenario;
import com.j2y.network.base.data.FpNetDataReq_regulation_info;
import com.j2y.network.base.data.FpNetDataReq_setting_systemEvent;
import com.j2y.network.base.data.FpNetDataReq_shareImage;
import com.j2y.network.base.data.FpNetDataReq_topic;
import com.j2y.network.base.data.FpNetDataRes_recordInfoList;
import com.j2y.network.base.data.FpNetData_familyTalk;
import com.j2y.network.base.data.FpNetData_setUserInfo;
import com.j2y.network.base.data.FpNetData_smileEvent;
import com.j2y.network.base.data.FpNetData_userInteraction;
import com.j2y.network.client.FpNetFacade_client;
import com.j2y.network.server.packet.PacketListener_connect;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;

import java.util.Collections;
import java.util.List;

//import org.jbox2d.common.Vec2;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
// FpNetFacade_server
//
//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public class FpNetServer_packetHandler
{
    private FpNetFacade_server _net_server;

    //private PacketListener_connect _packetListener_connect;
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	public FpNetServer_packetHandler(FpNetFacade_server net_server)
	{
        _net_server = net_server;
        RegisterMessageCallBackList();
	}

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // 메시지 핸들러
    //
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 메시지 콜백 클래스 등록
    public void RegisterMessageCallBackList()
    {
        //_packetListener_connect = new PacketListener_connect();


        // 연결
        _net_server.RegisterMessageCallBack(FpNetConstants.ClientAccepted, onConnected);

        // 사용자 정보
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_setUserInfo, onReq_setUserInfo);
        //RegisterMessageCallBack(FpNetConstants.ClientDisconnected, onDisConnected);
        // 시나리오 변경
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_ChangeScenario, onReq_changeScenario);

        // 이벤트.
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_OnStartGame, onReq_startGame);
        _net_server.RegisterMessageCallBack(FpNetConstants.CSC_ShareImage, onCSC_shareimage);
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_TalkRecordInfo, onReq_talk_record_info);
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_exitRoom, onReq_exit_room);
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_smileEvent, onReq_smile_event);

        // tic tac toe
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_OnStart_Tic_Tac_Toe, onReq_start_tic_tac_toe);
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_OnEnd_Tic_Tac_Toe, onReq_end_tic_tac_toe);

        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_selectIndex_Tic_Tac_Toe, onReq_selectIndex_tic_tac_toe);
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_throwback_Tic_Tac_Toe, onReq_throwback_tic_tac_toe);

        // Family Talk
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_familyTalk_voice, onReq_familyTalk_voice);

        // regulation_inf
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_regulation_Info,onReq_regulation_info);
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_clearBubble, onReq_clearBubble);

        // user message
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_userInput_bubbleMove, onReq_userInput_bubbleMove);
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_userInteraction, onReq_userInteraction);

        // system event setting
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_systemEvent, onReq_systemEventSetting);

        // topic
        _net_server.RegisterMessageCallBack(FpNetConstants.CSReq_toppic, onReq_topping);
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 클라 연결
    FpNetMessageCallBack onConnected = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {

            Log.i("[J2Y]", "[Network] 클라 연결");
            _net_server.AddClient(inMsg._socket);

            int clientIndex = _net_server._clients.size()-1;
            _net_server.Send_ServerState(_net_server.GetClientByIndex((clientIndex)));

            int clientId = FpNetServer_client._index-1;
            FpsTalkUser taget = Manager_users.Instance.FindTalkUser_byId(clientId);
           _net_server.Send_connect_clientId(taget);
        }
    };
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 방 나가기 ( 종료.)
    FpNetMessageCallBack onReq_exit_room = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg) throws InterruptedException {
            Log.i("[J2Y]", "[패킷수신] 방 나가기");

            FpsRoot.Instance._exitServer = true;
            FpsRoot.Instance.CloseServer();
            if( Activity_serverMain_andEngine.Instance != null) Activity_serverMain_andEngine.Instance.CloseServer();

            FpNetServer_client client = (FpNetServer_client)inMsg._obj;
            _net_server.RemoveClient(client);

            // 유저를 전부 제거 disconnect 한다.
            Manager_users.Instance.User_allRelease();
            Manager_actor.Instance = null;

            Activity_serverStart.Instance.finish();
            MainActivity.Instance.startActivity(new Intent(MainActivity.Instance, Activity_serverStart.class));
        }
    };
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 클라 연결끊김
//    FpNetMessageCallBack onDisConnected = new FpNetMessageCallBack()
//    {
//        @Override
//        public void CallBack(FpNetIncomingMessage inMsg)
//        {
//            FpNetServer_client client = (FpNetServer_client)inMsg._obj;
//            client.destroy();
//            _clients.remove(client);
//        }
//    };

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 클라 정보 세팅
    FpNetData_setUserInfo _setUserInfo_data = null;
    FpNetMessageCallBack onReq_setUserInfo = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
            Log.i("[J2Y]", "[패킷수신] 클라 정보 세팅");

            //FpNetData_setUserInfo setUserInfo_data = new FpNetData_setUserInfo();
            if( _setUserInfo_data != null) _setUserInfo_data = null;

            _setUserInfo_data = new FpNetData_setUserInfo();
            _setUserInfo_data.Parse(inMsg);

            if(FpsRoot.Instance._room_user_names == "")
                FpsRoot.Instance._room_user_names = _setUserInfo_data._userName;
            else
                FpsRoot.Instance._room_user_names += (", " + _setUserInfo_data._userName);

            // 접속한 사용자들의 버블 정보를 만든다, 접속한 클라이언트들의 id 정보를 만든다.
            String bubblesInfo = "";
            String clientsInfo = "";
            //FpsRoot.Instance.
            //for( FpNetServer_client c : FpNetFacade_server.Instance._clients )
            for( FpsTalkUser user : Manager_users.Instance.Get_talk_users().values() )// FpNetFacade_server.Instance._clients )
            {
                FpNetServer_client c = user._net_client;
                bubblesInfo += c._bubble_color_type + ",";
                clientsInfo += c._clientID + ",";
            }

            // 방정보 전파
            FpNetDataNoti_roomInfo outMsg = new FpNetDataNoti_roomInfo();
            outMsg._userNames = FpsRoot.Instance._room_user_names;
            outMsg._clientsInfo = clientsInfo;
            outMsg._bubblesInfo = bubblesInfo;
            Log.i("[J2Y]", "[방이름] " + outMsg._userNames);

            _net_server.BroadcastPacket(FpNetConstants.SCNoti_roomUserInfo, outMsg);

            if( !MainActivity.Instance._serverActivityStart )
            {
                MainActivity.Instance.startActivity(new Intent(MainActivity.Instance, Activity_serverMain_andEngine.class));
                MainActivity.Instance._serverActivityStart = true;
            }
            if( Activity_serverMain_andEngine.Instance != null)
            {
                Activity_serverMain_andEngine.Instance.release_attractor();

                //
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        Activity_serverMain_andEngine.Instance.Init_attractor();

                        //클라 정보 업데이트.
                        FpNetFacade_server.Instance.Send_clientUpdate();
                    }
                }, 500);
            }
        }
    };

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 스마일 이벤트
    FpNetMessageCallBack onReq_smile_event = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
            Log.i("[J2Y]", "[패킷수신] 스마일 이벤트");

//            FpNetServer_client client = (FpNetServer_client)inMsg._obj;
//
//            if(FpsScenarioDirector.Instance.GetActiveScenarioType() == FpNetConstants.SCENARIO_RECORD)
//            {
////                FpNetData_smileEvent data = new FpNetData_smileEvent();
////                data.Parse(inMsg);
//                // todo: 메인쓰레드, 렌더링쓰레드 충돌남
//                // 현재 레코드 시간
//                int event_time = (int) FpsRoot.Instance._socioPhone.GetRecordTime();
//
//
//                FpsTalkUser talk_user = Activity_serverMain.Instance.GetTalkUser(client);
//                talk_user._smile_events.add(event_time);
//
//                Activity_serverMain.Instance.OnEvent_smile(event_time);
//
//
//                // 이벤트 전파
//                FpNetData_smileEvent outMsg = new FpNetData_smileEvent();
//                outMsg._time = event_time;
//                _net_server.BroadcastPacket(FpNetConstants.SCNoti_smileEvent, outMsg);
//            }
        }
    };

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 게임 시작
    FpNetMessageCallBack onReq_startGame = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {


            if( Manager_actor.Instance != null)
            {
                if( Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_BEE).size() == 0 )
                {
                    Activity_serverMain_andEngine.Instance.OnEvent_honeybee();
                }
            }

//            Log.i("[J2Y]", "[패킷수신] 게임 시작");
//
//            FpNetServer_client client = (FpNetServer_client)inMsg._obj;
//
//            //if(client._clientID == 0)
//            {
//                if(FpsScenarioDirector.Instance.GetActiveScenarioType() == FpNetConstants.SCENARIO_GAME)
//                {
//                    //((FpsScenario_game) FpsScenarioDirector.Instance.GetActiveScenario()).StartGame();
//
//                    _net_server.BroadcastPacket(FpNetConstants.SCNoti_startGame);
//                }
//            }
        }
    };

    // tic tac toe
    FpNetMessageCallBack onReq_start_tic_tac_toe = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
            Log.i("[J2Y]", "[패킷수신] tic tac toe 게임 시작");
//            Activity_serverMain.Instance._isNotLocatorDevice = FpsRoot.Instance._localization._server.getLocator() == null ? true : false;
//
//            //로케이터 서버가 있을때.
//            if( !Activity_serverMain.Instance._isNotLocatorDevice )
//            {
//                //Activity_serverMain.Instance._tictactoe._tictactoe_runningMsg_event = true;
//                Activity_serverMain.Instance.Initialization_tictactoe();
//                //_net_server.BroadcastPacket(FpNetConstants.SCNoti_Start_Tic_Tac_Toe);
//
//
//                _net_server.Send_Start_TicTacToe();
//
//                // 대화 내용 저장 안하고 그냥 날려버림.
//                if( FpsRoot.Instance._scenarioDirector._activeScenarioType == FpNetConstants.SCENARIO_RECORD)
//                {
//                    FpsRoot.Instance._socioPhone.stopRecord();
//                    FpsRoot.Instance._socioPhone.recordRelease();
//
//                    //if( FpsRoot.Instance._scenarioDirector._activeScenarioType == FpNetConstants.SCENARIO_GAME) ??
//                    FpNetFacade_server.Instance.Send_endBomb();
//
//                }
//                FpsRoot.Instance._scenarioDirector._activeScenarioType = FpNetConstants.SCENARIO_TIC_TAC_TOE;
//                FpsRoot.Instance._scenarioDirector.ChangeScenario(FpNetConstants.SCENARIO_NONE);
//            }
//            else //로케이터 가 없을때.
//            {
//                //not locator
//                Activity_serverMain.Instance._isNotLocatorDevice_time = System.currentTimeMillis();
//
//            }
        }
    };

    FpNetMessageCallBack onReq_end_tic_tac_toe = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
            Log.i("[J2Y]", "[패킷수신] tic tac toe 게임 종료");

            //Activity_serverMain.Instance._tictactoe._tictactoe_runningMsg_event = true;
            //_net_server.BroadcastPacket(FpNetConstants.SCNoti_Start_Tic_Tac_Toe);

            //Activity_serverMain.Instance._tictactoe._tictactoe_runningMsg_event = false;
            //Activity_serverMain.Instance._tictactoe._prvStyle = FpsTicTacToe.eTictactoeImageIndex.EMPTY;
            _net_server.BroadcastPacket(FpNetConstants.SCNoti_end_Tic_Tac_Toe);

            //FpNetFacade_server.Instance._clients
            /*
             for(FpNetServer_client client : _clients)
        {
            client.SendPacket(msgID, outPacket);
        }
             */
        }
    };

    FpNetMessageCallBack onReq_selectIndex_tic_tac_toe = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
//            FpNetServer_client client = (FpNetServer_client)inMsg._obj;
//            FpNetDataReq_TicTacToe_index data = new FpNetDataReq_TicTacToe_index();
//            data.Parse(inMsg);
//
//            //data._index //선택한 인덱스.
//            Activity_serverMain.Instance.TicTacToe_tileChange(data._index, FpsTicTacToe.eTictactoeImageIndex.getValue( data._style + 1 ), client);
//
//            if( Activity_serverMain.Instance._tictactoe.TicTacToe_checkSuccessful(Activity_serverMain.Instance._tictactoe._prvStyle) )
//            {
//                Activity_serverMain.Instance._tictactoe._tictactoe_runningMsg_event = false;
//                FpNetFacade_server.Instance.BroadcastPacket(FpNetConstants.SCNoti_end_Tic_Tac_Toe);
//
//                FpNetFacade_server.Instance.Send_UserWin_TicTacToe(Activity_serverMain.Instance.GetTalkUser( client ));
//            }

            // 로케이터 보다 먼저 처리한다는 전제.
            //FpNetDataReq_TicTacToe_index msg = new FpNetDataReq_TicTacToe_index();
            //msg.Parse(inMsg);
            //Activity_serverMain.Instance._ttt_style = msg._style;
            //Activity_serverMain.Instance._netEvent_tttSytleSuccess = true;

            // 스타일 메세지가 도착하고 3초이네에 로케이터가 성공하지 않으면 무효처리한다. (안됨.)
            //Handler handler = new Handler();
            //handler.postDelayed(new netEvent_tttStyle_EnabledDelay(), 3000);
        }
    };
    class netEvent_tttStyle_EnabledDelay implements  Runnable
    {
        public void run()
        {
            //Activity_serverMain.Instance._ttt_style = 0;
            //Activity_serverMain.Instance._netEvent_tttSytleSuccess = false;
        }
    }
    FpNetMessageCallBack onReq_throwback_tic_tac_toe = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
            //if( Activity_serverMain.Instance._tictactoe != null)
            {
               // Activity_serverMain.Instance._tictactoe.Throwback();
            }

        }
    };

    // regulation_info
    FpNetMessageCallBack onReq_regulation_info = new FpNetMessageCallBack()
    {

        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
            FpNetServer_client client = (FpNetServer_client)inMsg._obj;

            FpNetDataReq_regulation_info data = new FpNetDataReq_regulation_info();
            data.Parse(inMsg);

            Activity_serverMain_andEngine.Info_regulation regulation = Activity_serverMain_andEngine.Instance.GetInfo_regulation();

            // regulation
            regulation._buffer_count = data._buffer_count;
            regulation._smile_effect = data._smile_effect;
            regulation._voice_hold = data._voice_hold;
            regulation._talkDelayTime = data._talkDelayTime;

            // flower
            regulation._flowerPlusSize = data._flowerPlusSize;
            regulation._flowerMaxSize = data._flowerMaxSize;
            regulation._flowerMinSize = data._flowerMinSize;
            regulation._flowerGoodSize = data._flowerGoodSize;
            regulation._flowerSmileSize = data._flowerSmileSize;

            regulation._colliderGoodSize = data._colliderGoodSize;
            regulation._colliderSmileSize = data._colliderSmileSize;
            regulation._colliderTalkSize = data._colliderTalkSize;

            Activity_serverMain_andEngine.Instance.OnEvent_mulCollider(Manager_actor.eType_actor.ACTOR_TALK, regulation._colliderTalkSize);
            Activity_serverMain_andEngine.Instance.OnEvent_mulCollider(Manager_actor.eType_actor.ACTOR_SMILE, regulation._colliderSmileSize);
            Activity_serverMain_andEngine.Instance.OnEvent_mulCollider(Manager_actor.eType_actor.ACTOR_GOOD, regulation._colliderGoodSize);

            FpsRoot.Instance._socioPhone.setSilenceVolThreshold((double)regulation._regulation_seekBar_0);
            FpsRoot.Instance._socioPhone.setSilenceVolVarThreshold((double)regulation._regulation_seekBar_1);
            FpsRoot.Instance._voice_threadhold = regulation._voice_hold;

//            Activity_serverMain.Instance._regulation_seekBar_0 = data._seekBar_0;
//            Activity_serverMain.Instance._regulation_seekBar_1 = data._seekBar_1;
//            Activity_serverMain.Instance._regulation_seekBar_2 = data._seekBar_2 > 3 ? data._seekBar_2 : 3;
//            Activity_serverMain.Instance._regulation_seekBar_3 = data._seekBar_3;
//            FpsRoot.Instance._voice_threadhold = data._seekBar_voice_hold;
//            FpsRoot._using_sociophone_voice = data._voiceProcessingMode == 0 ? false : true;
//
//            Activity_serverMain.Instance._regulation_seekBar_smileEffect = data._seekBar_regulation_smileEffect;
//            Activity_serverMain.Instance._plusMoverRadius = data._seekBar_bubble_plusSIze;
//
//            FpsRoot.Instance._socioPhone.setSilenceVolThreshold((double)Activity_serverMain.Instance._regulation_seekBar_0);
//            FpsRoot.Instance._socioPhone.setSilenceVolVarThreshold((double)Activity_serverMain.Instance._regulation_seekBar_1);
        }
    };

    FpNetMessageCallBack onReq_clearBubble = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg) throws InterruptedException {

            //Activity_serverMain.Instance.ClearBubble();
            Event_deleteTalk deleteTalk = new Event_deleteTalk();
            Activity_serverMain_andEngine.Instance.Add_event(deleteTalk);
        }
    };

    FpNetMessageCallBack onReq_familyTalk_voice = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {

            FpNetServer_client client = (FpNetServer_client)inMsg._obj;
            FpNetData_familyTalk data = new FpNetData_familyTalk();
            data.Parse(inMsg);

            FpsRoot.Instance.onJ2yTurnDataReceived(client._clientID, data._voice);
        }
    };

    // system event setting
    FpNetMessageCallBack onReq_systemEventSetting = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {

            FpNetDataReq_setting_systemEvent data = new FpNetDataReq_setting_systemEvent();
            data.Parse(inMsg);

            Activity_serverMain_andEngine.Instance.GetInfo_regulation()._systemEvent_clam = data.Get_clam();
            Activity_serverMain_andEngine.Instance.GetInfo_regulation()._systemEvent_pair = data.Get_pair();

//            FpNetServer_client client = (FpNetServer_client)inMsg._obj;
//            FpNetData_familyTalk data = new FpNetData_familyTalk();
//            data.Parse(inMsg);
//
//            FpsRoot.Instance.onJ2yTurnDataReceived(client._clientID, data._voice);
        }
    };



  //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 시나리오 변경
    FpNetMessageCallBack onReq_changeScenario = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {

            Log.i("[J2Y]", "[패킷수신] 시나리오 변경");

            FpNetServer_client client = (FpNetServer_client)inMsg._obj;
            FpNetDataReq_changeScenario data = new FpNetDataReq_changeScenario();
            data.Parse(inMsg);


            Manager_contents.eType_contents scenario = Manager_contents.eType_contents.IntToType_contents(data._changeScenario);
            Manager_contents.Instance.Content_change(scenario);
//            //if(client._clientID == 0)
            //if(FpsScenarioDirector.Instance._activeScenarioType != data._changeScenario)
            if(Manager_contents.Instance.GetCurrentContent().getValue() != data._changeScenario)
            {
                // client update
//                if( data._changeScenario == FpNetConstants.SCENARIO_RECORD )
//                {
//                    FpNetFacade_server.Instance.Send_clientUpdate();
//                }

//                if((data._changeScenario == FpNetConstants.SCENARIO_GAME) && (FpsScenarioDirector.Instance.GetActiveScenarioType() == FpNetConstants.SCENARIO_RECORD))
//                {
//                    ((FpsScenario_record) FpsScenarioDirector.Instance.GetActiveScenario()).Start_familyBomb();
//                }
//                else
//                {
//                    FpsRoot.Instance._scenarioDirector.ChangeScenario(data._changeScenario);
//                }

//                for(FpNetServer_client clinet : _net_server._clients)
//                {
//                    clinet._curScenario = FpsRoot.Instance._scenarioDirector.GetActiveScenario();
//                }


                FpNetDataNoti_changeScenario outMsg = new FpNetDataNoti_changeScenario();
                outMsg._changeScenario = data._changeScenario;



                _net_server.BroadcastPacket(FpNetConstants.SCNoti_ChangeScenario, outMsg);
            }
        }
    };

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 이미지 공유
    static IUpdateHandler releaseUpdate = null;
    static IUpdateHandler releaseUpdate_topic = null;
    FpNetMessageCallBack onCSC_shareimage = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
            Log.i("[J2Y]", "[패킷수신] 이미지 공유");

            FpNetDataReq_shareImage data = new FpNetDataReq_shareImage();
            data.Parse(inMsg);

            // 이미지 전송을 강제 취소 시킨다.
//            FpNetDataReq_shareImage release = new FpNetDataReq_shareImage();
//            for( FpsTalkUser user : Manager_users.Instance.Get_talk_users().values())
//            {
//                _net_server.SendPacket(FpNetConstants.CSC_ShareImage, user._net_client._clientID, release);
//            }
            //

            //_net_server.BroadcastPacket(FpNetConstants.CSC_ShareImage, data);
            //for( int i=0; i<Manager_users.Instance.Get_talk_users().size(); i++)

            _net_server.SendPacket(FpNetConstants.CSC_ShareImage, data._clientId, data);

            if( data.Get_count() != 0)
            {
                for( int i=0; i<data.Get_count(); ++i)
                {
                    //Manager_resource.Instance.Create_sprite()
                    Bitmap shareImage = FpNetUtil.ByteArrayToBitmap(data.Get_bitArray(i));

                    if( data.Get_count() == 1)
                    {
                        Activity_serverMain_andEngine.Instance.OnEvent_shareimage(data._clientId, -1, shareImage);
                    }
                    else
                    {
                        Activity_serverMain_andEngine.Instance.OnEvent_shareimage(data._clientId, i, shareImage);
                    }
                }
                setReposition();
            }
            else
            {
                //Manager_resource.Instance.ReleaseAll_sprites(Activity_serverMain_andEngine.Instance.Get_scene());
                //Manager_resource.Instance.ReleaseAll_flash_Sprites(Activity_serverMain_andEngine.Instance.Get_scene());
                if( releaseUpdate == null)
                {
                    Scene scene = Activity_serverMain_andEngine.Instance.Get_scene();
                    releaseUpdate = Manager_resource.Instance.ReleaseAll_flash_Sprites(scene);
                    scene.registerUpdateHandler(releaseUpdate);
                    Manager_resource.flashSprittRelease = true;
                    //Manager_resource.releaseType = Manager_resource.Instance.TYPE_FLASH_SPRITE_SHAREIMAGE;
                    Manager_resource.deleteFlashSprite_clientId = data._clientId;
                }
                else
                {
                    Manager_resource.flashSprittRelease = true;
                    Scene scene = Activity_serverMain_andEngine.Instance.Get_scene();
                    Manager_resource.deleteFlashSprite_clientId = data._clientId;
                }
            }
//            //FpNetServer_client client = (FpNetServer_client)inMsg._obj;
//            FpNetDataReq_shareImage data = new FpNetDataReq_shareImage();
//            data.Parse(inMsg);
//
//            //debug
//            MainActivity.Debug_begin_timecount(MainActivity.Instance._deviceRole+"_onCSC_shareimage");
//
//            if(FpsScenarioDirector.Instance.GetActiveScenarioType() == FpNetConstants.SCENARIO_RECORD)
//            {
//                FpsScenario_record scenario_record = ((FpsScenario_record)FpsScenarioDirector.Instance.GetActiveScenario());
//                scenario_record.SetShareImage(data._bitMapByteArray);
//            }
//
//            MainActivity.Debug_end_timecount();
//
//            Toast.makeText(MainActivity.Instance, "SHareImage_2", Toast.LENGTH_LONG).show();
//            _net_server.BroadcastPacket(FpNetConstants.CSC_ShareImage, data);
        }
    };

    private void setReposition()
    {
        Activity_serverMain_andEngine.Instance.ShareImage_reposition();
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 대화 정보(버블, 웃음 이벤트 목록) 요청
    FpNetMessageCallBack onReq_talk_record_info = new FpNetMessageCallBack()
    {
        //# 버블 데이터 만들어 보내는 함수.
        @Override
        public void CallBack(FpNetIncomingMessage inMsg) {
            Log.i("[J2Y]", "[패킷수신] 대화 정보(버블, 웃음 이벤트 목록) 요청");


            //FpNetServer_client client = (FpNetServer_client)inMsg._obj;
            for (FpNetServer_client client : Manager_users.Instance.Get_talk_users().keySet())
            {
                if (client == null)
                    break;

                if (Manager_contents.Instance.GetCurrentContent() == Manager_contents.eType_contents.CONTENTS_TALK)
                {
                    FpNetDataRes_recordInfoList outMsg = new FpNetDataRes_recordInfoList();

                    // todo: 메인쓰레드, 렌더링쓰레드 충돌남
                    FpsTalkUser talk_user = Manager_users.Instance.GetTalkUser(client);

                    if (null == talk_user)
                        break;

                    Actor_attractor attractor = Manager_actor.Instance.Get_attractor(talk_user._uid_attractor);

                    outMsg._attractor._x = attractor.Get_Body().getPosition().x;// * PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
                    outMsg._attractor._y = attractor.Get_Body().getPosition().y;// * PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT;
                    outMsg._attractor._color = client._clientID;

                    for (BaseActor actor : Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_TALK))
                    {
                        Actor_talk talk = (Actor_talk) actor;
                        if( talk.Get_colorId() == client._clientID)
                        {
                            Vector2 pos = talk.Get_Body().getPosition();
                            outMsg.AddRecordData(talk.GetStart_time(), talk.GetEnd_time(),
                                    (outMsg._attractor._x - pos.x )* PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
                                    (outMsg._attractor._y - pos.y) * PhysicsConnector.PIXEL_TO_METER_RATIO_DEFAULT,
                                    talk.Get_Scale(), talk.Get_colorId(), talk._answerID);
                        }
                    }
                    //outMsg._smile_events.addAll(talk_user._smile_events);
                    client.SendPacket(FpNetConstants.SCRes_TalkRecordInfo, outMsg);
                }
            }
        }
    };

    //------------------------------------------------------------------------------------------------------------------------------------------------------
    // 사용자 메세지
    FpNetMessageCallBack onReq_userInput_bubbleMove = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg)
        {
            if( Activity_serverMain_andEngine.Instance != null )
            {
                FpNetDataReq_bubbleMove data = new FpNetDataReq_bubbleMove();
                data.Parse(inMsg);

                Activity_serverMain_andEngine.Instance.MoveUserBubble_add(data._dirX, data._dirY, data._clientid );
                FpNetFacade_server.Instance.Send_clientUpdate();
            }
        }
    };
    FpNetMessageCallBack onReq_userInteraction = new FpNetMessageCallBack()
    {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg) throws InterruptedException {
            FpNetData_userInteraction data = new FpNetData_userInteraction();
            data.Parse(inMsg);

            // # event test
            //Activity_serverMain_andEngine.Instance.Create_good(data._send_client_id, data._clientid);

            Event_createGood event = new Event_createGood(data);
            Activity_serverMain_andEngine.Instance.Add_event(event);
        }
    };

    FpNetMessageCallBack onReq_topping = new FpNetMessageCallBack() {
        @Override
        public void CallBack(FpNetIncomingMessage inMsg) throws InterruptedException
        {

            FpNetDataReq_topic data = new FpNetDataReq_topic();
            data.Parse(inMsg);

            Activity_serverMain_andEngine main = Activity_serverMain_andEngine.Instance;
            Manager_resource.Instance.Create_flashSprite(data._clientId, main.Get_scene(), main, FpNetUtil.ByteArrayToBitmap(data.Get_bitArray()), data.Get_Text() );

            main.currentTime_releaseTopic = System.currentTimeMillis();
        }
    };

}
