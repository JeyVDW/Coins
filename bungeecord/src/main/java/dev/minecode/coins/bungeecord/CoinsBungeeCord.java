package dev.minecode.coins.bungeecord;

import dev.minecode.coins.bungeecord.command.CoinsCommand;
import dev.minecode.coins.bungeecord.manager.PlayerManagerProviderAddon;
import dev.minecode.coins.common.CoinsCommon;
import dev.minecode.core.bungeecord.CoreBungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class CoinsBungeeCord extends Plugin {

    private static CoinsBungeeCord instance;

    public static CoinsBungeeCord getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        makeInstances();
        registerCommand();
    }

    @Override
    public void onDisable() {
        CoreBungeeCord.getInstance().onDisable();
    }

    private void makeInstances() {
        instance = this;
        CoreBungeeCord.getInstance().registerPlugin(this, true);
        CoinsCommon.getInstance().setPlayerManagerProvider(new PlayerManagerProviderAddon());
    }

    private void registerCommand() {
        getProxy().getPluginManager().registerCommand(this, new CoinsCommand("coins"));
    }
}
