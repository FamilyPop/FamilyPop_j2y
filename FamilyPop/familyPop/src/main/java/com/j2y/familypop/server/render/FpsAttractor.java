package com.j2y.familypop.server.render;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.j2y.familypop.MainActivity;
//import com.j2y.familypop.activity.Activity_serverMain;
import com.j2y.familypop.activity.manager.Manager_resource;
import com.nclab.familypop.R;

import processing.core.PImage;
//import shiffman.box2d.*;

import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.Constants;
//import org.jbox2d.collision.shapes.*;
//import org.jbox2d.common.*;
//import org.jbox2d.dynamics.*;

import processing.core.PApplet;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
// FpsAttractor
//
//
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class FpsAttractor
{
	// We need to keep track of a Body and a radius
	public Body _body;
	float rad;
	public int _color;

    //Box2DProcessing _box2d;
	PhysicsWorld _physicsWorld = null;

	private PImage _userImage = null;
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	public FpsAttractor(PhysicsWorld physicsWorld, float _rad, float _x, float _y, int tempC)
    {
		_physicsWorld = physicsWorld;
		_color = tempC;
		rad = _rad;

		// back 160511
//		//body.setType(BodyType.STATIC);
//		// Define a _body
//		BodyDef bd_ = new BodyDef();
//		bd_.type = BodyType.STATIC;
//		// Set its position
//		bd_.position = _box2d.coordPixelsToWorld(_x, _y);
//		body = _box2d.world.createBody(bd_);
//
//		// Make the _body's shape a circle
//		CircleShape cs_ = new CircleShape();
//		cs_.m_radius = _box2d.scalarPixelsToWorld(rad);
//
//		body.createFixture(cs_, 1);

		//andengine
//		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1.0f, 0.01f, 0.1f); // 밀도 ,탄성, 마찰력
//		AnimatedSprite face = new AnimatedSprite(_x, _y, Manager_resource.Instance.GetTiledTexture("user-01.png"), Activity_serverMain.Instance.getVertexBufferObjectManager());
//
//		_body = createCircleBody(_physicsWorld, face, BodyDef.BodyType.StaticBody, objectFixtureDef, 0.5f);
//
//		if( _body != null)
//		{
//			Activity_serverMain.Instance._scene.attachChild(face);
//			physicsWorld.registerPhysicsConnector(new PhysicsConnector(face, _body, true, true));
//		}

	}

    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    public Vec2 GetPosition()
//    {
//        return _box2d.getBodyPixelCoord(body);
//    }
	public Vector2 GetAttractorPos(){ return _body.getPosition(); }
	//public Vec2 GetAttractorPos(Box2DProcessing _box2d)
//	{
//		return _box2d.getBodyPixelCoord(body);
//	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------
	// Formula for gravitational attraction
	// We are computing this in "world" coordinates
	// No need to convert to pixels and back
	//public Vec2 attract(Body bubbleBody)
	public Vector2 attract(Body bubbleBody)
	{
    //back 16 06 11
//		float g_ = 1000; // Strength of force
//		// clone() makes us a copy
//		Vec2 pos_ = body.getWorldCenter();
//		Vec2 pos_mover_ = bubbleBody.getWorldCenter();
//		// Vector pointing from mover to attractor
//		Vec2 force_ = pos_.sub(pos_mover_);
//		float dist_ = force_.length();
//
//		// Keep force within bounds
//		dist_ = PApplet.constrain(dist_, 1, 2);
//		force_.normalize();
//		// Note the attractor's mass is 0 because it's fixed so can't use that
//		float strength_ = (g_ * 1 * bubbleBody.m_mass) / (dist_ * dist_); // Calculate
//                                      // gravitional
//                                      // force
//                                      // magnitude
//		force_.mulLocal(strength_); // Get force vector --> magnitude *
//		// direction
//		return force_;

		Vector2 ret = null;

		ret = new Vector2( 	(GetAttractorPos().x - bubbleBody.getPosition().x) * 1.1f,
							(GetAttractorPos().y - bubbleBody.getPosition().y) * 1.1f );

		return ret;
	}
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	//public void display(PApplet _pApplet, Box2DProcessing _box2d)
	public void display()
	{
		//back 160511
//		// We look at each _body and get its screen position
//		Vec2 pos = _box2d.getBodyPixelCoord(body);
//		// Get its angle of rotation
//		float angle_ = body.getAngle();
//		_pApplet.pushMatrix();
//
//		_pApplet.translate(pos.x, pos.y);
//
//		_pApplet.rotate(angle_);
//		_pApplet.fill(_color);
//		//_pApplet.stroke(0);
//		_pApplet.noStroke();
//		_pApplet.strokeWeight(1);
//		_pApplet.ellipse(0, 0, rad * 2, rad * 2);
//		_pApplet.popMatrix();
	}

//	private int getColor(int index)
//	{
//        /*
//        0xffff6666,     // 빨강
//		0xff66ff66,     // 녹색
//		0xffffff66,     // 노랑
//		0xff6666ff,     // 파랑
//		0xffffffff,     // 보라
//        0xff66ff66, // 임시
//        */
//
//
//		switch(index)
//		{
//			case 0: return R.color.red;
//			case 1: return R.color.green;
//			case 2: return R.color.yellow;
//			case 3: return R.color.blue;
//			case 4: return R.color.pink;
//			case 5: return R.color.grey;   //임시?
//		}
//
//		return R.color.black; // 없는 컬러
//	}

	//======================================================================================================================\
	// andengine
	private Body createCircleBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyDef.BodyType pBodyType, final FixtureDef pFixtureDef, float multiplyRadius)
	{
		final float[] sceneCenterCoordinates = pAreaShape.getSceneCenterCoordinates();
		final float centerX = sceneCenterCoordinates[Constants.VERTEX_INDEX_X];
		final float centerY = sceneCenterCoordinates[Constants.VERTEX_INDEX_Y];
		return PhysicsFactory.createCircleBody(pPhysicsWorld, centerX, centerY, pAreaShape.getWidthScaled() * multiplyRadius, pAreaShape.getRotation(), pBodyType, pFixtureDef, PIXEL_TO_METER_RATIO_DEFAULT);
	}
}
