package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class CreditStage extends Stage implements ScreenListener {

	private Skin defaultSkin;
	private BitmapFont font;
	
	
	public void init(AssetManagerX assetManager) {		
		this.defaultSkin = new Skin(Gdx.files.internal("data/skins/default.json"));
		Table uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		font = assetManager.getFont("verdana", 24);
	}

	public void render() {
		Table.drawDebug(this);
		this.act();
		this.draw();
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
	}
}
