package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;

/**
 * 
 * @author yannick
 * 
 */
public class ServerContactMine extends ServerLevelObject {
	private final float DURATION_TILL_EXPLOSION_MAX = 3.0f;
	private final float DURATION_TILL_EXPLOSION_MIN = 1.0f;

	private final float DURATION_EXPLOSION = 1.0f;
	private boolean isActive = false;
	private float timer;
	private long sourceID;

	public ServerContactMine() {
		super();
		sourceID = -1;
	}

	public long getSourceID() {
		return sourceID;
	}

	@Override
	public void initialize() {
		super.initialize();
		timer = MathUtils.random(DURATION_TILL_EXPLOSION_MIN, DURATION_TILL_EXPLOSION_MAX);;
	}

	@Override
	public void beginContact(Contact contact) {
		ServerEntity otherEntity = this.identifyContactFixtures(contact);

		switch (otherEntity.getEntityType()) {
		case Hunter:
		case Knight:
		case Tank:
		case Noob:
		case HayBale:
			this.isActive = true;

		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	public void update(float deltaTime) {
		Vector2 pos = this.physicsBody.getPosition().cpy();
		if (isActive) {
			entityState = EntityStates.ATTACK;
			timer -= deltaTime;
			System.out.println("Time left: " + timer);
			if (timer <= 0) {
				System.out.println(" server mine exploding");
				isActive = false;
				entityState = EntityStates.EXPLODING;
				timer = MathUtils.random(DURATION_TILL_EXPLOSION_MIN, DURATION_TILL_EXPLOSION_MAX);
				this.physicsBody.getBody().getFixtureList().get(1);
			}
		} else {
			entityState = EntityStates.NONE;
		}
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.ContactMine;
	}

	@Override
	public void initPhysics(PhysixManager manager) {
		PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.KinematicBody,
				manager)
				.position(
						new Vector2(properties.getFloat("x"), properties
								.getFloat("y"))).fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
				.friction(0.0f).sensor(true).restitution(0.0f).shapeBox(10, 10));
		body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
				.friction(0.0f).sensor(true).restitution(0.0f).shapeCircle(2));
		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
	}

}
