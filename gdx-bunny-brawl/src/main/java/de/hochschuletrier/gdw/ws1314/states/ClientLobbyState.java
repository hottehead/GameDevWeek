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
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

public class ClientLobbyState extends GameState {
	private static final Logger logger = LoggerFactory.getLogger(ClientLobbyState.class);

	protected ClientLobbyManager clientLobby;
	
	private ConsoleCmd sendPlayerUpdate;
	
    @Override
    public void init (AssetManagerX assetManager) {
        super.init (assetManager);
        
        this.clientLobby = new ClientLobbyManager(new PlayerData(1,"John", EntityType.Hunter, TeamColor.WHITE, false));
        logger.info("Client-Lobby created.");
        
        this.sendPlayerUpdate = new ConsoleCmd("sendPlayerUpdate",0,"[DEBUG]Post playerdata",0) {
			@Override
			public void execute(List<String> args) {
				clientLobby.sendChanges();
			}
		};
		
		Main.getInstance().console.register(this.sendPlayerUpdate);
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
}