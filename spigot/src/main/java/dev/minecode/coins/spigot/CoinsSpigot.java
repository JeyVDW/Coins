package dev.minecode.coins.spigot;

import dev.minecode.coins.common.CoinsCommon;
import dev.minecode.coins.spigot.command.CoinsCommand;
import dev.minecode.coins.spigot.manager.VaultManager;
import dev.minecode.coins.spigot.object.CoinsPlayerAddonProvider;
import dev.minecode.coins.spigot.object.PlaceholderExpansion;
import dev.minecode.core.spigot.CoreSpigot;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CoinsSpigot extends JavaPlugin {

    private static CoinsSpigot instance;
    private CoreSpigot coreSpigot;
    private CoinsCommon coinsCommon;

    private VaultManager vaultManager;
    private boolean vaultEnabled;

    public static CoinsSpigot getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        makeInstances();
        registerCommand();
    }

    @Override
    public void onDisable() {
        CoreSpigot.getInstance().onDisable();
    }

    private void makeInstances() {
        instance = this;
        coreSpigot = new CoreSpigot(this);
        coinsCommon = new CoinsCommon();
        CoinsCommon.getInstance().setCoinsPlayerAddon(new CoinsPlayerAddonProvider());

        if (getServer().getPluginManager().getPlugin("Vault") != null)
            vaultManager = new VaultManager();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new PlaceholderExpansion(this).register();
    }

    private void registerCommand() {
        getCommand("coins").setExecutor(new CoinsCommand());
    }

    public VaultManager getVaultManager() {
        return vaultManager;
    }

    public boolean isVaultEnabled() {
        return vaultEnabled;
    }

    public void setVaultEnabled(boolean vaultEnabled) {
        this.vaultEnabled = vaultEnabled;
    }
}
