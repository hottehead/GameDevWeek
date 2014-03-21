package de.hochschuletrier.gdw.ws1314.entity.player;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.state.IStateListener;

/**
 * 
 * @author ElFapo
 *
 */
public class StatePlayerWaiting extends StatePlayer
{
	private StatePlayer 		waitFinishedState;
	
    private float 		waitTime;
    private float		timer;
	
	public StatePlayerWaiting(IStateListener owner)
	{
		super(owner);
		waitFinishedState = null;
	}

	@Override
	public void update(float dt)
	{
		timer += dt;
		if(timer >= waitTime && waitFinishedState != null){
			getOwner().switchToState(waitFinishedState);
		}
	}

	@Override
	public void init()
	{
		timer = 0.0f;
	}
	
	public void setWaitTime(float waitTime)
	{
		this.waitTime = waitTime;
	}

	@Override
	public void exit()
	{
		((ServerPlayer) getOwner()).moveEnd();
	}

	public EntityStates getCurrentState(){return EntityStates.WAITING;}

	public void setWaitFinishedState(StatePlayer state)
	{
		waitFinishedState = state;
	}

}
