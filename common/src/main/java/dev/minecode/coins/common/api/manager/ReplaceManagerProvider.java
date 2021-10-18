package dev.minecode.coins.common.api.manager;

import dev.minecode.coins.api.manager.ReplaceManager;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.Language;
import dev.minecode.core.api.object.LanguageAbstract;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ReplaceManagerProvider implements ReplaceManager {

    private String message;

    public ReplaceManagerProvider(String message) {
        this.message = message;
    }

    public ReplaceManagerProvider(BaseComponent[] message) {
        this.message = ComponentSerializer.toString(message);
    }

    public ReplaceManagerProvider(Language language, LanguageAbstract path) {
        this.message = CoreAPI.getInstance().getLanguageManager().getString(language, path);
    }

    public ReplaceManagerProvider replaceAll(String toReplace, String replaceWith) {
        if (message != null)
            this.message = this.message.replaceAll(toReplace, replaceWith);
        return this;
    }

    public ReplaceManager coinsPlayer(CoinsPlayer coinsPlayer, String replacement) {
        return replaceAll("%" + replacement + "UUID%", String.valueOf(coinsPlayer.getCorePlayer().getUuid()))
                .replaceAll("%" + replacement + "Name%", String.valueOf(coinsPlayer.getCorePlayer().getName()))
                .replaceAll("%" + replacement + "Coins%", String.valueOf(coinsPlayer.getCoins()));
    }

    @Override
    public dev.minecode.core.api.manager.ReplaceManager getCoreReplaceManager() {
        return CoreAPI.getInstance().getReplaceManager(message);
    }
}
