package de.hochschuletrier.gdw.ws1314.basic;

public class PlayerInfo {
	protected String name;
	protected boolean isReady;
	protected Team team;
	//protected CharacterClass charClass;
	
	public PlayerInfo(String name) {
		this(name, Team.WHITE);
	}
	
	public PlayerInfo(String name, Team team) {
		this.name = name;
		this.team = team;
		this.isReady = false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Team getTeam() {
		return this.team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public void changeTeam()
	{
		if (this.team == Team.WHITE)
			this.team = Team.BLACK;
		else
			this.team = Team.WHITE;
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
