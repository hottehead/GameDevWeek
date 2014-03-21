package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.MainMenuStage;
import de.hochschuletrier.gdw.ws1314.shaders.DemoShader;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;

/**
 * Menu state
 * 
 * @author Santo Pfingsten
 */
public class MainMenuState extends GameState implements InputProcessor {

	private DemoShader demoShader;
	InputInterceptor inputProcessor;
	private LocalMusic music;
	AnimationExtended walking;
	private int stateChangeDuration=500;
	private MainMenuStage stage;

	public MainMenuState() {
	}

	@Override
	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
		walking = assetManager.getAnimation("hunterWhiteWalkRight");
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
	}

	@Override
	public void render() {
		try{
			TextureRegion keyFrame = walking.getKeyFrame(stateTime);
		
			DrawUtil.batch.draw(keyFrame, 0, 0);
		// stage.render();
		}catch(Exception e){
			System.out.println("walking.getKeyFrame(stateTime) in Method render throws NullPointer");
		}
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
		inputProcessor.setActive(false);
	}

	@Override
	public void onLeaveComplete() {

	}

	@Override
	public void dispose() {
		stage.dispose();
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
		return true;
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
}
