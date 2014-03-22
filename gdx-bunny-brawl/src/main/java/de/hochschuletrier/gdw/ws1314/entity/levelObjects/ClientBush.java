package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;


/**
 * 
 * @author yannick
 *
 */
public class ClientBush extends ClientLevelObject
{
	private static final float DYING_ANIMATION_TIME	= 1.0f;

	public ClientBush()
	{
		super();
	}

    @Override
    public void doEvent(EventType event) {

    }

	@Override
	public EntityType getEntityType()
	{
		return EntityType.Bush;
	}

	@Override
	public void dispose() {
		ClientDieEntity e = ClientEntityManager.getInstance().createDyingGhost(EntityType.Bush, DYING_ANIMATION_TIME);
		e.setPosition(getPosition());
		e.enable();
		e.setLevelObjectState(EntityStates.DISPOSE);
		super.dispose();
	}
}
