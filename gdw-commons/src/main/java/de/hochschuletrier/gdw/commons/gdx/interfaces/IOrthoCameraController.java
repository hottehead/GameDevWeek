package de.hochschuletrier.gdw.commons.gdx.interfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;

public interface IOrthoCameraController {

	public void update();

	public void setCamera(OrthographicCamera camera);

	public OrthographicCamera getCamera();
}
