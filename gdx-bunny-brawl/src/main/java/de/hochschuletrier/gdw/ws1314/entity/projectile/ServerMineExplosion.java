package de.hochschuletrier.gdw.ws1314.entity.projectile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerLevelObject;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;


public class ServerMineExplosion extends ServerLevelObject{
	
	//==================================================
	// VARIABLES
	private long sourceID;

	private Vector2 		originPosition;
	private float 			despawnTime;
	private float			damage;
	private float			hitCircleRadius;
	
	private boolean 		physicsInitialized;
	
	private static final Logger logger = LoggerFactory.getLogger(ServerMineExplosion.class);


	//==================================================
	public ServerMineExplosion() {
		super();
		sourceID = -1;
		this.originPosition = ServerEntityManager.getInstance().getEntityById(sourceID).getPosition();
		this.despawnTime = 0.0f;
		this.damage = 80.0f;
		this.hitCircleRadius = 30.0f;
		this.physicsInitialized = false;
	}
	
	public void setDamage(float dmg) {
		damage = dmg;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public void setPhysicalParameters(float despawnTime) {
		this.despawnTime = despawnTime;
	}
	
	public void setHitCircleRadius(float radius) {
		hitCircleRadius = radius;
	}
	
	public void setSource(long sourceID) {
		this.sourceID = sourceID;
		ServerPlayer player = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(sourceID);
		this.originPosition = player.getPosition();
		
	}

	

	
	public void setDespawnTime(float despawnTime) {
		this.despawnTime = despawnTime;
	}
	
	public float getDespawnTime() {
		return this.despawnTime;
	}

	public long getSourceID() {
		return sourceID;
	}

	@Override
	public void beginContact(Contact contact) {
		ServerEntity otherEntity = this.identifyContactFixtures(contact);
		
		if(otherEntity == null) {
			return;
		}
		
		switch(otherEntity.getEntityType()) {
			case Hunter:
			case Tank:
			case Knight:
			case Noob:
				ServerPlayer player = (ServerPlayer) otherEntity;
				player.applyDamage(damage);
				break;
			default:
				break;
		}
	}

	public void endContact(Contact contact) {
	}

	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	public void enable() {}
	public void disable() {}
	public void dispose() {}
	public void initialize() {}
	public void reset() {}

	public void update(float deltaTime) {
		if (!physicsInitialized)
			return;
		
		Vector2 pos = this.physicsBody.getPosition().cpy();
		float distance = pos.sub(originPosition).len();
		
	}

	@Override
	public EntityType getEntityType(){
		return EntityType.Projectil;
	}

	@Override
	public void initPhysics(PhysixManager manager){
		this.originPosition = new Vector2(properties.getFloat("x"), properties.getFloat("y"));
		
		PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager)
								.position(this.originPosition)
								.fixedRotation(true)
								.create();
		
		body.createFixture(new PhysixFixtureDef(manager)
								.density(0.5f)
								.friction(0.0f)
								.restitution(0.0f)
								.shapeCircle(hitCircleRadius)
								.sensor(true));
		
		body.setGravityScale(0);
		body.addContactListener(this);

		setPhysicsBody(body);

		physicsInitialized = true;
	}
}

