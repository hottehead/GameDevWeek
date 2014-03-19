package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

public class PlayerData {
	private int id;
	private String playername;
	private EntityType type;
	private TeamColor team;
	private boolean accept;
	
	public PlayerData(int id, String playername, EntityType type, TeamColor team, boolean accept){
		this.playername = playername;
		this.type = type;
		this.team = team;
		this.accept = accept;
	}
	
	public int getId(){
		return id;
	}
	
	public String getPlayername() {
		return playername;
	}
	public EntityType getType() {
		return type;
	}
	public TeamColor getTeam() {
		return team;
	}
	public boolean isAccept() {
		return accept;
	}
	
	public String toString() {
		return playername + " " + type.toString() + " " + team.toString() + " " + accept;
	}
}