package de.hochschuletrier.gdw.ws1314.game;

import java.awt.List;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

/**
 * 
 * @author Santo Pfingsten
 */
public class Ball extends PhysixEntity implements ContactListener {

	private final Vector2 origin = new Vector2();
	private float radius;
	private final float maxVelocity;

	public Ball(float x, float y, float radius) {
		origin.set(x, y);
		this.radius = radius;
		maxVelocity = 100;
	}

	public void initPhysics(PhysixManager manager) {

		PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager)
				.position(origin).fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
				.friction(0.5f).restitution(0.4f).shapeCircle(100));
		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);

	}

	public void update() {

		if (physicsBody.getPosition().x + 50 >= Gdx.graphics.getWidth()) {
			physicsBody.setLinearDamping(10);
			//System.out.println(physicsBody.getLinearVelocity().len());
			if(physicsBody.getLinearVelocity().len() == 0){
				physicsBody.applyImpulse(-10, 0);;
			}
		} else if (physicsBody.getPosition().x <= 0) {

			physicsBody.setPosition(Gdx.graphics.getWidth(),physicsBody.getPosition().y);

		} else if (physicsBody.getPosition().y >= Gdx.graphics.getHeight()) {

			physicsBody.setPosition(physicsBody.getPosition().x, 0);

		} else if (physicsBody.getPosition().y <= 0) {

			physicsBody.setPosition(physicsBody.getPosition().x,
					Gdx.graphics.getHeight());

		} else {
			physicsBody.setLinearDamping(1);
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				physicsBody.applyImpulse(-10, 0);
			} else if (Gdx.input.isKeyPressed(Keys.UP)) {
				physicsBody.applyImpulse(0, -10);
			} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				physicsBody.applyImpulse(0, 10);
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				physicsBody.applyImpulse(10, 0);
			} else {
				physicsBody.setLinearDamping(1);
			}
		}

		Vector2 linearVelocity = physicsBody.getLinearVelocity();

		if (linearVelocity.len() > maxVelocity) {
			physicsBody.setLinearVelocity(linearVelocity.cpy().scl(
					maxVelocity / linearVelocity.len()));

			physicsBody.setLinearVelocity(linearVelocity.cpy().scl(
					maxVelocity / linearVelocity.len()));
		}
		/*
		 * if(physicsBody.getPosition().x >= Gdx.graphics.getWidth()/2){
		 * physicsBody.applyImpulse(-12,0); }else{
		 * physicsBody.applyImpulse(12,0); }
		 */
	}


	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		PhysixBody bodyA = (PhysixBody)contact.getFixtureA().getBody().getUserData();
		PhysixBody bodyB = (PhysixBody)contact.getFixtureB().getBody().getUserData();
		if(bodyB.getOwner() instanceof Ball){
			bodyA.scale(0.5f);
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		PhysixBody bodyA = (PhysixBody)contact.getFixtureA().getBody().getUserData();
		PhysixBody bodyB = (PhysixBody)contact.getFixtureB().getBody().getUserData();
		if(bodyB.getOwner() instanceof Ball){
			bodyA.scale(2f);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
}
