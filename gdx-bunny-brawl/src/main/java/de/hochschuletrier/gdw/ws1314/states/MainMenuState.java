package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input.Keys;
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
public class MainMenuState extends GameState implements InputProcessor {

	InputInterceptor inputProcessor;
	private LocalMusic music;
	private int stateChangeDuration=500;
	private MainMenuStage stage;
	
	private Logger logger;
	private OptionListener optionListener;
	private PlayListener playListener; //testing

	public MainMenuState() {
	}

	@Override
	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
		logger = LoggerFactory.getLogger(MainMenuState.class);
		
		this.music = new LocalMusic(assetManager);
		inputProcessor = new InputInterceptor(this) {
			@Override
			public boolean keyUp(int keycode) {
				switch (keycode) {
				case Keys.ESCAPE:
					if (GameStates.GAMEPLAY.isActive()) {
						GameStates.MAINMENU.activate(
								 new SplitHorizontalTransition(stateChangeDuration).reverse(),
								null);
					} else if (GameStates.MAINMENU.isActive())
						GameStates.GAMEPLAY.activate(
								new SplitHorizontalTransition(stateChangeDuration), null);
					return true;
				}
				return isActive && mainProcessor.keyUp(keycode);
			}
		};
		
		Main.inputMultiplexer.addProcessor(inputProcessor);

		stage = new MainMenuStage();
		stage.init(assetManager);
		stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.optionListener = new OptionListener();
		this.playListener = new PlayListener();
	}

	@Override
	public void render() {
		stage.render();
	}

	float stateTime = 0f;

	@Override
	public void update(float delta) {
		stateTime += delta;
		music.update(stateChangeDuration);
	}

	@Override
	public void onEnter() {
		inputProcessor.setActive(true);
		
		//add listener to buttons in stage
		stage.getOptionsButton().addListener(this.optionListener);
		stage.getStartButton().addListener(this.playListener);

		if (this.music.isMusicPlaying())
			//this.music.deMute();
			this.music.setFade('i', 5000);
		else
			this.music.play("music-lobby-loop");
	}

	@Override
	public void onLeave() {
		//this.music.mute();
		this.music.setFade('o', this.stateChangeDuration);
		
		stage.getOptionsButton().removeListener(this.optionListener);
		stage.getStartButton().removeListener(this.playListener);
		inputProcessor.setActive(false);
	}

	@Override
	public void onLeaveComplete() {
//		stage.dispose();

	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
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
			logger.info("TODO: Exit Game");
		}
	}
	
	private class PlayListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to GamplayState - for testing purpose");
			if (GameStates.GAMEPLAY.isActive()) {
				GameStates.MAINMENU.activate(
						 new SplitHorizontalTransition(stateChangeDuration).reverse(),
						null);
			} else if (GameStates.MAINMENU.isActive())
				GameStates.GAMEPLAY.activate(
						new SplitHorizontalTransition(stateChangeDuration), null);
		}
	}
}
