package com.j2y.familypop.activity.manager.states;

import com.badlogic.gdx.math.Vector2;
import com.facebook.internal.Utility;
import com.j2y.familypop.activity.Activity_serverMain_andEngine;
import com.j2y.familypop.activity.manager.actors.BaseActor;

import org.andengine.util.math.MathUtils;

import java.util.ArrayList;

/**
 * Created by lsh on 2016-08-03.
 */
public class State_ActorRotationAxis extends BaseState
{
    BaseActor _mover = null;
    BaseActor _axisActor1 = null;
    BaseActor _axisActor2 = null;

    Vector2 _centerAxis = null;
    float _moveSpeed;
    boolean _direction = true;  // true 오른쪽, false 왼쪽

    float _prvAngle = 0.0f;
    float _angleBuffer = 0.0f;

    // back
//    public State_ActorRotationAxis(BaseActor mover, Vector2 axis,  float speed, boolean direction)
//    {
//        _mover = mover;
//        _axis = axis;
//        _moveSpeed = speed;
//        _direction = direction;
//    }

    public State_ActorRotationAxis(BaseActor mover, BaseActor axisActor1, BaseActor axisActor2,  float speed, boolean direction)
    {
        _mover = mover;
        _axisActor1 = axisActor1;
        _axisActor2 = axisActor2;
        _moveSpeed = speed;
        _direction = direction;

        _targetActors = new ArrayList<>();
        _targetActors.add(_axisActor1);
        _targetActors.add(_axisActor2);

        set_targetActor(0);
    }

    @Override
    public void init()
    {
        super.init();

        Vector2 Pos1 = _axisActor1.Get_Body().getPosition();
        Vector2 Pos2 = _axisActor2.Get_Body().getPosition();

        float X = ((Pos1.x + Pos2.x)/2);
        float Y = ((Pos1.y + Pos2.y)/2);

        _centerAxis = new Vector2();
        _centerAxis.x = X;
        _centerAxis.y = Y;

        Vector2 moverPos = _mover.Get_Body().getPosition();
        Vector2 targetPos = _targetActor.Get_Body().getPosition();
        _targetDistance =  MathUtils.distance(moverPos.x, moverPos.y, targetPos.x, targetPos.y);


        // back 각 구하기
//        Vector2 moverPos = _mover.Get_Body().getPosition();
//        float dx = _axis.x - moverPos.x;
//        float dy = _axis.y - moverPos.y;
//        double rad =Math.atan2(dx, dy);
//        float degree = (float)((rad*180)/Math.PI);
//
//        //_radius = Math.abs( MathUtils.distance(moverPos.x, moverPos.y, _axis.x, _axis.y) );
//        _mover._rotation_axis_angle = MathUtils.degToRad(degree);
    }

    int _targetActorIndex = -1;
    int _updateCount = 0;
    ArrayList<BaseActor> _targetActors;
    BaseActor _targetActor = null;
    float _targetDistance = 0.0f;

    @Override
    public boolean onUpdate(float pSecondsElapsed)
    {

       if( _targetDistance > 5){ _targetDistance -= (pSecondsElapsed * 2f); }

        if( _angleBuffer < (Math.PI / 2.0f) * 3)
        {
            _mover.rotation_axis(pSecondsElapsed, _targetActor.Get_Body().getPosition(), _targetDistance, _moveSpeed, _direction);
            _angleBuffer += (Math.abs(_mover._rotation_axis_angle) - Math.abs(_prvAngle));
            _prvAngle = _mover._rotation_axis_angle;
        }
        else
        {
            _updateCount++;
            set_targetActor(_updateCount  % 2);
            _direction = !_direction;
            init();
            _angleBuffer = 0.0f;
            _prvAngle = 0.0f;


            Vector2 moverPos = _mover.Get_Body().getPosition();
            Vector2 axis = _targetActor.Get_Body().getPosition();
            float dx = axis.x - moverPos.x;
            float dy = axis.y - moverPos.y;
            double rad =Math.atan2(dx, dy);
            float degree = (float)((rad*180)/Math.PI);

            _mover._rotation_axis_angle = (float)rad;
            //State_end();
        }

        //if()
        //State_end();
        //_mover.rotation_axis(pSecondsElapsed, _axis, 5, _moveSpeed, _direction);
        // back
//        if( _angleBuffer > Math.PI)
//        {
//            State_end();
//        }
//        else
//        {
//            _mover.rotation_axis(pSecondsElapsed, _axis, _radius, 5, _direction);
//            _angleBuffer += (Math.abs(_mover._rotation_axis_angle) - Math.abs(_prvAngle));
//            _prvAngle = _mover._rotation_axis_angle;
//        }

        return super.onUpdate(pSecondsElapsed);
    }
    @Override
    public  void release()
    {
        super.release();
    }

    private void set_targetActor(int index)
    {
        if( _targetActorIndex == index) return;

        _targetActor = _targetActors.get(index);
        _targetActorIndex = index;
    }


}
