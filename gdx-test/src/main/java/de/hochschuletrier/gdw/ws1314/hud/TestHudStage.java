package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.hud.elements.HealthBar;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.BoxOffsetDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.StaticTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.VisualBox;

public class TestHudStage {

	HealthBar healthBar;
	
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
		
		healthBar = new HealthBar(100);
		healthBar.initVisual(assetManager);
		
		this.attackIcon = new VisualBox(
				assetManager.getTexture("debugAttackIcon"), 500, 300, 64, 64);
		this.attackIcon = new BoxOffsetDecorator(this.attackIcon,
				new StaticTextElement(assetManager.getFont("verdana", 24), "Attacke", this.attackIcon.getWidth() * 0.5f, -14));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#render()
	 */
	public void render() {
		// this.setCamera(DrawUtil.getCamera());
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		healthBar.draw();
		attackIcon.draw();
	}

	float accum = 0;

	public void step(float dt) {
		accum = accum + dt;
		if (accum > .50f) {
			accum -= 1.0;
			healthBar.get().setValue(MathUtils.random() * 100);
		}

	}

}
