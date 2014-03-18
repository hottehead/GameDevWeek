package de.hochschuletrier.gdw.ws1314.states.entity;

import java.util.ArrayList;

import de.hochschuletrier.gdw.ws1314.basic.PlayerInfo;
import de.hochschuletrier.gdw.ws1314.basic.Team;

public class Lobby {
	
	protected ArrayList<PlayerInfo> teamWhite;
	protected ArrayList<PlayerInfo> teamBlack;
	protected boolean joinTeamWhite;
	
	public Lobby() {
		this.teamWhite = new ArrayList<PlayerInfo>();
		this.teamBlack = new ArrayList<PlayerInfo>();
		this.joinTeamWhite = true;
	}

	public void playerJoin(String name)
	{
		PlayerInfo p = new PlayerInfo(name);
		
		if (this.joinTeamWhite) 
		{
			p.setTeam(Team.WHITE);
			this.teamWhite.add(p);
		}
		else
		{
			p.setTeam(Team.BLACK);
			this.teamBlack.add(p);
		}
		this.joinTeamWhite = !this.joinTeamWhite;
	}
	
	public void playerSwitchTeam()
	{
		
	}
	
	public void playerLeave()
	{
		
	}
	
	public void setCharacterClass()
	{
		
	}
}
