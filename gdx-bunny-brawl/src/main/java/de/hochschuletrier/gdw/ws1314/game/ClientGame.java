package de.hochschuletrier.gdw.ws1314.game;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.basic.GameInfo;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.hud.GameplayStage;
import de.hochschuletrier.gdw.ws1314.input.InputHandler;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.render.CameraFollowingBehaviour;
import de.hochschuletrier.gdw.ws1314.render.EntityRenderer;
import de.hochschuletrier.gdw.ws1314.render.LevelBoundings;
import de.hochschuletrier.gdw.ws1314.render.MaterialManager;
import de.hochschuletrier.gdw.ws1314.shaders.FrameBufferFBO;
import de.hochschuletrier.gdw.ws1314.shaders.TextureAdvection;

/**
 * Created by Jerry on 18.03.14.
 */
// Modfied by El Fapo: updated intention changes
public class ClientGame {
	private static final Logger logger = LoggerFactory.getLogger(ClientGame.class);

	private ClientEntityManager entityManager;
	private NetworkManager netManager;
	private int Inputmask;
	private TiledMap map;
	private TiledMapRendererGdx mapRenderer;
	private InputHandler inputHandler;
	private EntityRenderer entityRenderer;

	private FrameBufferFBO sceneToTexture;
	private TextureAdvection postProcessing;
	private TextureAdvection advShader;

	private ClientPlayer player;
	private GameInfo gameInfo;
	
	private GameplayStage stage;


	public ClientGame() {
		entityManager = ClientEntityManager.getInstance();
		netManager = NetworkManager.getInstance();

		inputHandler = new InputHandler();
		Main.inputMultiplexer.addProcessor(inputHandler);
	}

	CameraFollowingBehaviour cameraFollowingBehaviour;

	public void init(AssetManagerX assets, String mapName) {
		//Map 
		map = assets.getTiledMap(mapName);
		HashMap<TileSet, Texture> tilesetImages = new HashMap<TileSet, Texture>();
		for (TileSet tileset : map.getTileSets()) {
			TmxImage img = tileset.getImage();
			String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(),
					img.getSource());
			tilesetImages.put(tileset, new Texture(filename));
		}
		mapRenderer = new TiledMapRendererGdx(map, tilesetImages);
		mapRenderer.setDrawLines(false);
		//Materials
		initMaterials(assets);
		//LevelBounds
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		LevelBoundings levelBounds = new LevelBoundings(width * 0.5f, height * 0.5f,
				map.getWidth() * map.getTileWidth(), map.getHeight()
						* map.getTileHeight());
		//Camera
		cameraFollowingBehaviour = new CameraFollowingBehaviour(DrawUtil.getCamera(),
				levelBounds);
		//GameInfo und LocalPlayer
		gameInfo = entityManager.getGameInfo();
		long playerId = entityManager.getPlayerEntityID();
		if (playerId != -1) {
			ClientEntity playerEntity = entityManager.getEntityById(playerId);
			if (playerEntity instanceof ClientPlayer) {
				player = (ClientPlayer) playerEntity;
			}
			cameraFollowingBehaviour.setFollowingEntity(playerEntity);
		}
		//HUD
		stage = new GameplayStage();
		stage.init(assets,this);
	}

	private void initMaterials(AssetManagerX assetManager) {
		MaterialManager materialManager = new MaterialManager(assetManager);
		entityRenderer = new EntityRenderer(materialManager);

		entityManager.provideListener(entityRenderer);

		sceneToTexture = new FrameBufferFBO(Format.RGBA8888, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);
		Main.getInstance().addScreenListener(sceneToTexture);

		postProcessing = new TextureAdvection("data/shaders/post.vert",
				"data/shaders/post.frag");
		logger.error(postProcessing.getLog());
		advShader = new TextureAdvection("data/shaders/texAdv.vert",
				"data/shaders/texAdv.frag");
		logger.error(advShader.getLog());
	}

	public void render() {
		sceneToTexture.begin();
		DrawUtil.batch.setShader(advShader);
		for (Layer layer : map.getLayers()) {
			if (layer.getType() == Layer.Type.OBJECT
					&& layer.getBooleanProperty("renderEntities", false)) {
				entityRenderer.draw();
			} else {
				mapRenderer.render(0, 0, layer);
			}
		}
		DrawUtil.batch.flush();
		sceneToTexture.end();

		DrawUtil.startRenderToScreen();
		DrawUtil.screenSpace.update();
		DrawUtil.batch.setShader(postProcessing);
		DrawUtil.batch.draw(sceneToTexture.getActiveFrameBuffer(), 0, 0);
		DrawUtil.batch.setShader(null);
		DrawUtil.batch.flush();
		DrawUtil.endRenderToScreen();

		DrawUtil.startRenderToScreen();
		stage.render();
		DrawUtil.endRenderToScreen();
	}

	public void update(float delta) {
		mapRenderer.update(delta);
		entityManager.update(delta);

		long playerId = entityManager.getPlayerEntityID();
		if (playerId != -1) {
			ClientEntity playerEntity = entityManager
					.getEntityById(playerId);
			if(playerEntity instanceof ClientPlayer) {
				player = (ClientPlayer) playerEntity;
			}
			cameraFollowingBehaviour.setFollowingEntity(playerEntity);
		}
		cameraFollowingBehaviour.update(delta);
	}

	public ScreenListener getHUD() {
		return stage;
	}

	public ClientPlayer getPlayer() {
		return player;
	}

	public GameInfo getGameInfo() {
		return gameInfo;
	}
	
}
