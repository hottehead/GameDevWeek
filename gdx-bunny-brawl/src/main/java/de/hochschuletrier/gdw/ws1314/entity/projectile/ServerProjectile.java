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
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

/**
 * 
 * @author Sonic
 *
 */

//
// Modified by ElFapo

public class ServerProjectile extends ServerEntity {
    
    //==================================================
    // VARIABLES
    private long sourceID;

    private FacingDirection facingDirection;
    private TeamColor 		teamColor;
    private Vector2 		originPosition;
    private float 			velocity;
    private float 			flightDistance;
    private float 			despawnTime;
    private float			damage;
    
    private boolean physicsInitialized;
    
    private static final Logger logger = LoggerFactory.getLogger(ServerProjectile.class);


    //==================================================
    public ServerProjectile() {
            super();
            
            sourceID = -1;
            this.teamColor = TeamColor.BOTH;
            this.facingDirection = FacingDirection.NONE;
            this.originPosition = FacingDirection.NONE.getDirectionVector();
            this.velocity = 0.0f;
            this.flightDistance = 0.0f;
            this.despawnTime = 0.0f;
            this.physicsInitialized = false;
    }
    
    public void setDamage(float dmg) {
    	damage = dmg;
    }
    
    public float getDamage() {
    	return damage;
    }
	
	public void setPhysicalParameters(float velocity, float distance, float despawnTime) {
		this.velocity = velocity;
		this.flightDistance = distance;
		this.despawnTime = despawnTime;
	}
	
	public void setSource(long sourceID) {
		this.sourceID = sourceID;

        ServerPlayer player = (ServerPlayer) ServerEntityManager.getInstance().getEntityById(sourceID);

        this.teamColor = player.getTeamColor();
        this.facingDirection = player.getFacingDirection();
        this.originPosition = player.getPosition();
	}

	public FacingDirection getFacingDirection() {
		return this.facingDirection;
	}

	public TeamColor getTeamColor() {
		return teamColor;
	}

	public float getFlightDistance() {
		return flightDistance;
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
                case Tank:
                case Hunter:
                case Knight:
                case Noob:
                    ServerPlayer player = (ServerPlayer)otherEntity;
                    if (player.getID() == sourceID)
                    	return;
                    if (player.getTeamColor() != this.teamColor)
                    	player.applyDamage(damage);
                    ServerEntityManager.getInstance().removeEntity(this);
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
		
		logger.info(getPosition().x + " " + getPosition().y + " - " + originPosition.x + " " + originPosition.y);
		
        Vector2 pos = this.physicsBody.getPosition().cpy();
        float distance = pos.sub(originPosition).len();
        if(distance > this.flightDistance) {
            ServerEntityManager.getInstance().removeEntity(this);
            logger.info("P O W !!!");
        }
	}

	@Override
	public EntityType getEntityType(){
		return EntityType.Projectil;
	}

	@Override
	public void initPhysics(PhysixManager manager){
            this.originPosition = new Vector2(properties.getFloat("x"), properties.getFloat("y"));
            float angle = this.facingDirection.getDirectionVector().getAngleRad();
                
            PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager)
                    .position(this.originPosition)
                    .fixedRotation(true)
                    .angle(angle)
                    .create();
            
            body.createFixture(new PhysixFixtureDef(manager)
                    .density(0.5f)
                    .friction(0.0f)
                    .restitution(0.0f)
                    .shapeCircle(30)
                    .sensor(true));
            
            body.setGravityScale(0);
            body.addContactListener(this);

            setPhysicsBody(body);

            Vector2 vel = new Vector2(	facingDirection.getDirectionVector().x * velocity,
                                                                            facingDirection.getDirectionVector().y * velocity);
            physicsBody.setLinearVelocity(vel);

            physicsInitialized = true;
	}	
}
