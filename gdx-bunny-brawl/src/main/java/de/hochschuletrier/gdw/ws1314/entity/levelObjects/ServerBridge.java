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

/**
 * 
 * @author yannick
 * 
 */
public class ServerBridge extends ServerLevelObject
{
	/* FIXME:
	 * Comment: von Fabio Gimmillaro (Der komische Typ ganz hinten rechts)
	 * Bridge braucht ID, damit man einer Brücke bestimmte Schalter hinzufügen kann
	 * Ich muss auch in ServerBridgeSwitch darauf zugreifen können
	 * also bitte noch Getter einfügen oder public setzen mir egal ^^
	 * 
	 * private final long ID;
	 * public ServerBridge(long ID)
	 * {
	 * 		this.ID = ID;
	 * }
	 * 
	*/
	
	public ServerBridge(long ID)
	{
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
		return EntityType.Bridge;
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
									.restitution(0.0f).shapeBox(100,200));
		
		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
		
	}
}
