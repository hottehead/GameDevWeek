package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 *
 */
public class ClientClover extends ClientLevelObject
{
	public ClientClover()
	{
		super();
	}
	
	@Override
	public EntityType getEntityType()
	{
		return EntityType.Clover;
	}

}
