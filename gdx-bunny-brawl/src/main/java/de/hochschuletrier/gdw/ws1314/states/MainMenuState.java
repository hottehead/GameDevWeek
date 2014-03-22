package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.MainMenuStage;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends GameState {
	private static final Logger logger = LoggerFactory.getLogger(MainMenuState.class);
	
    private LocalMusic music;
	private int stateChangeDuration=500;
	private MainMenuStage stage;
	private StartServerClick startServerClickListener;
	private StartClientClick startClientClickListener;
	private StartForeverAloneClick startForeverAloneListener;

    public MainMenuState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        
		this.music = Main.musicManager.getMusicStreamByStateName(GameStates.MAINMENU);
		
		this.startServerClickListener = new StartServerClick();
		this.startClientClickListener = new StartClientClick();
		this.startForeverAloneListener = new StartForeverAloneClick();
		
    }

    @Override
    public void render() {
		stage.render();
	}

	float stateTime = 0f;

	@Override
	public void update(float delta) {
		stateTime += delta;
		music.update();
		Main.musicManager.getMusicStreamByStateName(GameStates.DUALGAMEPLAY).update();
		if(Main.startAsServer){
			logger.info("Start as Server...");
			startServer();
		}
    }

    @Override
    public void onEnter() {
        if (this.music.isMusicPlaying()) {
			this.music.setFade('i', 2500);
        }
		else{
        	this.music.play("music-lobby-loop");
        }
        
        stage = new MainMenuStage();
		stage.init(assetManager);
        
		stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		stage.getStartServerButton().addListener(this.startServerClickListener);
		stage.getStartClientButton().addListener(this.startClientClickListener);
		stage.getStartForeverAloneButton().addListener(this.startForeverAloneListener);
    }

    @Override
    public void onLeave() {
    	if (this.music.isMusicPlaying()) {
    		this.music.setFade('o', 2500);
    	}
		
        stage.getStartServerButton().removeListener(this.startServerClickListener);
		stage.getStartClientButton().removeListener(this.startClientClickListener);
		stage.getStartForeverAloneButton().removeListener(this.startForeverAloneListener);
		
		Main.inputMultiplexer.removeProcessor(this.stage);
		
		stage = null;
	}

	@Override
	public void onLeaveComplete() {
    }

    @Override
    public void dispose() {
		if (this.stage != null)
			stage.dispose();
    }

	public void startServer(){
		if (!NetworkManager.getInstance().isServer())
		{
			int port;
			if(Main.port > 0){
				port = Main.port;
			} else {
				port = NetworkManager.getInstance().getDefaultPort();
			}

			NetworkManager.getInstance().listen(NetworkManager.getInstance().getDefaultServerIp(), port, 10);

			if (!NetworkManager.getInstance().isServer())
			{
				logger.warn("Server could not be created. Another Server allready running or Port is blocked.");
				return;
			}
		}

		logger.info("Changing State to Server-Lobby...");
		GameStates.SERVERLOBBY.init(assetManager);
		GameStates.SERVERLOBBY.activate();
	}
    
    private class StartServerClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			// TODO: Nur Temporär zum localen Testen
	        startServer();
		}
    }
    
    private class StartClientClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			// TODO: Nur Temporär zum localen Testen
		    if (!NetworkManager.getInstance().isClient())
		    {
		        NetworkManager.getInstance().connect("localhost", NetworkManager.getInstance().getDefaultPort());
		        
		        if (!NetworkManager.getInstance().isClient()) {
		        	logger.warn("Connection could not be established! Server maybe not running.");
		        	GameStates.MAINMENU.init(assetManager);
		        	GameStates.MAINMENU.activate();
		        	return;
		        }
		    }
			
			logger.info("Changing State to Client-Lobby...");
			GameStates.CLIENTLOBBY.init(assetManager);
			GameStates.CLIENTLOBBY.activate();
		}
    }
    
    private class StartForeverAloneClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			GameStates.DUALGAMEPLAY.init(assetManager);
			GameStates.DUALGAMEPLAY.activate();
		}
    }
}
