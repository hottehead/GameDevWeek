package de.hochschuletrier.gdw.ws1314.lobby;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.PlayerUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

/**
 * Created by Sonic
 */

public class ServerLobbyManager implements PlayerUpdateCallback {
	private static final Logger logger = LoggerFactory.getLogger(ServerLobbyManager.class);
	
	private List<PlayerData> players;
	private String map = "";
	
	public ServerLobbyManager()
	{
		this.players = new ArrayList<PlayerData>();
		NetworkManager.getInstance().setPlayerUpdateCallback(this);
	}
	
	
	public List<PlayerData> getPlayers() {
		return players;
	}
	
	public String getMap() {
		return map;
	}


	@Override
	public void callback(String playerName, EntityType type, TeamColor team, boolean accept) 
	{
		PlayerData playerData = new PlayerData(playerName, type, team, accept);
		
		logger.info(playerName + " has joined the Lobby.");
		
		this.players.add(playerData);
		
		PlayerData[] p = new PlayerData[this.players.size()];
		
		NetworkManager.getInstance().sendLobbyUpdate(this.map, this.players.toArray(p));
		
	}
	
}
