package de.hochschuletrier.gdw.ws1314.entity;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;

/**
 * 
 * @author ElFapo
 *
 */
public abstract class ServerEntity extends PhysixEntity implements ContactListener
{
	private long 	id = -1;
    protected SafeProperties properties;
	
	public ServerEntity()
	{
		super();
	}

    protected void setId(long id)
    {
        if(this.id == -1)
        {
            this.id = id;
        }
    }
	
	public long getID()
	{
		return this.id;
	}
	
	public abstract void enable();
	public abstract void disable();
    public abstract void dispose();
    public abstract void initialize();

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
            } else {
                return entityA;
            }
            
        } catch(Exception e) {
            
        }
        
        return null;
    }
    
}
