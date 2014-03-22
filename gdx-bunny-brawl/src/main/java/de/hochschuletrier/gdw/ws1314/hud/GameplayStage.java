package de.hochschuletrier.gdw.ws1314.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.hud.elements.HealthBar;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.BoxOffsetDecorator;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.DynamicTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.MinMaxValue;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.StaticTextElement;
import de.hochschuletrier.gdw.ws1314.hud.elements.base.VisualBox;

public class GameplayStage extends AutoResizeStage {
	private BitmapFont font;
	private Skin defaultSkin;

	private MinMaxValue fpsValue;
	private DynamicTextElement fpsCounter;

	private HealthBar healthBar;
	private VisualBox classIcon;

	private VisualBox buff1, buff2, buff3;

	private VisualBox attackIcon, layEggIcon;

	private VisualBox scoreTeamIcon, scoreEnemyIcon;
	private MinMaxValue scoreTeam, scoreEnemy;
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
		StaticTextElement decor;

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
		classIcon = new VisualBox(assetManager.getTexture("debugClass"), 20,
				Gdx.graphics.getHeight() - 80, 60, 60);
		decor = new StaticTextElement(font, "Klasse",
				this.classIcon.getWidth() * .5f, -14);
		classIcon = new BoxOffsetDecorator(classIcon, decor);

		// buffs
		buff1 = new VisualBox(assetManager.getTexture("debugBuff"), 250,
				Gdx.graphics.getHeight() - 80, 20, 20);
		buff2 = new VisualBox(assetManager.getTexture("debugBuff"), 280,
				Gdx.graphics.getHeight() - 80, 20, 20);
		buff3 = new VisualBox(assetManager.getTexture("debugBuff"), 310,
				Gdx.graphics.getHeight() - 80, 20, 20);

		// action icons
		attackIcon = new VisualBox(assetManager.getTexture("debugAttackIcon"),
				(Gdx.graphics.getWidth() * .5f) - 50,
				Gdx.graphics.getHeight() - 80, 60, 60);
		decor = new StaticTextElement(font, "Attacke",
				attackIcon.getWidth() * .5f, -14);
		attackIcon = new BoxOffsetDecorator(this.attackIcon, decor);
		layEggIcon = new VisualBox(assetManager.getTexture("debugAttackIcon"),
				(Gdx.graphics.getWidth() * .5f) + 50,
				Gdx.graphics.getHeight() - 80, 60, 60);
		decor = new StaticTextElement(font, "Ei ablegen",
				layEggIcon.getWidth() * .5f, -14);
		layEggIcon = new BoxOffsetDecorator(this.layEggIcon, decor);

		// score icons
		DynamicTextElement textScore;

		scoreTeamIcon = new VisualBox(
				assetManager.getTexture("debugScoreTeam"),
				(Gdx.graphics.getWidth() * .5f) - 50, 40, 60, 60);
		scoreTeam = new MinMaxValue(0, maxScore, 1);
		textScore = new DynamicTextElement(font, "0",
				scoreTeamIcon.getWidth() * .5f, 50, scoreTeam);
		textScore.setDecimalPLace(0);
		scoreTeamIcon = new BoxOffsetDecorator(this.scoreTeamIcon, textScore);
		decor = new StaticTextElement(font, "Team 1",
				this.scoreTeamIcon.getWidth() * .5f, -14);
		scoreTeamIcon = new BoxOffsetDecorator(this.scoreTeamIcon, decor);

		scoreEnemyIcon = new VisualBox(
				assetManager.getTexture("debugScoreEnemy"),
				(Gdx.graphics.getWidth() * .5f) + 50, 40, 60, 60);
		scoreEnemy = new MinMaxValue(0, maxScore, 1);
		textScore = new DynamicTextElement(font, "0",
				scoreEnemyIcon.getWidth() * .5f, 50, scoreEnemy);
		textScore.setDecimalPLace(0);
		scoreEnemyIcon = new BoxOffsetDecorator(this.scoreEnemyIcon, textScore);
		decor = new StaticTextElement(font, "Team 2",
				this.scoreEnemyIcon.getWidth() * .5f, -14);
		scoreEnemyIcon = new BoxOffsetDecorator(this.scoreEnemyIcon, decor);
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
		scoreEnemyIcon.draw();
		scoreTeamIcon.draw();

		DrawUtil.batch.flush();
		this.draw();
		Table.drawDebug(this);
	}

	private void initSkin(AssetManagerX assetManager) {
		this.defaultSkin = new Skin(
				Gdx.files.internal("data/huds/default.json"));
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
		scoreTeam.stepValue();
	}

	public void advanceScoreEnemeyTeam() {
		scoreEnemy.stepValue();
	}

	@SuppressWarnings("incomplete-switch")
	public void setDisplayedPlayer(ClientPlayer playerEntity) {
		visualDataEntity = playerEntity;
		if (playerEntity.getPlayerInfo() != null) {
			switch (playerEntity.getPlayerInfo().getTeam()) {
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
				}
				break;
			}
		}
	}

}
