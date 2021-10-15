package dev.minecode.coins.common.api;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.api.manager.EventManager;
import dev.minecode.coins.api.manager.FileManager;
import dev.minecode.coins.api.manager.ReplaceManager;
import dev.minecode.coins.common.CoinsCommon;
import dev.minecode.coins.common.api.manager.EventManagerProvider;
import dev.minecode.coins.common.api.manager.FileManagerProvider;
import dev.minecode.coins.common.api.manager.PlayerManagerProvider;
import dev.minecode.coins.common.api.manager.ReplaceManagerProvider;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlugin;
import dev.minecode.core.api.object.Language;
import dev.minecode.core.api.object.LanguageAbstract;
import net.md_5.bungee.api.chat.BaseComponent;

public class CoinsAPIProvider extends CoinsAPI {

    private EventManagerProvider eventManagerProvider;
    private FileManagerProvider fileManagerProvider;

    private CorePlugin thisCorePlugin;

    public CoinsAPIProvider() {
        makeInstances();
    }

    private void makeInstances() {
        CoinsAPI.setInstance(this);

        thisCorePlugin = CoreAPI.getInstance().getPluginManager().getPlugin("Coins");
        fileManagerProvider = new FileManagerProvider();
        eventManagerProvider = new EventManagerProvider();
    }

    @Override
    public EventManager getEventManager() {
        return eventManagerProvider;
    }

    @Override
    public FileManager getFileManager() {
        return fileManagerProvider;
    }

    @Override
    public PlayerManagerProvider getPlayerManager() {
        return CoinsCommon.getInstance().getPlayerManagerProvider();
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
    public CorePlugin getThisCorePlugin() {
        return thisCorePlugin;
    }
}
