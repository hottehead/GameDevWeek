package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.basic.GameInfo;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.hud.FinishedGameStage;
import de.hochschuletrier.gdw.ws1314.hud.MainMenuStage;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.sound.LocalMusic;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class FinishedGameState extends GameState {
	private static final Logger logger = LoggerFactory.getLogger(FinishedGameState.class);
	
    private LocalMusic music;
	
	private FinishedGameStage stage;
	
	private BackButtonClick backButtonListener;

    public FinishedGameState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        
		this.music = Main.musicManager.getMusicStreamByStateName(GameStates.FINISHEDGAME);
		this.backButtonListener = new BackButtonClick();
		
    }

    @Override
    public void render() {
		stage.render();
	}

	float stateTime = 0f;

	@Override
	public void update(float delta) {
		stateTime += delta;
		music.update();
    }

    @Override
    public void onEnter() {
    	if (NetworkManager.getInstance().isClient()) {
    		NetworkManager.getInstance().disconnectFromServer();
    	}
    	
    	this.stage = new FinishedGameStage();
    	
    	GameInfo gameInfo = ClientEntityManager.getInstance().getGameInfo();
    	PlayerData playerData = ClientEntityManager.getInstance().getPlayerData();
    	
    	if ((gameInfo.getTeamPointsBlack() > gameInfo.getTeamPointsWhite() && playerData.getTeam() == TeamColor.BLACK) ||
    		(gameInfo.getTeamPointsWhite() > gameInfo.getTeamPointsBlack() && playerData.getTeam() == TeamColor.WHITE)) {
    		
    		this.stage.setFinishStateText("Victory!");
    	} else {
    		this.stage.setFinishStateText("Defeat!");
    	}
    	
    	this.stage.setBlackTeamSore(gameInfo.getTeamPointsBlack());
    	this.stage.setWhiteTeamScore(gameInfo.getTeamPointsWhite());
    	
    	this.stage.init(assetManager);
    	
    	this.stage.getBackButton().addListener(this.backButtonListener);
    	
    	stage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void onLeave() {
		Main.inputMultiplexer.removeProcessor(this.stage);
		
		this.stage.getBackButton().removeListener(this.backButtonListener);
		this.stage.dispose();
		this.stage = null;
	}

	@Override
	public void onLeaveComplete() {
    }

    @Override
    public void dispose() {
    }
    
    private class BackButtonClick extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			GameStates.MAINMENU.activate();
		}
    }
}
