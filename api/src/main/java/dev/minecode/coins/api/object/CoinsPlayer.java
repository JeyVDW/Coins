package dev.minecode.coins.api.object;

import dev.minecode.core.api.object.CorePlayer;

public interface CoinsPlayer {

    boolean reload();

    boolean save();

    CorePlayer getCorePlayer();

    int getCoins();

    boolean setCoins(int coins);

    boolean addCoins(int coins);

    boolean removeCoins(int coins);

}
