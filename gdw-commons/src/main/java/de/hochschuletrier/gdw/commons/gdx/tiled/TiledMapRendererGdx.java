package de.hochschuletrier.gdw.commons.gdx.tiled;

import de.hochschuletrier.gdw.commons.tiled.ITiledMapRenderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TileSetAnimation;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.Point;

import java.util.ArrayList;
import java.util.Map;

/**
 * A map renderer which renders the TiledMap with libgdx
 * 
 * @author Santo Pfingsten
 */
public class TiledMapRendererGdx implements ITiledMapRenderer {

	final TiledMap map;
	final int mapTileWidth;
	final int mapTileHeight;
	final Color layerFilter = Color.WHITE.cpy();
	final ShapeRenderer shapeRenderer = new ShapeRenderer();
	boolean drawLines;
    float stateTime = 0;

	public TiledMapRendererGdx(TiledMap map, Map<TileSet, Texture> tilesetImages) {
		this.map = map;
		mapTileWidth = map.getTileWidth();
		mapTileHeight = map.getTileHeight();

		for (TileSet tileset : map.getTileSets()) {
			tileset.setAttachment(tilesetImages.get(tileset));
		}
	}
    
	public void setDrawLines(boolean drawLines) {
		this.drawLines = drawLines;
	}

    public void update(float delta) {
        stateTime += delta;
    }

	/**
	 * Render the whole tile map at a given location
	 * 
	 * @param x
	 *            The x location to render at
	 * @param y
	 *            The y location to render at
	 */
	@Override
	public void render(int x, int y) {
		render(x, y, 0, 0, map.getWidth(), map.getHeight());
	}

	@Override
	public void render(int x, int y, Layer layer) {
		render(x, y, 0, 0, map.getWidth(), map.getHeight(), layer);
	}

	@Override
	public void render(int x, int y, int sx, int sy, int width, int height) {
		for (Layer layer : map.getLayers()) {
			render(x, y, sx, sy, width, height, layer);
		}
	}

	@Override
	public void render(int x, int y, int sx, int sy, int width, int height, Layer layer) {
		if (layer.isObjectLayer()) {
			renderObjects(layer, x, y, sx, sy);
		} else {
			for (int ty = 0; ty < height; ty++) {
				renderTileLayerLine(layer, x, y, sx, sy, width, ty);
			}
			DrawUtil.resetColor();
		}
	}

	@Override
	public void renderTileLayers(int x, int y, int sx, int sy, int width, int height) {
		for (Layer layer : map.getLayers()) {
			if (layer.isTileLayer()) {
				for (int ty = 0; ty < height; ty++) {
					renderTileLayerLine(layer, x, y, sx, sy, width, ty);
				}
				DrawUtil.resetColor();
			}
		}
	}

	/**
	 * Render a section of this tile layer
	 * 
	 * @param x
	 *            The x location to render at
	 * @param y
	 *            The y location to render at
	 * @param sx
	 *            The x tile location to start rendering
	 * @param sy
	 *            The y tile location to start rendering
	 * @param width
	 *            The number of tiles across to render
	 * @param ty
	 *            The line of tiles to render
	 */
	private void renderTileLayerLine(Layer layer, int x, int y, int sx, int sy,
			int width, int ty) {
		if (layer.getBooleanProperty("invisible", false)) {
			return;
		}

		float opacity = layer.getOpacity();
		if (opacity == 0) {
			return;
		}

        boolean test = true;
		layerFilter.a = opacity;
		DrawUtil.setColor(layerFilter);

		TileInfo[][] tiles = layer.getTiles();
		for (TileSet tileset : map.getTileSets()) {
			int id = tileset.getIndex();
            
            int animationOffset = 0;
            TileSetAnimation anim = tileset.getAnimation();
            if(anim != null) {
                int frameNumber = (int)(stateTime / anim.frameDuration);
                frameNumber = frameNumber % anim.numFrames;
                animationOffset = anim.tileOffset * frameNumber;
            }

			Texture image = null;
			for (int tx = 0; tx < width; tx++) {
				if ((sx + tx < 0) || (sy + ty < 0)) {
					continue;
				}
				if (sx + tx >= map.getWidth() || sy + ty >= map.getHeight()) {
					continue;
				}

				TileInfo info = tiles[sx + tx][sy + ty];
				if (info != null && info.tileSetId == id) {
					if (image == null) {
						image = (Texture) tileset.getAttachment();
					}

					int sheetX = tileset.getTileX(tiles[sx + tx][sy + ty].localId) + animationOffset;
                    
					int sheetY = tileset.getTileY(tiles[sx + tx][sy + ty].localId);
                    
					int tileOffsetY = tileset.getTileHeight() - mapTileHeight;

					float px = x + (tx * mapTileWidth);
					float py = y + (ty * mapTileHeight) - tileOffsetY;

					DrawUtil.batch.draw(image, px, py,
                            tileset.getTileWidth(), tileset.getTileHeight(),
                            (int)(sheetX * tileset.getTileWidth()), ((int)sheetY * tileset.getTileHeight()),
                            tileset.getTileWidth(), tileset.getTileHeight(),
                            false, true);
				}
			}
		}
	}

