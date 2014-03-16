package de.hochschuletrier.gdw.commons.gdx.tiled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

import de.hochschuletrier.gdw.commons.tiled.ITiledMapRenderer;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;

/**
 * A Map renderer which renders the TiledMap with a vbo, just for testing.. does not seem to improve anything
 *
 * @author Santo Pfingsten
 */
public class TiledMapRendererGdxVBO implements ITiledMapRenderer {

    final TiledMap map;
    final Color layerFilter = Color.WHITE.cpy();
    final ArrayList<LayerImageVBO> vbos = new ArrayList<LayerImageVBO>();
	final Map<TileSet, Texture> images;

    public static class TileInfoX {

        float x, y, width, height;
        float srcX, srcY, srcWidth, srcHeight;

        public TileInfoX(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.srcX = srcX;
            this.srcY = srcY;
            this.srcWidth = srcWidth;
            this.srcHeight = srcHeight;
        }
    }

    public static class LayerImageVBO {

		private final Texture image;
        private final Mesh mesh;

		public LayerImageVBO(Texture image, ArrayList<TileInfoX> list) {
            float invTexWidth = 1f / image.getWidth();
            float invTexHeight = 1f / image.getHeight();
            this.image = image;
            int maxVertices = 4 * list.size();
            int maxIndices = 6 * list.size();
            float[] vertices = new float[4 * maxVertices];
            short[] indices = new short[maxIndices];

            int ipos = 0;
            int ipos2 = 0;
            int vpos = 0;
            for (TileInfoX info : list) {
                // top left
                vertices[vpos++] = info.x;
                vertices[vpos++] = info.y;
                vertices[vpos++] = (info.srcX) * invTexWidth;
                vertices[vpos++] = (info.srcY) * invTexHeight;

                // bottom left
                vertices[vpos++] = info.x;
                vertices[vpos++] = info.y + info.height;
                vertices[vpos++] = (info.srcX) * invTexWidth;
                vertices[vpos++] = (info.srcY + info.srcHeight) * invTexHeight;

                // top right
                vertices[vpos++] = info.x + info.width;
                vertices[vpos++] = info.y;
                vertices[vpos++] = (info.srcX + info.srcWidth) * invTexWidth;
                vertices[vpos++] = (info.srcY) * invTexHeight;

                // bottom right
                vertices[vpos++] = info.x + info.width;
                vertices[vpos++] = info.y + info.height;
                vertices[vpos++] = (info.srcX + info.srcWidth) * invTexWidth;
                vertices[vpos++] = (info.srcY + info.srcHeight) * invTexHeight;

                indices[ipos++] = (short) ipos2;
                indices[ipos++] = (short) (ipos2 + 1);
                indices[ipos++] = (short) (ipos2 + 2);

                indices[ipos++] = (short) (ipos2 + 1);
                indices[ipos++] = (short) (ipos2 + 2);
                indices[ipos++] = (short) (ipos2 + 3);

                ipos2 += 4;
            }

            mesh = new Mesh(true, maxVertices, maxIndices,
                    new VertexAttribute(Usage.Position, 2, "a_position"),
                    new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
            mesh.setVertices(vertices);
            mesh.setIndices(indices);
            mesh.setAutoBind(true);
        }

        public void draw() {
            image.bind();
            throw new RuntimeException("need a shader here to render the tiled map");
//            mesh.render(GL20.GL_TRIANGLES);
        }
    }

	public TiledMapRendererGdxVBO(TiledMap map, Map<TileSet, Texture> tilesetImages) {
        this(map, new Layer[]{map.getLayers().get(0)}, tilesetImages);
    }

	public TiledMapRendererGdxVBO(TiledMap map, Layer[] layers,
			Map<TileSet, Texture> tilesetImages) {
        images = tilesetImages;

        int mapTileWidth = map.getTileWidth();
        int mapTileHeight = map.getTileHeight();
        this.map = map;
        ArrayList<TileSet> tileSets = map.getTileSets();
        for (Layer layer : layers) {
            if (layer.isTileLayer()) {
                TileInfo[][] tiles = layer.getTiles();
                HashMap<TileSet, ArrayList<TileInfoX>> tileSetInfoMap = new HashMap<TileSet, ArrayList<TileInfoX>>();
                for (int y = 0; y < tiles[0].length; y++) {
                    for (int x = 0; x < tiles.length; x++) {
                        if (tiles[x][y] != null) {
                            TileSet tileSet = tileSets.get(tiles[x][y].tileSetId);
                            ArrayList<TileInfoX> list = tileSetInfoMap.get(tileSet);
                            if (list == null) {
                                list = new ArrayList<TileInfoX>();
                                tileSetInfoMap.put(tileSet, list);
                            }

                            int sheetX = tileSet.getTileX(tiles[x][y].localId);
                            int sheetY = tileSet.getTileY(tiles[x][y].localId);

                            int tileOffsetY = tileSet.getTileHeight() - mapTileHeight;

                            float px = x * mapTileWidth;
                            float py = (y * mapTileHeight) - tileOffsetY;

                            list.add(new TileInfoX(px, py, tileSet.getTileWidth(), tileSet.getTileHeight(), sheetX * tileSet.getTileWidth(), sheetY * tileSet.getTileHeight(), tileSet.getTileWidth(), tileSet.getTileHeight()));
                        }
                    }
                }

                for (TileSet tileSet : tileSetInfoMap.keySet()) {
					Texture image = images.get(tileSet);
                    ArrayList<TileInfoX> list = tileSetInfoMap.get(tileSet);
                    vbos.add(new LayerImageVBO(image, list));
                }
            }
        }
    }

    @Override
    public void render(int x, int y, int sx, int sy, int width, int height, Layer layer) {
        render();
    }
    @Override
    public void render(int x, int y) {
        render();
    }

    @Override
    public void render(int x, int y, Layer layer) {
        render();
    }

    @Override
    public void render(int x, int y, int sx, int sy, int width, int height) {
        render();
    }

    @Override
    public void renderTileLayers(int x, int y, int sx, int sy, int width, int height) {
        render();
    }

    public void render() {
        for (LayerImageVBO vbo : vbos) {
            vbo.draw();
        }
    }
    
    @Override
    public void dispose() {
		for (Texture image : images.values()) {
            image.dispose();
        }
        images.clear();
    }
}
