package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.ListElement;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;

public class FinishedGameStage extends Stage implements ScreenListener {
	
	private BitmapFont font;
	private Skin defaultSkin;
	
	private String finishStateText;
	private int blackTeamScore;
	private int whiteTeamScore;
	
	private Table uiTable;
	
	//server-client-testing
	private TextButton backButton;
	
	public FinishedGameStage() {
		super();
	}

	AssetManagerX assetManager;

	public void init(AssetManagerX assetManager) {
		this.assetManager = assetManager;
		
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		uiTable = new Table();
		
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		
		Label finishStateLabel= new Label(this.finishStateText, defaultSkin);
		uiTable.add(finishStateLabel).row().row();
		
		uiTable.add(new Label("= Score =", defaultSkin)).row();
		
		Label teamScores = new Label("Team WHITE: " + this.whiteTeamScore + " | " + "Team BLACK: " + this.blackTeamScore, defaultSkin);
		uiTable.add(teamScores).row().row();
		
		//testing server-client stuff
		backButton = new TextButton("zurück zum Menü", defaultSkin);
		uiTable.add(backButton);
	}

	public void render() {		
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		DrawUtil.batch.flush();
		this.draw();
		//Table.drawDebug(this);
	}
	
	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
	}
	
	public TextButton getBackButton() {
		return backButton;
	}
	
	public void setWhiteTeamScore(int score)
	{
		this.whiteTeamScore = score;
	}
	
	public void setBlackTeamSore(int score)
	{
		this.blackTeamScore = score;
	}
	
	public void setFinishStateText(String text)
	{
		this.finishStateText = text;
	}
	
	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);

	}
}
