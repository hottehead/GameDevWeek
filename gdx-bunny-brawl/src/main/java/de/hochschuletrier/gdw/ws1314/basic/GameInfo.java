package de.hochschuletrier.gdw.ws1314.basic;

import de.hochschuletrier.gdw.commons.utils.Point;
import de.hochschuletrier.gdw.ws1314.entity.TeamSpawnZone;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Jerry on 21.03.14.
 */
public class GameInfo {
    private static final Logger logger = LoggerFactory.getLogger(GameInfo.class);

	private ArrayList<GameInfoListener> listeners = new ArrayList<>();

    private int teamPointsWhite = 0;
	private int teamPointsBlack = 0;
	private int remainigEggs = 0;

	private TeamSpawnZone teamSpawnZoneWhite;
	private TeamSpawnZone teamSpawnZoneBlack;


	public Point getASpawnPoint(TeamColor team)	{

		switch(team){
			case BLACK:
				return teamSpawnZoneBlack.getRandomPointInZone();
			case WHITE:
				return teamSpawnZoneWhite.getRandomPointInZone();
		}

		return new Point(0,0);
	}

	public void clear(){
		teamPointsWhite = 0;
		teamPointsBlack = 0;
		remainigEggs = 0;
	}
	
	public void addListner(GameInfoListener listener){
		listeners.add(listener);
	}

	public void removeListner(GameInfoListener listener){
		listeners.remove(listener);
	}

	private void sendUpdate(){
		for(GameInfoListener listener : listeners){
			listener.gameInfoChanged(teamPointsBlack,teamPointsWhite,remainigEggs);
		}
	}

	public int getTeamPointsWhite() {
		return teamPointsWhite;
	}

	public void setTeamPointsWhite(int teamPointsWhite) {
		this.teamPointsWhite = teamPointsWhite;
		sendUpdate();
	}

	public int getTeamPointsBlack() {
		return teamPointsBlack;
	}

	public void setTeamPointsBlack(int teamPointsBlack) {
		this.teamPointsBlack = teamPointsBlack;
		sendUpdate();
	}

	public void setTeamSpawnZoneWhite(TeamSpawnZone teamSpawnZoneWhite) {
		this.teamSpawnZoneWhite = teamSpawnZoneWhite;
	}

	public void setTeamSpawnZoneBlack(TeamSpawnZone teamSpawnZoneBlack) {
		this.teamSpawnZoneBlack = teamSpawnZoneBlack;
	}

	public int getRemainigEggs() {
		return remainigEggs;
	}

	public void setRemainigEggs(int remainigEggs) {
		this.remainigEggs = remainigEggs;
		sendUpdate();
	}

	public int getAllEggs(){
		return remainigEggs + teamPointsBlack + teamPointsWhite;
	}
	
	public void scoreBlack(){
		teamPointsBlack++;
		remainigEggs--;
	}
	
	public void scoreWhite(){
		teamPointsWhite++;
		remainigEggs--;
	}
	
	public void incrementRemaining(){
		remainigEggs++;
	}
}
