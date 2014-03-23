package de.hochschuletrier.gdw.commons.gdx.state;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.state.transition.Transition;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles game states and their transitions
 *
 * @author Santo Pfingsten
 */
public abstract class StateBasedGame implements ApplicationListener {

    private GameState currentState, nextState, prevState;
    private Transition entering, leaving;
    private long lastTime = System.currentTimeMillis();
    private final ArrayList<ScreenListener> screenListeners = new ArrayList<ScreenListener>();
    private  Logger logger = LoggerFactory.getLogger(StateBasedGame.class);
    public StateBasedGame() {
        currentState = new GameState();
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void changeState(GameState state) {
        changeState(state, null, null);
    }

    public void changeState(GameState state, Transition out, Transition in) {
        if (state == null) {
            throw new IllegalArgumentException("State must not be null!");
        }
        if (out == null) {
            out = new Transition();
        }
        if (in == null) {
            in = new Transition();
        }
        leaving = out;
        entering = in;

        nextState = state;

        currentState.onLeave();
        nextState.onEnter();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    
    public void addScreenListener(ScreenListener listener) {
        screenListeners.add(listener);
    }
    
    public void removeScreenListener(ScreenListener listener) {
        screenListeners.remove(listener);
    }

    @Override
    public void resize(int width, int height) {
    	logger.info("Resizing Viewport to "+ width + "  "+ height);
        for(ScreenListener listener: screenListeners) {
            listener.resize(width, height);
        }
    }

    @Override
    public final void render() {
        update();

        preRender();

        if (leaving != null) {
            if (leaving.isReverse()) {
                leaving.render(nextState, currentState);
            } else {
                leaving.render(currentState, nextState);
            }
        } else if (entering != null) {
            if (entering.isReverse()) {
                entering.render(prevState, currentState);
            } else {
                entering.render(currentState, prevState);
            }
        } else {
            currentState.render();
        }

        postRender();
    }

    protected void preRender() {
    }

    protected void postRender() {
    }

    private void update() {
        long time = System.currentTimeMillis();
        float delta = (time - lastTime) * 0.001f;
        lastTime = time;

        preUpdate(delta);

        updateTransitions(delta);

        currentState.update(delta);
        if (nextState != null) {
            nextState.update(delta);
        }

        postUpdate(delta);
    }

    private void updateTransitions(float delta) {
        if (leaving != null) {
            leaving.update(delta);
            if (!leaving.isDone()) {
                return;
            }
            currentState.onLeaveComplete();
            prevState = currentState;
            currentState = nextState;
            nextState = null;
            leaving = null;
        }
        if (entering != null) {
            entering.update(delta);
            if (!entering.isDone()) {
                return;
            }
            currentState.onEnterComplete();
            prevState = null;
            entering = null;
        }
    }

    protected void preUpdate(float delta) {
    }

    protected void postUpdate(float delta) {
    }
}
