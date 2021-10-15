package dev.minecode.coins.common.api.manager;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.api.manager.FileManager;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.FileObject;

public class FileManagerProvider implements FileManager {

    private FileObject config;
    private FileObject players;

    public FileManagerProvider() {
        makeInstances();
    }

    private void makeInstances() {
        config = CoreAPI.getInstance().getFileManager().getFileObject(CoinsAPI.getInstance().getThisCorePlugin(), "config.yml");

        if (!CoreAPI.getInstance().isUsingSQL())
            players = CoreAPI.getInstance().getFileManager().getFileObject(CoinsAPI.getInstance().getThisCorePlugin(), "players.yml");
    }

    @Override
    public boolean saveData() {
        if (!CoreAPI.getInstance().isUsingSQL())
            return players.save();
        return false;
    }

    @Override
    public FileObject getConfig() {
        return config;
    }

    @Override
    public FileObject getPlayers() {
        return players;
    }
}
