package de.hochschuletrier.gdw.ws1314.entity;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author ElFapo
 *
 */
public abstract class ClientEntity
{
	private Vector2 	position;
	private long 		id;
	
	public ClientEntity()
	{
		this.position = new Vector2(0.0f, 0.0f);
		this.id = -1l;
	}

	public Vector2 getPosition() 				{ return this.position; }
	public long getID()							{ return id; }
	
	public void setPosition(Vector2 position)	{ this.position = position; }
	public void setID(long id)					{ this.id = id; }
	
	public abstract EntityType getEntityType();
	
	public abstract void enable();
	public abstract void disable();
    public abstract void dispose();
    public abstract void update(float delta);
	
	public abstract void render();
}
