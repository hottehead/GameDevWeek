package de.hochschuletrier.gdw.ws1314.hud.elements;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.AutoResizeStage;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.BoxOffsetDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.StaticTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.VisualBox;

public class GameplayStage extends AutoResizeStage {
	private BitmapFont font;
	private Skin defaultSkin;
	
	private HealthBar healthBar;
	private float accum = 0;
	private VisualBox classIcon;
	
	private VisualBox buff1, buff2, buff3;
	
	private VisualBox attackIcon;
	private VisualBox layEggIcon;
	

	public GameplayStage() {
		super();
	}
	
	public void init(AssetManagerX assetManager) {
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		Table uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		this.addActor(uiTable);
		font = assetManager.getFont("verdana", 24);
		StaticTextElement decor;
		
		//healthbar
		healthBar = new HealthBar(100);
		healthBar.initVisual(assetManager, 114, 450, 300, 40);
		
		//class icon
		classIcon = new VisualBox(assetManager.getTexture("debugClass"), 50, 425, 64, 64);
		decor = new StaticTextElement(font, "Klasse", this.classIcon.getWidth()*.5f, -14);
		classIcon = new BoxOffsetDecorator(classIcon, decor);
		
		//buffs
		buff1 = new VisualBox(assetManager.getTexture("debugBuff"), 334, 425, 20, 20);
		buff2 = new VisualBox(assetManager.getTexture("debugBuff"), 364, 425, 20, 20);
		buff3 = new VisualBox(assetManager.getTexture("debugBuff"), 394, 425, 20, 20);
		
		//attackIcon
		attackIcon = new VisualBox(assetManager.getTexture("debugAttackIcon"), 300, 300, 64, 64);
		decor = new StaticTextElement(font, "Attacke", attackIcon.getWidth()*.5f,  -14);
		attackIcon = new BoxOffsetDecorator(this.attackIcon, decor);
		//
		layEggIcon = new VisualBox(assetManager.getTexture("debugAttackIcon"), 400, 300, 64, 64);
		decor = new StaticTextElement(font, "Ei ablegen", layEggIcon.getWidth()*.5f,  -14);
		layEggIcon = new BoxOffsetDecorator(this.layEggIcon, decor);
		
		
	}

	public void render() {
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		healthBar.draw();
		classIcon.draw();
		buff1.draw();
		buff2.draw();
		buff3.draw();
		attackIcon.draw();
		layEggIcon.draw();
		
		DrawUtil.batch.flush();
		this.draw();
		Table.drawDebug(this);
	}
	
	
	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
	}

	public void step(float dt) {
		accum = accum + dt;
		if (accum > .50f) {
			accum -= 1.0;
			healthBar.get().setValue(MathUtils.random() * 100);
		}
	}
}
