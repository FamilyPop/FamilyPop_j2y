package com.j2y.familypop.activity.manager.contents.server;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.ViewConfiguration;

import com.j2y.familypop.MainActivity;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.lobby.Activity_locatorNowCalibrating;
import com.j2y.familypop.activity.manager.Manager_actor;
import com.j2y.familypop.activity.manager.Manager_resource;
import com.j2y.familypop.activity.manager.Manager_users;
import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.familypop.activity.manager.actors.Actor_talk;
import com.j2y.familypop.activity.manager.contents.BaseContents;
import com.j2y.familypop.activity.server.event_server.Event_createTalk;
import com.j2y.familypop.server.FpsRoot;
import com.j2y.familypop.server.FpsTalkUser;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by lsh on 2016-05-17.
 */
public class Contents_talk extends BaseContents
{
    public Contents_talk()
    {
    }

    @Override
    public void init()
    {
        super.init();

        Activity_serverMain_andEngine aMain = Activity_serverMain_andEngine.Instance;
        FpsRoot.Instance._socioPhone.startRecord(0, "temp");
        aMain.Instance.release_text(aMain._draw_ipAddress);
        //aMain._draw_ipAddress.setVisible(false);
        //aMain._draw_ipAddress = null;
    }

    @Override
    public boolean update()
    {
        //Log.e("[J2Y]", "talk_update");
        if( Activity_serverMain_andEngine.Instance != null)
        {
            process_turn_data_average(Activity_serverMain_andEngine.Instance.GetInfo_regulation()._regulation_seekBar_2);
        }
        return super.update();
    }

    @Override
    public void release() {

    }

    public ArrayList<Integer> speakerBuffer = new ArrayList<Integer>();
    int currentSpeakerId = 0;
    int previousSpeakerId = 0;
    float _bubbleSize = 150.0f;
    Actor_talk _current_bubble = null;


    public void OnTurnDataReceived(int speakerID) {
        //Log.i("[J2Y]", "OnTurnDataReceived: " + speakerID[0]);
        //Log.i("[J2Y]", "ThreadID:[OnTurn]" + (int) Thread.currentThread().getId());

        synchronized (speakerBuffer) {
            speakerBuffer.add(0, speakerID);

            //int window_size = Activity_serverMain.Instance._regulation_seekBar_2;
            int window_size = Activity_serverMain_andEngine.Instance.GetInfo_regulation()._regulation_seekBar_2;
            if (speakerBuffer.size() > window_size) {
                speakerBuffer.remove(window_size);

                currentSpeakerId = getMaxCountValue(speakerBuffer);
/*
                ArrayList<Integer> testList = new ArrayList<Integer>();
                testList.add(1);
                testList.add(1);
                testList.add(0);
                testList.add(3);
                testList.add(3);
                testList.add(3);
                testList.add(3);

                Log.i("KAIST", "Test: " + (getMaxCountValue(testList) == 3));
                */
            }
        }
    }



