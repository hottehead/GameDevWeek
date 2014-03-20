package de.hochschuletrier.gdw.ws1314.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;

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
            PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager)
                .position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
                .fixedRotation(false).create();

            body.createFixture(new PhysixFixtureDef(manager)
                .density(0.5f)
                .friction(0.0f)
                .restitution(0.0f)
                .shapeBox(this.properties.getFloat("width"), this.properties.getFloat("height"))
                .sensor(true));

            body.setGravityScale(0);
            body.addContactListener(this);
            setPhysicsBody(body);
	}
}
