package dev.minecode.coins.api.manager;

import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.core.api.object.CorePlayer;

import java.util.UUID;

public interface PlayerManager {

    CoinsPlayer getCoinsPlayer(CorePlayer corePlayer);

    CoinsPlayer getCoinsPlayer(int id);

    CoinsPlayer getCoinsPlayer(UUID uuid);

    CoinsPlayer getCoinsPlayer(String name);

}
