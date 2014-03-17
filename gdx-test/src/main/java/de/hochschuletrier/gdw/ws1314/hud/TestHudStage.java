package de.hochschuletrier.gdw.ws1314.hud;

import java.io.InputStream;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class TestHudStage extends AutoResizeStage {

	Table uiTable;
	Skin defaultSkin;
	
	public TestHudStage() {
		uiTable = new Table();
	}
	
	private void initSkin(AssetManagerX assetManager) {
		defaultSkin = new Skin(Gdx.files.internal("data/huds/uiskin.json"));
	}
	
	/* (non-Javadoc)
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#init()
	 */
	@Override
	public void init(AssetManagerX assetManager) {
		uiTable = new Table();
		initSkin(assetManager);
		
		uiTable.setFillParent(true); // ganzen platz nutzen
		
		this.addActor(uiTable);
		
		uiTable.setSkin(defaultSkin);
		uiTable.add(new Label("Hello World", defaultSkin));
		uiTable.row();
		uiTable.add(new Label("Hello World", defaultSkin));
		Button tb = new Button(defaultSkin);
		
		
		uiTable.add(tb);
		
//		InputStream is = CurrentResourceLocator.read("data/json/hudstyle.json");
		
		uiTable.debug(Debug.all);
		
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
