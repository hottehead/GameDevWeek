package de.hochschuletrier.gdw.ws1314.entity.levelObjects.server;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;

/**
 * 
 * @author yannick
 * 
 */
public class ServerLevelObject extends ServerEntity
{
	protected boolean isVisible;

	@Override
	public void enable()
	{
		this.isVisible = true;
	}

	@Override
	public void disable()
	{
		this.isVisible = false;
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void initialize()
	{
		this.isVisible = true;
	}

	@Override
	public void update(float deltaTime)
	{
	}

	public void setVisibility(boolean b)
	{
		this.isVisible = b;
	}

	public boolean getVisibility()
	{
		return this.isVisible;
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
}
