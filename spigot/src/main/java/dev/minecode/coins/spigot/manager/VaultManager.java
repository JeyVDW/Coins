package dev.minecode.coins.spigot.manager;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.coins.spigot.CoinsSpigot;
import dev.minecode.coins.spigot.object.EconomyIntegration;
import dev.minecode.core.api.CoreAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.spongepowered.configurate.ConfigurationNode;

public class VaultManager {

    private final Plugin vaultPlugin;
    private Economy economy;
    private boolean vaultEnabled;

    private long refreshDelay;

    public VaultManager() {
        if ((vaultPlugin = CoinsSpigot.getInstance().getServer().getPluginManager().getPlugin("Vault")) == null) return;

        ConfigurationNode conf = CoinsAPI.getInstance().getFileManager().getConfig().getConf();
        boolean vaultConfig = conf.node("vault").getBoolean();
        refreshDelay = conf.node("refreshDelay").getLong();

        if (!vaultConfig || !vaultPlugin.isEnabled()) return;

        if (setupEconomy()) {
            vaultEnabled = true;
            if (CoreAPI.getInstance().isUsingSQL())
                runEconomyChecker();
        }
    }

    public boolean setupEconomy() {
        Economy vaultEcoHook = new EconomyIntegration();
        Bukkit.getServicesManager().register(Economy.class, vaultEcoHook, vaultPlugin, ServicePriority.Highest);

        RegisteredServiceProvider<Economy> rsp = CoinsSpigot.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        return (economy = rsp.getProvider()) != null;
    }

    public void runEconomyChecker() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CoinsSpigot.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                CoinsPlayer coinsPlayer = CoinsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                Economy economy = CoinsSpigot.getInstance().getVaultManager().getEconomy();
                double vaultBalance = economy.getBalance(player);

                if (vaultBalance != coinsPlayer.getCoins()) {
                    double diff = coinsPlayer.getCoins() - vaultBalance;
                    if (diff < 0)
                        CoinsSpigot.getInstance().getVaultManager().getEconomy().withdrawPlayer(player, diff * (-1));
                    else
                        CoinsSpigot.getInstance().getVaultManager().getEconomy().depositPlayer(player, diff);
                }
            }
        }, 0, refreshDelay);
    }

    public Economy getEconomy() {
        return economy;
    }

    public boolean isVaultEnabled() {
        return vaultEnabled;
    }
}
