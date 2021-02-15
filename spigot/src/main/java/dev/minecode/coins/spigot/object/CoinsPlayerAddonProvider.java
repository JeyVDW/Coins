package dev.minecode.coins.spigot.object;

import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.coins.common.object.CoinsPlayerAddon;
import dev.minecode.coins.spigot.api.object.CoinsPlayerProvider;
import dev.minecode.core.api.object.CorePlayer;

public class CoinsPlayerAddonProvider implements CoinsPlayerAddon {
    @Override
    public CoinsPlayer newCoinsPlayer(CorePlayer corePlayer) {
        return new CoinsPlayerProvider(corePlayer);
    }
}
