package de.hochschuletrier.gdw.ws1314.entity;

import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;

/**
 * 
 * @author ElFapo
 *
 */
public abstract class ServerEntity extends PhysixEntity
{
	private long 	id = -1;
    private SafeProperties properties;

    public static EntityType type;
	
	public ServerEntity(){
		super();
	}

    protected void setId(long id){
        if(this.id == -1){
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

    public void setProperties(SafeProperties properties){
        this.properties = properties;
    }
}
