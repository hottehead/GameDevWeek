package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 * 
 */
public class ClientBridge extends ClientLevelObject
{
	public ClientBridge()
	{
		super();
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityType.Bridge;
	}
}
