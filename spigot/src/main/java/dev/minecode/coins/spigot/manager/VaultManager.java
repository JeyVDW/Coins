package dev.minecode.coins.spigot.manager;

import dev.minecode.coins.spigot.CoinsSpigot;
import dev.minecode.coins.spigot.object.EconomyIntegration;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class VaultManager {

    private Economy economy;
    private boolean vaultEnabled;

    public VaultManager() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) return;

        setupEconomy();
        vaultEnabled = true;
    }

    public void setupEconomy() {
        economy = new EconomyIntegration();
        Bukkit.getServicesManager().register(Economy.class, economy, CoinsSpigot.getInstance(), ServicePriority.Highest);
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean isVaultEnabled() {
        return vaultEnabled;
    }
}
