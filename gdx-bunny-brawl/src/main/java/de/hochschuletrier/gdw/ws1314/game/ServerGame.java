package de.hochschuletrier.gdw.ws1314.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hochschuletrier.gdw.ws1314.basic.PlayerInfo;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
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
	private NetworkManager netManager;
	private TiledMap map;
	private ServerPlayer player = new ServerPlayer();
    private long eggid = 0;
    private List<PlayerData> playerDatas;

	public ServerGame( List<PlayerData> playerDatas) {
		entityManager = ServerEntityManager.getInstance();
        entityManager.setPhysixManager(manager);
		netManager = NetworkManager.getInstance();
		map = loadMap("data/maps/miniarena.tmx");
        this.playerDatas = playerDatas;
		//loadSolids();
    }


	public void init(AssetManagerX assets) {
		//player.initPhysics(manager);
        Main.getInstance().console.register(gravity_f);
		HashMap<TileSet, Texture> tilesetImages = new HashMap<TileSet, Texture>();
		map = loadMap("data/maps/miniarena.tmx");
		for (TileSet tileset : map.getTileSets()) {
			TmxImage img = tileset.getImage();
			String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(),
					img.getSource());
			tilesetImages.put(tileset, new Texture(filename));
		}

        float offset = 0f;
        for(PlayerData playerData : playerDatas ){
            ServerPlayer sp = entityManager.createEntity(ServerPlayer.class,new Vector2(0f,0f+offset));
            
            switch(playerData.getType())
            {
                case Noob:
                    sp.setPlayerKit(PlayerKit.NOOB);
                    break;
                case Knight:
                    sp.setPlayerKit(PlayerKit.KNIGHT);
                    break;
                case Hunter:
                    sp.setPlayerKit(PlayerKit.HUNTER);
                    break;
                case Tank:
                    break;
            }

            PlayerInfo pinfo = new PlayerInfo(playerData.getPlayername(),playerData.getTeam());
            sp.setPlayerInfo(pinfo);
            netManager.setPlayerEntityId(playerData.getPlayerId(),sp.getID());
            offset += 10f;
        }
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

	public TiledMap loadMap(String filename) {
		try {
			return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Map konnte nicht geladen werden: "
					+ filename);
		}
	}

	public void loadSolids() {
		for (int i = 0; i < map.getLayers().size(); i++) {
			Layer l = map.getLayers().get(i);
			ArrayList<LayerObject> objects = l.getObjects();
			if (objects == null) {
				continue;
			}

			for (int k = 0; k < objects.size(); k++) {
				LayerObject layerObject = objects.get(k);
				Vector2 origin = new Vector2(layerObject.getX(), layerObject.getY());
				int x = layerObject.getX();
				int y = layerObject.getY();

				boolean b = l.getBooleanProperty("solid", false);
				System.out.println(b);
				if (b) {
					Primitive p = layerObject.getPrimitive();
					if (p == Primitive.POINT) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, manager)
								.position(origin).fixedRotation(true).create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						points.add(new de.hochschuletrier.gdw.commons.utils.Point(x, y));
						body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
								.friction(0.5f).restitution(0.4f).shapePolygon(points));
					} else if (p == Primitive.RECT) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, manager)
								.position(origin).fixedRotation(true).create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						points.add(new de.hochschuletrier.gdw.commons.utils.Point(x, y));
						body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
								.friction(0.5f).restitution(0.4f).shapeBox(x, y));
					} else if (p == Primitive.TILE) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, manager)
								.position(origin).fixedRotation(true).create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						for (int j = 0; j < points.size(); j++) {
							points.add(new de.hochschuletrier.gdw.commons.utils.Point(x,
									y));
						}
						body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
								.friction(0.5f).restitution(0.4f).shapePolygon(points));
					} else if (p == Primitive.POLYGON) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, manager)
								.position(origin).fixedRotation(true).create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						for (int j = 0; j < points.size(); j++) {
							points.add(new de.hochschuletrier.gdw.commons.utils.Point(x,
									y));
						}
						body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
								.friction(0.5f).restitution(0.4f).shapePolygon(points));
					} else if (p == Primitive.POLYLINE) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, manager)
								.position(origin).fixedRotation(true).create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						for (int j = 0; j < points.size(); j++) {
							points.add(new de.hochschuletrier.gdw.commons.utils.Point(x,
									y));
						}
						body.createFixture(new PhysixFixtureDef(manager).density(0.5f)
								.friction(0.5f).restitution(0.4f).shapePolygon(points));
					}
				}
			}
		}
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
