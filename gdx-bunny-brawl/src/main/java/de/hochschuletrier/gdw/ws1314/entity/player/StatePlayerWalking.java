package de.hochschuletrier.gdw.ws1314.entity.player;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.state.IStateListener;

/**
 * 
 * @author ElFapo
 *
 */
public class StatePlayerWalking extends StatePlayer
{
	private FacingDirection movingDirection;
	
	private Vector2 		velocity;
	private PhysixBody 		body;
	
	private float			speedFactor;
	
	public StatePlayerWalking(IStateListener owner)
	{
		super(owner);
		movingDirection = FacingDirection.NONE;
		body = null;
		speedFactor = 1.0f;
	}

	@Override
	public void update(float dt)
	{
		if (body == null)
			return;
		
		//===============
		
//		ServerPlayer player = (ServerPlayer) getOwner();
//		
//		Vector2 newVelocity = new Vector2(movingDirection.getDirectionVector().x * player.getPlayerKit().accelerationImpulse * dt,
//										  movingDirection.getDirectionVector().y * player.getPlayerKit().accelerationImpulse * dt);
//		body.setLinearDamping(0.0f);
//		newVelocity.x += body.getLinearVelocity().x;
//		newVelocity.y += body.getLinearVelocity().y;
//		
//		float len = newVelocity.len();
//		float maxVel = player.getPlayerKit().getMaxVelocity() * speedFactor;
//		if (len > maxVel)
//		{
//			newVelocity.x = newVelocity.x / len * maxVel;
//			newVelocity.y = newVelocity.y / len * maxVel;
//		}
//			
//		body.setLinearVelocity(newVelocity);
		
		//===============
		ServerPlayer player = (ServerPlayer) getOwner();
		Vector2 directionV = movingDirection.getDirectionVector();
		float maxVel = player.getPlayerKit().getMaxVelocity();
		
		body.applyImpulse(directionV.x * maxVel, directionV.y * maxVel);
		
		Vector2 vel = body.getLinearVelocity();
		vel.clamp(0, maxVel);
		
		body.setLinearVelocity(vel);
	}

	@Override
	public void init()
	{
		((ServerPlayer) getOwner()).moveBegin(movingDirection);
	}

	@Override
	public void exit()
	{
		((ServerPlayer) getOwner()).moveEnd();
	}

	public EntityStates getCurrentState(){return EntityStates.WALKING;}

	public void setMovingDirection(FacingDirection direction)
	{
		movingDirection = direction;
	}
	
	public void setPhysixBody(PhysixBody body)
	{
		this.body = body;
	}
	
	public void setSpeedFactor(float speedFactor)
	{
		this.speedFactor = speedFactor;
	}
}
