package de.hochschuletrier.gdw.ws1314.basic;

import de.hochschuletrier.gdw.commons.utils.Point;
import de.hochschuletrier.gdw.ws1314.entity.TeamSpawnZone;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Observable;

/**
 * Created by Jerry on 21.03.14.
 */
public class GameInfo extends Observable {
    private static final Logger logger = LoggerFactory.getLogger(GameInfo.class);


    private int TeamPointsWhite = 0;
	private int TeamPointsBlack = 0;
	private int allEggs = 0;

	private TeamSpawnZone TeamSpawnZoneWhite;
	private TeamSpawnZone TeamSpawnZoneBlack;


	public Point getASpawnPoint(TeamColor team)
	{
		switch(team){
			case BLACK:
				return TeamSpawnZoneBlack.getRandomPointInZone();
			case WHITE:
				return TeamSpawnZoneWhite.getRandomPointInZone();
		}

		return new Point(0,0);
	}

	public int getTeamPointsWhite() {
		return TeamPointsWhite;
	}

	public void setTeamPointsWhite(int teamPointsWhite) {
		TeamPointsWhite = teamPointsWhite;
		notifyObservers(this);
	}

	public int getTeamPointsBlack() {
		return TeamPointsBlack;
	}

	public void setTeamPointsBlack(int teamPointsBlack) {
		TeamPointsBlack = teamPointsBlack;
		notifyObservers(this);
	}

	public void setTeamSpawnZoneWhite(TeamSpawnZone teamSpawnZoneWhite) {
		TeamSpawnZoneWhite = teamSpawnZoneWhite;
	}

	public void setTeamSpawnZoneBlack(TeamSpawnZone teamSpawnZoneBlack) {
		TeamSpawnZoneBlack = teamSpawnZoneBlack;
	}

	public int getAllEggs() {
		return allEggs;
	}

	public void setAllEggs(int allEggs) {
		this.allEggs = allEggs;
	}
}