	/**
	 * Render a section of this object layer
	 * 
	 * @param x
	 *            The x location to render at
	 * @param y
	 *            The y location to render at
	 * @param sx
	 *            The x tile location to start rendering
	 * @param sy
	 *            The y tile location to start rendering
	 * @param mapTileWidth
	 *            the tile width specified in the map file
	 * @param mapTileHeight
	 *            the tile height specified in the map file
	 */
	private void renderObjects(Layer layer, int x, int y, int sx, int sy) {
		DrawUtil.setLineWidth(2.0f);

		int offsX = x + sx * map.getTileWidth() - x;
		int offsY = sy * map.getTileHeight() - y;
		DrawUtil.translate(-offsX, -offsY);

		for (LayerObject object : layer.getObjects()) {
			switch (object.getPrimitive()) {
			case POLYGON:
				if (drawLines) {
					drawPolyLine(object.getPoints(), true);
				}
				break;
			case POLYLINE:
				if (drawLines) {
					drawPolyLine(object.getPoints(), false);
				}
				break;
			case TILE:
				drawTile(object);
				break;
			case RECT:
				DrawUtil.drawRect(object.getX(), object.getY(), object.getWidth(),
						object.getHeight());
				break;
			case POINT:
				DrawUtil.drawRect(object.getX() - 1, object.getY() - 1, 2, 2);
				break;
			default:
				throw new AssertionError(object.getPrimitive().name());
			}
		}
		DrawUtil.translate(offsX, offsY);
	}

	private void drawPolyLine(ArrayList<Point> points, boolean loop) {
		DrawUtil.batch.end();
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		Point first = null;
		Point last = null;
		for (Point point : points) {
			if (last != null) {
				shapeRenderer.line(last.x, last.y, point.x, point.y);
			} else {
				first = point;
			}
			last = point;
		}
		if (loop && last != null && first != null) {
			shapeRenderer.line(last.x, last.y, first.x, first.y);
		}
		shapeRenderer.end();
		DrawUtil.batch.begin();
	}

	private void drawTile(LayerObject object) {
		int gid = object.getGid();
		TileSet tileset = map.findTileSet(gid);
		Texture image = (Texture) tileset.getAttachment();

		int sheetX = tileset.getTileX(gid - tileset.getFirstGID());
		int sheetY = tileset.getTileY(gid - tileset.getFirstGID());

		DrawUtil.batch.draw(image, object.getLowestX(), object.getLowestY(),
				tileset.getTileWidth(), tileset.getTileHeight(),
				sheetX * tileset.getTileWidth(), sheetY * tileset.getTileHeight());
	}

	@Override
	public void dispose() {
		for (TileSet tileset : map.getTileSets()) {
			Texture image = (Texture) tileset.getAttachment();
			if (image != null) {
				image.dispose();
			}
		}
	}
}
