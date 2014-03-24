package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.hud.elements.ChatWindow;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

public class ClientLobbyStage extends Stage implements ScreenListener {
	
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
		
		this.defaultSkin = assetManager.getSkin("default");

		Table root = new Table();
		root.setFillParent(true); // ganzen platz in Tabelle nutzen
		root.debug(Debug.all); //debug output
		this.addActor(root);
		font = assetManager.getFont("verdana", 24);
		/* Zeile 1*/
		root.row();
		
		/* Zeile 2*/
		root.row();
		/* Zeile 3*/
		root.row();
		/* Zeile 4*/
		root.row();
	}

	public void render() {		
		Table.drawDebug(this);
		this.act();
		this.draw();
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

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
	}
}
