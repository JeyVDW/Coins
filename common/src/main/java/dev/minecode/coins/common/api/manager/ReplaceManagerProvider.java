package dev.minecode.coins.common.api.manager;

import dev.minecode.coins.api.manager.ReplaceManager;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.core.api.object.Language;
import dev.minecode.core.api.object.LanguageAbstract;
import net.md_5.bungee.api.chat.BaseComponent;

public class ReplaceManagerProvider extends dev.minecode.core.common.api.manager.ReplaceManagerProvider implements ReplaceManager {
    public ReplaceManagerProvider(String message) {
        super(message);
    }

    public ReplaceManagerProvider(BaseComponent[] message) {
        super(message);
    }

    public ReplaceManagerProvider(Language language, LanguageAbstract path) {
        super(language, path);
    }

    @Override
    public dev.minecode.core.api.manager.ReplaceManager coinsPlayer(CoinsPlayer coinsPlayer, String replacement) {
        return replaceAll("%" + replacement + "UUID%", String.valueOf(coinsPlayer.getCorePlayer().getUuid()))
                .replaceAll("%" + replacement + "Name%", String.valueOf(coinsPlayer.getCorePlayer().getName()))
                .replaceAll("%" + replacement + "Coins%", String.valueOf(coinsPlayer.getCoins()));
    }
}
