package de.hochschuletrier.gdw.ws1314.entity.player;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.state.IStateListener;
import de.hochschuletrier.gdw.ws1314.state.State;

/**
 * 
 * @author ElFapo
 *
 */
public class StatePlayerWalking extends State
{
	FacingDirection movingDirection;
	
	Vector2 velocity;
	PhysixBody body;
	
	public StatePlayerWalking(IStateListener owner)
	{
		super(owner);
		movingDirection = FacingDirection.NONE;
		body = null;
	}

	@Override
	public void update(float dt)
	{
		if (body == null)
			return;
		
		ServerPlayer player = (ServerPlayer) getOwner();
		
		Vector2 newVelocity = new Vector2(movingDirection.getDirectionVector().x * player.getPlayerKit().accelerationImpulse * dt,
										  movingDirection.getDirectionVector().y * player.getPlayerKit().accelerationImpulse * dt);
		body.setLinearDamping(0.0f);
		newVelocity.x += body.getLinearVelocity().x;
		newVelocity.y += body.getLinearVelocity().y;
		
		float len = newVelocity.len();
		float maxVel = player.getPlayerKit().getMaxVelocity();
		if (len > maxVel)
		{
			newVelocity.x = newVelocity.x / len * maxVel;
			newVelocity.y = newVelocity.y / len * maxVel;
		}
			
		body.setLinearVelocity(newVelocity);
		
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

	public void setMovingDirection(FacingDirection direction)
	{
		movingDirection = direction;
	}
	
	public void setPhysixBody(PhysixBody body)
	{
		this.body = body;
	}
}
