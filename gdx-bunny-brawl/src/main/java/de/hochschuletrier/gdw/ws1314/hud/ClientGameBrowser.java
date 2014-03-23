package de.hochschuletrier.gdw.ws1314.hud;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.states.ChooseClassState;

public class ClientGameBrowser extends AutoResizeStage   {
	private Logger logger = LoggerFactory.getLogger(ClientGameBrowser.class);

	TextField serverIP;
	TextField serverPort;
	Label IpLabel, PortLabel;
	Button join;
	private Skin defaultSkin;
	private Table root;

	@Override
	public void init(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(Gdx.files.internal("data/huds/default.json"));
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		root = new Table();
		root.setFillParent(true);
		root.debug(Debug.all);
		join = new Button(new Label("Verbinden", defaultSkin), defaultSkin, "default");
		serverIP = new TextField("127.0.0.1", defaultSkin);
		serverPort = new TextField("54293", defaultSkin);
		root.add(serverIP);
		root.add(serverPort);
		root.row();
		root.add(join).colspan(2);
		this.addActor(root);
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

	@Override
	public void render() {
		Table.drawDebug(this);
		act();
		draw();
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
	}

}
