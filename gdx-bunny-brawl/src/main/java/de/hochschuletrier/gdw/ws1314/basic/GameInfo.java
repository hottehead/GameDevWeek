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
public class GameInfo extends Observable {
    private static final Logger logger = LoggerFactory.getLogger(GameInfo.class);

	private ArrayList<GameInfoListener> listeners = new ArrayList<>();

    private int TeamPointsWhite = 0;
	private int TeamPointsBlack = 0;
	private int remainigEggs = 0;

	private TeamSpawnZone TeamSpawnZoneWhite;
	private TeamSpawnZone TeamSpawnZoneBlack;


	public Point getASpawnPoint(TeamColor team)	{

		switch(team){
			case BLACK:
				return TeamSpawnZoneBlack.getRandomPointInZone();
			case WHITE:
				return TeamSpawnZoneWhite.getRandomPointInZone();
		}

		return new Point(0,0);
	}

	public void addListner(GameInfoListener listener){
		listeners.add(listener);
	}

	public void removeListner(GameInfoListener listener){
		listeners.remove(listener);
	}

	private void sendUpdate(){
		for(GameInfoListener listener : listeners){
			listener.gameInfoChanged(TeamPointsBlack,TeamPointsWhite,remainigEggs);
		}
	}

	public int getTeamPointsWhite() {
		return TeamPointsWhite;
	}

	public void setTeamPointsWhite(int teamPointsWhite) {
		TeamPointsWhite = teamPointsWhite;
		sendUpdate();
	}

	public int getTeamPointsBlack() {
		return TeamPointsBlack;
	}

	public void setTeamPointsBlack(int teamPointsBlack) {
		TeamPointsBlack = teamPointsBlack;
		sendUpdate();
	}

	public void setTeamSpawnZoneWhite(TeamSpawnZone teamSpawnZoneWhite) {
		TeamSpawnZoneWhite = teamSpawnZoneWhite;
	}

	public void setTeamSpawnZoneBlack(TeamSpawnZone teamSpawnZoneBlack) {
		TeamSpawnZoneBlack = teamSpawnZoneBlack;
	}

	public int getRemainigEggs() {
		return remainigEggs;
	}

	public void setRemainigEggs(int remainigEggs) {
		this.remainigEggs = remainigEggs;
		sendUpdate();
	}

	public int getAllEggs(){
		return remainigEggs + TeamPointsBlack + TeamPointsWhite;
	}
}
