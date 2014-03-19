package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 *
 */
public class ClientContactMine extends ClientLevelObject
{
	public ClientContactMine()
	{
		super();
	}
	
	@Override
	public EntityType getEntityType()
	{
		return EntityType.ContactMine;
	}

}
