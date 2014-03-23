package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.StartServerStage;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;

public class StartServerState extends GameState {
	
	private Logger logger;
	private StartServerStage stage;
	
	private BackListener backListener;
	private StartServerListener startServerListener;
	
	public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        logger = LoggerFactory.getLogger(OptionState.class);
        
        backListener = new BackListener();
        startServerListener = new StartServerListener();
        
        stage = new StartServerStage();
        stage.init(assetManager);
		stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
	
    public void render() {
    	stage.render();
    }

    public void update(float delta) {
    }

    public void onEnter() {
    	stage.init(assetManager);
	    Main.inputMultiplexer.addProcessor(stage);
		stage.getBackButton().addListener(backListener);
		stage.getStartServerButton().addListener(startServerListener);
		stage.setSelectedMap(Main.getInstance().gamePreferences.getString(PreferenceKeys.mapName, ""));
    }

    public void onEnterComplete() {
    }

    public void onLeave() {
    	stage.getStartServerButton().removeListener(startServerListener);
    	stage.getBackButton().removeListener(backListener);
		Main.inputMultiplexer.removeProcessor(stage);
		stage.clear();
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
	
	private class StartServerListener extends ClickListener {
    	public void clicked(InputEvent event, float x, float y) {
    		if (!NetworkManager.getInstance().isServer())
    		{
    			int port;
    			if(Main.port > 0){
    				port = Main.port;
    			} else {
    				port = NetworkManager.getInstance().getDefaultPort();
    			}

    			NetworkManager.getInstance().server(null, port, NetworkManager.getInstance().getDefaultPlayerCount(), NetworkManager.getInstance().getDefaultServerIp());

    			if (!NetworkManager.getInstance().isServer())
    			{
    				logger.warn("Server could not be created. Another Server allready running or Port is blocked.");
    				return;
    			}
    		}
    		
    		if (!stage.getSelectedLevel().getText().toString().isEmpty()) {
    			Main.getInstance().gamePreferences.putString(PreferenceKeys.mapName, stage.getSelectedLevel().getText().toString());
    		}
    		
    		logger.info("Changing State to Server-Lobby...");
    		GameStates.SERVERLOBBY.init(assetManager);
    		GameStates.SERVERLOBBY.activate();
    	}
	}
}
