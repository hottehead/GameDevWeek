package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;

public class LoadGameState extends GameState {

	private boolean isDone;
	TextureRegion splashscreen;
	
	@Override
	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
		splashscreen = new TextureRegion(new Texture(
				Gdx.files.internal("data/images/ladescreen.png")));
		
	}

	@Override
	public void render() {
		DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				Color.BLACK);
		DrawUtil.batch.draw(splashscreen, 0, 0);
		float drawWidth = Gdx.graphics.getWidth() - 100.0f;
		DrawUtil.fillRect(50, Gdx.graphics.getHeight() / 2 - 25,
				(int) (drawWidth * assetManager.getProgress()), 50, Color.GREEN);
		DrawUtil.drawRect(50, Gdx.graphics.getHeight() / 2 - 25, drawWidth, 50,
				Color.GREEN);
	}

	@Override
	public void update(float delta) {
		if (isDone) {
			return;
		}

		assetManager.update();

		if (assetManager.getProgress() == 1) {
			// VSync was only disabled to speed up loading
			Gdx.graphics.setVSync(true);

			Main.getInstance().onLoadComplete();
			isDone = true;
		}
	}

	@Override
	public void dispose() {
	}
}
