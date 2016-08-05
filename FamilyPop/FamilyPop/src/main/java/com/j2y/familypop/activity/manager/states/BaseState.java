package com.j2y.familypop.activity.manager.states;

/**
 * Created by lsh on 2016-08-03.
 */
public class BaseState {
    private boolean mInit;
    private boolean mEnd;

    public void BaseState() {
    }

    public void init() {

    }

    public boolean onUpdate(float pSecondsElapsed) {
        return mEnd;
    }

    public void release() {
        mInit = false;
        mEnd = false;
    }

    public void State_end() {
        mEnd = true;
    }

    public void Init_scuccess() {
        mInit = true;
    }

    public boolean Get_InitState() {
        return mInit;
    }
}
