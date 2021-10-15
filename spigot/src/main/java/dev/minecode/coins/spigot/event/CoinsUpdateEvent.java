package dev.minecode.coins.spigot.event;

import dev.minecode.coins.api.object.CoinsPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CoinsUpdateEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

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

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}