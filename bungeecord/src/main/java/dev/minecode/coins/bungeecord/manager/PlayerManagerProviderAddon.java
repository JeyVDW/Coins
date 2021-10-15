package dev.minecode.coins.bungeecord.manager;


import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.coins.bungeecord.api.object.CoinsPlayerProvider;
import dev.minecode.coins.common.api.manager.PlayerManagerProvider;
import dev.minecode.core.api.object.CorePlayer;

public class PlayerManagerProviderAddon extends PlayerManagerProvider {

    @Override
    public CoinsPlayer newPlayer(CorePlayer corePlayer) {
        return new CoinsPlayerProvider(corePlayer);
    }

}
