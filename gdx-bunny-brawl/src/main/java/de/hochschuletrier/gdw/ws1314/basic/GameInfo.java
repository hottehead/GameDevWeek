package de.hochschuletrier.gdw.ws1314.basic;

import de.hochschuletrier.gdw.commons.utils.Point;
import de.hochschuletrier.gdw.ws1314.entity.TeamSpawnZone;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import org.omg.CORBA.IntHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Observable;

/**
 * Created by Jerry on 21.03.14.
 */
public class GameInfo extends Observable {
    private static final Logger logger = LoggerFactory.getLogger(GameInfo.class);


    private HashMap<TeamColor,IntHolder> TeamPoints = new HashMap<>();

    private HashMap<TeamColor,TeamSpawnZone> TeamSpawnZone = new HashMap<>();

    public void Increment(TeamColor team){
        if(!TeamPoints.containsKey(team)){
            logger.warn("Team {} exsistiert nicht.",team.name());
            return;
        }

         TeamPoints.get(team).value++;
        notifyObservers(this);
    }

    public void addTeam(TeamColor team, TeamSpawnZone zone) {
        TeamPoints.put(team,new IntHolder(0));
        TeamSpawnZone.put(team,zone);
    }

	public void addTeam(TeamColor team) {
		TeamPoints.put(team,new IntHolder(0));
	}

	public Point getASpawnPoint(TeamColor team)
	{
		if(!TeamSpawnZone.containsKey(team)){
			logger.warn("Team {} exsistiert nicht. Ein Punkt 0,0 Wurde zurück gegeben.",team.name());
			return new Point(0,0);
		}

		return TeamSpawnZone.get(team).getRandomPointInZone();
	}

    public int getTeamPoints(TeamColor team) {
        if(!TeamPoints.containsKey(team)){
            logger.warn("Team {} exsistiert nicht.",team.name());
            return 0;
        }

        return TeamPoints.get(team).value;
    }
}
