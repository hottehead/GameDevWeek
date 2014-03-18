package de.hochschuletrier.gdw.ws1314.entity.levelObjects.server;

import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;

/**
 * 
 * @author yannick
 * 
 */
public class ServerLevelObject extends ServerEntity
{
	protected boolean isVisible;

	@Override
	public void enable()
	{
	}

	@Override
	public void disable()
	{
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void initialize()
	{
		this.isVisible = true;
	}

	@Override
	public void update(float deltaTime)
	{
	}

	public void setVisibility(boolean b)
	{
		this.isVisible = b;
	}

	public boolean getVisibility()
	{
		return this.isVisible;
	}
}
