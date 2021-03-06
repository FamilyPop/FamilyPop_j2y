//package com.j2y.familypop.server.render;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.FixtureDef;
//import com.j2y.familypop.activity.Activity_serverMain;
//import com.j2y.familypop.activity.Activity_serverMain_andEngine;
//import com.j2y.familypop.activity.manager.Manager_resource;
//
//import shiffman.box2d.*;
//
//import org.andengine.entity.scene.Scene;
//import org.andengine.entity.sprite.AnimatedSprite;
//import org.andengine.extension.physics.box2d.PhysicsFactory;
//import org.andengine.extension.physics.box2d.PhysicsWorld;
//import org.jbox2d.collision.AABB;
//import org.jbox2d.collision.shapes.*;
//import org.jbox2d.common.*;
//import org.jbox2d.dynamics.*;
//
//import processing.core.PApplet;
//
////++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
////
//// FpsBubble
////
////
////++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//
//public class FpsBubble
//{
//    public final static int Type_Normal = 0;
//    public final static int Type_Smile = 1;
//    public final static int Type_Good = 2;
//
//	// We need to keep track of a Body and a radius
//    public int _type;
//	public Body body;
//    public float _rad;
//	int _color;
//    public int _colorId;
//    public int _colorGood = -1;
//	Fixture fd;
//
//    //Box2DProcessing _box2d;
//    Scene _scene;
//    PhysicsWorld _physicsWorld;
//
//    public boolean _isMoving;   // 어트랙터로 움직일지 여부
//    public int _start_time;
//    public int _end_time;
//
//    private ProcessingImage_base _image_smile;
//
//    private ProcessingImage_base _image_good;
//
//
//    private ProcessingImage_base _image_bubble;
//
//	//------------------------------------------------------------------------------------------------------------------------------------------------------
//	//public boolean CreateMover(PApplet applet, Box2DProcessing box2d, float rad, float x, float y, int tempC, int colorId, int type)
//    public boolean CreateMover(Scene scene, PhysicsWorld physicsWorld, float rad, float x, float y, int tempC, int colorId, int type)
//	{
//        //setContinuousPhysics
//		try
//        {
//            //_box2d = box2d;
//            _scene = scene;
//            _physicsWorld = physicsWorld;
//            _color = tempC;
//            _colorId = colorId;
//            _rad = rad;
//
//            // andEngine create Body
//            Body body = null;
//            final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef( 0, 0, 0.2f);
//
//            AnimatedSprite face = new AnimatedSprite(x, y, Manager_resource.Instance.GetTiledTexture("image_bead_0.png"), Activity_serverMain_andEngine.Instance.getVertexBufferObjectManager());
//            PhysicsFactory.createCircleBody(_physicsWorld, face, BodyDef.BodyType.DynamicBody, objectFixtureDef);
//
//            body.setLinearVelocity(new Vector2(0, 0));
//            body.setAngularVelocity(0);
//            _isMoving = false;
//            body.setActive(false);
//
//			// Define a _body
//			BodyDef bd_ = new BodyDef();
//			bd_.type = BodyType.DYNAMIC;
//
//
//			// Set its position
//			bd_.position = _box2d.coordPixelsToWorld(x, y);
//
//
//			body = _box2d.world.createBody(bd_);
//
//
//
//            AABB aabb = new AABB();
//
//            aabb.lowerBound.set(-100.0f, -100.0f);
//
//
//            body.setFixedRotation(false);
//            body.setBullet(false);
//
//			// Make the _body's shape a circle
//			CircleShape cs_ = new CircleShape();
//			cs_.m_radius = _box2d.scalarPixelsToWorld(this._rad);
//
//
//            // Define a fixture
//			FixtureDef fd_ = new FixtureDef();
//
//			fd_.shape = cs_;
//
//			fd_.density = 0.0001f;
//			fd_.friction = 1;
//			fd_.restitution = 0.0000001f;
//
//            fd_.filter.groupIndex = colorId;
//
//
//
//			fd = body.createFixture(fd_);
//			body.setLinearVelocity(new Vec2(0, 0));
//			body.setAngularVelocity(0);
//            _isMoving = false;
//            body.setActive(false);
//
//            _type = type;
//            if(_type == Type_Smile)
//            {
//                _image_smile = new ProcessingImage_base(applet, "image_bubble_orange_smile.png");
//                _image_smile.SetPosition(new Vec2(x, y));
//            }
//            else if( _type == Type_Good)
//            {
//                switch (_colorGood)
//                {
//                    case 0: _image_good = new ProcessingImage_base(applet, "image_good_pink.png");   break;      // pink
//                    case 1: _image_good = new ProcessingImage_base(applet, "image_good_red.png");   break;      // red
//                    case 2: _image_good = new ProcessingImage_base(applet, "image_good_yellow.png");   break;      // yellow
//                    case 3: _image_good = new ProcessingImage_base(applet, "image_good_green.png");   break;      // green
//                    case 4: _image_good = new ProcessingImage_base(applet, "image_good_phthalogreen.png");   break;      // phthalogreen
//                    case 5: _image_good = new ProcessingImage_base(applet, "image_good_blue.png");   break;      // blue
//                    case -1: _image_good = new ProcessingImage_base(applet, "image_bead_6.png");   break;
//                }
//
//                _image_good.SetPosition(new Vec2(x, y));
//
//            }
//            else
//            {
//
//                switch (colorId)
//                {
//                    case 0: _image_bubble = new ProcessingImage_base(applet, "image_bead_4.png");   break;      // pink
//                    case 1: _image_bubble = new ProcessingImage_base(applet, "image_bead_0.png");   break;      // red
//                    case 2: _image_bubble = new ProcessingImage_base(applet, "image_bead_2.png");   break;      // yellow
//                    case 3: _image_bubble = new ProcessingImage_base(applet, "image_bead_1.png");   break;      // green
//                    case 4: _image_bubble = new ProcessingImage_base(applet, "image_bead_5.png");   break;      // phthalogreen
//                    case 5: _image_bubble = new ProcessingImage_base(applet, "image_bead_3.png");   break;      // blue
//                    case 6: _image_bubble = new ProcessingImage_base(applet, "image_bead_6.png");   break;
//                }
//
//                _image_bubble.SetPosition(new Vec2(x, y));
//            }
//
//            _box2d.setContinuousPhysics(true);
//            _box2d.setWarmStarting(true);
//            _box2d.world.setContinuousPhysics(true);
//            _box2d.world.setWarmStarting(true);
//        }
//		catch (NullPointerException e)
//		{
//			return false;
//		}
//
//		return true;
//	}
//
//	public void DestroyMover()
//	{
//		body.destroyFixture(fd);
//	}
//
//    public void StartMover(int record_end_time)
//    {
//        _isMoving = true;
//        //_isMoving = false;
//        body.setActive(true);
//
//        _end_time = record_end_time;
//    }
//
//	//------------------------------------------------------------------------------------------------------------------------------------------------------
//	public void PlusMoverRadius(float rad)
//	{
//        if(null == _box2d || null == body)
//            return;
//
//        if(_rad > 150f) return;
//        //if( _rad > Activity_serverMain.Instance._regulation_seekBar_3)
//
//        _rad += rad;
//        Fixture ft = body.getFixtureList();
//        if (ft.getShape() != null)
//            ft.getShape().m_radius += _box2d.scalarPixelsToWorld(rad);
//
//        return;
//	}
//
//	//------------------------------------------------------------------------------------------------------------------------------------------------------
//	public void applyForce(Vector2 _v)
//	{
//		body.applyForce(_v, body.getWorldCenter());
//	}
//
//    public Vec2 GetScreenPosition()
//    {
//        return _box2d.getBodyPixelCoord(body);
//    }
//    //------------------------------------------------------------------------------------------------------------------------------------------------------
//    public Vec2 GetPosition()
//    {
//        return _box2d.getBodyPixelCoord(body);
//    }
//
//	//------------------------------------------------------------------------------------------------------------------------------------------------------
//	public void display(PApplet applet)
//	{
//	    // We look at each _body and get its screen position
//		Vec2 pos = _box2d.getBodyPixelCoord(body);
//
//        if(_type == Type_Smile || _type == Type_Good)
//        {
//
//            int size = (int)(_rad * 2);
//            pos.x -= (size / 2);
//            pos.y -= (size / 2);
//
//            if( _type == Type_Smile)
//            {
//                _image_smile.Display(applet, (int)pos.x, (int)pos.y, size, size);
//            }
//            else if( _type == Type_Good)
//            {
//                _image_good.Display(applet, (int)pos.x, (int)pos.y, size, size);
//            }
//        }
//        else
//        {
////            // Get its angle of rotation
////            float angle_ = body.getAngle();
////            applet.pushMatrix();
////            applet.translate(pos.x, pos.y);
////            applet.rotate(angle_);
////            // _pApplet.fill(150);
////            applet.noFill();
////            applet.stroke(_color);
////            applet.strokeWeight(3);
////            applet.ellipse(0, 0, _rad * 2, _rad * 2);
////
////            // Let's add a line so we can see the rotation
////            // _pApplet.line(0, 0, _rad, 0);
////            applet.popMatrix();
//
//            int size = (int)(_rad * 2);
//            pos.x -= (size / 2);
//            pos.y -= (size / 2);
//
//            _image_bubble.Display(applet, (int)pos.x, (int)pos.y, size, size);
//        }
//    }
//}
