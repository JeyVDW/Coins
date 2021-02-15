package dev.minecode.coins.common.api.manager;

import dev.minecode.coins.api.manager.FileManager;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.FileObject;

public class FileManagerProvider implements FileManager {

    private FileObject config;
    private FileObject data;

    public FileManagerProvider() {
        makeInstances();
    }

    private void makeInstances() {
        config = CoreAPI.getInstance().getFileObject("config.yml", CoreAPI.getInstance().getPluginName());
        data = CoreAPI.getInstance().getFileObject("data.yml", CoreAPI.getInstance().getPluginName());
    }

    @Override
    public boolean saveDatas() {
        boolean saved = true;
        if (!data.save())
            saved = false;
        return saved;
    }

    @Override
    public FileObject getConfig() {
        return config;
    }

    @Override
    public FileObject getData() {
        return data;
    }
}
