package com.j2y.familypop.activity.manager;

import com.j2y.familypop.activity.manager.contents.BaseContents;
import com.j2y.familypop.activity.manager.contents.client.Contents_clientReady;
import com.j2y.familypop.activity.manager.contents.client.Contents_clientTalk;
import com.j2y.familypop.activity.manager.contents.server.Contents_ready;
import com.j2y.familypop.activity.manager.contents.server.Contents_talk;

import java.util.HashMap;

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
}
