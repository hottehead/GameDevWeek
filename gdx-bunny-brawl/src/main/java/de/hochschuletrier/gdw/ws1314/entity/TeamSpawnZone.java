package de.hochschuletrier.gdw.ws1314.entity;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.utils.Point;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

import java.util.Random;

/**
 * Created by Jerry on 21.03.14.
 */
public class TeamSpawnZone extends Zone {

    private TeamColor teamOwner;
    private int[] rect = new int[4];

    public void setRect(int x, int y, int width, int height){
        rect[0] = x;
        rect[1] = y;
        rect[2] = width;
        rect[3] = height;
    }

    public Point getRandomPointInZone(){
        Random r = new Random();
        return new Point(rect[0]+r.nextInt(rect[2]),rect[1]+r.nextInt(rect[3]));
    }

    public void setTeamWhite(){
        teamOwner = TeamColor.WHITE;
    }

    public void setTeamBlack(){
        teamOwner = TeamColor.BLACK;
    }

    public TeamColor getTeamOwner(){
        return teamOwner;
    }

    @Override
    public void initPhysics(PhysixManager manager){

    }
}
