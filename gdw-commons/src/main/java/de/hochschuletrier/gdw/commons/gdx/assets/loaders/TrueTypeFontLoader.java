package de.hochschuletrier.gdw.commons.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetLoaderParametersX;
import de.hochschuletrier.gdw.commons.gdx.assets.TrueTypeFont;

public class TrueTypeFontLoader extends
		AsynchronousAssetLoaderX<TrueTypeFont, TrueTypeFontLoader.TrueTypeFontParameter> {

	public TrueTypeFontLoader(FileHandleResolver resolver) {
		super(resolver);
		// TODO Auto-generated constructor stub
	}

	static class TrueTypeFontParameter extends AssetLoaderParametersX<TrueTypeFont> {

	}

	@Override
	public String getFilePrefix() {
		return "";
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file,
			TrueTypeFontParameter parameter) {
		// TODO Auto-generated method stub

	}

	@Override
	public TrueTypeFont loadSync(AssetManager manager, String fileName, FileHandle file,
			TrueTypeFontParameter parameter) {
		return new TrueTypeFont(resolve(fileName));
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
			TrueTypeFontParameter parameter) {
		// TODO Auto-generated method stub
		return null;
	}
}
