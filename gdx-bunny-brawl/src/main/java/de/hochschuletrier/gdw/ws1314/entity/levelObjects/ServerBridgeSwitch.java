package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
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
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;
import de.hochschuletrier.gdw.ws1314.game.ServerGame;

import java.util.ArrayList;

/**
 * 
 * @author yannick
 * 
 */

public class ServerBridgeSwitch extends ServerLevelObject
{
    private ArrayList<Long> targetIDs;
	private static final Logger logger = LoggerFactory.getLogger(ServerBridgeSwitch.class);

	public ServerBridgeSwitch()	{
		super();
        targetIDs = new ArrayList<Long>();
	}
	
	public void pushSwitch()
	{
	    for(long id : targetIDs) {
	        ServerBridge bridge = (ServerBridge) ServerEntityManager.getInstance().getEntityById(id);
	        bridge.setVisibility(!bridge.getVisibility());
	    }
	}

	public void initialize()
	{
		super.initialize();
	}

    public void addTargetID(Long id){
        targetIDs.add(id);
    }

	@Override
	public void setVisibility(boolean b) {
		super.setVisibility(b);
		NetworkManager.getInstance().sendEntityEvent(getID(), EventType.SWITCH);
	}

	@Override
	public void beginContact(Contact contact)
	{
		 ServerEntity otherEntity = this.identifyContactFixtures(contact);

	        switch(otherEntity.getEntityType()) {
	            case SwordAttack:
	            case Projectil:
                    for(Long targetID : targetIDs) {
                        ServerBridge bridge = (ServerBridge) ServerEntityManager.getInstance().getEntityById(targetID);
                        bridge.setVisibility(!bridge.getVisibility());
                    }
	                break;
	            default:
	                break;
	        }
	        
	}

	@Override
	public void endContact(Contact contact)
	{
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityType.BridgeSwitch;
	}

	@Override
	public void initPhysics(PhysixManager manager)
	{
		// TODO Auto-generated method stub
		PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, manager)
									.position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
									.fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager)
									.density(0.5f).friction(0.0f)
									.restitution(0.0f).shapeBox(50,50)
                                    .sensor(true));

		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
		
	}

    @Override
    public void update(float deltaTime) {
        // TODO Auto-generated method stub
        
    }
}
