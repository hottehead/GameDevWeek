package de.hochschuletrier.gdw.ws1314.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.ws1314.game.ClientServerConnect;

/**
 * 
 * @author yannick
 *
 */
public class InputHandler extends InputInterceptor
{
	private ClientServerConnect netManager;
	
	public InputHandler(InputProcessor inputProcessor)
	{
		super(inputProcessor);
		netManager = ClientServerConnect.getInstance();
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		switch (keycode) 
		{
			case (Input.Keys.A): {
				netManager.sendAction(PlayerIntention.MOVE_LEFT_ON);
				break;
			}
			case (Input.Keys.S): {
				netManager.sendAction(PlayerIntention.MOVE_DOWN_ON);
				break;
			}
			case (Input.Keys.D): {
				netManager.sendAction(PlayerIntention.MOVE_RIGHT_ON);
				break;
			}
			case (Input.Keys.W): {
				netManager.sendAction(PlayerIntention.MOVE_UP_ON);
				break;
			}
			case (Input.Keys.SPACE): {
				netManager.sendAction(PlayerIntention.ATTACK_1);
				break;
			}
			case (Input.Keys.E): {
				netManager.sendAction(PlayerIntention.DROP_EGG);
				break;
			}
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		switch (keycode) 
		{
			case (Input.Keys.A): {
				netManager.sendAction(PlayerIntention.MOVE_LEFT_OFF);
				break;
			}
			case (Input.Keys.S): {
				netManager.sendAction(PlayerIntention.MOVE_DOWN_OFF);
				break;
			}
			case (Input.Keys.D): {
				netManager.sendAction(PlayerIntention.MOVE_RIGHT_OFF);
				break;
			}
			case (Input.Keys.W): {
				netManager.sendAction(PlayerIntention.MOVE_UP_OFF);
				break;
			}
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
