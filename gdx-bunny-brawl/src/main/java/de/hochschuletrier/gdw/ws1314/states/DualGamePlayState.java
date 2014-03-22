package de.hochschuletrier.gdw.ws1314.states;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.game.ClientGame;
import de.hochschuletrier.gdw.ws1314.game.ServerGame;
import de.hochschuletrier.gdw.ws1314.network.ClientIdCallback;
import de.hochschuletrier.gdw.ws1314.network.DisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;
import de.hochschuletrier.gdw.ws1314.sound.LocalSound;

/**
 * Menu state
 * 
 * @author Santo Pfingsten
 */
public class DualGamePlayState extends GameState implements DisconnectCallback, ClientIdCallback {
	private static final Logger logger = LoggerFactory.getLogger(DualGamePlayState.class);
	
	private boolean isServerInitialized = false;
	private boolean isClientInitialized = false;
	private ClientGame clientGame;
	private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
	private LocalMusic stateMusic;
	private LocalSound stateSound;
	
	private ServerGame serverGame;

    private List<PlayerData> playerDatas = null;
    
    private String mapName = "map01";
	
    public void setPlayerDatas(List<PlayerData> playerDatas) {
        this.playerDatas = playerDatas;
    }

	public DualGamePlayState() {
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
	}

	public void render() {
		if (!isClientInitialized) return;
		DrawUtil.batch.setProjectionMatrix(DrawUtil.getCamera().combined);
		clientGame.render();
	}

	@Override
	public void update(float delta) {
		if (isServerInitialized) {
			serverGame.update(delta);
		Main.musicManager.getMusicStreamByStateName(GameStates.MAINMENU).update();
		}
		
		if (isClientInitialized) {
			clientGame.update(delta);
		}
		
		fpsCalc.addFrame();
	}

	@Override
	public void onEnter() {
		isServerInitialized = false;
		isClientInitialized = false;
		
		this.playerDatas = new ArrayList<>();
		
		NetworkManager.getInstance().setClientIdCallback(this);
		
		createLocalConnection();
	}

	@Override
	public void onLeave() {
		NetworkManager.getInstance().setClientIdCallback(null);
		
		clientGame = null;
		serverGame = null;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void callback(String msg) {
		logger.warn(msg);
		GameStates.MAINMENU.init(assetManager);
		GameStates.MAINMENU.activate();
	}
	
	private void createLocalConnection() {
		NetworkManager.getInstance().listen(NetworkManager.getInstance().getDefaultServerIp(), NetworkManager.getInstance().getDefaultPort(), 10);
		NetworkManager.getInstance().connect("localhost", NetworkManager.getInstance().getDefaultPort());
	}

	@Override
	public void callback(int playerid) {
		logger.info("PlayerID received");
		PlayerData p = new PlayerData(playerid, "Long John", EntityType.Hunter, TeamColor.WHITE, true);
		this.playerDatas.add(p);
		internalServerInit();
		internalClientInit();
	}
	
	private void internalServerInit() {
		if(playerDatas == null || playerDatas.size() == 0) {
            logger.warn("playerDatas sind Leer. Bitte setPlayerDatas aufrufen.");
        }
		
		serverGame = new ServerGame(playerDatas);
		serverGame.init(assetManager, mapName);
		
		isServerInitialized = true;
	}
	
	private void internalClientInit()
	{
		clientGame = new ClientGame();
		clientGame.init(assetManager, mapName);
		
		
		stateMusic = new LocalMusic(assetManager);
		stateSound = LocalSound.getInstance();
		stateSound.init(assetManager);
		
		isClientInitialized = true;
	}
}
