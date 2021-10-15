package dev.minecode.coins.bungeecord.event;

import dev.minecode.coins.api.object.CoinsPlayer;
import net.md_5.bungee.api.plugin.Event;

public class CoinsUpdateEvent extends Event {

    private final CoinsPlayer coinsPlayer;
    private final double oldCoins;

    public CoinsUpdateEvent(CoinsPlayer coinsPlayer, double oldCoins) {
        this.coinsPlayer = coinsPlayer;
        this.oldCoins = oldCoins;
    }

    public CoinsPlayer getCoinsPlayer() {
        return coinsPlayer;
    }

    public double getOldCoins() {
        return oldCoins;
    }

}