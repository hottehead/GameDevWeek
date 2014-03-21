package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelListElement;

public class MainMenuStage extends AutoResizeStage {
	
	private BitmapFont font;
	private Skin defaultSkin;
	
	private LevelList levelList;
	private TextButton startButton;
	private TextField playerNameField;
	
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
		uiTable.row().padTop(20);
		Label label = new Label("escape still works - level list not", defaultSkin);
		uiTable.add(label);
		uiTable.row().padTop(20);
		
		//level list
		levelList = new LevelList(defaultSkin);
		//add levels for testing
		levelList.addLevel("does nothing");
		levelList.addLevel("new level");
		uiTable.add(levelList);
		
		uiTable.row();
		
		//start Button
		startButton = new TextButton("LADEN", defaultSkin);
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
	
	public TextButton getStartButton() {
		return startButton;
	}
}
