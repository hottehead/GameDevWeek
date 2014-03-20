package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 *
 */
public class ClientCarrot extends ClientLevelObject
{
	public ClientCarrot()
	{
		super();
	}
	
	@Override
	public EntityType getEntityType()
	{
		return EntityType.Carrot;
	}

}
