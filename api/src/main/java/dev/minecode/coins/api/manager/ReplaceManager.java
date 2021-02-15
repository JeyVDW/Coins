package dev.minecode.coins.api.manager;

import dev.minecode.coins.api.object.CoinsPlayer;

public interface ReplaceManager {
    dev.minecode.core.api.manager.ReplaceManager coinsPlayer(CoinsPlayer coinsPlayer, String replacement);
}