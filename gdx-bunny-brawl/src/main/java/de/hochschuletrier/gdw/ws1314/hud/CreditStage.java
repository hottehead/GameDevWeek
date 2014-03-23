package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class CreditStage extends AutoResizeStage {

	private Skin defaultSkin;
	private BitmapFont font;
	private boolean isInited;
	
	public CreditStage() {
		super();
	}
	
	public void init(AssetManagerX assetManager) {		
		if (isInited)
		return;
	else
		isInited = !isInited;
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
		Table uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		font = assetManager.getFont("verdana", 24);
	}

	public void render() {
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		DrawUtil.batch.flush();
		this.draw();
		Table.drawDebug(this);
	}

	public void clear() {
		super.clear();
		isInited = false;
	}
}
