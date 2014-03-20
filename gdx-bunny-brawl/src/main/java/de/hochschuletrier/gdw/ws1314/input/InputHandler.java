package de.hochschuletrier.gdw.ws1314.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ws1314.game.ClientServerConnect;

/**
 * 
 * @author yannick
 *
 */
public class InputHandler implements InputProcessor
{
	private ClientServerConnect netManager;
	
	public InputHandler()
	{
		netManager = ClientServerConnect.getInstance();
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		switch (keycode) 
		{
			case (Input.Keys.A):
				netManager.sendAction(PlayerIntention.MOVE_LEFT_ON);
				return true;
			case (Input.Keys.S):
				netManager.sendAction(PlayerIntention.MOVE_DOWN_ON);
				return true;
			case (Input.Keys.D):
				netManager.sendAction(PlayerIntention.MOVE_RIGHT_ON);
				return true;
			case (Input.Keys.W):
				netManager.sendAction(PlayerIntention.MOVE_UP_ON);
				return true;
			case (Input.Keys.SPACE):
				netManager.sendAction(PlayerIntention.ATTACK_1);
				return true;
			case (Input.Keys.E):
				netManager.sendAction(PlayerIntention.DROP_EGG);
				return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		switch (keycode) 
		{
			case (Input.Keys.A):
				netManager.sendAction(PlayerIntention.MOVE_LEFT_OFF);
				return true;
			case (Input.Keys.S):
				netManager.sendAction(PlayerIntention.MOVE_DOWN_OFF);
				return true;
			case (Input.Keys.D):
				netManager.sendAction(PlayerIntention.MOVE_RIGHT_OFF);
				return true;
			case (Input.Keys.W):
				netManager.sendAction(PlayerIntention.MOVE_UP_OFF);
				return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}

}
