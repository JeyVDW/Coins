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
        config = CoreAPI.getInstance().getFileManager().getFileObject("config.yml", CoreAPI.getInstance().getPluginManager().getPluginName());
        data = CoreAPI.getInstance().getFileManager().getFileObject("data.yml", CoreAPI.getInstance().getPluginManager().getPluginName());
    }

    @Override
    public boolean saveDatas() {
        return data.save();
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
