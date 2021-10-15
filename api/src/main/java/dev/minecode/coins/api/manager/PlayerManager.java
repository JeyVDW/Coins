package dev.minecode.coins.api.manager;

import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.core.api.object.CorePlayer;

import java.util.UUID;

public interface PlayerManager {

    CoinsPlayer getPlayer(CorePlayer corePlayer);

    CoinsPlayer getPlayer(UUID uuid);

    CoinsPlayer getPlayer(String name);

}
