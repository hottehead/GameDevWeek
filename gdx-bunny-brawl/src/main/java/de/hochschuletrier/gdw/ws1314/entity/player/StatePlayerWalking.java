package de.hochschuletrier.gdw.ws1314.entity.player;

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
	
	public StatePlayerWalking(IStateListener owner)
	{
		super(owner);
		movingDirection = FacingDirection.NONE;
	}

	@Override
	public void update(float dt)
	{
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
}
