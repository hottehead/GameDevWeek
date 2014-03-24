package de.hochschuletrier.gdw.ws1314.entity;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

/**
 * 
 * @author ElFapo
 *
 */
public abstract class ServerEntity extends PhysixEntity implements ContactListener
{
	private long 	id = -1;
    protected SafeProperties properties;
    
    private FacingDirection facingDirection;
	
	public ServerEntity()
	{
		super();
		facingDirection = FacingDirection.NONE;
	}

    protected void setId(long id)
    {
        if(this.id == -1)
        {
            this.id = id;
        }
    }
	
    public long getID() {
        return this.id;
    }
	
	public abstract void enable();
	public abstract void disable();
        
    public void dispose(PhysixManager manager) {
        
        if(physicsBody != null) {
              manager.destroy(physicsBody);
        }
        
    }   
    
    public abstract void initialize();
    public abstract void reset();

	public abstract void update(float deltaTime);
	
	public abstract EntityType getEntityType();

    public void setProperties(SafeProperties properties){
        this.properties = properties;
    }
    
    /**
    * method used by the beginContact method of ServerEntity-Implementations
    * to identify the collided Object
    */
    protected ServerEntity identifyContactFixtures(Contact contact) {
        
        PhysixBody bodyA = (PhysixBody)contact.getFixtureA().getBody().getUserData();
        PhysixBody bodyB = (PhysixBody)contact.getFixtureB().getBody().getUserData();
        
        try {
            ServerEntity entityA = (ServerEntity)bodyA.getOwner();
            ServerEntity entityB = (ServerEntity)bodyB.getOwner();
            
            if(entityA.getID() == this.getID()) {
                return entityB;
            } else if(entityB.getID() == this.getID()) {
                return entityA;
            }
            
        } catch(Exception e) {
            
        }
        
        return null;
    }
    
    protected Fixture getCollidingFixture(Contact contact) {
    	Fixture fixtureA = contact.getFixtureA();
    	Fixture fixtureB = contact.getFixtureB();
    	PhysixBody bodyA = (PhysixBody)fixtureA.getBody().getUserData();
        PhysixBody bodyB = (PhysixBody)fixtureB.getBody().getUserData();
        ServerEntity entityA = (ServerEntity)bodyA.getOwner();
        ServerEntity entityB = (ServerEntity)bodyB.getOwner();
        
        if (entityA != null && entityA.getID() == this.getID())
        	return fixtureA;
        if (entityB != null && entityB.getID() == this.getID())
        	return fixtureB;
        return null;
    }
    
    protected Fixture getOppoosingCollidingFixture(Contact contact) {
    	Fixture fixtureA = contact.getFixtureA();
    	Fixture fixtureB = contact.getFixtureB();
    	PhysixBody bodyA = (PhysixBody)fixtureA.getBody().getUserData();
        PhysixBody bodyB = (PhysixBody)fixtureB.getBody().getUserData();
        ServerEntity entityA = (ServerEntity)bodyA.getOwner();
        ServerEntity entityB = (ServerEntity)bodyB.getOwner();
        
        if (entityA != null && entityA.getID() != this.getID())
        	return fixtureA;
        if (entityB != null && entityB.getID() != this.getID())
        	return fixtureB;
        return null;
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
    
    
    public FacingDirection getFacingDirection() {
    	return facingDirection;
    }
    
    public void setFacingDirection(FacingDirection direction) {
    	this.facingDirection = direction;
    }
}
