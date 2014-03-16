package de.hochschuletrier.gdw.commons.gdx.input;

import com.badlogic.gdx.InputProcessor;

public class InputInterceptor implements InputProcessor {
    protected final InputProcessor mainProcessor;
    protected boolean isActive;
    
    public InputInterceptor(InputProcessor mainProcessor) {
        this.mainProcessor = mainProcessor;
    }
    
    public final void setActive(boolean active) {
        isActive = active;
    }
    
    public final boolean isActive() {
        return isActive;
    }

    @Override
    public boolean keyDown(int keycode) {
        return isActive && mainProcessor.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return isActive && mainProcessor.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return isActive && mainProcessor.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return isActive && mainProcessor.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return isActive && mainProcessor.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return isActive && mainProcessor.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return isActive && mainProcessor.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return isActive && mainProcessor.scrolled(amount);
    }
}
