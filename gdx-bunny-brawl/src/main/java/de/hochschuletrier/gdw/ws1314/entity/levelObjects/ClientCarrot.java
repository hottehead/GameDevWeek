package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;

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
    public void doEvent(EventType event) {

    }

	@Override
	public EntityType getEntityType()
	{
		return EntityType.Carrot;
	}

}
