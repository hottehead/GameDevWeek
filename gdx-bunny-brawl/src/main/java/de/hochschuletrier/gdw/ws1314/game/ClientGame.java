package de.hochschuletrier.gdw.ws1314.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.input.InputHandler;
import de.hochschuletrier.gdw.ws1314.render.EntityRenderer;
import de.hochschuletrier.gdw.ws1314.render.MaterialInfo;
import de.hochschuletrier.gdw.ws1314.render.MaterialManager;
import de.hochschuletrier.gdw.ws1314.shaders.TextureAdvection;

/**
 * Created by Jerry on 18.03.14.
 */
// Modfied by El Fapo: updated intention changes
public class ClientGame {
	private ClientEntityManager entityManager;
	private ClientServerConnect netManager;
	private int Inputmask;
	private TiledMap map;
	private TiledMapRendererGdx mapRenderer;
	private InputHandler inputHandler;
	private EntityRenderer entityRenderer; 
	
	
	private FrameBuffer sceneToTexture;
	private TextureRegion sceneToTextureBuffer;
	private TextureAdvection postProcessing;

	public ClientGame() { 
		entityManager = ClientEntityManager.getInstance();
		netManager = ClientServerConnect.getInstance();
		
		inputHandler = new InputHandler();
		Main.inputMultiplexer.addProcessor(inputHandler);
		
	}

	public void init(AssetManagerX assets) {
		map = loadMap("data/maps/miniarena.tmx");
		HashMap<TileSet, Texture> tilesetImages = new HashMap<TileSet, Texture>();
		
		for (TileSet tileset : map.getTileSets()) {
			TmxImage img = tileset.getImage();
			String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(),
					img.getSource());
			tilesetImages.put(tileset, new Texture(filename));
		}
		mapRenderer = new TiledMapRendererGdx(map, tilesetImages);
		mapRenderer.setDrawLines(false);
		
		initMaterials(assets);
	}
	
	private void initMaterials(AssetManagerX assetManager) {
		MaterialManager materialManager = new MaterialManager(assetManager);
		materialManager.provideMaterial(ClientPlayer.class,
				new MaterialInfo("debugTeam", 32, 32, 0));
		
		
		entityRenderer = new EntityRenderer(materialManager);
		entityManager.provideListener(entityRenderer);
		
		sceneToTexture = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		sceneToTextureBuffer = new TextureRegion(sceneToTexture.getColorBufferTexture());
		sceneToTextureBuffer.flip(false, false);
		
		postProcessing = new TextureAdvection("data/shaders/edgeDetection.vert", "data/shaders/edgeDetection.frag");
		
	}



	public void render() {
		sceneToTexture.begin();
		
		for (Layer layer : map.getLayers()) {
			mapRenderer.render(0, 0, layer);
		}
		entityRenderer.draw();
		DrawUtil.batch.flush();
		sceneToTexture.end();
		
		DrawUtil.batch.setShader(postProcessing);
		DrawUtil.batch.draw(sceneToTextureBuffer, 0, 0);
		DrawUtil.batch.setShader(null);
	}

	public void update(float delta) {
		entityManager.update(delta);
	}

	public TiledMap loadMap(String filename) {
		try {
			return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Map konnte nicht geladen werden: "
					+ filename);
		}
	}

}
