package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author yannick
 * 
 */
public class ClientEgg extends ClientLevelObject
{

    private static final Logger logger = LoggerFactory.getLogger(ClientEgg.class);

	public ClientEgg()
	{
		super();
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityType.Ei;
	}

    public void setPosition(Vector2 position)	{
        super.setPosition(position);
    }
}
