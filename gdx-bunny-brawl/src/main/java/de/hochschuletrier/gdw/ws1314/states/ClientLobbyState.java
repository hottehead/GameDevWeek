package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.lobby.ServerLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

public class ClientLobbyState extends GameState {
	private static final Logger logger = LoggerFactory.getLogger(ClientLobbyState.class);

	protected ClientLobbyManager clientLobby;
	
    @Override
    public void init (AssetManagerX assetManager) {
        super.init (assetManager);
        
        clientLobby = new ClientLobbyManager(new PlayerData("John", EntityType.Hunter, TeamColor.WHITE, false));
        logger.info("Client-Lobby created.");
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
        // TODO
    }
}
