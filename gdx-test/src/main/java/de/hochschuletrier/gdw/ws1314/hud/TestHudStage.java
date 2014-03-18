package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.hud.elements.BarBackgroundDecoration;
import de.hochschuletrier.gdw.ws1314.hud.elements.BarFrontDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.BoxOffsetDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.DynamicTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.MinMaxValue;
import de.hochschuletrier.gdw.ws1314.hud.elements.NinePatchSettings;
import de.hochschuletrier.gdw.ws1314.hud.elements.StaticTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.VisualBar;
import de.hochschuletrier.gdw.ws1314.hud.elements.VisualBox;

public class TestHudStage {

	VisualBox visualBar;
	MinMaxValue healthBar;

	VisualBox attackIcon;
	VisualBox eiAblegenIcon;

	public TestHudStage() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#init()
	 */
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
		BitmapFont hudFont = assetManager.getFont("verdana", 14);

		visualBar = new BoxOffsetDecorator(backgroundHealth,
				new DynamicTextElement(hudFont, "HP: ",
						backgroundHealth.getWidth() * 0.5f,
						backgroundHealth.getHeight() + 2, healthBar));
		visualBar = new BarFrontDecorator(visualBar, frontBarTex,
				new NinePatchSettings(1, 2, 2, 1));

		healthBar.setValue(100);

		this.attackIcon = new VisualBox(
				assetManager.getTexture("debugAttackIcon"), 500, 300, 64, 64);
		this.attackIcon = new BoxOffsetDecorator(this.attackIcon,
				new StaticTextElement(hudFont, "Attacke", this.attackIcon.getWidth() * 0.5f, -14));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#render()
	 */
	public void render() {
		// this.setCamera(DrawUtil.getCamera());
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		visualBar.draw();
		attackIcon.draw();
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
