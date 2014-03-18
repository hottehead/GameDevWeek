package de.hochschuletrier.gdw.ws1314.entity.levelObjects.server;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 * 
 */
public class Egg extends ServerLevelObject
{
	@Override
	public void initialize()
	{
		super.initialize();
		Egg.type = EntityType.Ei;
	}
}
