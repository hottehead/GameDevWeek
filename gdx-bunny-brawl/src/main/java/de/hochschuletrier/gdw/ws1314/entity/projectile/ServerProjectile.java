package de.hochschuletrier.gdw.ws1314.entity.projectile;

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
import de.matthiasmann.twlthemeeditor.gui.CollapsiblePanel.Direction;

/**
 * 
 * @author Sonic
 *
 */

public class ServerProjectile extends ServerEntity {
    
    //==================================================
    // CONSTANTS
    private static final float speed = 2.0f;
    private static final float flightDistance = 50.0f;

    //==================================================
    // VARIABLES
    private final long sourceID;

    private final FacingDirection facingDirection;
    private final TeamColor teamColor;
    private final Vector2 originPosition;

    //==================================================
    public ServerProjectile(long sourceID) {
            super();

            this.sourceID = sourceID;

            ServerPlayer player = (ServerPlayer)ServerEntityManager.getInstance().getEntityById(sourceID);

            this.teamColor = player.getTeamColor();
            this.facingDirection = player.getFacingDirection();
            this.originPosition = player.getPosition();
    }
	
//	public float getSpeed() {
//		return speed;
//	}
//	public void setSpeed(float speed) {
//		this.speed = speed;
//	}

	public FacingDirection getFacingDirection() {
		return this.facingDirection;
	}
//	public void setFacingDirection(FacingDirection direction) {
//		this.facingDirection = direction;
//	}

	public TeamColor getTeamColor() {
		return teamColor;
	}
//	public void setTeamColor(TeamColor teamColor) {
//		this.teamColor = teamColor;
//	}

//	public float getFlightDistance() {
//		return flightDistance;
//	}
//	public void setFlightDistance(float flightDistance) {
//		this.flightDistance = flightDistance;
//	}

	public long getSourceID() {
		return sourceID;
	}
//	public void setSourceID(long sourceID) {
//		this.sourceID = sourceID;
//	}

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
                    if(player.getTeamColor() != this.teamColor) {
                        ServerEntityManager.getInstance().removeEntity(this);
                    }
                    break;
                default:
                    break;
            }
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	@Override
	public void enable() {
	}

	@Override
	public void disable() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void initialize() {

	}

	@Override
	public void update(float deltaTime) {
            Vector2 position = this.physicsBody.getPosition();
            float distance = this.originPosition.sub(position).len();
            if(distance > this.flightDistance) {
                ServerEntityManager.getInstance().removeEntity(this);
            }
	}

	@Override
	public EntityType getEntityType(){
		return EntityType.Projectil;
	}

	@Override
	public void initPhysics(PhysixManager manager){
            PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager).position(this.originPosition).fixedRotation(true).create();
            body.createFixture(new PhysixFixtureDef(manager).density(0.5f).friction(0.0f).restitution(0.0f).shapeCircle(30));
            body.setGravityScale(0);
            body.addContactListener(this);
            
            Vector2 impulse = this.facingDirection.getDirectionVector().cpy().scl(this.speed);
            body.applyImpulse(impulse);
            
            setPhysicsBody(body);
	}
	
}
