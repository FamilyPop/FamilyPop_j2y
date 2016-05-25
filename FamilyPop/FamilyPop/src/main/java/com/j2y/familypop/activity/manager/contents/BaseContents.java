package com.j2y.familypop.activity.manager.contents;

/**
 * Created by lsh on 2016-05-12.
 */
public class BaseContents
{
    private boolean mInit;
    private boolean mEnd;

    public BaseContents()
    {
        mInit = false;
        mEnd = false;
    }
    public void init()
    {
    }
    public boolean update()
    {
        return mEnd;
    }
    public void release()
    {
        mInit = false;
        mEnd = false;
    }

    public void Contents_end(){ mEnd = true; }
    public void Init_scuccess(){ mInit = true; }
    public boolean Get_InitState(){return mInit;}
}
