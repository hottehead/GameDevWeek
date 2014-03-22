package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.basic.GameInfo;
import de.hochschuletrier.gdw.ws1314.basic.GameInfoListener;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.game.ServerGame;
import de.hochschuletrier.gdw.ws1314.hud.ServerGamePlayStage;
import de.hochschuletrier.gdw.ws1314.network.DisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Menu state
 * 
 * @author Santo Pfingsten
 */
public class ServerGamePlayState extends GameState implements InputProcessor, DisconnectCallback, GameInfoListener {
    private static final Logger logger = LoggerFactory.getLogger(ServerGamePlayState.class);
    
	private ServerGame game;
	private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);

    private List<PlayerData> playerDatas = null;
    
    private ServerGamePlayStage stage;
    
    private DisconnectClick disconnectClickListener;
    
    private String mapName;

	public ServerGamePlayState() {
	}
	
    public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public void setPlayerDatas(List<PlayerData> playerDatas) {
        this.playerDatas = playerDatas;
    }

    @Override
	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
		
		this.disconnectClickListener = new DisconnectClick();
	}

	@Override
	public void render() {
        DrawUtil.batch.setProjectionMatrix(DrawUtil.getCamera().combined);
        this.stage.render();
	}

	@Override
	public void update(float delta) {
		if (game != null) {
			game.update(delta);
		}
		
		fpsCalc.addFrame();
	}

	@Override
	public void onEnter() {
       super.onEnter();
       
		if(playerDatas == null || playerDatas.size() == 0) {
            logger.warn("playerDatas sind Leer. Bitte setPlayerDatas aufrufen.");
            // Hier evtl. zurück ins vorherige GameState wechseln, 
            // dieser Fehler sollte aber im Normalfall nicht auftretten.
        }
        
        NetworkManager.getInstance().setDisconnectCallback(this);
        
        game = new ServerGame(playerDatas);
		game.init(assetManager, this.mapName);
		
		Main.inputMultiplexer.addProcessor(this);
		
		this.stage = new ServerGamePlayStage();
		this.stage.init(assetManager);
		
		this.stage.getDisconnectButton().addListener(this.disconnectClickListener);
	}

	@Override
	public void onLeave() {
		super.onLeave();
		
		NetworkManager.getInstance().setDisconnectCallback(null);
		
		this.stage.getDisconnectButton().removeListener(this.disconnectClickListener);
		
		Main.inputMultiplexer.removeProcessor(this);
		
		this.stage = null;
		this.game = null;
	}

	@Override
	public void dispose() {
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
	
	private class DisconnectClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			NetworkManager.getInstance().stopServer();
		}
	}

	@Override
	public void callback(String msg) {
		logger.info(msg);
		GameStates.MAINMENU.init(assetManager);
		GameStates.MAINMENU.activate();
	}

	@Override
	public void gameInfoChanged(int blackPoints, int whitePoints, int remainingEgg) {
		GameInfo gi = ServerEntityManager.getInstance().getGameInfo();
		
		// WinningCondition HERE:
		if (blackPoints > (gi.getAllEggs() / 2) || whitePoints > (gi.getAllEggs() / 2))
		{
			NetworkManager.getInstance().sendGameState(GameStates.FINISHEDGAME);
			NetworkManager.getInstance().stopServer();
		}
	}
}
