package de.hochschuletrier.gdw.ws1314.entity.levelObjects.client;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;


/**
 * 
 * @author yannick
 *
 */
public class Bush extends ClientLevelObject
{
	public Bush(Vector2 position, long id)
	{
		super(position, id);
		Bush.type = EntityType.Bush;
	}
}
