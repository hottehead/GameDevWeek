package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;
import de.hochschuletrier.gdw.ws1314.sound.LocalSound2;

public class OptionStage extends AutoResizeStage {
	
	private Skin defaultSkin;
	private BitmapFont font;
	private boolean isInited = false;
	
	private Slider master, music, sound;
	private ImageButton back;	
	
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
		

		Image title = new Image(assetManager.getTexture("menuButtonOptions"));
		uiTable.add(title).pad(50).row();
		
		Label label = new Label("Sound-Optionen", defaultSkin);
		uiTable.add(label).row();
		
		Table tmp = new Table();
//		uiTable.add(tmp).row();
//		label = new Label("Master-Sound", defaultSkin);	
//		tmp.add(label);
//		this.master = new Slider(0, 100, 2, false, defaultSkin);
//		tmp.add(master);
//		
		tmp = new Table();
		uiTable.add(tmp).row();
		label = new Label("Musik", defaultSkin);
		tmp.add(label);
		this.music = new Slider(0, 100, 2, false, defaultSkin);
		this.music.setValue(LocalMusic.getSystemVolume() * 100);
		tmp.add(music);
		
		tmp = new Table();
		uiTable.add(tmp).row();
		label = new Label("Sound", defaultSkin);
		tmp.add(label);
		this.sound = new Slider(0, 100, 2, false, defaultSkin);
		this.sound.setValue(LocalSound2.getSystemVolume() * 100);
		tmp.add(sound);
		
		TextureRegion texture = new TextureRegion(assetManager.getTexture("menuButtonBack"));
		ImageButtonStyle style = new ImageButtonStyle(defaultSkin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(texture);
		this.back = new ImageButton(style);
		uiTable.add(back).padTop(20);
		
//		this.back = new TextButton("zurueck", defaultSkin);
//		uiTable.add(back).padTop(20);
	}

	public void render() {
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		DrawUtil.batch.flush();
		this.draw();
//		Table.drawDebug(this);
	}
	
	public void clear() {
		super.clear();
		isInited = false;
	}
	
	//getter for sound slider
//	public Slider getMasterSlider() {
//		return master;
//	}
//	
	public Slider getSoundSlider() {
		return sound;
	}
	
	public Slider getMusicSlider() {
		return music;
	}
	
	public ImageButton getBackButton() {
		return back;
	}
}
