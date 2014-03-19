package de.hochschuletrier.gdw.ws1314.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.PlayerUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 * Created by Sonic
 */

public class ServerLobbyManager implements PlayerUpdateCallback {
	private static final Logger logger = LoggerFactory.getLogger(ServerLobbyManager.class);
	
	private HashMap<Integer, PlayerData> players;
	private String map = "";
	private ArrayList<IServerLobbyListener> listener;
	
	public ServerLobbyManager()
	{
		this.players = new HashMap<>();
		this.listener = new ArrayList<>();
		NetworkManager.getInstance().setPlayerUpdateCallback(this);
	}
	
	public List<PlayerData> getPlayers() {
		return new ArrayList<PlayerData>(players.values());
	}
	
	public String getMap() {
		return map;
	}
	
	public void addServerLobbyListener(IServerLobbyListener listener)
	{
		this.listener.add(listener);
	}

	public void removeServerLobbyListener(IServerLobbyListener listener)
	{
		this.listener.remove(listener);
	}
	
	@Override
	public void callback(int playerid, String playerName, EntityType type, TeamColor team, boolean accept) 
	{
		if (this.players.containsKey(playerid)) {
			this.players.remove(playerid);
			logger.info(playerName + " has changed Settings.");
		} else {
			logger.info(playerName + " has joined the Lobby.");
		}
		
		PlayerData pd = new PlayerData(playerid, playerName, type, team, accept);
		this.players.put(playerid, pd);
		
		NetworkManager.getInstance().sendLobbyUpdate(this.map, this.players.values().toArray(new PlayerData[0]));
		
		if (checkReadiness())
		{
			// change into GameplayState
			logger.info("All Players are ready.");
			fireStartGame();
		}
	}
	
	private boolean checkReadiness()
	{
		for(PlayerData p : this.players.values())
		{
			if (!p.isAccept())
				return false;
		}
		return true;
	}
	
	private void fireStartGame()
	{
		for(IServerLobbyListener l : this.listener)
		{
			l.startGame();
		}
	}
	
}
