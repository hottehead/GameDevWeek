package de.hochschuletrier.gdw.ws1314.hud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.preferences.PreferenceKeys;

public class ClientGameBrowser extends Stage implements ScreenListener {
	private Logger logger = LoggerFactory.getLogger(ClientGameBrowser.class);

	private TextField serverIP;
	private TextField serverPort;
	private TextField playerName;
	private Button connect;
	private Skin skin;
	private Table root;

	public void init(AssetManagerX assetManager) {
		this.skin = assetManager.getSkin("bunnyBrawl");
		Skin defaultSkin = assetManager.getSkin("default");

		root = new Table();
		root.setFillParent(true);
		root.debug(Debug.all);
		connect = new Button(skin,"connect");
		serverIP = new TextField("127.0.0.1", defaultSkin);
		serverPort = new TextField("54293", defaultSkin);
		playerName = new TextField("NAME", defaultSkin);
		playerName.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER) {
					Main.getInstance().gamePreferences.putString(
							PreferenceKeys.playerName, playerName.getText());
					return true;
				}
				return false;
			}
		});
		root.add(new Label("Name:", defaultSkin)).colspan(2);
		root.add(playerName).fill().colspan(2);
		root.row();
		root.add(new Label("Server IP", defaultSkin));
		root.add(serverIP).fill();
		root.add(new Label("Server Port", defaultSkin));
		root.add(serverPort).fill();
		root.row();
		root.add(connect).colspan(4);
		this.addActor(root);
	}

	public String getIP() {
		return serverIP.getText();
	}

	public int getPort() {
		return Integer.valueOf(serverPort.getText());
	}

	public Button getJoin() {
		return connect;
	}

	public void render() {
		Table.drawDebug(this);
		act();
		draw();
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
	}

}
