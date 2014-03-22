package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.InputProcessor;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.game.ServerGame;
import de.hochschuletrier.gdw.ws1314.hud.ServerGamePlayStage;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Menu state
 * 
 * @author Santo Pfingsten
 */
public class ServerGamePlayState extends GameState implements InputProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ServerGamePlayState.class);
	private ServerGame game;
	private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);

    private List<PlayerData> playerDatas = null;
    
    private ServerGamePlayStage stage;
    

	public ServerGamePlayState() {
	}

    public void setPlayerDatas(List<PlayerData> playerDatas) {
        this.playerDatas = playerDatas;
    }

    @Override
	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
		game = new ServerGame(playerDatas);
		game.init(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		
		this.stage = new ServerGamePlayStage();
		this.stage.init(assetManager);
	}

	@Override
	public void render() {
        DrawUtil.batch.setProjectionMatrix(DrawUtil.getCamera().combined);
        this.stage.render();
	}

	@Override
	public void update(float delta) {
		game.update(delta);
		fpsCalc.addFrame();
	}

	@Override
	public void onEnter() {
        if(playerDatas == null || playerDatas.size() == 0) {
            logger.warn("playerDatas sind Leer. Bitte setPlayerDatas aufrufen.");
        }
	}

	@Override
	public void onLeave() {
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
}
