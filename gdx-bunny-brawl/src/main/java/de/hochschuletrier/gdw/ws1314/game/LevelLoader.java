package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.LayerObject.Primitive;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.commons.utils.Point;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Jerry on 18.03.14.
 */
public class LevelLoader {
	private static ServerEntityManager entityManager;
	private static PhysixManager physicsManager;
	private static Vector2 startpos;
	private static TiledMap map;
	private static Set<Class> classes;
	private static HashMap<String, String> classToPath = new HashMap<>();
	private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);

	public static void load(TiledMap map, ServerEntityManager entityManager,
			PhysixManager physicsManager) {
		LevelLoader.map = map;

		LevelLoader.entityManager = entityManager;
		LevelLoader.physicsManager = physicsManager;
		entityManager.Clear();
		physicsManager.reset();
		try {
			classes = ClassUtils
					.findClassesInPackage("de.hochschuletrier.gdw.ws1314.entity.levelObjects");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Class clazz : classes) {

			String fullName = clazz.getName();
			String[] parts = fullName.split("\\.");
			String name = parts[parts.length - 1];
			name = name.replace("Server", "");
			name = name.toLowerCase();
			classToPath.put(name, fullName);
		}

		for (Layer layer : map.getLayers()) {
			if (layer.isObjectLayer()) {
				loadObjectLayer(layer);
			}
		}
	}

	private static void loadObjectLayer(Layer layer) {
		for (LayerObject object : layer.getObjects()) {
			String type = object.getType();
			if (object.getPrimitive() == LayerObject.Primitive.TILE)
				type = object.getProperty("type", null);

			TileSet findTileSet = map.findTileSet(object.getGid());
			if (findTileSet != null) {
				type = findTileSet.getProperty("type", null);
				if (type == null) {
					logger.warn("Couldn't find type for object with GID "
							+ object.getGid());
					continue;
				}
			}else {
				continue;
			}
			try {
				Class clazz = Class.forName(classToPath.get(type));
				entityManager.createEntity(clazz,
						new Vector2(object.getX(), object.getY()));
				logger.info("Creating Entity Type " + clazz);
				System.out.println("asdsd");
			} catch (ClassNotFoundException e) {
				logger.error("No class found for type " + type);
				e.printStackTrace();
			}

			if (object.getProperties() != null) {
				object.getProperties()
						.setString(
								"renderLayer",
								layer.getProperty("renderLayer",
										String.valueOf(layer.getIndex())));
			}

			switch (object.getPrimitive()) {
			case POINT:
				createPoint(type, object.getX(), object.getY(), object.getProperties());
				break;
			case RECT:
				createRect(type, object.getX(), object.getY(), object.getWidth(),
						object.getHeight(), object.getProperties());
				break;
			case TILE:

				createTile(type, object.getX(), object.getLowestY(), object.getWidth(),
						object.getHeight(), object.getProperties(), object.getName(),
						object.getGid());
				break;
			case POLYGON:
				createPolygon(type, object.getPoints(), object.getProperties());
				break;
			case POLYLINE:
				createPolyLine(object.getType(), object.getPoints(),
						object.getProperties(), object.getName());
				break;
			}
		}
	}

	/**
	 * Create ground, paths, etc here
	 * 
	 * @param type
	 *            the type set in the editor
	 * @param points
	 *            the points of the line (absolute points)
	 * @param properties
	 *            the object properties
	 */
	private static void createPolyLine(String type, ArrayList<Point> points,
			SafeProperties properties, String name) {
		switch (type) {
		case "solid":
			PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, physicsManager)
					.create();
			body.createFixture(new PhysixFixtureDef(physicsManager).density(0.5f)
					.friction(0.5f).restitution(0.4f).shapePolyline(points));
			break;
		}
	}

	/**
	 * Create deadzones, triggers, etc here
	 * 
	 * @param type
	 *            the type set in the editor
	 * @param points
	 *            the points of the line (absolute points)
	 * @param properties
	 *            the object properties
	 */
	private static void createPolygon(String type, ArrayList<Point> points,
			SafeProperties properties) {
		switch (type) {
		case "solid":
			PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, physicsManager)
					.create();
			body.createFixture(new PhysixFixtureDef(physicsManager).density(0.5f)
					.friction(0.5f).restitution(0.4f).shapePolygon(points));
			break;

		}
	}

	/**
	 * Create rectangle deadzones, triggers, etc here
	 * 
	 * @param type
	 *            the type set in the editor
	 * @param x
	 *            the distance from left in pixels
	 * @param y
	 *            the distance from top in pixels
	 * @param width
	 *            width in pixels
	 * @param height
	 *            height in pixels
	 * @param properties
	 *            the object properties
	 */
	private static void createRect(String type, int x, int y, int width, int height,
			SafeProperties properties) {
		x += width / 2;
		y += height / 2;

		ServerEntity entity = null;
		switch (type) {
		case "solid":
			PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, physicsManager)
					.position(x, y).create();
			body.createFixture(new PhysixFixtureDef(physicsManager).density(0.5f)
					.friction(0.5f).restitution(0.4f).shapeBox(width, height));
			break;
		default:
			System.err.println("Unknown Rect-Object in Map, type: " + type);
			break;
		}
	}

	/**
	 * Create items, enemies, etc here
	 * 
	 * @param type
	 *            the type set in the editor
	 * @param x
	 *            the distance from left in pixels
	 * @param y
	 *            the distance from top in pixels
	 * @param width
	 *            width in pixels, NOT ACCURATE!!
	 * @param height
	 *            height in pixels, NOT ACCURATE!!
	 * @param properties
	 *            the object properties
	 */
	private static void createTile(String type, int x, int y, int width, int height,
			SafeProperties properties, String name, int gid) {

		x += width / 2;
		y += height / 2;

		ServerEntity entity = null;
		switch (type) {
		case "solid":
			PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, physicsManager)
					.position(x, y).create();
			body.createFixture(new PhysixFixtureDef(physicsManager).density(0.5f)
					.friction(0.5f).restitution(0.4f).shapeBox(width, height));
		}

		if (entity != null) {
			/*
			 * if(entity.isBottomPositioned()) { y += height/2; }
			 * entity.setOrigin(x, y); entity.setInitialSize(width, height);
			 */
		}
	}

	/**
	 * Currently no plan for use
	 * 
	 * @param type
	 *            the type set in the editor
	 * @param x
	 *            the distance from left in pixels
	 * @param y
	 *            the distance from top in pixels
	 * @param properties
	 *            the object properties
	 */
	private static void createPoint(String type, int x, int y, SafeProperties properties) {
		switch (type) {
		case "solid":
			PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, physicsManager)
					.position(x, y).create();
			body.createFixture(new PhysixFixtureDef(physicsManager).density(0.5f)
					.friction(0.5f).restitution(0.4f).shapeBox(1, 1));
			break;
		}
	}

	public void loadSolids() {
		for (int i = 0; i < map.getLayers().size(); i++) {
			Layer l = map.getLayers().get(i);
			ArrayList<LayerObject> objects = l.getObjects();
			if (objects == null) {
				continue;
			}

			for (int k = 0; k < objects.size(); k++) {
				LayerObject layerObject = objects.get(k);
				Vector2 origin = new Vector2(layerObject.getX(), layerObject.getY());
				int x = layerObject.getX();
				int y = layerObject.getY();

				boolean b = l.getBooleanProperty("solid", false);
				System.out.println(b);
				if (b) {
					Primitive p = layerObject.getPrimitive();
					if (p == Primitive.POINT) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody,
								physicsManager).position(origin).fixedRotation(true)
								.create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						points.add(new de.hochschuletrier.gdw.commons.utils.Point(x, y));
						body.createFixture(new PhysixFixtureDef(physicsManager)
								.density(0.5f).friction(0.5f).restitution(0.4f)
								.shapePolygon(points));
					} else if (p == Primitive.RECT) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody,
								physicsManager).position(origin).fixedRotation(true)
								.create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						points.add(new de.hochschuletrier.gdw.commons.utils.Point(x, y));
						body.createFixture(new PhysixFixtureDef(physicsManager)
								.density(0.5f).friction(0.5f).restitution(0.4f)
								.shapeBox(x, y));
					} else if (p == Primitive.TILE) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody,
								physicsManager).position(origin).fixedRotation(true)
								.create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						for (int j = 0; j < points.size(); j++) {
							points.add(new de.hochschuletrier.gdw.commons.utils.Point(x,
									y));
						}
						body.createFixture(new PhysixFixtureDef(physicsManager)
								.density(0.5f).friction(0.5f).restitution(0.4f)
								.shapePolygon(points));
					} else if (p == Primitive.POLYGON) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody,
								physicsManager).position(origin).fixedRotation(true)
								.create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						for (int j = 0; j < points.size(); j++) {
							points.add(new de.hochschuletrier.gdw.commons.utils.Point(x,
									y));
						}
						body.createFixture(new PhysixFixtureDef(physicsManager)
								.density(0.5f).friction(0.5f).restitution(0.4f)
								.shapePolygon(points));
					} else if (p == Primitive.POLYLINE) {
						PhysixBody body = new PhysixBodyDef(BodyType.StaticBody,
								physicsManager).position(origin).fixedRotation(true)
								.create();
						List<de.hochschuletrier.gdw.commons.utils.Point> points = new ArrayList<de.hochschuletrier.gdw.commons.utils.Point>();
						for (int j = 0; j < points.size(); j++) {
							points.add(new de.hochschuletrier.gdw.commons.utils.Point(x,
									y));
						}
						body.createFixture(new PhysixFixtureDef(physicsManager)
								.density(0.5f).friction(0.5f).restitution(0.4f)
								.shapePolygon(points));
					}
				}
			}
		}
	}

}
