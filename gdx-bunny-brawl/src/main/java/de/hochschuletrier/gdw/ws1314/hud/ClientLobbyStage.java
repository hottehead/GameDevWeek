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
import de.hochschuletrier.gdw.ws1314.hud.elements.ChatWindow;
import de.hochschuletrier.gdw.ws1314.hud.elements.LevelList;
import de.hochschuletrier.gdw.ws1314.hud.elements.ListElement;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.DisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

public class ClientLobbyStage extends AutoResizeStage {
	
	private BitmapFont font;
	private Skin defaultSkin;
	private ClientLobbyManager clientManager;
	
	private TextButton readyButton;
	private TextButton swapTeamButton;
	private TextButton disonnectButton;
	
	private Table whiteTeamList;
	private Table blackTeamList;
	
	public ClientLobbyStage(ClientLobbyManager manager) {
		super();
		this.clientManager = manager;
	}

	public void init(AssetManagerX assetManager) {
		
		//init generic stuff
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		Table uiTable = new Table();
		uiTable.debug(Debug.all);
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); //debug output
		this.addActor(uiTable);
		font = assetManager.getFont("verdana", 24);
		
		//info
		Label label = new Label("===>   Client Lobby   <===", defaultSkin);
		uiTable.add(label);
		uiTable.row().pad(20).bottom();
		
		//Player-Liste
		Table playerList = new Table();
		playerList.setFillParent(false);
		playerList.debug(Debug.all);
		playerList.top();
		
		Table whiteList = new Table();
		whiteList.padRight(20);
		whiteList.add(new Label("Team WHITE", defaultSkin)).row();
		this.whiteTeamList = new Table();
		whiteList.add(this.whiteTeamList);
		
		Table blackList = new Table();
		blackList.top();
		blackList.add(new Label("Team Black", defaultSkin)).row();
		this.blackTeamList = new Table();
		blackList.add(this.blackTeamList);
		
		playerList.add(whiteList);
		playerList.add(blackList);
		
		uiTable.add(playerList);
		
		uiTable.row().pad(20);
		
		//PlayerSettings change
		swapTeamButton = new TextButton("Swap Team", defaultSkin);
		uiTable.add(swapTeamButton);
		uiTable.padRight(100);
		readyButton = new TextButton("Ready!", defaultSkin);
		uiTable.add(readyButton).row();
		
		//Disconnect
		disonnectButton = new TextButton("Disconnect", defaultSkin);
		uiTable.add(disonnectButton);
		uiTable.add(new ChatWindow(defaultSkin));
	}

	public void render() {		
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		Table.drawDebug(this);
		this.act(Gdx.graphics.getDeltaTime());
		
		this.whiteTeamList.reset();
		
		for (PlayerData p : this.clientManager.getTeamPlayers(TeamColor.WHITE))
		{
			this.whiteTeamList.row();
			this.whiteTeamList.add(new Label(p.getPlayername(), defaultSkin)).padRight(20);
			this.whiteTeamList.add(new Label(p.getType().toString(), defaultSkin)).padRight(20);
			this.whiteTeamList.add(new Label("" + p.isAccept(), defaultSkin));
		}
		
		this.blackTeamList.reset();
		
		for (PlayerData p : this.clientManager.getTeamPlayers(TeamColor.BLACK))
		{
			this.blackTeamList.row();
			this.blackTeamList.add(new Label(p.getPlayername(), defaultSkin)).padRight(20);
			this.blackTeamList.add(new Label(p.getType().toString(), defaultSkin)).padRight(20);
			this.blackTeamList.add(new Label("" + p.isAccept(), defaultSkin));
		}
		
		DrawUtil.batch.flush();
		this.draw();
		//Table.drawDebug(this);
	}
	
	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
	}
	
	public TextButton getReadyButton() {
		return readyButton;
	}
	
	public TextButton getSwapTeamButton() {
		return swapTeamButton;
	}
	
	public TextButton getDisconnectButton() {
		return this.disonnectButton;
	}
}
