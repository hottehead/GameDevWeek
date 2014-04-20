package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.hud.elements.ChatWindow;
import de.hochschuletrier.gdw.ws1314.hud.elements.ListElement;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

public class ClientLobbyStage extends Stage implements ScreenListener {

	private Skin defaultSkin;
	private ClientLobbyManager clientManager;

	private Button readyButton;
	private TextButton swapTeamButton;
	private Button disonnectButton;
	Button whiteHunter, whiteKnight, whiteTank, blackHunter, blackTank, blackKnight;
	HorizontalGroup bunnies, whiteBunnies, blackBunnies;
	List<ListElement> playerList;
	Table uiTable, playerPanel;
	ButtonGroup whites, blacks;

	public ClientLobbyStage(ClientLobbyManager manager) {
		super();
		this.clientManager = manager;
	}

	public void init(AssetManagerX assetManager) {
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		Skin bunny = assetManager.getSkin("bunnyBrawl");
		uiTable = new Table();
		uiTable.debug(Debug.all);
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		uiTable.debug(Debug.all); // debug output
		uiTable.defaults().expand();
		this.addActor(uiTable);
//		uiTable.setBackground(bunny.getDrawable("white"));
		//Map
		uiTable.add(new Label("Next Map: " + clientManager.getMap(), bunny, "garfield")).colspan(2)
				.center();
		//Countdwn
		uiTable.add(new Label("COUNTDOWN", bunny, "garfield"));
		uiTable.row();
		//Header
		uiTable.add(new Label("Choose your Character", bunny,"garfield")).colspan(3);
		uiTable.row();
		// Bunnie Wappen
		bunnies = new HorizontalGroup();
		whites = new ButtonGroup();
		whites.setMinCheckCount(1);
		whites.setMaxCheckCount(1);
		whiteBunnies = new HorizontalGroup();
		whiteHunter = new Button(bunny, "whiteHunter");
		whiteKnight = new Button(bunny, "whiteKnight");
		whiteTank = new Button(bunny, "whiteTank");
		whiteHunter.addListener(new ChangeClassListener(EntityType.Hunter));
		whiteKnight.addListener(new ChangeClassListener(EntityType.Knight));
		whiteTank.addListener(new ChangeClassListener(EntityType.Tank));
		whiteBunnies.addActor(whiteHunter);
		whiteBunnies.addActor(whiteKnight);
		whiteBunnies.addActor(whiteTank);
		whites.add(whiteHunter);
		whites.add(whiteKnight);
		whites.add(whiteTank);

		blackBunnies = new HorizontalGroup();
		blacks = new ButtonGroup();
		blacks.setMinCheckCount(1);
		blacks.setMaxCheckCount(1);
		blackTank = new Button(bunny, "blackTank");
		blackKnight = new Button(bunny, "blackKnight");
		blackHunter = new Button(bunny, "blackHunter");
		blackHunter.addListener(new ChangeClassListener(EntityType.Hunter));
		blackKnight.addListener(new ChangeClassListener(EntityType.Knight));
		blackTank.addListener(new ChangeClassListener(EntityType.Tank));
		blackBunnies.addActor(blackHunter);
		blackBunnies.addActor(blackKnight);
		blackBunnies.addActor(blackTank);
		blacks.add(blackHunter);
		blacks.add(blackKnight);
		blacks.add(blackTank);

		bunnies.addActor(whiteBunnies);
		bunnies.addActor(blackBunnies);
		uiTable.add(bunnies).colspan(3).center();
		uiTable.row();
		// ChatWindow
		uiTable.add(new ChatWindow(defaultSkin)).bottom().left().fill();
		// PlayerSettings change
		VerticalGroup buttons = new VerticalGroup();
		swapTeamButton = new TextButton("Change Team", defaultSkin);
		swapTeamButton.addListener(new SwapTeamClick());
		buttons.addActor(swapTeamButton);
		readyButton = new Button(bunny, "ready");
		readyButton.addListener(new AcceptClick());
		buttons.addActor(readyButton);
		// Disconnect
		disonnectButton = new Button(bunny, "quitServer");
		disonnectButton.addListener(new DisconnectClick());
		buttons.addActor(disonnectButton);
		uiTable.add(buttons);
		// Connected PLayers Panl
		playerPanel = new Table();
		playerPanel.debug(Debug.all);
		playerPanel.add(new Label("Player", bunny,"garfield"));
		playerPanel.row();
		playerList = new List<ListElement>(defaultSkin);
		playerPanel.add(playerList).fill().expand();
		uiTable.add(playerPanel).fill();

		if (clientManager.getPlayerData().getTeam() == TeamColor.WHITE) {
			whiteBunnies.setVisible(true);
			blackBunnies.setVisible(false);
		} else {
			whiteBunnies.setVisible(false);
			blackBunnies.setVisible(true);
		}
	}

	public void render() {
//		Table.drawDebug(this);
		this.act();
		playerList.getItems().clear();
		for (PlayerData p : clientManager.getTeamPlayers(TeamColor.WHITE)) {
			String ready ="";
			if (p.isAccept()) {
				ready =" - READY";
			}
			playerList.getItems().add(new ListElement(p.getPlayername() +ready, defaultSkin));
		}
		for (PlayerData p : clientManager.getTeamPlayers(TeamColor.BLACK)) {
			String ready ="";
			if (p.isAccept()) {
				ready ="READY";
			}
			playerList.getItems().add(new ListElement(p.getPlayername()+ready, defaultSkin));
		}
		this.draw();
	}

	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = assetManager.getSkin("default");
	}

	private class ChangeClassListener extends ClickListener {
		EntityType type;

		public ChangeClassListener(EntityType type) {
			// TODO Auto-generated constructor stu
			this.type = type;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			clientManager.changeEntityType(type);
		}
	}

	public Button getReadyButton() {
		return readyButton;
	}

	public Button getSwapTeamButton() {
		return swapTeamButton;
	}

	public Button getDisconnectButton() {
		return this.disonnectButton;
	}

	private class AcceptClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			clientManager.toggleReadyState();
		}
	}

	private class SwapTeamClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (clientManager.getPlayerData().getTeam() == TeamColor.WHITE) {
				whiteBunnies.setVisible(false);
				blackBunnies.setVisible(true);
				if (clientManager.getPlayerData().getType() == EntityType.Hunter) {
					blackHunter.setChecked(true);
				} else if (clientManager.getPlayerData().getType() == EntityType.Tank) {
					blackTank.setChecked(true);
				} else {
					blackKnight.setChecked(true);
				}
			} else {
				whiteBunnies.setVisible(true);
				blackBunnies.setVisible(false);
				if (clientManager.getPlayerData().getType() == EntityType.Hunter) {
					whiteHunter.setChecked(true);
				} else if (clientManager.getPlayerData().getType() == EntityType.Tank) {
					whiteTank.setChecked(true);
				} else {
					whiteKnight.setChecked(true);
				}
			}
			clientManager.swapTeam();
		}

	}

	private class DisconnectClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			NetworkManager.getInstance().disconnectFromServer();
		}
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
		uiTable.invalidateHierarchy();
	}
}
