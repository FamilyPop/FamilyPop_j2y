package com.j2y.familypop.activity.manager.states;

import com.j2y.familypop.activity.manager.actors.BaseActor;

/**
 * Created by lsh on 2016-08-03.
 */
public class State_ActorMove extends BaseState
{

    private BaseActor _mover = null;
    private BaseActor _target = null;
    private float _moveSpeed = 0.0f;
    private float _distance = 0.0f;

    public State_ActorMove(BaseActor mover, BaseActor target, float distance, float speed)
    {
        _mover = mover;
        _target = target;
        _distance = distance;
        _moveSpeed = speed;
    }
    @Override
    public void init()
    {
        super.init();
    }
    @Override
    public boolean onUpdate(float pSecondsElapsed)
    {
        if( _mover.moveTo_target(pSecondsElapsed, _target.Get_Body().getPosition(), _distance, _moveSpeed)) State_end();

        return super.onUpdate(pSecondsElapsed);
    }
    @Override
    public  void release()
    {
        super.release();
    }
}
