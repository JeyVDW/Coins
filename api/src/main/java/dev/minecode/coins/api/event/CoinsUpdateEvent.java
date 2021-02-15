package dev.minecode.coins.api.event;

import dev.minecode.coins.api.object.CoinsEvent;
import dev.minecode.coins.api.object.CoinsPlayer;

public class CoinsUpdateEvent implements CoinsEvent {

    private CoinsPlayer coinsPlayer;
    private double oldCoins;

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