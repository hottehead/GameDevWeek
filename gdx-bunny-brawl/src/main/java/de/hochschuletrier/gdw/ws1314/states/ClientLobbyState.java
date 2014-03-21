package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.hud.ClientLobbyStage;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.ClientIdCallback;
import de.hochschuletrier.gdw.ws1314.network.GameStateCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClientLobbyState extends GameState implements GameStateCallback {

	private static final Logger logger = LoggerFactory.getLogger(ClientLobbyState.class);

	protected ClientLobbyManager clientLobby;
	
	private ClientLobbyStage stage;
	
	// Listener
	private AcceptClick acceptClick;
	
	// Console Commands
	private ConsoleCmd sendPlayerUpdate;
	private ConsoleCmd cpAccept;
	private ConsoleCmd cpTeam;
	private ConsoleCmd cpClass;
	
    @Override
    public void init (AssetManagerX assetManager) {
        super.init (assetManager);
        
	    this.acceptClick = new AcceptClick();
    }

    @Override
    public void render () {
    	this.stage.render();
    }

    @Override
    public void update (float delta) {
        // TODO
    }

    @Override
    public void dispose () {
    	
    }
    
    // GameStateCallback
	@Override
	public void callback(GameStates gameStates) {
		logger.info("GameStateChange received");
		if (gameStates == GameStates.CLIENTGAMEPLAY)
		{
			gameStates.init(assetManager);
			gameStates.activate();
			logger.info("ClientGamePlayState activated.");
		}
	}
	
	private class AcceptClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			clientLobby.toggleReadyState();
		}
    }
	
	@Override
	public void onEnter() {
    	super.onEnter();
    	
    	this.clientLobby = new ClientLobbyManager(Main.playerName);
    	
    	this.stage = new ClientLobbyStage(this.clientLobby);
	    this.stage.init(assetManager);
    	
	    // TODO: Temporär nur zum localen Testen
	    if (!NetworkManager.getInstance().isClient())
	    {
	        NetworkManager.getInstance().connect("localhost", NetworkManager.getInstance().getDefaultPort());
	        
	        if (!NetworkManager.getInstance().isClient()) {
	        	logger.warn("Connection could not be established! Server maybe not running.");
	        	GameStates.MAINMENU.init(assetManager);
	        	GameStates.MAINMENU.activate();
	        	return;
	        }
	    }
	    
	    this.clientLobby.sendChanges();
	    
	    NetworkManager.getInstance().setGameStateCallback(this);
	    
	    logger.info("Client-Lobby entered.");
	    
	    /*
	    this.sendPlayerUpdate = new ConsoleCmd("sendPlayerUpdate",0,"[DEBUG]Post playerdata",0) {
			@Override
			public void execute(List<String> args) {
				clientLobby.sendChanges();
			}
		};
		*/
	    
	    this.stage.getReadyButton().addListener(this.acceptClick);
	    
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
		//Main.getInstance().console.register(this.sendPlayerUpdate);
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
		
		this.stage.getReadyButton().removeListener(this.acceptClick);
		
		this.clientLobby = null;
		this.stage = null;
	}

	@Override
	public void onLeaveComplete() {
		// TODO Auto-generated method stub
		super.onLeaveComplete();
	}
}
