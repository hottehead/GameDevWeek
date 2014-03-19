package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 *
 */
public class ServerContactMine extends ServerLevelObject
{
	public ServerContactMine()
	{
		super();
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
	}
	
	@Override
	public void beginContact(Contact contact)
	{
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
		return EntityType.ContactMine;
	}

	@Override
	public void initPhysics(PhysixManager manager)
	{
		
	}

}
