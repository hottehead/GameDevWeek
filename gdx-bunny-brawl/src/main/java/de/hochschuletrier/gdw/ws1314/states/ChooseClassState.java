package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;

public class ChooseClassState extends GameState {

	private Logger logger;
	
	public void init(AssetManagerX assetManager) {
        super.init(assetManager);

		logger = LoggerFactory.getLogger(ChooseClassState.class);
    }

    public void render() {
    }

    public void update(float delta) {
    }

    public void onEnter() {
    }

    public void onEnterComplete() {
    }

    public void onLeave() {
    }

    public void onLeaveComplete() {
    }

    public void dispose() {
    }
    
	private class BackListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("");
		}
	}
	
}
