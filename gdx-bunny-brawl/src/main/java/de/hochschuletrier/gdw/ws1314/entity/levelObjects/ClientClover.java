package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;

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
    public void doEvent(EventType event) {

    }
	
	@Override
	public EntityType getEntityType()
	{
		return EntityType.Clover;
	}

}
