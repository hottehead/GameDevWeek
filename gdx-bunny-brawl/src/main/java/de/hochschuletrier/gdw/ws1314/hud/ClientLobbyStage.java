package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;
import com.esotericsoftware.tablelayout.Cell;
import com.esotericsoftware.tablelayout.Value.CellValue;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelListElement;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

public class ClientLobbyStage extends AutoResizeStage {
	
	private BitmapFont font;
	private Skin defaultSkin;
	private ClientLobbyManager clientManager;
	
	private TextButton readyButton;
	
	private Table playerList;
	
	public ClientLobbyStage(ClientLobbyManager manager) {
		super();
		this.clientManager = manager;
	}

	public void init(AssetManagerX assetManager) {
		//init generic stuff
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		Table uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		font = assetManager.getFont("verdana", 24);
		
		//info
		Label label = new Label("===   Client Lobby   ===", defaultSkin);
		uiTable.add(label);
		uiTable.row().pad(20).bottom();
		
		this.playerList = new Table();
		this.playerList.setFillParent(false);
		this.playerList.debug(Debug.all);
		uiTable.add(this.playerList);
		
		uiTable.row().pad(20);
		
		//start Button
		readyButton = new TextButton("Waschmaschine kaufen! Jetzt!", defaultSkin);
		uiTable.add(readyButton);
	}

	public void render() {		
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());
		
		this.playerList.reset();
		this.playerList.add(new Label("Team WHITE", defaultSkin));
		this.playerList.add(new Label("Team Black", defaultSkin));
		
		for (PlayerData p : this.clientManager.getTeamPlayers(TeamColor.WHITE))
		{
			this.playerList.row();
			this.playerList.add(new Label(p.getPlayername(), defaultSkin)).padRight(20);
			this.playerList.add(new Label(p.getType().toString(), defaultSkin)).padRight(20);
			this.playerList.add(new Label("" + p.isAccept(), defaultSkin));
		}
		
		DrawUtil.batch.flush();
		this.draw();
		Table.drawDebug(this);
	}
	
	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
	}
	
	public TextButton getReadyButton() {
		return readyButton;
	}
}
