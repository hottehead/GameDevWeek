package de.hochschuletrier.gdw.ws1314.states;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.basic.GameInfo;
import de.hochschuletrier.gdw.ws1314.basic.GameInfoListener;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.game.ClientGame;
import de.hochschuletrier.gdw.ws1314.game.ServerGame;
import de.hochschuletrier.gdw.ws1314.network.ClientIdCallback;
import de.hochschuletrier.gdw.ws1314.network.DisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;
import de.hochschuletrier.gdw.ws1314.sound.LocalSound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu state
 * 
 * @author Santo Pfingsten
 */
public class DualGamePlayState extends GameState implements DisconnectCallback, ClientIdCallback, GameInfoListener {
	private static final Logger logger = LoggerFactory.getLogger(DualGamePlayState.class);
	
	private boolean isServerInitialized = false;
	private boolean isClientInitialized = false;
	private ClientGame clientGame;
	private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
	private LocalMusic stateMusic;
	private LocalSound stateSound;
	
	private ServerGame serverGame;

    private List<PlayerData> playerDatas = null;
    
    // mapName wird jetzt in der Settingsdatei hinterlegt!
    private String mapName;
    
    private boolean isWinningConditionComplied;
	
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
		this.stateMusic = Main.musicManager.getMusicStreamByStateName(GameStates.DUALGAMEPLAY);
		/*this.stateMusic.play("music-gameplay-loop");
		this.stateMusic.setVolume(0.5f);
		this.stateMusic.logger.info("gp state music fading on init? >> " + this.stateMusic.getFading());*/
	}

	public void render() {
		if (!isClientInitialized) return;
		DrawUtil.batch.setProjectionMatrix(DrawUtil.getCamera().combined);
		clientGame.render();
		this.serverGame.getManager().render();
	}

	@Override
	public void update(float delta) {
		if (isServerInitialized) {
			serverGame.update(delta);
			Main.musicManager.getMusicStreamByStateName(GameStates.MAINMENU).update();
		}
		
		if (isClientInitialized) {
			clientGame.update(delta);
			Main.musicManager.getMusicStreamByStateName(GameStates.MAINMENU).update();
			this.stateMusic.update();
		}
		
		fpsCalc.addFrame();
	}

	@Override
	public void onEnter() {
		isServerInitialized = false;
		isClientInitialized = false;
		isWinningConditionComplied = false;
		if (this.stateMusic.isMusicPlaying()) 
			this.stateMusic.setFade('i', 2500);
		
		/*stateSound = LocalSound.getInstance();
		stateSound.init(assetManager);*/
		
		this.mapName = Main.getInstance().gamePreferences.getString(PreferenceKeys.mapName, "map01");
		
		this.playerDatas = new ArrayList<>();
		
		NetworkManager.getInstance().setClientIdCallback(this);
		
		createLocalConnection();
	}

	@Override
	public void onLeave() {
		NetworkManager.getInstance().setClientIdCallback(null);
		//if (this.stateMusic.isMusicPlaying())
			//this.stateMusic.setFade('o', 2500);
		
		if (isServerInitialized) {
			ServerEntityManager.getInstance().getGameInfo().removeListner(this);
		}
			
		clientGame = null;
		serverGame = null;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void disconnectCallback(String msg) {
		logger.warn(msg);
		GameStates.MAINMENU.init(assetManager);
		GameStates.MAINMENU.activate();
	}
	
	private void createLocalConnection() {
		NetworkManager.getInstance().server(null, NetworkManager.getInstance().getDefaultPort(), NetworkManager.getInstance().getDefaultPlayerCount(), NetworkManager.getInstance().getDefaultServerIp());
		NetworkManager.getInstance().connect("localhost", NetworkManager.getInstance().getDefaultPort());
	}

	@Override 
	public void clientIdCallback(int playerid) {
		PlayerData p = new PlayerData(playerid, 
				Main.getInstance().gamePreferences.getString(PreferenceKeys.playerName, "Player"),
				EntityType.Hunter,
				TeamColor.WHITE,
				true);
	
		ClientEntityManager.getInstance().setPlayerData(p);
		this.playerDatas.add(p);
		
		internalServerInit();
		internalClientInit();
	}
	
	private void internalServerInit() {
		if(playerDatas == null || playerDatas.size() == 0) {
            logger.warn("playerDatas sind Leer. Bitte setPlayerDatas aufrufen.");
        }
		
		ServerEntityManager.getInstance().getGameInfo().addListner(this);
		
		serverGame = new ServerGame(playerDatas);
		serverGame.init(assetManager, this.mapName);
		
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
	
	@Override
	public void gameInfoChanged(int blackPoints, int whitePoints, int remainingEgg) {
		GameInfo gi = ServerEntityManager.getInstance().getGameInfo();
		
		// WinningCondition HERE:
		if (blackPoints > (gi.getAllEggs() / 2) || whitePoints > (gi.getAllEggs() / 2))
		{
			logger.info("Winning-Condition complied.");
			NetworkManager.getInstance().sendGameState(GameStates.FINISHEDGAME);
			// Der Server wird nicht explizit gestoppt, da alle Clients ihre Verbindung schließen sollen
			// und dann geschieht das automatisch.
		}
	}
}
