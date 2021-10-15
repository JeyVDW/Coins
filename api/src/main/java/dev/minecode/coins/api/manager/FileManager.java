package dev.minecode.coins.api.manager;

import dev.minecode.core.api.object.FileObject;

public interface FileManager {

    boolean saveData();

    FileObject getPlayers();

    FileObject getConfig();

}
