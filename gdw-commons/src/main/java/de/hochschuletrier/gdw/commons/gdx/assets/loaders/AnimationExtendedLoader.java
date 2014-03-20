package de.hochschuletrier.gdw.commons.gdx.assets.loaders;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetLoaderParametersX;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended.PlayMode;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationLoader.PlayType;
import de.hochschuletrier.gdw.commons.jackson.JacksonList;

public class AnimationExtendedLoader extends
		AsynchronousAssetLoaderX<AnimationExtended, AnimationExtendedLoader.AnimationExtendedParameter> {

	public AnimationExtendedLoader(FileHandleResolver resolver) {
		super(resolver);
		// TODO Auto-generated constructor stub
	}

	static public class AnimationExtendedParameter extends
			AssetLoaderParametersX<AnimationExtended> {
		@JacksonList(value = Float.class)
		public ArrayList<Float> frameDuration = new ArrayList<Float>();
		public PlayMode playType = PlayMode.NORMAL;
		public Integer rows = 1;
		public Integer columns = 1;
		public Integer frames = 1;

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

	@Override
	public String getFilePrefix() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file,
			AnimationExtendedParameter parameter) {
		// TODO Auto-generated method stub

	}

	@Override
	public AnimationExtended loadSync(AssetManager manager, String fileName,
			FileHandle file, AnimationExtendedParameter parameter) {
		Texture texture = manager.get(fileName, Texture.class);
		int tileWidth = texture.getWidth() / parameter.columns;
		int tileHeight = texture.getHeight() / parameter.rows;
		TextureRegion[][] tmp = TextureRegion.split(texture, tileWidth, tileHeight);
		TextureRegion[] frames = new TextureRegion[parameter.columns * parameter.rows];
		int index = 0;
		for (int i = 0; i < parameter.rows; i++) {
			for (int j = 0; j < parameter.columns; j++) {
				frames[index] = tmp[i][j];
				frames[index].flip(false, true);
				index++;
			}
		}
		float[] frameDurations = new float[parameter.frameDuration.size()];
		for (int i = 0; i < parameter.frameDuration.size(); i++) {
			frameDurations[i] = parameter.frameDuration.get(i);
		}
		AnimationExtended anim = new AnimationExtended(parameter.playType,
				frameDurations, frames);
		return anim;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
			AnimationExtendedParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		TextureLoader.TextureParameter params = new TextureParameter();
		params.format = parameter.format;
		params.genMipMaps = parameter.genMipMaps;
		params.minFilter = parameter.minFilter;
		params.magFilter = parameter.magFilter;
		params.wrapU = parameter.wrapU;
		params.wrapV = parameter.wrapV;
		deps.add(new AssetDescriptor(parameter.filename, Texture.class, params));
		return deps;
	}

}
