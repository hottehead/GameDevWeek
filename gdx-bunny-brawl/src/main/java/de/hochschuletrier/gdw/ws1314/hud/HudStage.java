package de.hochschuletrier.gdw.ws1314.hud;


import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;


public class HudStage extends AutoResizeStage {

	
	private Skin defaultSkin;

	@Override
	public void init(AssetManagerX assetManager) {
		
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
		
		Table uiTable = new Table(defaultSkin);
		
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		
		uiTable.add( initStatusBar(assetManager) ).align(Align.bottom|Align.left);
		
		uiTable.invalidate();
	}

	private Actor initStatusBar(AssetManagerX assetManager) {
		Table statusBarTable = new Table(defaultSkin);
		
		Image img = new Image(assetManager.getTexture("HudEmblemHunterWhite"));
		statusBarTable.add(new Label("Class", defaultSkin));
		statusBarTable.row();
		statusBarTable.add(img);
		
		
		return statusBarTable;
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		this.draw();
		
		Table.drawDebug(this);
	}

	public void setDisplayedPlayer(ClientPlayer playerEntity) {
		
	}

}
