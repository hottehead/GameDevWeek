package de.hochschuletrier.gdw.ws1314.lobby;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.network.LobbyUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.MatchUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;

/**
 * Created by Sonic
 */

public class ClientLobbyManager implements LobbyUpdateCallback, MatchUpdateCallback {
	private static final Logger logger = LoggerFactory.getLogger(ClientLobbyManager.class);
	
	private PlayerData myData;
	private List<PlayerData> connectedPlayers;
	private String map = "";
	
	public ClientLobbyManager(PlayerData playerData)
	{
		NetworkManager.getInstance().setLobbyUpdateCallback(this);
		this.connectedPlayers = new ArrayList<PlayerData>();
		this.myData = playerData;
		sendChanges();
	}
	
	public List<PlayerData> getConnectedPlayers() {
		return connectedPlayers;
	}
	
	public String getMap() {
		return map;
	}
	
	public PlayerData getPlayerData() {
		return myData;
	}
	public void setPlayerData(PlayerData playerData) {
		this.myData = playerData;
	}

	// LobbyUpdateCallback
	@Override
	public void callback(String map, PlayerData[] players) {
		this.map = map;
		
		this.connectedPlayers.clear();
		for (int i = 0; i < players.length; i++) {
			this.connectedPlayers.add(players[i]);
		}
		logger.info("Current Map: " + this.map);
		
		for (PlayerData p : this.connectedPlayers)
		{
			logger.info("PID: " + p.getPlayerId() + "|Name: " + p.getPlayername() + "|Class: " + p.getType() + "|Team: " + p.getTeam() + "|R: " + p.isAccept());
		}
	}
	
	// MatchUpdateCallback
	@Override
	public void callback(String map) {
		this.map = map;
	}
	
	public void sendChanges() {
		NetworkManager.getInstance().sendPlayerUpdate(myData.getPlayername(), myData.getType(), myData.getTeam(), myData.isAccept());
	}
	
	public void toggleReadyState()
	{
		this.myData.toggleAccept();
		sendChanges();
	}

	public void swapTeam() {
		this.myData.swapTeam();
		sendChanges();
	}
	
	public void changeEntityType(EntityType newType)
	{
		this.myData.changeEntityType(newType);
		logger.info("New Player Class: " + this.myData.getType());
		sendChanges();
	}
}
