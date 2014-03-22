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
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerMineExplosion;

/**
 * 
 * @author yannick
 *
 */
public class ServerContactMine extends ServerLevelObject
{
	private final float DURATION_TILL_EXPLOSION = 5.0f;
	private boolean isActive = false;
	private float timer;
	
	public ServerContactMine()
	{
		super();
	}
	
	@Override
	public void initialize()
	{
		super.initialize();
		timer = DURATION_TILL_EXPLOSION;
	}
	
	@Override
	public void beginContact(Contact contact)
	{
		ServerEntity otherEntity = this.identifyContactFixtures(contact);
		
		switch(otherEntity.getEntityType()){
			case Hunter:
			case Knight:
			case Tank:
			case HayBale:
				this.isActive = true;
				
		}
	}
	
	@Override
	public void endContact(Contact contact)
	{
		
	}
	
	public void update(float deltaTime) {
        Vector2 pos = this.physicsBody.getPosition().cpy();
        timer -= deltaTime;
        if(isActive){
        	if(timer <= 0){
        		ServerMineExplosion explosion = new ServerMineExplosion();
        		explosion.setSource(this.getID());
        		isActive = false;
        	}
        }
	}
	
	@Override
	public EntityType getEntityType()
	{
		return EntityType.ContactMine;
	}

	@Override
	public void initPhysics(PhysixManager manager)
	{
		PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, manager)
									.position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
									.fixedRotation(false).create();
		body.createFixture(new PhysixFixtureDef(manager)
									.density(0.5f).friction(0.0f).sensor(true)
									.restitution(0.0f).shapeBox(10,10));
		body.setGravityScale(0);
		body.addContactListener(this);
		setPhysicsBody(body);
	}

    @Override
    public void update(float deltaTime) {
        // TODO Auto-generated method stub
        
    }

}
