package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.lobby.ClientLobbyManager;
import de.hochschuletrier.gdw.ws1314.lobby.IServerLobbyListener;
import de.hochschuletrier.gdw.ws1314.lobby.ServerLobbyManager;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;

public class ServerLobbyState extends GameState implements IServerLobbyListener {
	private static final Logger logger = LoggerFactory.getLogger(ServerLobbyState.class);
	
	protected ServerLobbyManager serverLobby;
	
    @Override
    public void init (AssetManagerX assetManager) {
        super.init (assetManager);
       
    	serverLobby = new ServerLobbyManager();
    	logger.info("Server-Lobby created.");
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

	@Override
	public void startGame() {
		((ServerGamePlayState) GameStates.SERVERGAMEPLAY.get()).setPlayerDatas(this.serverLobby.getPlayers());
		GameStates.SERVERGAMEPLAY.init(assetManager);
		GameStates.SERVERGAMEPLAY.activate();
	}
}
