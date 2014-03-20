package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

public class PlayerData {
	private int id;
	private String playername;
	private EntityType type;
	private TeamColor team;
	private boolean accept;
	private long entityid;
	
	public PlayerData(int id, String playername, EntityType type, TeamColor team, boolean accept){
		this(id,playername,type,team,accept, 0);
	}
	public PlayerData(int id, String playername, EntityType type, TeamColor team, boolean accept, long entityid){
		this.id = id;
		this.playername = playername;
		this.type = type;
		this.team = team;
		this.accept = accept;
		this.entityid = entityid;
	}
	
	public void setEntityId(long entityid){
		this.entityid = entityid;
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
	public long getEntityId() {
		return entityid;
	}
	
	public String toString() {
		return "id: " + id + " name: " + playername + " type:" + type.toString() + " team:" + team.toString() + " " + accept;
	}
}