package dev.minecode.coins.common.object;

import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.core.api.object.CorePlayer;

public interface CoinsPlayerAddon {

    CoinsPlayer newCoinsPlayer(CorePlayer corePlayer);

}
