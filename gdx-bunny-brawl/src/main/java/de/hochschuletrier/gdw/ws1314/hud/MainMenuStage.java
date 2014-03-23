package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.ListElement;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;

public class MainMenuStage extends AutoResizeStage {
	
	private BitmapFont font;
	private Skin defaultSkin;
	
	private LevelList levelList;
	
	private TextField playerNameField;
	
	private Table uiTable;
	
	//buttons
	private TextButton playServer;
	private TextButton gameBrowser;
	private TextButton options; 
	private TextButton credits;
	private TextButton exit;
	
	//test
	TextButton startServerAndPlay;
	
	public MainMenuStage() {
		super();
	}

	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Keys.ENTER) {
			if(playerNameField.getText()!="") {
				Main.getInstance().gamePreferences.putString(PreferenceKeys.playerName, playerNameField.getText());
			}
			return true;
		}
		return super.keyDown(keyCode);
	}

	AssetManagerX assetManager;

	public void init(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
		uiTable = new Table();
		this.assetManager = assetManager;
		
		Main.inputMultiplexer.addProcessor(this);
		
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		
		Label playerNameLabel = new Label("Player name: ", defaultSkin);
		uiTable.add(playerNameLabel);

		uiTable.add(playerNameLabel);		
		playerNameField = new TextField(Main.getInstance().gamePreferences.getString(PreferenceKeys.playerName, "Fluffly Bunny"), defaultSkin);
		playerNameField.setMaxLength(12);
		
		uiTable.add(playerNameField);
		uiTable.row().padTop(20);
		Label label = new Label("Welcome to the League of Bunny Brawllllll!!!111!!1111", defaultSkin);
		uiTable.add(label);
		
		uiTable.row().padTop(20);
		
		Table tmpTable = new Table();
		uiTable.add(tmpTable).pad(20);
		
		gameBrowser = new TextButton("Spielen als Client", defaultSkin);
		playServer = new TextButton("Spielen als Server", defaultSkin);
		options = new TextButton("Optionen", defaultSkin);
		credits = new TextButton("Credits", defaultSkin);
		exit = new TextButton("Beenden", defaultSkin);
		
		tmpTable.add(gameBrowser).pad(5).prefSize(50);
		tmpTable.add(playServer).pad(5).prefSize(50);

		uiTable.row();		
		tmpTable = new Table();
		uiTable.add(tmpTable).pad(20);
		
		tmpTable.add(options).pad(5);
		tmpTable.add(credits).pad(5);
		tmpTable.add(exit).pad(5);
		
		//testing server-client stuff
		startServerAndPlay = new TextButton("Teststart", defaultSkin);
		tmpTable.add(startServerAndPlay);
	}

	public void render() {		
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		DrawUtil.batch.flush();
		this.draw();
		//Table.drawDebug(this);
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
		if(this.xScale >0 && this.yScale>0)
			uiTable.setScale(this.xScale, this.yScale);
	}
	
	public TextButton getGameBrowserButton() {
		return gameBrowser;
	}
	
	public TextButton getPlayServerButton() {
		return playServer;
	}	
	
	public TextButton getStartServerAndPlayButton() {
		return startServerAndPlay;
	}
	
	public TextButton getOptionsButton() {
		return options;
	}
	
	public TextButton getCreditsButton() {
		return options;
	}
	
	public TextButton getExitButton() {
		return exit;
	}
}
