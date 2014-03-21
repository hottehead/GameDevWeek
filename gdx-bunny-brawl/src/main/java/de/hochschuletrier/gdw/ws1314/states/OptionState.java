package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.hud.OptionStage;

public class OptionState extends GameState {
	
	private Logger logger;
	private OptionStage stage;
	
	public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        logger = LoggerFactory.getLogger(MainMenuState.class);
        
        stage = new OptionStage();
        stage.init(assetManager);
		stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void render() {
    	stage.render();
    }

    public void update(float delta) {
    }

    public void onEnter() {
    }

    public void onEnterComplete() {
    }

    public void onLeave() {
    	stage.dispose();
    }

    public void onLeaveComplete() {
    }

    public void dispose() {
    }
}
