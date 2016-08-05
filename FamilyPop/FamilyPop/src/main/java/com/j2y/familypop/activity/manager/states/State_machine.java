package com.j2y.familypop.activity.manager.states;

import java.util.ArrayList;

/**
 * Created by lsh on 2016-08-03.
 */
public class State_machine
{
    private ArrayList<BaseState> _states;

    public void Add_State(BaseState state)
    {
        _states.add(state);
    }

    public State_machine()
    {
        _states = new ArrayList<>();
    }

    //================================================================================================
    // 메인 함수
    //================================================================================================
    public void Init() {

    }

    private BaseState deleteItem;
    public void Update(float pSecondsElapsed)
    {
        if( _states.size() == 0) return;

        BaseState item = _states.get(0);

        if (!item.Get_InitState())
        {
            item.init();
            item.Init_scuccess();
        }
        else
        {
            if (item.onUpdate(pSecondsElapsed))
            {
                //release
                item.release();
                _states.remove(item);
            }
        }
    }
    public void Release() {

    }
}
