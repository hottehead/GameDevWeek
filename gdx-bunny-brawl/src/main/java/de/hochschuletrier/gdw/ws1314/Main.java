package de.hochschuletrier.gdw.ws1314;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.TrueTypeFont;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationLoader;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.SleepDummyLoader;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.GdxResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.utils.StringUtils;
import de.hochschuletrier.gdw.commons.gdx.devcon.DevConsoleView;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitVerticalTransition;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.network.LobbyUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.MatchUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.network.PlayerDisconnectCallback;
import de.hochschuletrier.gdw.ws1314.network.PlayerUpdateCallback;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
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

	public final DevConsole console = new DevConsole(16);
	private final DevConsoleView consoleView = new DevConsoleView(console);
	private Skin skin;
	public static final InputMultiplexer inputMultiplexer = new InputMultiplexer();

	public static Main getInstance() {
		if (instance == null) {
			instance = new Main();
		}
		return instance;
	}


	private void setupDummyLoader() {
		// Just adding some sleep dummies for a progress bar test
		InternalFileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
		assetManager.setLoader(SleepDummyLoader.SleepDummy.class, new SleepDummyLoader(
				fileHandleResolver));
		SleepDummyLoader.SleepDummyParameter dummyParam = new SleepDummyLoader.SleepDummyParameter(
				100);
		for (int i = 0; i < 50; i++) {
			assetManager.load("dummy" + i, SleepDummyLoader.SleepDummy.class, dummyParam);
		}
	}

	private void loadAssetLists() {
		TextureParameter param = new TextureParameter();
		param.minFilter = param.magFilter = Texture.TextureFilter.Linear;

		assetManager.loadAssetList("data/json/images.json", Texture.class, param);
		assetManager.loadAssetList("data/json/sounds.json", Sound.class, null);
		assetManager.loadAssetList("data/json/music.json", Music.class, null);
		assetManager.loadAssetListWithParam("data/json/animations.json", Animation.class,
				AnimationLoader.AnimationParameter.class);
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
	public String s_map = "";
	public PlayerData[] c_players;
	public PlayerData[] s_players = new PlayerData[3];
	
	public int playercount = 0;
	

	@Override
	public void create() {
		//s_players[0] = new PlayerData("supertyp", EntityType.Hunter, (byte) 0, false);
		CurrentResourceLocator.set(new GdxResourceLocator(Files.FileType.Internal));
		DrawUtil.init();
		setupDummyLoader();
		loadAssetLists();
		setupGdx();
		skin = new Skin(Gdx.files.internal("data/skins/basic.json"));
		consoleView.init(assetManager, skin);
		addScreenListener(consoleView);
		inputMultiplexer.addProcessor(consoleView.getInputProcessor());

		GameStates.LOADING.init(assetManager);
		GameStates.LOADING.activate();
        
		NetworkManager.getInstance().init();

		NetworkManager.getInstance().setMatchUpdateCallback(new MatchUpdateCallback() {
			
			@Override
			public void callback(String map) {
				s_map = map;
				logger.info("New map: " + map);
			}
		});
		NetworkManager.getInstance().setPlayerUpdateCallback(new PlayerUpdateCallback() {
			
			@Override
			public void callback(int playerid, String playerName, EntityType type, TeamColor team,
					boolean accept) {
				if(playercount >= 5)
					return;
				PlayerData tmp = new PlayerData(playerid, playerName, type, team, accept);
				logger.info("New Player: " + tmp.toString());
				s_players[playercount++] = tmp;
			}
		});
		NetworkManager.getInstance().setLobbyUpdateCallback(new LobbyUpdateCallback() {
			@Override
			public void callback(String map, PlayerData[] players) {
				logger.info("Map: " + map);
				logger.info("Playercount: " + players.length);
				for(int i = 0; i < players.length; i++)
					logger.info("Player" + i + ": " + players[i].getPlayername());
				c_players = players;
			}
		});
		
		NetworkManager.getInstance().setPlayerDisconnectCallback(new PlayerDisconnectCallback() {
			
			@Override
			public void callback(Integer[] playerid) {
				// TODO Auto-generated method stub
				List<PlayerData> players = new ArrayList<PlayerData>();
				for(int i = 0; i < s_players.length; i++){
					boolean inlist = true;
					for(int j = 0; j < playerid.length; j++){
						if(playerid[j] == s_players[i].getId()){
							inlist = false;
							break;
						}
					}
					if(inlist)
						players.add(s_players[i]);
				}
				s_players = players.toArray(new PlayerData[players.size()]);
				NetworkManager.getInstance().sendLobbyUpdate(s_map, s_players);
			}
		});
    	
		console.register(new ConsoleCmd("sendLobbyUpdate",0,"[DEBUG]") {

			@Override
			public void execute(List<String> args) {
				// TODO Auto-generated method stub
				NetworkManager.getInstance().sendLobbyUpdate(s_map, s_players);
			} 
		});
		console.register(new ConsoleCmd("sendMatchUpdate",0,"[DEBUG]Post a mapname.",1) {
			@Override
			public void showUsage() {
				showUsage("<mapname-text>");
			}
			
			@Override
			public void execute(List<String> args) {
				NetworkManager.getInstance().sendMatchUpdate(StringUtils.untokenize(args, 1, -1, false));
			}
			
		});
		
		console.register(new ConsoleCmd("sendPlayerUpdate",0,"[DEBUG]Post playerdata",1){
			@Override
			public void showUsage() {
				showUsage("<playername>");
			}
			
			@Override
			public void execute(List<String> args) {
				logger.info(args.get(1));
				NetworkManager.getInstance().sendPlayerUpdate(args.get(1),EntityType.Noob,TeamColor.BLACK,false);
			}
		});
	}

	public void onLoadComplete() {
		GameStates.MAINMENU.init(assetManager);
		GameStates.GAMEPLAY.init(assetManager);
		GameStates.MAINMENU.activate(new SplitVerticalTransition(500).reverse(), null);
	}

	@Override
	public void dispose() {
		DrawUtil.batch.dispose();
		GameStates.dispose();
		consoleView.dispose();
		skin.dispose();
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
		super.resize(width, height);
		DrawUtil.setViewport(width, height);
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
