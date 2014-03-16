package de.hochschuletrier.gdw.commons.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetLoaderParametersX;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationX;

/**
 * 
 * @author Santo Pfingsten
 */
public class AnimationXLoader extends
		AsynchronousAssetLoaderX<AnimationX, AnimationXLoader.AnimationXParameter> {
	private static final String PREFIX = "AnimationX:";

	public AnimationXLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle fileHandle,
			AnimationXParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		TextureParameter params = new TextureParameter();
		params.format = parameter.format;
		params.genMipMaps = parameter.genMipMaps;
		params.minFilter = parameter.minFilter;
		params.magFilter = parameter.magFilter;
		params.wrapU = parameter.wrapU;
		params.wrapV = parameter.wrapV;
		deps.add(new AssetDescriptor(parameter.filename, Texture.class, params));
		return deps;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle fileHandle,
			AnimationXParameter parameter) {
	}

	@Override
	public AnimationX loadSync(AssetManager manager, String fileName,
			FileHandle fileHandle, AnimationXParameter parameter) {
		return new AnimationX(manager.get(parameter.filename, Texture.class),
				parameter.rows, parameter.columns, parameter.frameTime, parameter.loop);
	}

	@Override
	public String getFilePrefix() {
		return PREFIX;
	}

	/**
	 * Parameter to be passed to
	 * {@link AssetManager#load(String, Class, AssetLoaderParameters)} if
	 * additional configuration is necessary for the {@link AnimationX}.
	 */
	static public class AnimationXParameter extends AssetLoaderParametersX<AnimationX> {
		public Integer rows = 1;
		public Integer columns = 1;
		public Float frameTime = 100.0f;
		public Boolean loop;

		/**
		 * the format of the final Texture. Uses the source images format if
		 * null
		 **/
		public Pixmap.Format format = null;
		/** whether to generate mipmaps **/
		public Boolean genMipMaps = Boolean.FALSE;
		public TextureFilter minFilter = TextureFilter.Nearest;
		public TextureFilter magFilter = TextureFilter.Nearest;
		public Texture.TextureWrap wrapU = Texture.TextureWrap.ClampToEdge;
		public Texture.TextureWrap wrapV = Texture.TextureWrap.ClampToEdge;
	}
}

