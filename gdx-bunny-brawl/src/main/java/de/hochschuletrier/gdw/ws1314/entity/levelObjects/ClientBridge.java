package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;

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

    private EntityType type = EntityType.Bridge;

    public void setHorizontalLeft()
    {
        type = EntityType.BRIDGE_HORIZONTAL_LEFT;
    }
    public void setHorizontalMiddle()
    {
        type = EntityType.BRIDGE_HORIZONTAL_MIDDLE;
    }
    public void setHorizontalRight()
    {
        type = EntityType.BRIDGE_HORIZONTAL_RIGHT;
    }
    public void setVerticalBottom()
    {
        type = EntityType.BRIDGE_VERTICAL_BOTTOM;
    }
    public void setVerticalMiddle()
    {
        type = EntityType.BRIDGE_VERTICAL_MIDDLE;
    }
    public void setVerticalTop()
    {
        type = EntityType.BRIDGE_VERTICAL_TOP;
    }


    @Override
    public void doEvent(EventType event) {

    }

	@Override
	public EntityType getEntityType()
	{
		return type;
	}
}
