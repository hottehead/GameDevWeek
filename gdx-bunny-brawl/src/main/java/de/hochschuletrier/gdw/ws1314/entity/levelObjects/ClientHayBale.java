package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;

/**
 * Created by Jerry on 22.03.14.
 */
public class ClientHayBale extends ClientLevelObject{
	public ClientHayBale()
	{
		super();
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityType.HayBale;
	}

	@Override
	public void doEvent(EventType event) {

	}

	public void setPosition(Vector2 position)	{
		super.setPosition(position);
	}
}
