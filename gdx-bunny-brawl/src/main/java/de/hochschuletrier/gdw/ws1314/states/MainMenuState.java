package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.MainMenuStage;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends GameState {
    private LocalMusic music;
	private int stateChangeDuration=500;
	private MainMenuStage stage;
	
	private Logger logger;
	private OptionListener optionListener;
	private ExitListener exitlistener;
	private PlayServerListener playServerListener;
	
	private StartServerClick startServerClickListener;
	private StartClientClick startClientClickListener;
	private StartForeverAloneClick startForeverAloneListener;

    public MainMenuState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

		logger = LoggerFactory.getLogger(MainMenuState.class);
		this.music = Main.musicManager.getMusicStreamByStateName(GameStates.MAINMENU);
		
        stage = new MainMenuStage();
		stage.init(assetManager);

		this.startServerClickListener = new StartServerClick();
		this.startClientClickListener = new StartClientClick();
		this.startForeverAloneListener = new StartForeverAloneClick();

		stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.optionListener = new OptionListener();
		this.exitlistener = new ExitListener();
		this.playServerListener = new PlayServerListener();
	}
	
    public void render() {

		stage.render();


		stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	float stateTime = 0f; 

	public void update(float delta) {
		stateTime += delta;
		music.update();
    }

    public void onEnter() {
        if (this.music.isMusicPlaying()) {
			this.music.setFade('i', 2500);
        }
		else{
        	this.music.play("music-lobby-loop");
        }

	    Gdx.input.setInputProcessor(stage);
		
	    stage.getStartServerButton().addListener(this.startServerClickListener);
		stage.getStartClientButton().addListener(this.startClientClickListener);
		stage.getStartForeverAloneButton().addListener(this.startForeverAloneListener);
		
		stage.getStartServerButton().addListener(this.startServerClickListener);
		stage.getStartClientButton().addListener(this.startClientClickListener);
		stage.getStartForeverAloneButton().addListener(this.startForeverAloneListener);
		stage.getOptionsButton().addListener(this.optionListener);
		stage.getExitButton().addListener(this.exitlistener);
		stage.getPlayServerButton().addListener(this.playServerListener);
	}

    public void onLeave() {
    	if (this.music.isMusicPlaying()) {
    		this.music.setFade('o', 2000);
    	}
    	
        stage.getStartServerButton().removeListener(this.startServerClickListener);
        stage.getStartClientButton().removeListener(this.startClientClickListener);
        stage.getStartForeverAloneButton().removeListener(this.startForeverAloneListener);
		
        stage.getStartServerButton().removeListener(this.startServerClickListener);
		stage.getStartClientButton().removeListener(this.startClientClickListener);
		stage.getStartForeverAloneButton().removeListener(this.startForeverAloneListener);
		stage.getPlayServerButton().removeListener(this.playServerListener);
		stage.getOptionsButton().removeListener(this.optionListener);
		stage.getExitButton().removeListener(this.exitlistener);
		Main.inputMultiplexer.removeProcessor(stage);
	}

	public void onLeaveComplete() {
	}

	public void dispose() {
		if (this.stage != null)
			stage.dispose();
	}

	public boolean keyDown(int keycode) {
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}
	
	//private listener 
	private class PlayClientListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to JoinServerState");
			GameStates.LOBBY.init(assetManager);
			GameStates.LOBBY.activate();
		}
	}
	
	private class PlayServerListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to StartServerState");
			GameStates.STARTSERVER.init(assetManager);
			GameStates.STARTSERVER.activate();
		}
	}
	
	private class OptionListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to OptionState");
			GameStates.OPTIONS.init(assetManager);
			GameStates.OPTIONS.activate();
		}
	}
	
	private class CreditsListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to CreditsState");
			GameStates.CREDITS.init(assetManager);
			GameStates.CREDITS.activate();
		}
	}
	
	private class ExitListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			Gdx.app.exit();
		}
	}

    private class StartServerClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Changing State to Server-Lobby...");
			GameStates.SERVERLOBBY.init(assetManager);
			GameStates.SERVERLOBBY.activate();
		}
    }
    
    private class StartClientClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
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
//			System.out.println("changed state");
//			System.out.println(Main.inputMultiplexer.getProcessors().toString());
		}
    }
}
