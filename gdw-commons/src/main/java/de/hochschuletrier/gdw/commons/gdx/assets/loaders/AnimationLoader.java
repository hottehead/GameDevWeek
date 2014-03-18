package de.hochschuletrier.gdw.commons.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetLoaderParametersX;

public class AnimationLoader extends
		AsynchronousAssetLoaderX<Animation, AnimationLoader.AnimationParameter> {

	public AnimationLoader(FileHandleResolver resolver) {
		super(resolver);
		// TODO Auto-generated constructor stub
	}
    
    public enum PlayType {
        NORMAL,
        REVERSED,
        LOOP,
        LOOP_REVERSED,
        LOOP_PINGPONG,
        LOOP_RANDOM;
    }

	static public class AnimationParameter extends AssetLoaderParametersX<Animation> {
		public Float frameDuration = 100.0f;
		public PlayType playType = PlayType.NORMAL;
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
		return "";
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file,
			AnimationParameter parameter) {
		// TODO Auto-generated method stub

	}


	@Override
	public Animation loadSync(AssetManager manager, String fileName, FileHandle file,
			AnimationParameter parameter) {
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
        
		Animation animation = new Animation(parameter.frameDuration, frames);
        int playMode = (parameter.playType == null) ? 0 : parameter.playType.ordinal();
		animation.setPlayMode(playMode);
		return animation;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
			AnimationParameter parameter) {
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
