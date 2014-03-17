package de.hochschuletrier.gdw.ws1314.entity;

import de.hochschuletrier.gdw.commons.gdx.physix.*;

/**
 * 
 * @author ElFapo
 *
 */
public abstract class ServerEntity extends PhysixEntity
{
	private long 	id;
	
	public ServerEntity(long id)
	{
		super();
		this.id = id;
	}
	
	public long getID()
	{
		return this.id;
	}
	
	public abstract void enable();
	public abstract void disable();
	public abstract void update(float deltaTime);
}
