package de.hochschuletrier.gdw.ws1314.entity.levelObjects.server;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

/**
 * 
 * @author yannick
 * 
 */
public class Bridge extends ServerLevelObject
{
	@Override
	public void initialize()
	{
		super.initialize();
		Bridge.type = EntityType.Bridge;
	}
}
