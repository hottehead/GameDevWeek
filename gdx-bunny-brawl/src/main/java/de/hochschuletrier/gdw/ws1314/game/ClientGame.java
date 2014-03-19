package de.hochschuletrier.gdw.ws1314.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.LayerObject.Primitive;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.input.InputHandler;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 * Created by Jerry on 18.03.14.
 */
public class ClientGame {
	private ClientEntityManager entityManager;
	//private ClientServerConnect netManager;
	private int Inputmask;
	private TiledMap map;
	private TiledMapRendererGdx mapRenderer;
	
	private InputHandler inputHandler;

	public ClientGame() {
		entityManager = ClientEntityManager.getInstance();
		//netManager = ClientServerConnect.getInstance();
	}

	public void init(AssetManagerX assets) {
		map = loadMap("data/maps/miniarena.tmx");
		HashMap<TileSet, Texture> tilesetImages = new HashMap<TileSet, Texture>();
		
		Gdx.input.setInputProcessor(inputHandler);
		
		for (TileSet tileset : map.getTileSets()) {
			TmxImage img = tileset.getImage();
			String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(),
					img.getSource());
			tilesetImages.put(tileset, new Texture(filename));
		}
		mapRenderer = new TiledMapRendererGdx(map, tilesetImages);
		mapRenderer.setDrawLines(false);
	}

	public void render() {
		for (Layer layer : map.getLayers()) {
			mapRenderer.render(0, 0, layer);
		}
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

	/*public boolean keyDown(int keycode) {

		switch (keycode) {
		case (Input.Keys.A): {
			netManager.sendAction(PlayerIntention.MOVE_TOGGLE_LEFT);
			break;
		}
		case (Input.Keys.S): {
			netManager.sendAction(PlayerIntention.MOVE_TOGGLE_DOWN);
			break;
		}
		case (Input.Keys.D): {
			netManager.sendAction(PlayerIntention.MOVE_TOGGLE_RIGHT);
			break;
		}
		case (Input.Keys.W): {
			netManager.sendAction(PlayerIntention.MOVE_TOGGLE_UP);
			break;
		}
		case (Input.Keys.SPACE): {
			netManager.sendAction(PlayerIntention.ATTACK_1);
			break;
		}
		case (Input.Keys.E): {
			netManager.sendAction(PlayerIntention.DROP_EGG);
			break;
		}
		}

		return false;
	}

	public boolean keyUp(int keycode) {
		switch (keycode) {
		case (Input.Keys.A): {
			netManager.sendAction(PlayerIntention.MOVE_TOGGLE_LEFT);
			break;
		}
		case (Input.Keys.S): {
			netManager.sendAction(PlayerIntention.MOVE_TOGGLE_DOWN);
			break;
		}
		case (Input.Keys.D): {
			netManager.sendAction(PlayerIntention.MOVE_TOGGLE_RIGHT);
			break;
		}
		case (Input.Keys.W): {
			netManager.sendAction(PlayerIntention.MOVE_TOGGLE_UP);
			break;
		}
		}
		return false;
	}*/

}
