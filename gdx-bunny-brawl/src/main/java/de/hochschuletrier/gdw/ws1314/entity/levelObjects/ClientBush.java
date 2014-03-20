package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;


/**
 * 
 * @author yannick
 *
 */
public class ClientBush extends ClientLevelObject
{
	public ClientBush()
	{
		super();
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityType.ContactMine;
	}
}
