package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 *
 */
public class ClientSpinach extends ClientLevelObject
{
	public ClientSpinach()
	{
		super();
	}
	
	@Override
	public EntityType getEntityType()
	{
		return EntityType.Spinach;
	}

}
