package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;

public class PlayerData {
	private String playername;
	private EntityType type;
	private byte team;
	private boolean accept;
	
	public PlayerData(String playername, EntityType type, byte team, boolean accept){
		this.playername = playername;
		this.type = type;
		this.team = team;
		this.accept = accept;
	}
	
	public String getPlayername() {
		return playername;
	}
	public EntityType getType() {
		return type;
	}
	public byte getTeam() {
		return team;
	}
	public boolean isAccept() {
		return accept;
	}   	
}