package de.hochschuletrier.gdw.ws1314.basic;

import java.util.ArrayList;


public class PlayerInfo {
	protected String name;
	protected boolean isReady;
	protected Team team;
	//protected CharacterClass charClass;
	
	protected ArrayList<PlayerInfoListener> playerListener;
	
	public PlayerInfo(String name) {
		this(name, Team.WHITE);
	}
	
	public PlayerInfo(String name, Team team) {
		this.name = name;
		this.team = team;
		this.isReady = false;
		this.playerListener = new ArrayList<PlayerInfoListener>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		this.fireNameChanged();
	}
	
	public Team getTeam() {
		return this.team;
	}
	public void setTeam(Team team) {
		this.team = team;
		this.fireTeamChanged();
	}
	
	public void changeTeam()
	{
		if (this.team == Team.WHITE)
			this.team = Team.BLACK;
		else
			this.team = Team.WHITE;
		
		this.fireTeamChanged();
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
		this.fireReadyStateChanged();
		return this.isReady;
	}
	
	protected void fireTeamChanged()
	{
		for (PlayerInfoListener pl : this.playerListener) {
			pl.teamChanged(this);
		}
	}
	
	protected void fireReadyStateChanged()
	{
		for (PlayerInfoListener pl : this.playerListener) {
			pl.readyStateChanged(this);
		}
	}
	
	protected void fireNameChanged()
	{
		for (PlayerInfoListener pl : this.playerListener) {
			pl.nameChanged(this);
		}
	}
	
}
