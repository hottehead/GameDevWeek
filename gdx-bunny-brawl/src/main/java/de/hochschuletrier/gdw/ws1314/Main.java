package de.hochschuletrier.gdw.ws1314;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.TrueTypeFont;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationExtendedLoader;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.TiledMapLoader.TiledMapParameter;
import de.hochschuletrier.gdw.commons.gdx.devcon.DevConsoleView;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitVerticalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.GdxResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ws1314.hud.HudResizer;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.preferences.GamePreferences;
import de.hochschuletrier.gdw.ws1314.sound.MusicManager;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 * 
 * @author Santo Pfingsten
 */
public class Main extends StateBasedGame {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	public static final int WINDOW_WIDTH = 1024;
	public static final int WINDOW_HEIGHT = 512;

	private final AssetManagerX assetManager = new AssetManagerX();
	private static Main instance;
	public final GamePreferences gamePreferences = new GamePreferences();
	public static MusicManager musicManager;

	public final DevConsole console = new DevConsole(16);
	private final DevConsoleView consoleView = new DevConsoleView(console);
	private Skin skin;
	public static final InputMultiplexer inputMultiplexer = new InputMultiplexer();
	public static final String playerName = "Long John " + (int) (Math.random() * 100.0);

	public static Main getInstance() {
		if (instance == null) {
			instance = new Main();
		}
		return instance;
	}

	private void loadAssetLists() {
		TextureParameter param = new TextureParameter();
		param.minFilter = param.magFilter = Texture.TextureFilter.Linear;

		assetManager.loadAssetList("data/json/images.json", Texture.class, param);

		assetManager.loadAssetList("data/json/sounds.json", Sound.class, null);

		assetManager.loadAssetList("data/json/music.json", Music.class, null);

		assetManager.loadAssetListWithParam("data/json/animations.json",
				AnimationExtended.class,
				AnimationExtendedLoader.AnimationExtendedParameter.class);
		TiledMapParameter mapParam = new TiledMapParameter();
		assetManager.loadAssetList("data/json/maps.json", TiledMap.class, mapParam);

		BitmapFontParameter fontParam = new BitmapFontParameter();
		fontParam.flip = true;
		assetManager.loadAssetList("data/json/fonts_bitmap.json", BitmapFont.class,
				fontParam);

		assetManager.loadAssetList("data/json/fonts_truetype.json", TrueTypeFont.class,
				null);
	}

	private void setupGdx() {
		KeyUtil.init();
		Gdx.graphics.setContinuousRendering(true);
		// Disable VSync for the loading state, to speed things up
		// This will be enabled when loading is done
		Gdx.graphics.setVSync(false);

		Gdx.input.setCatchMenuKey(true);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public int c_own_id;
	public String s_map = "";
	public PlayerData[] c_players;
	public List<PlayerData> s_players = new ArrayList<PlayerData>();
	
	//public int playercount = 0;
	
	
	@Override
	public void create() {
		//s_players[0] = new PlayerData("supertyp", EntityType.Hunter, (byte) 0, false);
		CurrentResourceLocator.set(new GdxResourceLocator(Files.FileType.Internal));
		DrawUtil.init();
		loadAssetLists();
		setupGdx();
		gamePreferences.init();
		musicManager = MusicManager.getInstance();
		musicManager.init(this.assetManager);
		skin = new Skin(Gdx.files.internal("data/skins/basic.json"));
		consoleView.init(assetManager, skin);
		addScreenListener(consoleView);
		inputMultiplexer.addProcessor(consoleView.getInputProcessor());

		GameStates.LOADING.init(assetManager);
		GameStates.LOADING.activate();
        
		NetworkManager.getInstance().init();
		
		console.register(new ConsoleCmd("chState",0,"[DEBUG] Change GameplayState",1){
			@Override
			public void showUsage() {
				showUsage("<GameplayStateName>");
			}
			
			@Override
			public void execute(List<String> args) {
				if (args.get(1).equals("lobby"))
				{
					if (NetworkManager.getInstance().isServer())
					{
						logger.info("Changing State to Server-Lobby...");
						GameStates.SERVERLOBBY.init(assetManager);
						GameStates.SERVERLOBBY.activate();
					}
					else if (NetworkManager.getInstance().isClient())
					{
						logger.info("Changing State to Client-Lobby...");
						GameStates.CLIENTLOBBY.init(assetManager);
						GameStates.CLIENTLOBBY.activate();
					}
					else
					{
						logger.info("Not yet connected...");
					}
				}
				if (args.get(1).equals("main"))
				{
					if (NetworkManager.getInstance().isServer())
					{
						logger.info("Changing State to Mainmenu...");
						GameStates.MAINMENU.init(assetManager);
						GameStates.MAINMENU.activate();
					}
					else if (NetworkManager.getInstance().isClient())
					{
						logger.info("Changing State to Mainmenu...");
						GameStates.MAINMENU.init(assetManager);
						GameStates.MAINMENU.activate();
					}
					else
					{
						logger.info("Not yet connected...");
					}
				}
			}
		});
		
	}

	public void onLoadComplete() {
		GameStates.MAINMENU.init(assetManager);
		GameStates.MAINMENU.activate(new SplitVerticalTransition(500).reverse(), null);
	}

	@Override
	public void dispose() {
		NetworkManager.getInstance().dispose();
		GameStates.dispose();
		consoleView.dispose();
		skin.dispose();
		Gdx.app.exit();
	}

	@Override
	protected void preRender() {
		DrawUtil.clearColor(Color.BLACK);
		DrawUtil.clear();
		DrawUtil.resetColor();

		DrawUtil.update();
		DrawUtil.batch.begin();
	}

	@Override
	protected void postRender() {
		DrawUtil.batch.end();
		if (consoleView.isVisible()) {
			consoleView.render();
		}
	}

	@Override
	protected void preUpdate(float delta) {
		if (consoleView.isVisible()) {
			consoleView.update(delta);
		}
		console.executeCmdQueue();
        
    	NetworkManager.getInstance().update();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.gl.glViewport(0, 0, width, height);
		super.resize(width, height);
		DrawUtil.setViewport(width, height);
		HudResizer.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "LibGDX Test";
		cfg.width = WINDOW_WIDTH;
		cfg.height = WINDOW_HEIGHT;
		cfg.useGL30 = false;
		
		new LwjglApplication(getInstance(), cfg);
	}
}
