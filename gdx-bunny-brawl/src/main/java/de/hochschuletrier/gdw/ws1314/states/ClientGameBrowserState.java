package de.hochschuletrier.gdw.ws1314.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.hud.ClientGameBrowser;
import de.hochschuletrier.gdw.ws1314.hud.ClientLobbyStage;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;

public class ClientGameBrowserState extends GameState {

	ClientGameBrowser clientGameBrowser;
	private Logger logger = LoggerFactory.getLogger(ClientGameBrowserState.class);
	private PlayClientListener playClientListener;

	public ClientGameBrowserState() {

	}

	@Override
	public void init(AssetManagerX assetManager) {
		super.init(assetManager);
		clientGameBrowser = new ClientGameBrowser();
		clientGameBrowser.init(assetManager);
		playClientListener = new PlayClientListener();
		clientGameBrowser.getJoin().addListener(playClientListener);
	}

	private class PlayClientListener extends ClickListener {
		public void clicked(InputEvent event, float x, float y) {
			logger.info("Change to JoinServerState");

			if (!NetworkManager.getInstance().isClient()) {
				NetworkManager.getInstance().connect(clientGameBrowser.getIP(),
						NetworkManager.getInstance().getDefaultPort());

				if (!NetworkManager.getInstance().isClient()) {
					logger.warn("Connection could not be established! Server maybe not running.");
					GameStates.MAINMENU.init(assetManager);
					GameStates.MAINMENU.activate();
					return;
				}
			}
			GameStates.CLIENTLOBBY.init(assetManager);
			GameStates.CLIENTLOBBY.activate();
		}
	}
	
	@Override
	public void render() {
		clientGameBrowser.render();
	}
	
	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		super.onEnter();
		Main.getInstance().addScreenListener(clientGameBrowser);
		Main.getInstance().inputMultiplexer.addProcessor(clientGameBrowser);
	}
	
	@Override
	public void onLeave() {
		// TODO Auto-generated method stub
		super.onLeave();
		Main.getInstance().removeScreenListener(clientGameBrowser);
		Main.getInstance().inputMultiplexer.removeProcessor(clientGameBrowser);
		clientGameBrowser.clear();
	}
}
