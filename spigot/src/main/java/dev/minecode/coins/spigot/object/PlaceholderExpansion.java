package dev.minecode.coins.spigot.object;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.spigot.CoinsSpigot;
import org.bukkit.OfflinePlayer;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private CoinsSpigot plugin;

    public PlaceholderExpansion(CoinsSpigot plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "MineCode";
    }

    @Override
    public String getIdentifier() {
        return "coins";
    }

    @Override
    public String getVersion() {
        return CoinsSpigot.getInstance().getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (identifier.equals("playercoins")) {
            return String.valueOf(CoinsAPI.getInstance().getCoinsPlayer(player.getUniqueId()).getCoins());
        }
        return null;
    }

}
