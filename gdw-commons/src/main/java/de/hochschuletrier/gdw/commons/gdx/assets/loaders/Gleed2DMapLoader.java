package de.hochschuletrier.gdw.commons.gdx.assets.loaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gleed2DMapLoader extends
AsynchronousAssetLoader<Map, Gleed2DMapLoader.Parameters> {
    private static final Logger logger = LoggerFactory.getLogger(Gleed2DMapLoader.class);

	public static class Parameters extends AssetLoaderParameters<Map> {
		/** Whether to load the map for a y-up coordinate system */
		public boolean yUp = true;
		/** generate mipmaps? **/
		public boolean generateMipMaps = false;
		/** The TextureFilter to use for minification **/
		public TextureFilter textureMinFilter = TextureFilter.Nearest;
		/** The TextureFilter to use for magnification **/
		public TextureFilter textureMagFilter = TextureFilter.Nearest;
	}

	Parameters parameter;
	Element root;
	XmlReader xml = new XmlReader();
	Map map;

	public Gleed2DMapLoader() {
		this(new InternalFileHandleResolver());

	}

	public Gleed2DMapLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	public Map load(String filename, Parameters param) {
		parameter = param;
		map = new Map();
		// FileHandle file = resolve(filename);
		FileHandle file = Gdx.files.local(filename);

		try {
			root = xml.parse(file);
		} catch (IOException e) {
            logger.error("Failed parsing map file", e);
		}
		String name = root.getAttribute("Name", "default");
		boolean isVisible = root.getBoolean("Visible", true);
		MapProperties props = map.getProperties();
		props.put("name", name);
		props.put("visible", isVisible);
		loadLayers(root.getChildByName("Layers"));
		return map;

	}

	private void loadLayers(Element layers) {
		for (Element layer : layers.getChildrenByName("Layer")) {
			boolean isVisible = layer.getBoolean("Visible", true);
			String name = layer.getAttribute("Name");
			Element scrollSpeed = layer.getChildByName("ScrollSpeed");
			float scrollSpeedX = Float.parseFloat(scrollSpeed.getChildByName("X")
					.getText());
			float scrollSpeedY = Float.parseFloat(scrollSpeed.getChildByName("Y")
					.getText());

			MapLayer currentLayer = new MapLayer();
			currentLayer.setName(name);
			currentLayer.setVisible(isVisible);
			currentLayer.getProperties().put("scrollSpeedX", scrollSpeedX);
			currentLayer.getProperties().put("scrollSpeedY", scrollSpeedY);
			loadItems(layer.getChildByName("Items"), currentLayer);
			map.getLayers().add(currentLayer);
		}
	}

	/**
	 * Parst die RGB
	 * 
	 * @param color
	 * @return
	 */
	private Color parseColor(Element color) {
		float r, g, b, a;
		r = Float.valueOf(color.getChildByName("R").getText());
		g = Float.valueOf(color.getChildByName("G").getText());
		b = Float.valueOf(color.getChildByName("B").getText());
		a = Float.valueOf(color.getChildByName("A").getText());

		return new Color(r / 255, g / 255, b / 255, a / 255);
	}

	private void loadItems(Element objects, MapLayer layer) {
		for (Element xml_Item : objects.getChildrenByName("Item")) {
			MapObject object = null;
			/* Parse general item data */
			// TODO: Opacity parsen
			String name = xml_Item.getAttribute("Name", "default");
			boolean isVisible = xml_Item.getBoolean("Visible", true);
			// Position
			Element xml_Position = xml_Item.getChildByName("Position");
			float x = Float.parseFloat(xml_Position.getChildByName("X").getText());
			float tmpPos = Float.parseFloat(xml_Position.getChildByName("Y").getText());
			float y = parameter.yUp ? -tmpPos : tmpPos;
			/* Farbe parsen. keine unterscheidung zwischen texture und primitive */
			Color color = Color.WHITE.cpy();
			Element texColor = xml_Item.getChildByName("TintColor");
			Element primitiveColor = xml_Item.getChildByName("FillColor");
			Element lineColor = xml_Item.getChildByName("LineColor");

			if (texColor != null) {
				color = parseColor(texColor);
			} else if (primitiveColor != null) {
				color = parseColor(primitiveColor);
			} else if (lineColor != null) {
				color = parseColor(lineColor);
			}
			String type = xml_Item.getAttribute("xsi:type", "noneFound");
			/* Parse a Circle Item */
			if (type.equals("CircleItem")) {
				// TODO: FillColor parsen
				Element xml_Radius = xml_Item.getChildByName("Radius");
				float radius = Float.parseFloat(xml_Radius.getText());
				object = new CircleMapObject(x, y, radius);
			}

			/* Parse a Path Item */
			else if (type.equals("PathItem")) {
				// TODO: Linewidth und LineColor parsen
				Array<Element> xml_Vertices = xml_Item.getChildByName("WorldPoints")
						.getChildrenByName("Vector2");
				float[] vertices = new float[xml_Vertices.size * 2];
				int i = 0;
				for (Element vertex : xml_Vertices) {
					vertices[i] = Float.parseFloat(vertex.getChildByName("X").getText());
					float posY = Float.parseFloat(vertex.getChildByName("Y").getText());
					vertices[i + 1] = parameter.yUp ? -posY : posY;
					i += 2;
				}
				/* Determine whether it's a polygon or not */
				if (Boolean.parseBoolean(xml_Item.getChildByName("IsPolygon").getText())) {
					object = new PolygonMapObject(vertices);
				} else {
					object = new PolylineMapObject(vertices);
				}
			}

			/* Parse a Rectangle Item */
			else if (type.equals("RectangleItem")) {
				// TODO: FillColor parsen
				float width = Float
						.parseFloat(xml_Item.getChildByName("Width").getText());
				float height = Float.parseFloat(xml_Item.getChildByName("Height")
						.getText());
				y -= height;
				object = new RectangleMapObject(x, y, width, height);
			}

			/* Parse a Texture Item */
			else if (type.equals("TextureItem")) {
				float rotation = Float.parseFloat(xml_Item.getChildByName("Rotation")
						.getText());
				Element xml_Scale = xml_Item.getChildByName("Scale");
				float scaleX = Float.parseFloat(xml_Scale.getChildByName("X").getText());
				float scaleY = Float.parseFloat(xml_Scale.getChildByName("Y").getText());
				boolean flipHorizontally = Boolean.parseBoolean(xml_Item.getChildByName(
						"FlipHorizontally").getText());
				boolean flipVertically = Boolean.parseBoolean(xml_Item.getChildByName(
						"FlipVertically").getText());
				Element xml_Origin = xml_Item.getChildByName("Origin");
				float originX = Float
						.parseFloat(xml_Origin.getChildByName("X").getText());
				float originY = Float
						.parseFloat(xml_Origin.getChildByName("Y").getText());
				originY = parameter.yUp ? -originY : originY;
				String filepath = xml_Item.getChildByName("texture_filename").getText();
				Pattern p = Pattern.compile("(.*\\\\)?([^\\\\]*)[.][^.]*$");
				Matcher matcher = p.matcher(filepath);
				boolean match = matcher.matches();
				String file =  matcher.group(2);
				object = new TextureMapObject();

				TextureMapObject tmp = (TextureMapObject) object;
				tmp.setRotation(rotation);
				tmp.setScaleX(scaleX);
				tmp.setScaleY(scaleY);
				tmp.setOriginX(originX);
				tmp.setOriginY(originY);
				tmp.getProperties().put("flipHorizontally", flipHorizontally);
				tmp.getProperties().put("flipVertically", flipVertically);
				tmp.getProperties().put("ressource", file); // filename
				tmp.setX(x);
				tmp.setY(y);
				// in props
			} else {
				throw new RuntimeException("Unknown Itemtype " + type
						+ " in GleedMapLayer " + layer.getName());
			}

			parseCustomProperties(object, xml_Item.getChildByName("CustomProperties"));
			/* Set general attributes */
			object.setName(name);
			object.setVisible(isVisible);
			object.setColor(color);
			object.getProperties().put("x", x); // positonen nochmals in
												// properties
			object.getProperties().put("y", y);
			layer.getObjects().add(object);
		}
	}

	private void parseCustomProperties(MapObject mapObject, Element xml_customProperties) {
		for (Element xml_property : xml_customProperties.getChildrenByName("Property")) {
			String type = xml_property.getAttribute("Type");
			if (type.equals("Item")) {
				mapObject.getProperties().put(xml_property.getAttribute("Name"),
						xml_property.getText());
			} else if (type.equals("bool")) {
				Element child = xml_property.getChildByName("boolean");
				mapObject.getProperties().put(xml_property.getAttribute("Name"),
						Boolean.valueOf(child.getText()));
			} else if (type.equals("string")) {
				Element child = xml_property.getChildByName("string");
				mapObject.getProperties().put(xml_property.getAttribute("Name"),
						String.valueOf(child.getText()));
			}
		}

	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file,
			Parameters parameter) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map loadSync(AssetManager manager, String fileName, FileHandle file,
			Parameters parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
			Parameters parameter) {
		// TODO Auto-generated method stub
		return null;
	}

}