    private synchronized void IsBubbleStarting(boolean isBubbleStarting) {
        //Log.i("KAIST", "-----Bubble starts: " + currentSpeakerId + "/" + previousSpeakerId);

        // 3.1. 이전 버블 발사
        if (_current_bubble != null) {
            int end_time = (int) FpsRoot.Instance._socioPhone.GetRecordTime();
            _current_bubble.StartMover(end_time);
            _current_bubble.Mul_collider(Activity_serverMain_andEngine.Instance.GetInfo_regulation()._colliderTalkSize); // 발사 할대 현재 설정을 넣어준다.
            _current_bubble = null;

        }

        AtomicReference<FpsTalkUser> userRef = new AtomicReference<>();
        Manager_users.Instance.FindTalkUser_byId(currentSpeakerId-2, userRef);

        if (userRef.get() != null)
        {
            // 3.2. 새로운 버블 생성
            int speakerId = currentSpeakerId - 2;
            int preSpeaker = previousSpeakerId-2;
            if( preSpeaker < 0){ preSpeaker = speakerId;}
            if( speakerId >= 0)
            {
                if( preSpeaker < 0){userRef.get()._answerCount++;}
                if( speakerId >= 0){userRef.get()._startTalkCount++;}

                Actor_talk bubble = null;
                Actor_attractor attractor = Manager_actor.Instance.Get_attractor(userRef.get()._uid_attractor);
                // 발사할 (지금 말하고있는 꽃잎 생성)
                String userImageName = Manager_resource.Instance.Get_userImage(Manager_resource.eImageIndex_color.IntToImageColor(attractor.Get_colorId()));
                String petalImageName;

                petalImageName = Manager_resource.Instance.Get_petalNames(Manager_resource.eImageIndex_color.IntToImageColor(preSpeaker), Manager_resource.eType_petal.PETAL_TALK);

                bubble = Activity_serverMain_andEngine.Instance.Create_talk(userImageName, petalImageName, attractor);
                bubble.Set_colorId(preSpeaker);
                bubble.SetStart_time((int)FpsRoot.Instance._socioPhone.GetRecordTime());
                //
                //Event_createTalk event = new Event_createTalk(userImageName, petalImageName, preSpeaker, attractor);

                if (bubble != null) {
                    _current_bubble = bubble;
                }
            }
        }
}
    private void IsBubbleGrowing(boolean isBubbleGrowing) {
        //Log.i("KAIST", "Bubble grows: " + currentSpeakerId + "/" + previousSpeakerId);

        if (_current_bubble != null) {
            //float scale = Activity_serverMain_andEngine.Instance.GetInfo_regulation()._plusMoverRadius;
            //float scale = Activity_serverMain_andEngine.Instance.GetInfo_regulation()._flowerPlusSize;
            //float elapsed
            //Activity_serverMain_andEngine.Instance.getEngine().getSecondsElapsedTotal()
            _current_bubble.Set_plusScale();
        }
    }
    private void IsBubbleEnding(boolean isBubbleGrowing)
    {
        // Log.i("KAIST", "-----Bubble ends: " + currentSpeakerId + "/" + previousSpeakerId);

        // 3.1. 이전 버블 발사
        if (_current_bubble != null) {
            int end_time = (int) FpsRoot.Instance._socioPhone.GetRecordTime();
            _current_bubble.StartMover(end_time);
            _current_bubble.Mul_collider(Activity_serverMain_andEngine.Instance.GetInfo_regulation()._colliderTalkSize); // 발사 할대 현재 설정을 넣어준다.
            _current_bubble = null;
        }
    }

    public synchronized void process_turn_data_average(int avg_count) {
        //int IGNORED_VALUE = avg_count;
        int IGNORED_VALUE = avg_count;

        //Log.i("KAIST", "CurrentSpeakerId: " + currentSpeakerId);

        boolean isBubbleStarting = (previousSpeakerId == 0 || previousSpeakerId == 1 || (previousSpeakerId != currentSpeakerId)) && (currentSpeakerId >= 2);
        boolean isBubbleGrowing = (previousSpeakerId >= 2) && (previousSpeakerId == currentSpeakerId);
        boolean isBubbleEnding = (previousSpeakerId >= 2) && (previousSpeakerId == 0 && previousSpeakerId == 1); // Condition #1
        isBubbleEnding |= (previousSpeakerId >= 2) && (previousSpeakerId != currentSpeakerId); // Condition #2
        isBubbleEnding |= (previousSpeakerId >= 2) && (_bubbleSize >= Activity_serverMain_andEngine.Instance.GetInfo_regulation()._regulation_seekBar_3); // Condition #3

        if (isBubbleStarting) {
            IsBubbleStarting(isBubbleStarting);
        } else if (isBubbleGrowing) {
            IsBubbleGrowing(isBubbleGrowing);
        } else if (isBubbleEnding) {
            IsBubbleEnding(isBubbleEnding);
        }
        previousSpeakerId = currentSpeakerId;
    }

    private static Integer getMaxCountValue(List<Integer> myList) {

        HashMap<Integer, Integer> frequencymap = new HashMap<Integer, Integer>();
        Integer maximum = 0;

        //Loop through list
        for (Integer integerObj : myList) {
            //If the hashmap contains the value then increase count
            if (frequencymap.containsKey(integerObj.intValue())) {
                frequencymap.put(integerObj.intValue(), frequencymap.get(integerObj.intValue()) + 1);
            } else {
                //add value with count of 1
                frequencymap.put(integerObj.intValue(), 1);
            }
        }

        //Loop through hashmap to find the maximum count
        for (int value : frequencymap.values()) {
            if (value > maximum) {
                maximum = value;
            }
        }

        //Loop through hashmap to find the maximum count
        Integer keyOfMaxValue = null;
        for (int key : frequencymap.keySet()) {
            Integer value = frequencymap.get(key);
            if (value == maximum) {
                keyOfMaxValue = key;
            }
        }

        return keyOfMaxValue;
        //return maximum;
    }
}
