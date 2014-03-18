package de.hochschuletrier.gdw.ws1314.entity.levelObjects.server;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 * 
 */
public class BridgeSwitch extends ServerLevelObject
{
	private Bridge bridge;

	public void pushSwitch()
	{
		bridge.setVisibility(!bridge.getVisibility());
	}

	public void initialize(Bridge bridge)
	{
		super.initialize();

		this.bridge = bridge;
		BridgeSwitch.type = EntityType.BridgeSwitch;
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
