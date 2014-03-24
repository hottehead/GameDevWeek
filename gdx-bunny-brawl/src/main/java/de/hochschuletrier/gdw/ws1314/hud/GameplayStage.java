package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.hud.elements.HealthBar;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.BoxOffsetDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.DynamicTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.MinMaxValue;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.StaticTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.VisualBox;

public class GameplayStage extends Stage implements ScreenListener {
	private BitmapFont font;
	private Skin defaultSkin;

	private MinMaxValue fpsValue;
	private DynamicTextElement fpsCounter;

	private int maxHealth = 100;
	
	private HealthBar healthBar;
	private VisualBox classIcon;
	StaticTextElement classIconText;

	private VisualBox buff1, buff2, buff3;

	private VisualBox attackIcon, layEggIcon;
	
	private VisualBox scoreWhiteIcon, scoreBlackIcon;
	private MinMaxValue scoreWhite, scoreBlack;
	private final int maxScore = 100;

	ClientPlayer visualDataEntity;

	AssetManagerX assetManager;

	public GameplayStage() {
		super();
		visualDataEntity = null;
	}

	public void init(AssetManagerX assetManager) {
		// init generic stuff
		this.assetManager = assetManager;
		initSkin(assetManager);
		Main.inputMultiplexer.addProcessor(this);
		Table uiTable = new Table();
		uiTable.setFillParent(true); // ganzen platz in Tabelle nutzen
		this.addActor(uiTable);
		font = assetManager.getFont("verdana", 24);

		// FPS counter
		fpsValue = new MinMaxValue(0, 1000, -1);
		fpsCounter = new DynamicTextElement(font, "0", 50, 5, fpsValue);
		fpsCounter.setDecimalPLace(3);

		// healthbar
		healthBar = new HealthBar(100);
		healthBar.initVisual(assetManager, 80, Gdx.graphics.getHeight() - 60,
				250, 40);
		healthBar.setDecimalSpace(2);

		// class icon
		classIcon = new VisualBox(
				assetManager.getTexture("HudEmblemKnightWhite"), 20,
				Gdx.graphics.getHeight() - 80, 54, 54);
		classIconText = new StaticTextElement(font, "Klasse",
				this.classIcon.getWidth() * .5f, -14);
		classIcon = new BoxOffsetDecorator(classIcon, classIconText);

		// buffs
		buff1 = new VisualBox(assetManager.getTexture("debugBuff"), 250,
				Gdx.graphics.getHeight() - 80, 52, 52);
		buff2 = new VisualBox(assetManager.getTexture("debugBuff"), 280,
				Gdx.graphics.getHeight() - 80, 52, 52);
		buff3 = new VisualBox(assetManager.getTexture("debugBuff"), 310,
				Gdx.graphics.getHeight() - 80, 52, 52);

		// action icons
		attackIcon = new VisualBox(assetManager.getTexture("debugAttackIcon"),
				(Gdx.graphics.getWidth() * .5f) - 50,
				Gdx.graphics.getHeight() - 80, 60, 60);
		classIconText = new StaticTextElement(font, "Attacke",
				attackIcon.getWidth() * .5f, -14);
		attackIcon = new BoxOffsetDecorator(this.attackIcon, classIconText);
		layEggIcon = new VisualBox(assetManager.getTexture("debugAttackIcon"),
				(Gdx.graphics.getWidth() * .5f) + 50,
				Gdx.graphics.getHeight() - 80, 60, 60);
		classIconText = new StaticTextElement(font, "Ei ablegen",
				layEggIcon.getWidth() * .5f, -14);
		layEggIcon = new BoxOffsetDecorator(this.layEggIcon, classIconText);

		// score icons
		DynamicTextElement textScore;

		scoreWhiteIcon = new VisualBox(
				assetManager.getTexture("HudEmblemWhite"),
				(Gdx.graphics.getWidth() * .5f) - 50, 40, 54, 54);
		scoreWhite = new MinMaxValue(0, maxScore, 1);
		textScore = new DynamicTextElement(font, "0",
				scoreWhiteIcon.getWidth() * .5f, 50, scoreWhite);
		textScore.setDecimalPLace(0);
		scoreWhiteIcon = new BoxOffsetDecorator(this.scoreWhiteIcon, textScore);
		classIconText = new StaticTextElement(font, "Team 1",
				this.scoreWhiteIcon.getWidth() * .5f, -14);
		scoreWhiteIcon = new BoxOffsetDecorator(this.scoreWhiteIcon,
				classIconText);

		scoreBlackIcon = new VisualBox(
				assetManager.getTexture("debugScoreEnemy"),
				(Gdx.graphics.getWidth() * .5f) + 50, 40, 136, 151);
		scoreBlack = new MinMaxValue(0, maxScore, 1);
		textScore = new DynamicTextElement(font, "0",
				scoreBlackIcon.getWidth() * .5f, 50, scoreBlack);
		textScore.setDecimalPLace(0);
		scoreBlackIcon = new BoxOffsetDecorator(this.scoreBlackIcon, textScore);
		classIconText = new StaticTextElement(font, "Team 2",
				this.scoreBlackIcon.getWidth() * .5f, -14);
		scoreBlackIcon = new BoxOffsetDecorator(this.scoreBlackIcon,
				classIconText);
	}

