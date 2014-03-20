package de.hochschuletrier.gdw.ws1314.entity;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

/**
 * 
 * @author yannick
 *
 */
public class Zone extends ServerEntity
{
	private EntityType currentZone;
	
	public void setWaterZone()
	{
		currentZone = EntityType.WaterZone;
	}
	
	public void setAbyssZone()
	{
		currentZone = EntityType.AbyssZone;
	}
	
	public void setGrassZone()
	{
		currentZone = EntityType.GrassZone;
	}
	
	public void setPathZone()
	{
		currentZone = EntityType.PathZone;
	}
	
	public void setStartZone()
	{
		currentZone = EntityType.StartZone;
	}
	
	@Override
	public void enable()
	{
	}

	@Override
	public void disable()
	{
	}

	@Override
	public void initialize()
	{
	}

	@Override
	public void update(float deltaTime)
	{
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
    public void reset()
    {
    }

	@Override
	public EntityType getEntityType()
	{
		return currentZone;
	}

	@Override
	public void initPhysics(PhysixManager manager)
	{
	}
}
