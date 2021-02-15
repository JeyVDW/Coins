package dev.minecode.coins.common.api;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.api.manager.EventManager;
import dev.minecode.coins.api.manager.FileManager;
import dev.minecode.coins.api.manager.ReplaceManager;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.coins.common.CoinsCommon;
import dev.minecode.coins.common.api.manager.EventManagerProvider;
import dev.minecode.coins.common.api.manager.FileManagerProvider;
import dev.minecode.coins.common.api.manager.ReplaceManagerProvider;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlayer;
import dev.minecode.core.api.object.Language;
import dev.minecode.core.api.object.LanguageAbstract;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

public class CoinsAPIProvider extends CoinsAPI {

    private FileManagerProvider fileManagerProvider;
    private EventManagerProvider eventManagerProvider;

    public CoinsAPIProvider() {
        makeInstances();
    }

    private void makeInstances() {
        CoinsAPI.setInstance(this);
        fileManagerProvider = new FileManagerProvider();
        eventManagerProvider = new EventManagerProvider();
    }

    @Override
    public FileManager getFileManager() {
        return fileManagerProvider;
    }

    @Override
    public ReplaceManager getReplaceManager(String message) {
        return new ReplaceManagerProvider(message);
    }

    @Override
    public ReplaceManager getReplaceManager(BaseComponent[] message) {
        return new ReplaceManagerProvider(message);
    }

    @Override
    public ReplaceManager getReplaceManager(Language language, LanguageAbstract path) {
        return new ReplaceManagerProvider(language, path);
    }

    @Override
    public EventManager getEventManager() {
        return eventManagerProvider;
    }

    @Override
    public CoinsPlayer getCoinsPlayer(CorePlayer corePlayer) {
        if (corePlayer == null) return null;

        return CoinsCommon.getInstance().getCoinsPlayerAddon().newCoinsPlayer(corePlayer);
    }

    @Override
    public CoinsPlayer getCoinsPlayer(int id) {
        CorePlayer corePlayer = CoreAPI.getInstance().getCorePlayer(id);
        return getCoinsPlayer(corePlayer);
    }

    @Override
    public CoinsPlayer getCoinsPlayer(UUID uuid) {
        CorePlayer corePlayer = CoreAPI.getInstance().getCorePlayer(uuid);
        return getCoinsPlayer(corePlayer);
    }

    @Override
    public CoinsPlayer getCoinsPlayer(String name) {
        CorePlayer corePlayer = CoreAPI.getInstance().getCorePlayer(name);
        return getCoinsPlayer(corePlayer);
    }
}
