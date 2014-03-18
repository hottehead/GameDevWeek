package de.hochschuletrier.gdw.ws1314.entity.levelObjects.client;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EntityState;

/**
 * 
 * @author yannick
 * 
 */
public class ClientLevelObject extends ClientEntity
{
	protected boolean isVisible;
	
	public ClientLevelObject(Vector2 position, long id)
	{
		super(position, id);
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
	public void update(int delta)
	{
	}

	@Override
	public void render()
	{
	}

	@Override
	public void setEntityState(EntityState state)
	{
	}
}
