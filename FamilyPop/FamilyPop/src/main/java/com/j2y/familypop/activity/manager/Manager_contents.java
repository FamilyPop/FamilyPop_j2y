package com.j2y.familypop.activity.manager;

import com.j2y.familypop.activity.manager.actors.Actor_attractor;
import com.j2y.familypop.activity.manager.actors.Actor_talk;
import com.j2y.familypop.activity.manager.actors.BaseActor;
import com.j2y.familypop.activity.manager.contents.BaseContents;
import com.j2y.familypop.activity.manager.contents.client.Contents_clientReady;
import com.j2y.familypop.activity.manager.contents.client.Contents_clientTalk;
import com.j2y.familypop.activity.manager.contents.server.Contents_ready;
import com.j2y.familypop.activity.manager.contents.server.Contents_talk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lsh on 2016-05-12.
 */


public class Manager_contents
{
    public static Manager_contents Instance = null;
    public static boolean IsServer = false;

    public enum eType_contents
    {
        CONTENTS_NOT(-1), CONTENTS_READY(0), CONTENTS_TALK(1);

        private int value;

        private eType_contents (int value) { this.value = value; }


        public int getValue () { return value; }
        public boolean IsEmpty(){return this.equals(eType_contents.CONTENTS_NOT);}
        public boolean Compare(int i){return value == i;}
        public static eType_contents IntToType_contents(int v)
        {
            eType_contents[] As = eType_contents.values();
            for( int i=0; i<As.length; ++i)
            {
                if( As[i].Compare(v)){ return As[i]; }
            }
            return eType_contents.CONTENTS_NOT;
        }
    }
    private eType_contents mCurrentContent;
    private eType_contents mNextContent;
    private HashMap<eType_contents, BaseContents> mContents;


    public eType_contents GetCurrentContent(){ return mCurrentContent; }
    public BaseContents GetCurrentContents()
    {

        BaseContents ret = null;
        if( GetCurrentContent() == eType_contents.CONTENTS_NOT) return ret;
        return mContents.get(GetCurrentContent());
    }

    public Manager_contents(boolean isServer)
    {
        IsServer = isServer;
        mCurrentContent = eType_contents.CONTENTS_NOT;
        mNextContent = eType_contents.CONTENTS_NOT;
        mContents = new HashMap<eType_contents, BaseContents>();

        // add contents
        if( IsServer )
        {
            mContents.put(eType_contents.CONTENTS_READY, new Contents_ready());
            mContents.put(eType_contents.CONTENTS_TALK, new Contents_talk());
        }
        else
        {
            mContents.put(eType_contents.CONTENTS_READY, new Contents_clientReady());
            mContents.put(eType_contents.CONTENTS_TALK, new Contents_clientTalk());
        }
        Instance = this;
    }


    public  synchronized void Content_change(eType_contents contents_type)
    {
        try
        {
            if( mCurrentContent != eType_contents.CONTENTS_NOT)
            {
                BaseContents contents = mContents.get(mCurrentContent);
                mNextContent = contents_type;
                contents.Contents_end();
            }
            else{ mCurrentContent = contents_type; }
        }
        catch (Exception e){}


    }
    public synchronized void update()
    {
        if( mCurrentContent == eType_contents.CONTENTS_NOT) return;

        BaseContents contents = mContents.get(mCurrentContent);

        if(!contents.Get_InitState())
        {
            contents.init();
            // 초기화.
            contents.Init_scuccess();
        }
        else
        {
            // update
            if(contents.update())
            {
                //release
                contents.release();
                mCurrentContent = mNextContent;
                mNextContent = eType_contents.CONTENTS_NOT;
            }
        }
    }

    public void Release_All()
    {
        mCurrentContent = eType_contents.CONTENTS_NOT;
    }

