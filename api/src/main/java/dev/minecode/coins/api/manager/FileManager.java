package dev.minecode.coins.api.manager;

import dev.minecode.core.api.object.FileObject;

public interface FileManager {

    boolean saveDatas();

    FileObject getData();

    FileObject getConfig();

}
