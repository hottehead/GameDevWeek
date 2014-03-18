package de.hochschuletrier.gdw.ws1314.game;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class ServerGame {

    private static final Logger logger = LoggerFactory.getLogger(ServerGame.class);

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 12;
    public static final int BOX2D_SCALE = 40;
    PhysixManager manager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);
    private ServerEntityManager entityManager;
    private NetworkManager netManager;

    public ServerGame() {
        entityManager = new ServerEntityManager();
        netManager = NetworkManager.getInstance();
	}

	public void init(AssetManagerX assets) {
        Main.getInstance().console.register(gravity_f);
    }
    public void render() {
        manager.render();

    }

    public void update(float delta) {
        entityManager.update(delta);
        manager.update(STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
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
