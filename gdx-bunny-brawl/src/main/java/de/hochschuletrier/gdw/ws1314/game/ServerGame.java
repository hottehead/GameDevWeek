package de.hochschuletrier.gdw.ws1314.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.LayerObject.Primitive;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.levelObjects.ServerEgg;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;

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
	private ClientServerConnect netManager;
	private ServerPlayer player = new ServerPlayer();
    private long eggid = 0;

	public ServerGame() {
		entityManager = ServerEntityManager.getInstance();
        entityManager.setPhysixManager(manager);
		netManager = ClientServerConnect.getInstance();
    }


	public void init(AssetManagerX assets) {
        Main.getInstance().console.register(gravity_f);
		HashMap<TileSet, Texture> tilesetImages = new HashMap<TileSet, Texture>();
		TiledMap map = assets.getTiledMap("dummy_fin_map2");
		LevelLoader.load(map, entityManager, manager);
		for (TileSet tileset : map.getTileSets()) {
			TmxImage img = tileset.getImage();
			String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(),
					img.getSource());
			tilesetImages.put(tileset, new Texture(filename));
		}
         entityManager.createEntity(ServerPlayer.class, new Vector2(0f,0f));
         entityManager.createEntity(ServerEgg.class, new Vector2(100f,100f));
         entityManager.createEntity(ServerEgg.class, new Vector2(50, 50));
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
