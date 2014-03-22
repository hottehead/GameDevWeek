package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;

/**
 * Created by Jerry on 22.03.14.
 */
public class ClientDieEntity extends ClientLevelObject {

	private  EntityType type;
	private  float dyingTime;


	public float getDyingTime() {
		return dyingTime;
	}

	public void setDyingTime(float dyingTime) {
		this.dyingTime = dyingTime;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		dyingTime -= delta;
	}

	@Override
	public EntityType getEntityType() {
		return type;
	}

	public void setEntityType(EntityType type)	{
		this.type = type;
	}

	@Override
	public void doEvent(EventType event) {

	}
}
