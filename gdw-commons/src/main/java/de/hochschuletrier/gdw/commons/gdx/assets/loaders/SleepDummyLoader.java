package de.hochschuletrier.gdw.commons.gdx.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.utils.QuietUtils;

/**
 * Sleep Dummy Loader
 *
 * @author Santo Pfingsten
 */
public class SleepDummyLoader extends AsynchronousAssetLoader<SleepDummyLoader.SleepDummy, SleepDummyLoader.SleepDummyParameter> {

    public static SleepDummy dummy = new SleepDummy();

    public SleepDummyLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle fileHandle, SleepDummyParameter parameter) {
        return null;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle fileHandle, SleepDummyParameter parameter) {
    }

    @Override
    public SleepDummy loadSync(AssetManager manager, String fileName, FileHandle fileHandle, SleepDummyParameter parameter) {
        QuietUtils.sleep(parameter.millis);
        return dummy;
    }

    /** Parameter to be passed to {@link AssetManager#load(String, Class, AssetLoaderParameters)} if additional configuration is
     * necessary for the {@link SleepDummy}. */
    static public class SleepDummyParameter extends AssetLoaderParameters<SleepDummy> {

        int millis;

        public SleepDummyParameter(int millis) {
            this.millis = millis;
        }
    }

    static public class SleepDummy {
    }
}
