package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelListElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.StaticTextElement;

public class MainMenuStage extends AutoResizeStage {
	
	private BitmapFont font;
	private Skin defaultSkin;
	
	private LevelList levelList;
	
	public MainMenuStage() {
		super();
	}

	public void init(AssetManagerX assetManager) {
		//init generic stuff
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		Table uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		font = assetManager.getFont("verdana", 24);

		
		//level list
		levelList = new LevelList(defaultSkin);
		//add levels for testing
		levelList.addLevel("level");
		levelList.addLevel("new level");
		levelList.addLevel("newer level");
		uiTable.addActor(levelList);
	}

	public void render() {		
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		DrawUtil.batch.flush();
		this.draw();
		Table.drawDebug(this);
	}
	
	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
	}
	
	public void addLevel(String levelName) {
		levelList.addLevel(levelName);
	}
	
	public LevelListElement getSelecetedLevel() {
		return levelList.getSelected();
	}
}
