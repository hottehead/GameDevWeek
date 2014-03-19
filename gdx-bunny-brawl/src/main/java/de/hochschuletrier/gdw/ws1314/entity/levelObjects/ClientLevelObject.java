package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EntityState;

/**
 * 
 * @author yannick
 * 
 */
public abstract class ClientLevelObject extends ClientEntity
{
	protected boolean isVisible;
	
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
	public void update(float delta)
	{
	}

	@Override
	public void render()
	{
	}

}
