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
	private Label IpLabel, portLabel, nameLabel;
	private Button join;
	private Skin defaultSkin;
	private Table root;

	public void init(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
		root = new Table();
		root.setFillParent(true);
		root.debug(Debug.all);
		join = new Button(new Label("Verbinden", defaultSkin), defaultSkin, "default");
		serverIP = new TextField("143.93.55.", defaultSkin);
		serverPort = new TextField("54293", defaultSkin);
		playerName = new TextField("Funny Bunny", defaultSkin);
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
		root.add(playerName);
		root.row();
		root.add(serverIP);
		root.add(serverPort);
		root.row();
		root.add(join).colspan(2);
		this.addActor(root);
	}

	public String getIP() {
		return serverIP.getText();
	}

	public int getPort() {
		return Integer.valueOf(serverPort.getText());
	}

	public Button getJoin() {
		return join;
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
