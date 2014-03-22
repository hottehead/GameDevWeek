package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelListElement;

public class StartServerStage extends AutoResizeStage {
	Skin defaultSkin;
	BitmapFont font;
	
	LevelList levelList;
	TextButton startServer;
	TextButton startServerAndPlay;
	TextButton back;

	public void init(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
		Table uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		font = assetManager.getFont("verdana", 24);
	
		uiTable.row().padTop(20);
		Label label = new Label("Levelauswahl", defaultSkin);
		uiTable.add(label);
		uiTable.row().padTop(20);
		
		//level list
		levelList = new LevelList(defaultSkin);
		//add levels for testing
		Array<String> levels = assetManager.getAssetNamesByType(TiledMap.class);
		
		for (String current : levels) {
			levelList.addLevel(current);
		}
		uiTable.add(levelList).padBottom(50).row();
		Table tmp = new Table(defaultSkin);
		uiTable.add(tmp);
		
		startServerAndPlay = new TextButton("Starte Server und selbst spielen", defaultSkin);
		tmp.add(startServerAndPlay);
		
		startServer = new TextButton("Starte Server", defaultSkin);
		tmp.add(startServer);
		
		back = new TextButton("zurueck", defaultSkin);
		tmp.add(back);
		
		uiTable.row();
	}

	public void render() {
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		DrawUtil.batch.flush();
		this.draw();
		Table.drawDebug(this);
	}
	
	public TextButton getBackButton() {
		return back;
	}
	
	public TextButton getStartServerButton() {
		return startServer;
	}
	
	public TextButton getStartServerAndPlayButton() {
		return startServerAndPlay;
	}
	
	public LevelListElement getSelectedLevel() {
		return levelList.getSelected();
	}

}
