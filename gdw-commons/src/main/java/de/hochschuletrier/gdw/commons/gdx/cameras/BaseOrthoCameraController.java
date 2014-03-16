package de.hochschuletrier.gdw.commons.gdx.cameras;

import com.badlogic.gdx.graphics.OrthographicCamera;

import de.hochschuletrier.gdw.commons.gdx.interfaces.IOrthoCameraController;

public abstract class BaseOrthoCameraController implements IOrthoCameraController {

    protected OrthographicCamera camera;
    boolean autoupdate = true;

    public BaseOrthoCameraController(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void update() {
        if (autoupdate) {
            camera.update();
        }
    }

    @Override
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public OrthographicCamera getCamera() {
        return camera;
    }
}
