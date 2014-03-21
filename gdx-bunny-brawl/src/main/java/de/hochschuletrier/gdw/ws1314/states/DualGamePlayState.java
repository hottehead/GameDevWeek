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
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.game.ClientGame;
import de.hochschuletrier.gdw.ws1314.game.ClientServerConnect;
import de.hochschuletrier.gdw.ws1314.game.ServerGame;
import de.hochschuletrier.gdw.ws1314.hud.ServerGamePlayStage;
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
public class DualGamePlayState extends GameState implements InputProcessor, DisconnectCallback, ClientIdCallback {
	private static final Logger logger = LoggerFactory.getLogger(DualGamePlayState.class);
	
	private boolean isServerInitialized = false;
	private boolean isClientInitialized = false;
	private ClientGame clientGame;
	private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
	private LocalMusic stateMusic;
	private LocalSound stateSound;
	
	private ServerGame serverGame;

    private List<PlayerData> playerDatas = null;
	
    public void setPlayerDatas(List<PlayerData> playerDatas) {
        this.playerDatas = playerDatas;
    }

	public DualGamePlayState() {
	}

	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
		
		this.playerDatas = new ArrayList<>();
		
		NetworkManager.getInstance().setClientIdCallback(this);
		
		logger.info("creating FakeConnecction");
		createFakeConnection();
		logger.info("fake Con created");
		
		Main.inputMultiplexer.addProcessor(this);
	}

	public void render() {
		if (!isClientInitialized) return;
		DrawUtil.batch.setProjectionMatrix(DrawUtil.getCamera().combined);
		// game.render();
		clientGame.render();
		
                
		//TODO: Jemand der weis woher das kommt bitte fixen, hier war nach dem Merge zwischen integration_test/network_gameplay und master ein Build-Fehler.
                //game.getManager().render();
	}

	@Override
	public void update(float delta) {
		if (isServerInitialized) {
			serverGame.update(delta);
		}
		
		if (isClientInitialized) {
			clientGame.update(delta);
		}
		
		fpsCalc.addFrame();
	}

	@Override
	public void onEnter() {
		
	}

	@Override
	public void onLeave() {
	}

	@Override
	public void dispose() {
		//stage.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
        //tmpGame.keyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
        //tmpGame.keyUp(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void callback(String msg) {
		/*
		logger.warn(msg);
		GameStates.MAINMENU.init(assetManager);
		GameStates.MAINMENU.activate();
		*/
	}
	
	private void createDummyPlayer() {
		this.playerDatas = new ArrayList<>();
		this.playerDatas.add(new PlayerData(1, "Long John", EntityType.Hunter, TeamColor.WHITE, true));
	}
	
	private void createFakeConnection() {
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
		serverGame.init(assetManager);
		
		isServerInitialized = true;
	}
	
	private void internalClientInit()
	{
		clientGame = new ClientGame();
		clientGame.init(assetManager);
		
		
		stateMusic = new LocalMusic(assetManager);
		stateSound = LocalSound.getInstance();
		stateSound.init(assetManager);
		
		isClientInitialized = true;
	}
}
