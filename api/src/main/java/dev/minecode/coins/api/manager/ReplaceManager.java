package dev.minecode.coins.api.manager;

import dev.minecode.coins.api.object.CoinsPlayer;

public interface ReplaceManager {

    ReplaceManager coinsPlayer(CoinsPlayer coinsPlayer, String replacement);

    dev.minecode.core.api.manager.ReplaceManager getCoreReplaceManager();

}