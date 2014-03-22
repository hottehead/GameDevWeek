package de.hochschuletrier.gdw.ws1314.entity.levelObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.basic.GameInfo;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author yannick
 * 
 */
public class ServerEgg extends ServerLevelObject
{
    private TeamColor teamColor;
    private boolean score;
    private float time;
    private Logger logger = LoggerFactory.getLogger(ServerEgg.class);
    
    public ServerEgg() {
        super();
        teamColor = TeamColor.BOTH;
    }

    @Override
    public void update(float delta) {
        if (score) {
            time += delta;
            if (time >= 10) {
                time = 0;
                if (teamColor == TeamColor.BLACK)
                    ServerEntityManager.getInstance().getGameInfo().scoreBlack();
                else if (teamColor == TeamColor.WHITE)
                    ServerEntityManager.getInstance().getGameInfo().scoreWhite();
                ServerEntityManager.getInstance().removeEntity(this);
            }
        }
    }
	
    @Override
    public void initialize() {
            super.initialize();
    }

    @Override
    public void beginContact(Contact contact) {
        
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public EntityType getEntityType() {
            return EntityType.Ei;
    }

    @Override
    public void initPhysics(PhysixManager manager) {

        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager)
                                            .position(new Vector2(properties.getFloat("x"),properties.getFloat("y")))
                                            .fixedRotation(false).create();

        body.createFixture(new PhysixFixtureDef(manager)
                                .sensor(true)
                                .density(0.5f)
                                .friction(0.0f)
                                .restitution(0.0f)
                                .shapeCircle(12));



        body.setGravityScale(0);
        body.addContactListener(this);
        setPhysicsBody(body);
    }

    public void startScoreProcess(TeamColor teamColor) {
        this.teamColor = teamColor;
        score = true;
    }
    
    public void stopScoreProcess() {
        this.teamColor = TeamColor.BOTH;
        score = false;
    }
}
