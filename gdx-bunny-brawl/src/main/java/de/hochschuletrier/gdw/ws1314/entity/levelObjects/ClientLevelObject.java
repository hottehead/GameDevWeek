package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;

/**
 * 
 * @author yannick
 * 
 */
public abstract class ClientLevelObject extends ClientEntity
{
	protected boolean isVisible;
	protected EntityStates entityState;
	
	public ClientLevelObject()
	{
		super();
	}

	@Override
	public void enable()
	{
		this.isVisible = true;
	}

	@Override
	public void disable()
	{
		this.isVisible = false;
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void update(float delta){
		super.update(delta);
	}

	@Override
	public void render()
	{
	}

	public EntityStates getLevelObjectState(){return entityState;}
	public void setLevelObjectState(EntityStates state){if(this.entityState!=state) { entityState = state; this.stateTime=0.0f; } }
}
