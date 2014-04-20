package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.ListElement;

public class MainMenuStage extends Stage implements ScreenListener {
	
	private Skin defaultSkin;
	private LevelList levelList;
	private Table root;
	
	//buttons
	private Button playServer;
	private Button gameBrowser;
	private Button options; 
	private Button credits;
	private Button exit;
	
	public MainMenuStage() {
		super();
	}


	AssetManagerX assetManager;
	private Skin bunnySkin;

	public void init(AssetManagerX assetManager) {
		this.defaultSkin = assetManager.getSkin("default");
		this.bunnySkin = assetManager.getSkin("bunnyBrawl");

		this.assetManager = assetManager;
				
		root = new Table();
		root.setFillParent(true); // ganzen platz in Tabelle nutzen
		
		gameBrowser = new Button(bunnySkin, "join");
		playServer = new Button(bunnySkin, "startServer");
		options =new Button(bunnySkin, "options");
		credits =new Button(bunnySkin, "credits");
		exit =new Button(bunnySkin, "quit");
		
		root.defaults();
		root.add(gameBrowser);
		root.row();
		root.add(playServer);
		root.row();
		root.add(options);
		root.row();
		root.add(credits);
		root.row();
		root.add(exit);
		
		this.addActor(root);
		root.debug(Debug.all);
	}

	public void render() {		
//		Table.drawDebug(this);
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
		getViewport().update(width, height, true);
	}
	
	public Button getGameBrowserButton() {
		return gameBrowser;
	}
	
	public Button getPlayServerButton() {
		return playServer;
	}
	
	public Button getOptionsButton() {
		return options;
	}
	
	public Button getCreditsButton() {
		return options;
	}
	
	public Button getExitButton() {
		return exit;
	}
}