	public void render() {
		Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		this.act(Gdx.graphics.getDeltaTime());

		fpsCounter.draw();
		healthBar.draw();
		classIcon.draw();
		buff1.draw();
		buff2.draw();
		buff3.draw();
		attackIcon.draw();
		layEggIcon.draw();
		scoreBlackIcon.draw();
		scoreWhiteIcon.draw();

		DrawUtil.batch.flush();
		this.draw();
		Table.drawDebug(this);
	}

	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = assetManager.getSkin("default");

	}

	// for testing the healthbar
	// can be deleted after connecting the ui with the gamelogic
	public void step() {
		if (visualDataEntity != null) {
			this.healthBar.get().setMaxValue(
					visualDataEntity.getPlayerKit().getBaseHealth());
			this.healthBar.get().setValue(visualDataEntity.getCurrentHealth());

		}
	}

	public void setFPSCounter(float delta) {
		fpsValue.setValue(1 / delta);
	}

	public void advanceScoreOwnTeam() {
		scoreWhite.stepValue();
	}

	public void advanceScoreEnemeyTeam() {
		scoreBlack.stepValue();
	}

	@SuppressWarnings("incomplete-switch")
	public void setDisplayedPlayer(ClientPlayer playerEntity) {

		visualDataEntity = playerEntity;
//		healthBar.get().setMaxValue(visualDataEntity.get) //TODO: set MaxHealth

		if (this.visualDataEntity != playerEntity) {
			visualDataEntity = playerEntity;
			switch (playerEntity.getTeamColor()) {
			case WHITE:
				switch (playerEntity.getEntityType()) {
				case Knight:
					this.classIcon.setTexture(assetManager
							.getTexture("HudEmblemKnightWhite"));
					break;
				case Tank:
					this.classIcon.setTexture(assetManager
							.getTexture("HudEmblemTankWhite"));
					break;
				case Hunter:
					this.classIcon.setTexture(assetManager
							.getTexture("HudEmblemHunterWhite"));
					break;
				default:
					this.classIcon.setTexture(assetManager
							.getTexture("HudEmblemHunterBlack"));
					break;
				}
				break;
			case BLACK:
				switch (playerEntity.getEntityType()) {
				case Knight:
					this.classIcon.setTexture(assetManager
							.getTexture("HudEmblemKnightBlack"));
					break;
				case Tank:
					this.classIcon.setTexture(assetManager
							.getTexture("HudEmblemTankBlack"));
					break;
				case Hunter:
					this.classIcon.setTexture(assetManager
							.getTexture("HudEmblemHunterBlack"));
					break;
				default:
					this.classIcon.setTexture(assetManager
							.getTexture("HudEmblemHunterBlack"));
					break;
				}
				break;
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
		
	}
	
}
