package de.hochschuletrier.gdw.ws1314.game;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.utils.Point;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.basic.GameInfo;
import de.hochschuletrier.gdw.ws1314.basic.GameInfoListener;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 * 
 * @author Santo Pfingsten
 */
public class ServerGame implements GameInfoListener {

	private static final Logger logger = LoggerFactory.getLogger(ServerGame.class);

	public static final int POSITION_ITERATIONS = 3;
	public static final int VELOCITY_ITERATIONS = 8;
	public static final float STEP_SIZE = 1 / 30.0f;
	public static final int GRAVITY = 12;
	public static final int BOX2D_SCALE = 40;
	PhysixManager manager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);
	private ServerEntityManager entityManager;
	private NetworkManager netManager;

	private ServerPlayer player = new ServerPlayer();
    private long eggid = 0;
    private List<PlayerData> playerDatas;
	private GameInfo gameInfo;

	public ServerGame( List<PlayerData> playerDatas) {
		entityManager = ServerEntityManager.getInstance();
        entityManager.setPhysixManager(manager);
		netManager = NetworkManager.getInstance();
		this.playerDatas = playerDatas;
		//loadSolids();
    }


	public void init(AssetManagerX assets, String mapName) {
		gameInfo = entityManager.getGameInfo();
        Main.getInstance().console.register(gravity_f);
		HashMap<TileSet, Texture> tilesetImages = new HashMap<TileSet, Texture>();
		
		TiledMap map = assets.getTiledMap(mapName);

		LevelLoader.load(map, entityManager, manager, gameInfo);
		for (TileSet tileset : map.getTileSets()) {
			TmxImage img = tileset.getImage();
			String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(),
					img.getSource());
			tilesetImages.put(tileset, new Texture(filename));
		}
		
        for(PlayerData playerData : playerDatas ){

            Point startpoint = gameInfo.getASpawnPoint(playerData.getTeam());

            ServerPlayer sp = entityManager.createEntity(ServerPlayer.class,new Vector2(startpoint.x,startpoint.y));

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
                    logger.info("Hunter erstellt !!!");
                    break;
                case Tank:
                	sp.setPlayerKit(PlayerKit.TANK);
                    break;
            }

            sp.setPlayerData(playerData);
            netManager.setPlayerEntityId(playerData.getPlayerId(),sp.getID());
        }
        
        gameInfo.addListner(this);
	}

	public void render() {
		manager.render();
	}

	public void update(float delta) {
		manager.update(STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		entityManager.update(delta);
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
	
	@Override
	public void gameInfoChanged(int blackPoints, int whitePoints, int remainingEgg) {
		GameInfo gi = ServerEntityManager.getInstance().getGameInfo();
		
		// WinningCondition HERE:
		if (blackPoints > (gi.getAllEggs() / 2) || whitePoints > (gi.getAllEggs() / 2))
		{
			logger.info("Winning-Condition complied.");
			NetworkManager.getInstance().sendGameState(GameStates.FINISHEDGAME);
		}
	}
	
	public void dispose()
	{
		entityManager.Clear();
		Main.getInstance().console.unregister(gravity_f);
	}
}
