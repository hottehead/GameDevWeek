package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.BuffIcon;
import de.hochschuletrier.gdw.ws1314.hud.elements.HealthBar;

public class TestHudStage extends AutoResizeStage {

	private Table uiTable;
	private Skin defaultSkin;
	
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
		
		uiTable.add(new HealthBar(defaultSkin)).expand(true, false );
		
		
		
//		uiTable.row();
//		uiTable.add(new Label("Hello World", defaultSkin));
//		TextButton tb = new TextButton("Press me.", defaultSkin, "toggle");
//		uiTable.add(tb);
//		
//		tb.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//			}
//		});
//		
//		Slider slider = new Slider(0, 100, 0.5f, false, defaultSkin);
//		uiTable.add(slider);
		
		
//		InputStream is = CurrentResourceLocator.read("data/json/hudstyle.json");
		
		uiTable.debug(Debug.cell);
		uiTable.pack();
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
