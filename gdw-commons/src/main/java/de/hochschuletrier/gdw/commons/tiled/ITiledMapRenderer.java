package de.hochschuletrier.gdw.commons.tiled;

/**
 *
 * @author Santo Pfingsten
 */
public interface ITiledMapRenderer {
    /**
     * Render the whole tile map at a given location
     *
     * @param x The x location to render at
     * @param y The y location to render at
     */
    public void render(int x, int y);

    /**
     * Render a single layer from the map
     *
     * @param x The x location to render at
     * @param y The y location to render at
     * @param layer The layer to render
     */
    public void render(int x, int y, Layer layer);

    /**
     * Render a section of the tile map
     *
     * @param x The x location to render at
     * @param y The y location to render at
     * @param sx The x tile location to start rendering
     * @param sy The y tile location to start rendering
     * @param width The width of the section to render (in tiles)
     * @param height The height of the secton to render (in tiles)
     */
    public void render(int x, int y, int sx, int sy, int width, int height);

    /**
     * Render a section of the specified layer
     *
     * @param x The x location to render at
     * @param y The y location to render at
     * @param sx The x tile location to start rendering
     * @param sy The y tile location to start rendering
     * @param width The width of the section to render (in tiles)
     * @param height The height of the secton to render (in tiles)
     * @param layer The layer to render
     */
    public void render(int x, int y, int sx, int sy, int width, int height, Layer layer);

    /**
     * Render a section of the tile map, but only the tile layers
     *
     * @param x The x location to render at
     * @param y The y location to render at
     * @param sx The x tile location to start rendering
     * @param sy The y tile location to start rendering
     * @param width The width of the section to render (in tiles)
     * @param height The height of the secton to render (in tiles)
     */
    public void renderTileLayers(int x, int y, int sx, int sy, int width, int height);

    /**
     * Dispose of all textures allocated for the tile sets
     */
    public void dispose();
}
