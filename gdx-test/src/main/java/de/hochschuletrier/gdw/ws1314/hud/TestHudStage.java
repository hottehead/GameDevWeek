package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.BarBackgroundDecoration;
import de.hochschuletrier.gdw.ws1314.hud.elements.BarFrontDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.BoxOffsetDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.DynamicTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.MinMaxValue;
import de.hochschuletrier.gdw.ws1314.hud.elements.NinePatchSettings;
import de.hochschuletrier.gdw.ws1314.hud.elements.StaticTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.VisualBar;
import de.hochschuletrier.gdw.ws1314.hud.elements.VisualBox;

public class TestHudStage extends AutoResizeStage {

	VisualBox visualBar;
	MinMaxValue healthBar;

	VisualBox attackIcon;
	VisualBox eiAblegenIcon;
	
	Skin defaultSkin;

	public TestHudStage() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#init()
	 */
	public void init(AssetManagerX assetManager) {
		//init generic stuff
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		Table uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		this.addActor(uiTable);
		
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
		
		
		//test code levelList
		LevelList list = new LevelList(defaultSkin);
		list.addLevel("new Level");
		list.addLevel("newer Level");
		list.addLevel("another level");
		uiTable.add(list);
		
		
		uiTable.debug(Debug.all);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#render()
	 */
	public void render() {
		// this.setCamera(DrawUtil.getCamera());
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());

		visualBar.draw();
		attackIcon.draw();
		
		this.draw();
		Table.drawDebug(this);
	}

	float accum = 0;

	public void step(float dt) {
		accum = accum + dt;
		if (accum > .50f) {
			accum -= 1.0;
			healthBar.setValue(MathUtils.random() * 100);
		}
	}
	
	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
	}

}
