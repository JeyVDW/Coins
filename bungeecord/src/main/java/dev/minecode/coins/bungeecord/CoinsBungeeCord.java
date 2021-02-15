package dev.minecode.coins.bungeecord;

import dev.minecode.coins.bungeecord.command.CoinsCommand;
import dev.minecode.coins.bungeecord.object.CoinsPlayerAddonProvider;
import dev.minecode.coins.common.CoinsCommon;
import dev.minecode.core.bungeecord.CoreBungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class CoinsBungeeCord extends Plugin {

    private static CoinsBungeeCord instance;

    private CoreBungeeCord coreBungeeCord;
    private CoinsCommon coinsCommon;

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
        coreBungeeCord = new CoreBungeeCord(this);
        coinsCommon = new CoinsCommon();
        CoinsCommon.getInstance().setCoinsPlayerAddon(new CoinsPlayerAddonProvider());
    }

    private void registerCommand() {
        getProxy().getPluginManager().registerCommand(this, new CoinsCommand("coins"));
    }
}
