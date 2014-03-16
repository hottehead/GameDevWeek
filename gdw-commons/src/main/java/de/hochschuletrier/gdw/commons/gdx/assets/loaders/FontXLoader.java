package de.hochschuletrier.gdw.commons.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetLoaderParametersX;
import de.hochschuletrier.gdw.commons.gdx.assets.FontX;

/**
 * 
 * @author Santo Pfingsten
 */
public class FontXLoader extends
		AsynchronousAssetLoader<FontX, FontXLoader.FontXParameter> {
	public FontXLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle fileHandle,
			FontXParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		BitmapFontParameter params = new BitmapFontParameter();
		params.flip = parameter.flip;
		params.minFilter = parameter.minFilter;
		params.magFilter = parameter.magFilter;
		params.bitmapFontData = parameter.bitmapFontData;
		deps.add(new AssetDescriptor(fileHandle, BitmapFont.class, params));
		return deps;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle fileHandle,
			FontXParameter parameter) {
	}

	@Override
	public FontX loadSync(AssetManager manager, String fileName, FileHandle fileHandle,
			FontXParameter parameter) {
		return new FontX(manager.get(fileName, BitmapFont.class));
	}

	/**
	 * Parameter to be passed to
	 * {@link AssetManager#load(String, Class, AssetLoaderParameters)} if
	 * additional configuration is necessary for the {@link FontX}.
	 */
	static public class FontXParameter extends AssetLoaderParametersX<FontX> {
		/** whether to flipY the font or not **/
		public Boolean flip = Boolean.FALSE;
		/** the minimum filter to be used for the backing texture */
		public TextureFilter minFilter = TextureFilter.Nearest;
		/** the maximum filter to be used for the backing texture */
		public TextureFilter magFilter = TextureFilter.Nearest;
		/**
		 * optional BitmapFontData to be used instead of loading the texture
		 * directly. Use this if your font is embedded in a skin.
		 **/
		public BitmapFontData bitmapFontData = null;
	}
}

