package dev.minecode.coins.common.api.manager;

import dev.minecode.coins.api.manager.PlayerManager;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.coins.common.CoinsCommon;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlayer;

import java.util.UUID;

public class PlayerManagerProvider implements PlayerManager {

    @Override
    public CoinsPlayer getCoinsPlayer(CorePlayer corePlayer) {
        if (corePlayer == null) return null;
        return CoinsCommon.getInstance().getCoinsPlayerAddon().newCoinsPlayer(corePlayer);
    }

    @Override
    public CoinsPlayer getCoinsPlayer(int id) {
        CorePlayer corePlayer = CoreAPI.getInstance().getPlayerManager().getCorePlayer(id);
        return getCoinsPlayer(corePlayer);
    }

    @Override
    public CoinsPlayer getCoinsPlayer(UUID uuid) {
        CorePlayer corePlayer = CoreAPI.getInstance().getPlayerManager().getCorePlayer(uuid);
        return getCoinsPlayer(corePlayer);
    }

    @Override
    public CoinsPlayer getCoinsPlayer(String name) {
        CorePlayer corePlayer = CoreAPI.getInstance().getPlayerManager().getCorePlayer(name);
        return getCoinsPlayer(corePlayer);
    }

}
