package de.hochschuletrier.gdw.commons.gdx.cameras;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;

public class DefaultOrthoCameraController extends BaseOrthoCameraController implements InputProcessor {
	private final IntIntMap keys = new IntIntMap();
	private final int LEFT = Keys.A;
	private final int RIGHT = Keys.D;
	private final int UP = Keys.W;
	private final int DOWN = Keys.S;
	private float velocity = 300;
	private final Vector3 tmp = new Vector3();

	public DefaultOrthoCameraController(OrthographicCamera camera) {
		super(camera);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		this.update(Gdx.graphics.getDeltaTime());
	}

	public void update(float deltaTime) {

		if (keys.containsKey(LEFT)) {
			tmp.set(camera.direction).crs(camera.up).nor()
			.scl(-deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(RIGHT)) {
			tmp.set(camera.direction).crs(camera.up).nor()
			.scl(deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(UP)) {
			tmp.set(camera.up).nor().scl(deltaTime * velocity);
			camera.position.add(tmp);
		}
		if (keys.containsKey(DOWN)) {
			tmp.set(camera.up).nor().scl(-deltaTime * velocity);
			camera.position.add(tmp);
		}
		super.update();
	}

	/**
	 * Sets the velocity in units per second for moving forward, backward and
	 * strafing left/right.
	 * 
	 * @param velocity
	 *            the velocity in units per second
	 */
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	@Override
	public boolean keyDown(int keycode) {
		keys.put(keycode, keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys.remove(keycode, keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		OrthographicCamera cam = camera;
		cam.zoom += (amount * 0.1f);
		return false;
	}
}
