package de.hochschuletrier.gdw.commons.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;

/**
 * Use if the asset does not have its own file, but loads another asset using the filename
 *
 * @author Santo Pfingsten
 */
public abstract class AsynchronousAssetLoaderX<T, P extends AssetLoaderParameters<T>> extends AsynchronousAssetLoader<T, P> {
    
	public AsynchronousAssetLoaderX (FileHandleResolver resolver) {
		super(resolver);
	}
    
    public abstract String getFilePrefix();
}
