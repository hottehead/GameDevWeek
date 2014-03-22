package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.OptionStage;

public class OptionState extends GameState {
	
	private Logger logger;
	private OptionStage stage;
	
	private BackListener backListener;
	
	public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        logger = LoggerFactory.getLogger(OptionState.class);
        
        backListener = new BackListener();
        
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
	    Main.inputMultiplexer.addProcessor(stage);
		stage.getBackButton().addListener(backListener);
    }

    public void onEnterComplete() {
    }

    public void onLeave() {
    	stage.getBackButton().removeListener(backListener);
		Main.inputMultiplexer.removeProcessor(stage);
    }

    public void onLeaveComplete() {
    }

    public void dispose() {
    }
    
    private class BackListener extends ClickListener {
    	public void clicked(InputEvent event, float x, float y) {
    		logger.info("Change state to MainMenuState");
    		GameStates.MAINMENU.activate();
    	}
    }
}




