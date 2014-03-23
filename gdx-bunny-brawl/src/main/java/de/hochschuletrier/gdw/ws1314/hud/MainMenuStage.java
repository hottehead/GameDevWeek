package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.ListElement;

public class MainMenuStage extends AutoResizeStage {
	
	private Skin defaultSkin;
	private LevelList levelList;
	private Table uiTable;
	
	//buttons
	private ImageButton playServer;
	private ImageButton gameBrowser;
	private ImageButton options; 
	private ImageButton credits;
	private ImageButton exit;
	
	//test
	TextButton startServerAndPlay;
	
	public MainMenuStage() {
		super();
	}


	AssetManagerX assetManager;

	public void init(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
		this.assetManager = assetManager;
		
		Main.inputMultiplexer.addProcessor(this);
		
		uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		this.addActor(uiTable);
		
		TextureRegion texture = new TextureRegion(assetManager.getTexture("menuButtonPlayClient"));
		ImageButtonStyle style = new ImageButtonStyle(defaultSkin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(texture);
		gameBrowser = new ImageButton(style);
		
		texture = new TextureRegion(assetManager.getTexture("menuButtonPlayServer"));
		style = new ImageButtonStyle(defaultSkin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(texture);
		playServer = new ImageButton(style);
		
		texture = new TextureRegion(assetManager.getTexture("menuButtonOptions"));
		style = new ImageButtonStyle(defaultSkin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(texture);
		options = new ImageButton(style);
		
		texture = new TextureRegion(assetManager.getTexture("menuButtonCredits"));
		style = new ImageButtonStyle(defaultSkin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(texture);
		credits = new ImageButton(style);
		
		texture = new TextureRegion(assetManager.getTexture("menuButtonExit"));
		style = new ImageButtonStyle(defaultSkin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(texture);
		exit = new ImageButton(style);
		
		startServerAndPlay =  new TextButton("Start Forever Alone! ",defaultSkin);
		uiTable.add(gameBrowser);
		uiTable.add(playServer);

		uiTable.row();

		uiTable.add(options);
		uiTable.add(credits);
		uiTable.add(exit);
		
		uiTable.debug(Debug.all);
	}

	public void render() {		
		Table.drawDebug(this);
		this.act();
		this.draw();
	}

	public void addLevel(String levelName) {
		levelList.addLevel(levelName);
	}
	
	public ListElement getSelecetedLevel() {
		return levelList.getSelected();
	}
	
	// getter for adding listener to the buttons

	//for testing server-client stuff
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	public ImageButton getGameBrowserButton() {
		return gameBrowser;
	}
	
	public ImageButton getPlayServerButton() {
		return playServer;
	}	
	
	public TextButton getStartServerAndPlayButton() {
		return startServerAndPlay;
	}
	
	public ImageButton getOptionsButton() {
		return options;
	}
	
	public ImageButton getCreditsButton() {
		return options;
	}
	
	public ImageButton getExitButton() {
		return exit;
	}
}
