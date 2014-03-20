package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

public class PlayerData {
	private int playerid;
	private String playername;
	private EntityType type;
	private TeamColor team;
	private boolean accept;
	private long entityid;
	
	public PlayerData(int playerid, String playername, EntityType type, TeamColor team, boolean accept){
		this(playerid,playername,type,team,accept, 0);
	}
	public PlayerData(int playerid, String playername, EntityType type, TeamColor team, boolean accept, long entityid){
		this.playerid = playerid;
		this.playername = playername;
		this.type = type;
		this.team = team;
		this.accept = accept;
		this.entityid = entityid;
	}
	
	public void setEntityId(long entityid){
		this.entityid = entityid;
	}
	
	public int getPlayerId(){
		return playerid;
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
	
	public boolean toggleAccept()
	{
		this.accept = !this.accept;
		return this.accept;
	}
	
	public TeamColor swapTeam()
	{
		if (this.team == TeamColor.WHITE)
			this.team = TeamColor.BLACK;
		else
			this.team = TeamColor.WHITE;
		
		return this.team;
	}
	
	public void changeEntityType(EntityType newType)
	{
		this.type = newType;
	}
	
	public String toString() {
		return "id: " + playerid + " name: " + playername + " type:" + type.toString() + " team:" + team.toString() + " " + accept;
	}
}
