package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;

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
    public void doEvent(EventType event) {

    }
	
	@Override
	public EntityType getEntityType()
	{
		return EntityType.Spinach;
	}

}
