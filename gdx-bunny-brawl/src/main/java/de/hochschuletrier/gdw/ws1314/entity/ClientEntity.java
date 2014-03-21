package de.hochschuletrier.gdw.ws1314.entity;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

/**
 * 
 * @author ElFapo
 *
 */
public abstract class ClientEntity
{
	public EventType 		activeAction = EventType.IDLE;
	private float 			stateTime;
	private Vector2 		position;
	protected FacingDirection direction;
	private long 			id;
	
	public ClientEntity()
	{
		this.position = new Vector2(0.0f, 0.0f);
		this.id = -1l;
		this.stateTime = 0.0f;
		this.direction = FacingDirection.RIGHT;
	}

	public Vector2 getPosition() 				{ return this.position; }
	public FacingDirection getFacingDirection()	{ return this.direction; }
	public long getID()							{ return id; }
	
	public void setPosition(Vector2 position)	{ this.position = position; }
	public void setFacingDirection(FacingDirection direction)	{ this.direction = direction; }
	public void setID(long id)					{ this.id = id; }
	
	public abstract EntityType getEntityType();
	
	public abstract void enable();
	public abstract void disable();
    public abstract void dispose();
    public void update(float delta){
		stateTime += delta;
	}
    public void enterNewState() {
    	stateTime = 0.0f;
    }
    
    public abstract void doEvent(EventType event);
	
	public abstract void render();
	
	public float getStateTime() {
		return stateTime;
	}
}
