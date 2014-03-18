package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.Main;

public class LevelSelectorStage extends AutoResizeStage {

	Table uiTable;
	private Skin defaultSkin;
//	List<LevelElement> levelList;
	
	public LevelSelectorStage() {
	}
	
	private void initSkin(AssetManagerX assetManager) {
		defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
	}
	
//	private void addLevel(String levelName) {
//		LevelElement button = new LevelElement(levelName, defaultSkin);
//		levelList.getItems().add(button);
//	}
	
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
		
//		levelList = new List<LevelElement>(defaultSkin);
//		ScrollPane sp = new ScrollPane(levelList);
//		addLevel("new Level");
//		addLevel("newer Level");
//		uiTable.add(sp);
		
		LevelList list = new LevelList(defaultSkin);
		list.addLevel("new Level");
		list.addLevel("newer Level");
		list.addLevel("another level");
		uiTable.add(list);
		
//		InputStream is = CurrentResourceLocator.read("data/json/hudstyle.json");
		
//		uiTable.debug(Debug.all);
	}
	/* (non-Javadoc)
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#render()
	 */
	@Override
	public void render() {
//		this.setCamera(DrawUtil.getCamera());
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT|GL11.GL_COLOR_BUFFER_BIT);
		
		this.act(Gdx.graphics.getDeltaTime());
		
		this.draw();
		
		Table.drawDebug(this);
		
	}	
}
