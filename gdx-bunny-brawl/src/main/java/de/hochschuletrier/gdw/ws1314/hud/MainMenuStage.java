package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;
import com.esotericsoftware.tablelayout.Value;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelListElement;
import de.hochschuletrier.gdw.ws1314.states.GameStates;
import de.hochschuletrier.gdw.ws1314.states.MainMenuState;

public class MainMenuStage extends AutoResizeStage {
	
	private BitmapFont font;
	private Skin defaultSkin;
	
	private LevelList levelList;
	private TextButton startButton;
	private TextField playerNameField;
	
	//buttons
	TextButton playServer;
	TextButton playClient;
	TextButton options; 
	TextButton credits;
	TextButton exit;
	
	public MainMenuStage() {
		super();
	}

	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Keys.ENTER) {
			if(playerNameField.getText()!="") {
				Main.getInstance().gamePreferences.putString("player-name", playerNameField.getText());
			}
			return true;
		}
		return super.keyDown(keyCode);
	}

	public void init(AssetManagerX assetManager) {
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		Table uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		font = assetManager.getFont("verdana", 24);
		
		Label playerNameLabel = new Label("Player name: ", defaultSkin);
		uiTable.add(playerNameLabel);		
		playerNameField = new TextField(Main.getInstance().gamePreferences.getString("player-name", "Fluffly Bunny"), defaultSkin);
		playerNameField.setMaxLength(12);
		
		uiTable.add(playerNameField);
		
		
//		uiTable.row().padTop(20);
//		Label label = new Label("escape still works - level list not", defaultSkin);
//		uiTable.add(label);
//		uiTable.row().padTop(20);
		
//		//level list
//		levelList = new LevelList(defaultSkin);
//		//add levels for testing
//		levelList.addLevel("does nothing");
//		levelList.addLevel("new level");
//		uiTable.add(levelList);
//		
//		uiTable.row();
//		
//		//start Button
//		startButton = new TextButton("LADEN", defaultSkin);
		
		uiTable.row().padTop(20);
		
		Table tmpTable = new Table();
		uiTable.add(tmpTable).pad(20);
		
		playClient = new TextButton("Spielen als Client", defaultSkin);
		playServer = new TextButton("Spielen als Server", defaultSkin);
		options = new TextButton("Optionen", defaultSkin);
		credits = new TextButton("Credits", defaultSkin);
		exit = new TextButton("Schließen", defaultSkin);
		
		tmpTable.add(playClient).pad(5).prefSize(50);
		tmpTable.add(playServer).pad(5).prefSize(50);

		uiTable.row();		
		tmpTable = new Table();
		uiTable.add(tmpTable).pad(20);
		
		tmpTable.add(options).pad(5);
		tmpTable.add(credits).pad(5);
		tmpTable.add(exit).pad(5);
		
		uiTable.add(startButton);
	}

	public void render() {		
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		DrawUtil.batch.flush();
		this.draw();
		Table.drawDebug(this);
	}
	
	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
	}
	
	public void addLevel(String levelName) {
		levelList.addLevel(levelName);
	}
	
	public LevelListElement getSelecetedLevel() {
		return levelList.getSelected();
	}
	
	// getter for adding listener to the buttons
	public TextButton getStartButton() {
		return startButton;
	}
	
	public TextButton getPlayClientButton() {
		return playClient;
	}
	
	public TextButton getPlayServerButton() {
		return playClient;
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
