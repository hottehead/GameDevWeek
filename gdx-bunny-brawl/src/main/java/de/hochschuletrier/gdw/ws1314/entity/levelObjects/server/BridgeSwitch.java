package de.hochschuletrier.gdw.ws1314.entity.levelObjects.server;

/**
 * 
 * @author yannick
 * 
 */
public class BridgeSwitch extends ServerLevelObject
{
	private Bridge bridge;

	public void pushSwitch()
	{
		bridge.setVisibility(!bridge.getVisibility());
	}

	public void initialize(Bridge bridge)
	{
		super.initialize();

		this.bridge = bridge;
	}
}
