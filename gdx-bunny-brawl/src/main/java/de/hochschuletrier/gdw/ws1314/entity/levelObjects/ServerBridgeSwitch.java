package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

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

/**
 * 
 * @author yannick
 * 
 */

public class ServerBridgeSwitch extends ServerLevelObject
{
	private long targetID;

	public ServerBridgeSwitch()
	{
		super();
	}
	
	// Note: Switch pushing must be solved by collision reaction
//	public void pushSwitch()
//	{
//		//bridge.setVisibility(!bridge.getVisibility());
//	}

	public void initialize()
	{
		super.initialize();
	}

	@Override
	public void beginContact(Contact contact)
	{
		 ServerEntity otherEntity = this.identifyContactFixtures(contact);

	        switch(otherEntity.getEntityType()) {
	            case Tank:
	            case Hunter:
	            case Knight:
	            case Noob:
	            	ServerEntityManager.getInstance().getEntityById(targetID);
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
	
	public long getTargetID()
	{
		return targetID;
	}
	
	public void setTargetID(long targetID)
	{
		this.targetID = targetID;
	}

	@Override
	public void initPhysics(PhysixManager manager)
	{
		// TODO Auto-generated method stub
		PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager)
									.position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
									.fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager)
									.density(0.5f).friction(0.0f)
									.restitution(0.0f).shapeBox(50,50));

		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
		
	}
}
