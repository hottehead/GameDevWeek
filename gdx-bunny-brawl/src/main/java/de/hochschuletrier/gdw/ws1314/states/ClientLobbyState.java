package de.hochschuletrier.gdw.ws1314.states;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.GameStateCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

public class ClientLobbyState extends GameState implements GameStateCallback {
	private static final Logger logger = LoggerFactory.getLogger(ClientLobbyState.class);

	protected ClientLobbyManager clientLobby;
	
	private ConsoleCmd sendPlayerUpdate;
	private ConsoleCmd cpAccept;
	private ConsoleCmd cpTeam;
	private ConsoleCmd cpClass;
	
    @Override
    public void init (AssetManagerX assetManager) {
        super.init (assetManager);
        
        this.clientLobby = new ClientLobbyManager("John");
        
        // TODO: Temporär nur zum localen Testen
        NetworkManager.getInstance().connect("localhost", 666);
        
        if (NetworkManager.getInstance().isClient())
        	logger.warn("Connection ");
        
        NetworkManager.getInstance().setGameStateCallback(this);
        
        
        logger.info("Client-Lobby created.");
        
        /*
        this.sendPlayerUpdate = new ConsoleCmd("sendPlayerUpdate",0,"[DEBUG]Post playerdata",0) {
			@Override
			public void execute(List<String> args) {
				clientLobby.sendChanges();
			}
		};
		*/
		
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
				super.showUsage("<ClassName>");
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
    public void render () {
        // TODO
    }

    @Override
    public void update (float delta) {
        // TODO
    }

    @Override
    public void dispose () {
    	//Main.getInstance().console.unregister(this.sendPlayerUpdate);
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
}
