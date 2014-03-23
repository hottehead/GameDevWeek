package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.hud.ClientLobbyStage;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.DisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.GameStateCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClientLobbyState extends GameState implements GameStateCallback, DisconnectCallback {

	private static final Logger logger = LoggerFactory.getLogger(ClientLobbyState.class);

	protected ClientLobbyManager clientLobby;
	
	private ClientLobbyStage stage;
	
	// Listener
	private AcceptClick acceptClick;
	private SwapTeamClick swapTeamClick;
	private DisconnectClick disconnectClick;
	
	// Console Commands
	private ConsoleCmd sendPlayerUpdate;
	private ConsoleCmd cpAccept;
	private ConsoleCmd cpTeam;
	private ConsoleCmd cpClass;
	
    @Override
    public void init (AssetManagerX assetManager) {
        super.init (assetManager);
        
	    this.acceptClick = new AcceptClick();
	    this.swapTeamClick = new SwapTeamClick();
	    this.disconnectClick = new DisconnectClick();
    }

    @Override
    public void render () {
    	this.stage.render();
    }

    @Override
    public void update (float delta) {
    }

    @Override
    public void dispose () {
    }
    
    // GameStateCallback
	@Override
	public void gameStateCallback(GameStates gameStates) {
		logger.info("GameStateChange received");
		if (gameStates == GameStates.CLIENTGAMEPLAY)
		{
			
			((ClientGamePlayState) gameStates.get()).setMapName(this.clientLobby.getMap());
			ClientEntityManager.getInstance().setPlayerData(this.clientLobby.getPlayerData());
			gameStates.init(assetManager);
			gameStates.activate();
			logger.info("ClientGamePlayState activated.");
		}
	}
	
	@Override
	public void onEnter() {
    	super.onEnter();
    	
    	this.clientLobby = new ClientLobbyManager(Main.getInstance().gamePreferences.getString(PreferenceKeys.playerName, "Player"));
    	
    	this.stage = new ClientLobbyStage(this.clientLobby);
	    this.stage.init(assetManager);
    	
	    this.clientLobby.sendChanges();
	    
	    NetworkManager.getInstance().setGameStateCallback(this);
	    NetworkManager.getInstance().setDisconnectCallback(this);
	    
	    logger.info("Client-Lobby entered.");
	    
	    // ButtonEvents
	    this.stage.getReadyButton().addListener(this.acceptClick);
	    this.stage.getSwapTeamButton().addListener(this.swapTeamClick);
	    this.stage.getDisconnectButton().addListener(this.disconnectClick);
	    
		this.cpAccept = new ConsoleCmd("cpAccept",0,"[DEBUG]",0) {
			@Override
			public void execute(List<String> args) {
				clientLobby.toggleReadyState();
			}
		};
		
		this.cpTeam = new ConsoleCmd("cpTeam",0,"[DEBUG]",0) {
			@Override
			public void execute(List<String> args) {
				clientLobby.swapTeam();
			}
		};
		
		this.cpClass = new ConsoleCmd("cpClass",0,"[DEBUG]",1) {
			
			@Override
			public void showUsage() {
				super.showUsage("<ClassName> => [hunter, knight, tank]");
			}
	
			@Override
			public void execute(List<String> args) {
				EntityType t;
				switch(args.get(1))
				{
				case "hunter":
					t = EntityType.Hunter;
					break;
				case "knight":
					t = EntityType.Knight;
					break;
				case "tank":
					t = EntityType.Tank;
					break;
				default:
					t = EntityType.Hunter;
				}
				
				clientLobby.changeEntityType(t);
			}
		};
		
		Main.getInstance().console.register(this.cpAccept);
		Main.getInstance().console.register(this.cpTeam);
		Main.getInstance().console.register(this.cpClass);
		
		stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(!NetworkManager.getInstance().isClient()){
			onLeave();
		}
    }

	@Override
	public void onEnterComplete() {
		// TODO Auto-generated method stub
		super.onEnterComplete();
	}

	@Override
	public void onLeave() {
		super.onLeave();
		
		Main.getInstance().console.unregister(this.cpAccept);
		Main.getInstance().console.unregister(this.cpTeam);
		Main.getInstance().console.unregister(this.cpClass);
		
		NetworkManager.getInstance().setGameStateCallback(null);
		NetworkManager.getInstance().setDisconnectCallback(null);
		
		// Remove ButtonListener
		this.stage.getReadyButton().removeListener(this.acceptClick);
		this.stage.getSwapTeamButton().removeListener(this.swapTeamClick);
	    this.stage.getDisconnectButton().removeListener(this.disconnectClick);
		
	    Main.inputMultiplexer.removeProcessor(this.stage);
	    
		this.clientLobby = null;
		this.stage = null;
	}

	@Override
	public void onLeaveComplete() {
		// TODO Auto-generated method stub
		super.onLeaveComplete();
	}
	
	private class AcceptClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			clientLobby.toggleReadyState();
		}
    }
	
	private class SwapTeamClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			clientLobby.swapTeam();
		}
		
	}
	
	private class DisconnectClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			NetworkManager.getInstance().disconnectFromServer();
		}
	}
	
	@Override
	public void disconnectCallback(String msg) {
		logger.warn(msg);
		GameStates.MAINMENU.init(assetManager);
		GameStates.MAINMENU.activate();
	}
}
