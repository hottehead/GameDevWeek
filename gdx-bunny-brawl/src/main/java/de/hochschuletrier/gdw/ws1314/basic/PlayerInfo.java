package de.hochschuletrier.gdw.ws1314.basic;

import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

public class PlayerInfo {
	protected String name;
	protected boolean isReady;
	protected TeamColor teamColor;
	//protected CharacterClass charClass;
	
	public PlayerInfo(String name) {
		this(name, TeamColor.WHITE);
	}
	
	public PlayerInfo(String name, TeamColor team) {
		this.name = name;
		this.teamColor = team;
		this.isReady = false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public TeamColor getTeam() {
		return this.teamColor;
	}
	public void setTeam(TeamColor teamColor) {
		this.teamColor = teamColor;
	}
	
	public void changeTeam()
	{
		if (this.teamColor == TeamColor.WHITE)
			this.teamColor = TeamColor.BLACK;
		else
			this.teamColor = TeamColor.WHITE;
	}
	
	public boolean isReady() {
		return isReady;
	}
	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
	
	public boolean toggleReadyState()
	{
		this.isReady = !this.isReady;
		return this.isReady;
	}
	
}
