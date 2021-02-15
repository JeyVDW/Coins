package dev.minecode.coins.api;

import dev.minecode.coins.api.manager.EventManager;
import dev.minecode.coins.api.manager.FileManager;
import dev.minecode.coins.api.manager.ReplaceManager;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlayer;
import dev.minecode.core.api.object.Language;
import dev.minecode.core.api.object.LanguageAbstract;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

public abstract class CoinsAPI {

    // Instance
    private static CoinsAPI instance;

    public static CoinsAPI getInstance() {
        return instance;
    }

    public static void setInstance(CoinsAPI coreAPI) {
        CoinsAPI.instance = coreAPI;
    }


    // CoreAPI
    private static CoreAPI getCoreAPI() {
        return CoreAPI.getInstance();
    }


    // Manager
    public abstract FileManager getFileManager();

    public abstract ReplaceManager getReplaceManager(String message);

    public abstract ReplaceManager getReplaceManager(BaseComponent[] message);

    public abstract ReplaceManager getReplaceManager(Language language, LanguageAbstract path);

    public abstract EventManager getEventManager();


    // Objects
    public abstract CoinsPlayer getCoinsPlayer(CorePlayer corePlayer);

    public abstract CoinsPlayer getCoinsPlayer(int id);

    public abstract CoinsPlayer getCoinsPlayer(UUID uuid);

    public abstract CoinsPlayer getCoinsPlayer(String name);

}
