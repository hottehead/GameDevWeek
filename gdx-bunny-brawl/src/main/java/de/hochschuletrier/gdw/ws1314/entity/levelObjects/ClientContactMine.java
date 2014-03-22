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
public class ClientContactMine extends ClientLevelObject
{
	private static final float DURATION_EXPLOSION = 1;

	public ClientContactMine()
	{
		super();
	}

    @Override
    public void doEvent(EventType event) {

    }

	@Override
	public EntityType getEntityType()
	{
		return EntityType.ContactMine;
	}
	
	@Override
	public void setLevelObjectState(EntityStates state) {
		boolean changing = state != entityState ? true : false;
		super.setLevelObjectState(state);
		if( changing && this.getLevelObjectState() == EntityStates.EXPLODING){
			ClientDieEntity entity =  ClientEntityManager.getInstance().createDyingGhost(EntityType.ContactMine, DURATION_EXPLOSION);
			entity.enable();
			entity.setPosition(this.getPosition());
			entity.setLevelObjectState(EntityStates.ATTACK);
			System.out.println("client contact exploding");
		}
	}
	
	public void update(float deltaTime){
		
	}
}
