package de.hochschuletrier.gdw.ws1314.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.network.ClientIdCallback;
import de.hochschuletrier.gdw.ws1314.network.DisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.LobbyUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.MatchUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 * Created by Sonic
 */

public class ClientLobbyManager implements LobbyUpdateCallback {
	private static final Logger logger = LoggerFactory.getLogger(ClientLobbyManager.class);
	
	private int playerId;
	private PlayerData myData;
	private HashMap<Integer, PlayerData> connectedPlayers;
	private String chosenMap = "";
	
	public ClientLobbyManager(String playerName)
	{
		this.connectedPlayers = new HashMap<>();
		
		this.playerId = 0;
		this.myData = new PlayerData(this.playerId, playerName, EntityType.Hunter, TeamColor.WHITE, false);
		
		NetworkManager.getInstance().setLobbyUpdateCallback(this);
		
		NetworkManager.getInstance().setClientIdCallback(new ClientIdCallback() {
			@Override
			public void clientIdCallback(int playerid) {
				logger.info("Your Unique-PlayerID: " + playerid);
				playerId = playerid;
			}
		});
		
		NetworkManager.getInstance().setMatchUpdateCallback(new MatchUpdateCallback() {
			@Override
			public void matchUpateCallback(String map) {
				chosenMap = map;
			}
		});
	}
	
	public List<PlayerData> getConnectedPlayers() {
		return new ArrayList<PlayerData>(this.connectedPlayers.values());
	}
	
	public List<PlayerData> getTeamPlayers(TeamColor team)
	{
		ArrayList<PlayerData> teamPlayers = new ArrayList<>();
		
		for (PlayerData p : this.connectedPlayers.values())
		{
			if (p.getTeam() == team)
				teamPlayers.add(p);
		}
		
		return teamPlayers;
	}
	
	public String getMap() {
		return chosenMap;
	}
	
	public PlayerData getPlayerData() {
		return myData;
	}
	public void setPlayerData(PlayerData playerData) {
		this.myData = playerData;
	}

	// LobbyUpdateCallback
	@Override
	public void lobbyUpateCallback(String map, PlayerData[] players) {
		this.chosenMap = map;
		
		this.connectedPlayers.clear();
		for (int i = 0; i < players.length; i++) {
			this.connectedPlayers.put(players[i].getPlayerId(), players[i]);
			
			if (this.playerId != 0 && this.playerId == players[i].getPlayerId())
				this.myData = players[i];
		}
		
		for (PlayerData p : this.connectedPlayers.values())
		{
			logger.info("PID: " + p.getPlayerId() + "|Name: " + p.getPlayername() + "|Class: " + p.getType() + "|Team: " + p.getTeam() + "|R: " + p.isAccept());
		}
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
