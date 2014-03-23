package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.CreditStage;

public class CreditState extends GameState {
	
	private Logger logger;
	private CreditStage stage;

	public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        
		logger = LoggerFactory.getLogger(MainMenuState.class);
		
		stage = new CreditStage();
		stage.init(assetManager);
		
    }

    public void render() {
    	stage.render();
    }

    public void update(float delta) {
    }

    public void onEnter() {
    	stage.init(assetManager);
	    Main.inputMultiplexer.addProcessor(stage);
    }

    public void onEnterComplete() {
    }

    public void onLeave() {
    	Main.inputMultiplexer.removeProcessor(stage);
    	stage.clear();
    }

    public void onLeaveComplete() {
    }

    public void dispose() {
    }
}
