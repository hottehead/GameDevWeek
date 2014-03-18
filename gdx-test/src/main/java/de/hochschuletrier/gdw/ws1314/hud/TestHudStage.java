package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.hud.elements.BarBackgroundDecoration;
import de.hochschuletrier.gdw.ws1314.hud.elements.BarFrontDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.BarTextDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.MinMaxValue;
import de.hochschuletrier.gdw.ws1314.hud.elements.NinePatchSettings;
import de.hochschuletrier.gdw.ws1314.hud.elements.StaticTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.VisualBar;

public class TestHudStage extends AutoResizeStage {

	VisualBar visualBar;
	MinMaxValue healthBar;

	public TestHudStage() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#init()
	 */
	@Override
	public void init(AssetManagerX assetManager) {
		Texture barTex = assetManager.getTexture("debugBar");
		Texture backBarTex = assetManager.getTexture("debugTooltip");
		Texture frontBarTex = assetManager.getTexture("debugBarDecorNine");

		healthBar = new MinMaxValue(0, 100, -1);
		VisualBar healthBarVisual = new VisualBar(barTex, 30, 30, 300, 40,
				healthBar);

		BarBackgroundDecoration backgroundHealth = new BarBackgroundDecoration(
				healthBarVisual, backBarTex);
		// BarFrontDecorator frontBar = new BarFrontDecorator(test,
		// frontBarTex);
		visualBar = new BarTextDecorator(backgroundHealth,
				new StaticTextElement(assetManager.getFont("verdana", 14),
						"100", backgroundHealth.getWidth() * 0.5f,
						backgroundHealth.getHeight() + 2));
		visualBar = new BarFrontDecorator(visualBar, frontBarTex,
				new NinePatchSettings(1, 2, 2, 1));

		healthBar.setValue(100);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#render()
	 */
	@Override
	public void render() {
		// this.setCamera(DrawUtil.getCamera());
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);

		this.act(Gdx.graphics.getDeltaTime());

		visualBar.draw();
	}

	float accum = 0;

	public void step(float dt) {
		accum = accum + dt;
		if (accum > .50f) {
			accum -= 1.0;
			healthBar.setValue(MathUtils.random() * 100);
		}

	}

}
