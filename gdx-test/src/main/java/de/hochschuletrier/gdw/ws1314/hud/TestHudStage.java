package de.hochschuletrier.gdw.ws1314.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.lwjgl.opengl.*;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class TestHudStage extends Stage implements IHudStage {

	Table uiTable;
	Skin defaultSkin;
	
	private void initSkin(AssetManagerX assetManager) {
		defaultSkin = new Skin();
		BitmapFont font = assetManager.getFont("verdana", 24);
		defaultSkin.add("default", font); // font heißt jetzt default
		
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
		uiTable.add(new Label("Hallo Welt", defaultSkin, "default", Color.RED));
		uiTable.add(new Label("Hallo Welt", defaultSkin, "default", Color.RED));
		uiTable.row();
		uiTable.add(new Label("Hallo Weltaaaaaaaaaaaa", defaultSkin, "default", Color.RED));
		
	}
	/* (non-Javadoc)
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#render()
	 */
	@Override
	public void render() {
		this.setCamera(DrawUtil.getCamera());
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT|GL11.GL_COLOR_BUFFER_BIT);
		
		this.act(Gdx.graphics.getDeltaTime());
		
		this.draw();
		uiTable.debug();
		
//		Table.drawDebug(this);
	}
	/* (non-Javadoc)
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}
	
	/* (non-Javadoc)
	 * @see de.hochschuletrier.gdw.ws1314.hud.IHudStage#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		this.setViewport(width, height, true); //FIXME: fixAspectRatio nötig?
	}
	
	
}
