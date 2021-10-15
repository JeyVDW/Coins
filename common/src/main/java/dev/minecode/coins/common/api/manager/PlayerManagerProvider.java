package dev.minecode.coins.common.api.manager;

import dev.minecode.coins.api.manager.PlayerManager;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlayer;

import java.util.UUID;

public abstract class PlayerManagerProvider implements PlayerManager {

    @Override
    public CoinsPlayer getPlayer(CorePlayer corePlayer) {
        if (corePlayer == null) return null;
        return newPlayer(corePlayer);
    }

    @Override
    public CoinsPlayer getPlayer(UUID uuid) {
        CorePlayer corePlayer = CoreAPI.getInstance().getPlayerManager().getPlayer(uuid);
        if (corePlayer == null) return null;
        return newPlayer(corePlayer);
    }

    @Override
    public CoinsPlayer getPlayer(String name) {
        CorePlayer corePlayer = CoreAPI.getInstance().getPlayerManager().getPlayer(name);
        if (corePlayer == null) return null;
        return newPlayer(corePlayer);
    }

    public abstract CoinsPlayer newPlayer(CorePlayer corePlayer);

}
