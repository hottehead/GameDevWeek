package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.BodyEditorLoader.RigidBodyModel;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.game.ClientGame;
import de.hochschuletrier.gdw.ws1314.hud.elements.ChatWindow;
import de.hochschuletrier.gdw.ws1314.hud.elements.HealthBar;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.BoxOffsetDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.DynamicTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.MinMaxValue;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.StaticTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.VisualBox;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.states.ClientGamePlayState;

public class GameplayStage extends Stage implements ScreenListener {
	ClientPlayer visualDataEntity;
	AssetManagerX assetManager;
	Skin hudSkin;
	ClientGame game;
	Logger logger = LoggerFactory.getLogger(GameplayStage.class);

	public GameplayStage() {
		super();
		visualDataEntity = null;
	}

	Table uiTable;
	Label pointsWhite, pointsBlack;
	HorizontalGroup teams;
	Image buffSpinach, buffClover, buffCarrot, avatar;
	Button dropEgg, attack;
	HealthBar healthbar;

	public void init(AssetManagerX assetManager, ClientGame game) {
		// init generic stuff
		this.game = game;
		this.assetManager = assetManager;
		hudSkin = assetManager.getSkin("bunnyBrawl");
		Main.inputMultiplexer.addProcessor(this);
		uiTable = new Table();
		uiTable.debug(Debug.all);
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		this.addActor(uiTable);
		// teamanzeige mit punktestand
		teams = new HorizontalGroup();
		pointsWhite = new Label("0", hudSkin, "garfield");
		pointsBlack = new Label("0", hudSkin, "garfield");
		teams.addActor(pointsWhite);
		teams.addActor(new Image(hudSkin, "shield_white"));
		teams.addActor(new Image(hudSkin, "shield_black"));
		teams.addActor(pointsBlack);
		uiTable.add(teams).colspan(3).expand().top();
		uiTable.row();
		// Reihe 2
		// PlayerInfo (avatar, buffs, healtbar)
		Table playerInfo = new Table();
		avatar = new Image(hudSkin, "wappenHunterBlack");
		playerInfo.add(avatar);
		// buffs
		Table stats = new Table();
		HorizontalGroup buffs = new HorizontalGroup();
		stats.debug(Debug.all);
		stats.defaults().top().right();
		buffCarrot = new Image(hudSkin, "boost_carrot");
		buffSpinach = new Image(hudSkin, "boost_spinach");
		buffClover = new Image(hudSkin, "boost_clover");
//		buffCarrot.addAction(Actions.alpha(0));
//		buffSpinach.addAction(Actions.alpha(0));
//		buffClover.addAction(Actions.alpha(0));
		buffCarrot.setVisible(false);
		buffSpinach.setVisible(false);
		buffClover.setVisible(false);
		buffs.addActor(buffCarrot);
		buffs.addActor(buffSpinach);
		buffs.addActor(buffClover);
		stats.add(buffs);
		stats.row();
		// healthbar
		healthbar = new HealthBar(assetManager);
		stats.add(healthbar).bottom();
		playerInfo.add(stats).fill();
		uiTable.add(playerInfo).bottom().left().fill();
		// Aktionen
		dropEgg = new Button(hudSkin, "eggDrop");
		dropEgg.setScale(0.5f);
		attack = new Button(hudSkin, "attack");
		attack.setScale(0.5f);
		Table actions = new Table();
		actions.add(dropEgg);
		actions.add(attack);

		uiTable.add(actions);
		// Chat
		uiTable.add(new ChatWindow(assetManager.getSkin("default"))).fill();
		
		//Listener
		attack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				logger.info("attack clicked");
				NetworkManager.getInstance().sendAction(PlayerIntention.ATTACK_1);
			}
		});
		dropEgg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				logger.info("drop egg clicked");

				NetworkManager.getInstance().sendAction(PlayerIntention.DROP_EGG);
			}
		});
	}

	public void render() {
//		Table.drawDebug(this);
		this.act(Gdx.graphics.getDeltaTime());
		pointsBlack.setText(String.valueOf(game.getGameInfo().getTeamPointsBlack()));
		pointsWhite.setText(String.valueOf(game.getGameInfo().getTeamPointsWhite()));

		// Stats
		if (game.getPlayer() != null) {
			// Avatar
			switch (game.getPlayer().getTeamColor()) {
			case WHITE:
				switch (game.getPlayer().getEntityType()) {
				case Knight:
					avatar.setDrawable(hudSkin, "wappenKnightWhite");
					break;
				case Tank:
					avatar.setDrawable(hudSkin, "wappenTankWhite");
					break;
				case Hunter:
					avatar.setDrawable(hudSkin, "wappenHunterWhite");
					break;
				default:
					break;
				}
				break;
			case BLACK:
				switch (game.getPlayer().getEntityType()) {
				case Knight:
					avatar.setDrawable(hudSkin, "wappenKnightBlack");
					break;
				case Tank:
					avatar.setDrawable(hudSkin, "wappenTankBlack");
					break;
				case Hunter:
					avatar.setDrawable(hudSkin, "wappenHunterBlack");
					break;
				default:
					break;
				}
				break;
			}

			// Stats
			healthbar.setMax(game.getPlayer().getPlayerKit().getBaseHealth());
			healthbar.setCurrent(game.getPlayer().getCurrentHealth());
			if (game.getPlayer().isBuffCarrotActive()) {
//				buffCarrot.addAction(Actions.fadeIn(1.3f, Interpolation.linear));
				buffCarrot.setVisible(true);
			} else {
//				buffCarrot.addAction(Actions.fadeOut(1f, Interpolation.linear));
				buffCarrot.setVisible(false);

			}
			if (game.getPlayer().isBuffSpinachActive()) {
//				buffSpinach.addAction(Actions.fadeIn(1.3f, Interpolation.linear));
				buffSpinach.setVisible(true);
			} else {
//				buffSpinach.addAction(Actions.fadeOut(1f, Interpolation.linear));
				buffSpinach.setVisible(false);
			}
			if (game.getPlayer().DidBuffCloverAppear()) {
				logger.info("buff clover appeared");
//				buffClover.addAction(Actions.sequence(Actions.alpha(0), Actions.show(),
//						Actions.fadeIn(0.3f, Interpolation.linear),
//						Actions.fadeOut(1f, Interpolation.linear), Actions.hide(),
//						Actions.alpha(1)));
				// buffClover.setVisible(true);
			}
		}
		this.draw();
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
	}
}
