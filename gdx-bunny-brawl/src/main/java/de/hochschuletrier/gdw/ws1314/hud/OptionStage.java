package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class OptionStage extends AutoResizeStage {
	
	private Skin defaultSkin;
	private BitmapFont font;
	private boolean isInited = false;
	
	private Slider master, music, sound;
	private TextButton back;
	
	public OptionStage() {
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
		

		Label title = new Label("Optionen", defaultSkin);
		uiTable.add(title).pad(50).row();
		
		Label label = new Label("Sound-Optionen", defaultSkin);
		uiTable.add(label).row();
		
		Table tmp = new Table();
		uiTable.add(tmp).row();
		label = new Label("Master", defaultSkin);	
		tmp.add(label);
		this.master = new Slider(0, 100, 2, false, defaultSkin);
		tmp.add(master);
		
		tmp = new Table();
		uiTable.add(tmp).row();
		label = new Label("Musik", defaultSkin);
		tmp.add(label);
		this.music = new Slider(0, 100, 2, false, defaultSkin);
		tmp.add(music);
		
		tmp = new Table();
		uiTable.add(tmp).row();
		label = new Label("Sound", defaultSkin);
		tmp.add(label);
		this.sound = new Slider(0, 100, 2, false, defaultSkin);
		tmp.add(sound);
		
		this.back = new TextButton("zurueck", defaultSkin);
		uiTable.add(back).padTop(20);
	}

	public void render() {
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		this.draw();
		Table.drawDebug(this);
	}
	
	public void clear() {
		super.clear();
		isInited = false;
	}
	
	//getter for sound slider
	public Slider getMasterSlider() {
		return master;
	}
	
	public Slider getSoundSlider() {
		return sound;
	}
	
	public Slider getMusicSlider() {
		return music;
	}
	
	public TextButton getBackButton() {
		return back;
	}
}
