package de.hochschuletrier.gdw.commons.tiled;

import java.util.ArrayList;

import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxObject;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxPointList;
import de.hochschuletrier.gdw.commons.utils.Point;

/**
 * An object read from an object group layer
 *
 * @author Santo Pfingsten
 */
public class LayerObject {

    /** The xml node storing the information we need */
    private final TmxObject node;
    /** All properties of this object ready to be read */
    private SafeProperties properties;
    /** The kind of object this is */
    private final Primitive primitive;
    /** An attachment for the game to store additional information */
    private final ArrayList<Point> points;
    private int lowestX;
    private int lowestY;
    private int x;
    private int y;
    private int width;
    private int height;
    private Object attachment;

    /** The object types */
    public enum Primitive {

        POINT,
        RECT,
        TILE,
        POLYGON,
        POLYLINE,
    }

    public enum PolyMode {

        RELATIVE_TO_FIRST,
        RELATIVE_TO_TOPLEFT,
        ABSOLUTE
    }

    /**
     * Create a new layer object
     *
     * @param node The xml node storing the information we need
     */
    LayerObject(TiledMap map, TmxObject node, PolyMode polyMode) {
        this.node = node;
        properties = node.getProperties();

        if (node.getPolygon() != null) {
            primitive = Primitive.POLYGON;
        } else if (node.getPolyline() != null) {
            primitive = Primitive.POLYLINE;
        } else if (node.getGid() != null) {
            primitive = Primitive.TILE;
        } else if (node.getWidth() == null || node.getHeight() == null) {
            primitive = Primitive.POINT;
        } else {
            primitive = Primitive.RECT;
        }

        x = node.getX();
        y = node.getY();
        width = (node.getWidth() == null) ? 0 : node.getWidth();
        height = (node.getHeight() == null) ? 0 : node.getHeight();

        if (primitive == Primitive.POLYGON || primitive == Primitive.POLYLINE) {
            TmxPointList pointsNode = node.getPolygon();
            if (pointsNode == null) {
                pointsNode = node.getPolyline();
            }
            String[] numbers = pointsNode.getPoints().split("[\\s,]");
            points = new ArrayList<Point>();

            lowestX = 0;
            lowestY = 0;
            for (int i = 0; i < numbers.length; i += 2) {
                int px = Integer.parseInt(numbers[i]);
                int py = Integer.parseInt(numbers[i + 1]);
                points.add(new Point(px, py));
                if (px < lowestX) {
                    lowestX = px;
                }
                if (py < lowestY) {
                    lowestY = py;
                }
            }

            if (polyMode == PolyMode.RELATIVE_TO_TOPLEFT && (lowestX < 0 || lowestY < 0)) {
                for (Point p : points) {
                    p.x += -lowestX;
                    p.y += -lowestY;
                }
                x -= -lowestX;
                y -= -lowestY;
            } else if (polyMode == PolyMode.ABSOLUTE) {
                int dx = node.getX();
                int dy = node.getY();
                for (Point p : points) {
                    p.x += dx;
                    p.y += dy;
                }
                x = 0;
                y = 0;
            }

            lowestX += node.getX();
            lowestY += node.getY();
        } else {
            points = null;

            lowestX = node.getX();
            lowestY = node.getY();
            if (primitive == Primitive.TILE) {
                TileSet set = map.findTileSet(node.getGid());
                if (set != null) {
                    width = set.getTileWidth();
                    height = set.getTileHeight();
                    if (properties == null) {
                        properties = new SafeProperties();
                    }
                    properties.setDefaults(set.getTileProperties(node.getGid()));
                    lowestY -= height;
                }
            }
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return node.getName();
    }

    /**
     * @return the type
     */
    public String getType() {
        return node.getType();
    }

    /**
     * @return the number of pixels from left
     */
    public int getX() {
        return x;
    }

    /**
     * @return the the number of pixels from top (not necessarily the topmost position)
     */
    public int getY() {
        return y;
    }

    public int getLowestX() {
        return lowestX;
    }

    public int getLowestY() {
        return lowestY;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the image
     */
    public TmxImage getImage() {
        return node.getImage();
    }

    public SafeProperties getProperties() {
        return properties;
    }

    public String getProperty(String propertyName, String def) {
        if (properties == null) {
            return def;
        }

        return properties.getString(propertyName, def);
    }

    public int getIntProperty(String propertyName, int def) {
        if (properties == null) {
            return def;
        }
        return properties.getInt(propertyName, def);
    }

    public float getFloatProperty(String propertyName, float def) {
        if (properties == null) {
            return def;
        }
        return properties.getFloat(propertyName, def);
    }

    public double getDoubleProperty(String propertyName, double def) {
        if (properties == null) {
            return def;
        }
        return properties.getDouble(propertyName, def);
    }

    public boolean getBooleanProperty(String propertyName, boolean def) {
        if (properties == null) {
            return def;
        }
        return properties.getBoolean(propertyName, def);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public int getGid() {
        if (node.getGid() == null) {
            return -1;
        }
        return node.getGid();
    }

    /**
     * @return the kind of object this is
     */
    public Primitive getPrimitive() {
        return primitive;
    }

    /**
     * @return the attachment
     */
    public Object getAttachment() {
        return attachment;
    }

    /**
     * @param attachment the attachment to set
     */
    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }
}
