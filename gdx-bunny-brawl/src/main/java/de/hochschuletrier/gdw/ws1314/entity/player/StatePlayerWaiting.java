package de.hochschuletrier.gdw.ws1314.entity.player;

import de.hochschuletrier.gdw.ws1314.state.IStateListener;
import de.hochschuletrier.gdw.ws1314.state.State;

/**
 * 
 * @author ElFapo
 *
 */
public class StatePlayerWaiting extends State
{
	private State 		waitFinishedState;
	
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
		if(timer >= waitTime && waitFinishedState != null)
			getOwner().switchToState(waitFinishedState);
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
	
	public void setWaitFinishedState(State state)
	{
		waitFinishedState = state;
	}

}
