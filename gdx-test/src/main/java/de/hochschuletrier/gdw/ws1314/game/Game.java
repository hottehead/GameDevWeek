package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.utils.PhysixUtil;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class Game {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 12;
    public static final int BOX2D_SCALE = 40;
	private BitmapFont verdana_26;
    PhysixManager manager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);
	private ArrayList<PhysixEntity> entities = new ArrayList<PhysixEntity>();
	private Player player;
	private Vase vase;
	Array<Ball> b = new Array<Ball>();

    public Game() {

	}

    // Test
	public void init(AssetManagerX assets) {
		PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, manager).position(200, 500)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(manager).density(1).friction(0.5f).shapeBox(2000, 10));
        
        /*body = new PhysixBodyDef(BodyType.StaticBody, manager).position(0, 0)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(manager).density(1).friction(0.5f).shapeBox(10, 1000));

       // PhysixUtil.createHollowCircle(manager, 180, 180, 150, 30, 6);
       // player = new Player(410, 350);
       // player.initPhysics(manager);
		//entities.add(player);

		/*vase = new Vase(0, 0);
		vase.initGraphics(assets);
		vase.initPhysics(manager);
        entities.add(vase);*/

		verdana_26 = assets.getFont("verdana", 24);
        Main.getInstance().console.register(gravity_f);
    }
    public void render() {
        manager.render();
		verdana_26.draw(DrawUtil.batch, "Press left mouse button to spawn a ball. "
				+ "Press right mouse button to replace the vase.", 0, 0);
    }

    public void update(float delta) {
    	
        manager.update(STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        if(b == null){
        	
        }else{
        	for(int i = 0; i < b.size; i++){
        		b.get(i).update();
        	}
        }
		//vase.update();
    }

    public void addBall(int x, int y) {
    	
    		Ball ball = new Ball(x, y, 30);
			b.add(ball);
            ball.initPhysics(manager);
            entities.add(ball);
    	
    }

    public Player getPlayer() {
        return player;
    }

    public Vase getVase() {
        return vase;
    }

    public PhysixManager getManager() {
        return manager;
    }

    ConsoleCmd gravity_f = new ConsoleCmd("gravity", 0, "Set gravity.", 2) {
        @Override
        public void showUsage() {
            showUsage("<x> <y>");
        }

        @Override
        public void execute(List<String> args) {
            try {
                float x = Float.parseFloat(args.get(1));
                float y = Float.parseFloat(args.get(2));

                manager.setGravity(x, y);
                logger.info("set gravity to ({}, {})", x, y);
            } catch (NumberFormatException e) {
                showUsage();
            }
        }
    };
}
