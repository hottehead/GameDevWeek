package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.BarBackgroundDecoration;
import de.hochschuletrier.gdw.ws1314.hud.elements.BuffIcon;
import de.hochschuletrier.gdw.ws1314.hud.elements.VisualBar;

public class TestHudStage extends AutoResizeStage {

	private Table uiTable;
	private Skin defaultSkin;
	
	VisualBar visualBar;
	VisualBar healthBar;
	
	public TestHudStage() {
		uiTable = new Table();
	}
	
	private void initSkin(AssetManagerX assetManager) {
		defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
	}
	
	/* (non-Javadoc)
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#init()
	 */
	@Override
	public void init(AssetManagerX assetManager) {
		uiTable = new Table();
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		
		uiTable.setFillParent(true); // ganzen platz nutzen
		
		this.addActor(uiTable);
		
		uiTable.setSkin(defaultSkin);
		BuffIcon icons = new BuffIcon(defaultSkin);
		
		uiTable.add(icons.createButton("buff"));
		uiTable.add(icons.createButton("buff"));
		uiTable.add(icons.createButton("buff"));
		uiTable.add(icons.createButton("buff"));
		
		uiTable.row().padTop(5);
		
		Texture barTex = assetManager.getTexture("debugBar");
		Texture backBarTex = assetManager.getTexture("debugBuff");
		Texture frontBarTex = assetManager.getTexture("debugBarDecor");
		
		healthBar =  new VisualBar(barTex, 0, 0, 100, 30, 0, 200, 1);
		BarBackgroundDecoration test = new BarBackgroundDecoration(healthBar, backBarTex);
		BarFrontDecorator frontBar = new BarFrontDecorator(test, frontBarTex);
		visualBar = new BarFrontDecorator(frontBar, assetManager.getTexture("debugBarDecor2"));
		
		
	}
	/* (non-Javadoc)
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#render()
	 */
	@Override
	public void render() {
//		this.setCamera(DrawUtil.getCamera());
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT|GL11.GL_COLOR_BUFFER_BIT);
		
		this.act(Gdx.graphics.getDeltaTime());
		
		
		visualBar.draw();
		
//		Table.drawDebug(this);
		
		
		
	}
	
	public void step(float dt) {
		healthBar.step(dt);
		
	}

}
