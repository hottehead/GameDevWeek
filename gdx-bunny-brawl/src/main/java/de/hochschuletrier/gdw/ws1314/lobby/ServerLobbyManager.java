package de.hochschuletrier.gdw.ws1314.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.PlayerDisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.PlayerUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 * Created by Sonic
 */

public class ServerLobbyManager implements PlayerUpdateCallback, PlayerDisconnectCallback {
	private static final Logger logger = LoggerFactory.getLogger(ServerLobbyManager.class);
	
	private HashMap<Integer, PlayerData> players;
	private String map;
	private ArrayList<IServerLobbyListener> listener;
	
	public ServerLobbyManager()
	{
		this.players = new HashMap<>();
		this.listener = new ArrayList<>();
		this.map = Main.getInstance().gamePreferences.getString(PreferenceKeys.mapName, "map01");
		NetworkManager.getInstance().setPlayerUpdateCallback(this);
		NetworkManager.getInstance().setPlayerDisconnectCallback(this);
		
		//this.map = Main.getInstance().gamePreferences.getString(PreferenceKeys.mapName, "map01");
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
	
	// PlayerUpdateCallback
	@Override
	public void callback(int playerid, String playerName, EntityType type, TeamColor team, boolean accept) 
	{
		if (this.players.containsKey(playerid)) {
			this.players.remove(playerid);
			logger.info(playerName + " has changed his Settings.");
		} else {
			logger.info(playerName + " has joined the Lobby.");
		}
		
		PlayerData pd = new PlayerData(playerid, playerName, type, team, accept);
		this.players.put(playerid, pd);
		
		sendLobbyUpdateToClients();
		
		if (checkReadiness())
		{
			// change into GameplayState
			logger.info("All Players are ready. [PlayerCount: " + this.players.size() + "]");
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
	
	private void sendLobbyUpdateToClients() {
		NetworkManager.getInstance().sendLobbyUpdate(this.map, this.players.values().toArray(new PlayerData[0]));
	}

	@Override
	public void callback(Integer[] playerid) {
		for (int i = 0; i < playerid.length; i++) {
			this.players.remove(playerid[i]);
		}
		sendLobbyUpdateToClients();
	}
	
}