    //[attractors id] = 비율정보.
    public float[] _talkRatios = null;
    private boolean Calculation_talkRatios()
    {
        boolean ret = false;
        // 액터들의 대화 비율을 구한다.
        CopyOnWriteArrayList<BaseActor> talks =  Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_TALK);
        CopyOnWriteArrayList<BaseActor> attractors =  Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_ATTRACTOR);
        _talkRatios = new float[attractors.size()];

        if(attractors.size()== 0)   return ret;
        if(talks.size()== 0)        return ret;

        int totalCount = talks.size();

        for( int j=0; j<attractors.size(); j++)
        {
            int talkCount = 0;
            Actor_attractor attractor = (Actor_attractor)attractors.get(j);
            for( int i=0; i<talks.size(); ++i)
            {
                if( attractor.Get_colorId() == talks.get(i).Get_colorId() )
                {
                    talkCount++;
                }
            }
            if(talks.size() !=0 ) _talkRatios[attractor.Get_colorId()] = (float)talkCount / (float)totalCount * 100.0f;
        }

        ret = true;

        return ret;
    }
    // 말을 제일 적게 한 사람 찾아 내기.
    public Actor_attractor Get_talkClam()
    {
        Calculation_talkRatios();

        CopyOnWriteArrayList<BaseActor> attractors =  Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_ATTRACTOR);
        Actor_attractor ret = null;

        float ratio = _talkRatios[0];
        //BaseActor targetAttractor = attractors.get(0);
        for( int i=0; i<_talkRatios.length; i++)
        {
            if( _talkRatios[i] < ratio)
            {
                ratio = _talkRatios[i];
                ret = (Actor_attractor)attractors.get(i);
            }
        }
        // null 예외처리.
        return ret;
    }
    // 서로 말을 적게한 두사람 찾아내기.
    // 기준이 되는 액터, [ 대상 액터 인덱스 ], 전체에서 차지하고있는 대화비율.
    private HashMap<Actor_attractor, float[]> _chatRate;
    private void Calculation_talkRatiosPair()
    {
        // 초기화
        CopyOnWriteArrayList<BaseActor> talks =  Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_TALK);
        CopyOnWriteArrayList<BaseActor> attractors =  Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_ATTRACTOR);
        _chatRate = new HashMap<>();
        final int totalTalkCount = talks.size();


        for( int i= 0; i<attractors.size(); ++i)
        {
            _chatRate.put((Actor_attractor)attractors.get(i), new float[attractors.size()]);
        }
        // 초기화 끝

        // 각 토크를 분리
        for (int i = 0; i < attractors.size(); ++i)
        {
            Actor_attractor attractor_standard = (Actor_attractor) attractors.get(i);


            for (int x = 0; x < talks.size(); x++)
            {
                Actor_talk talk = (Actor_talk) talks.get(x);

                if( attractor_standard.Get_colorId() == talk._startTalkID)
                {

                    for( int j=0; j<attractors.size(); ++j)
                    {
                        if( talk._answerID == j)
                        {
                            _chatRate.get(attractor_standard)[talk._answerID] +=1;
                            break;
                        }
                    }
                }
            }
        }
    }
    // targetActor 을 보고 이 액터와 가장 이야기를 안한 액터를 리턴한다.
    public Actor_attractor Get_talkClamPair( Actor_attractor targetActor)
    {
        Actor_attractor ret = null;
        Calculation_talkRatiosPair();

        // 가장 이야기를 안한 페어를 찾는다.
        CopyOnWriteArrayList<BaseActor> attractors =  Manager_actor.Instance.GetActorsList(Manager_actor.eType_actor.ACTOR_ATTRACTOR);
        int selectIndex = 0;
        float tempbuffer = _chatRate.get(targetActor)[selectIndex];

        for( int i=0; i< attractors.size(); ++i )
        {
            if( tempbuffer > _chatRate.get(targetActor)[i] )
            {
                tempbuffer = _chatRate.get(targetActor)[i];
                selectIndex = i;
            }
        }
        ret = (Actor_attractor) attractors.get(selectIndex);
        return ret;
    }
}
