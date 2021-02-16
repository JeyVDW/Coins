package dev.minecode.coins.spigot.api.object;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.api.event.CoinsUpdateEvent;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.coins.spigot.CoinsSpigot;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlayer;
import dev.minecode.core.api.object.FileObject;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CoinsPlayerProvider implements CoinsPlayer {

    private static FileObject fileObject = CoinsAPI.getInstance().getFileManager().getData();
    private static ConfigurationNode conf = fileObject.getConf();

    private static int startCoins = CoinsAPI.getInstance().getFileManager().getConfig().getConf().node("startCoins").getInt();

    private int coins;
    private CorePlayer corePlayer;
    private boolean exists;
    private Statement statement;
    private ResultSet resultSet;

    public CoinsPlayerProvider(CorePlayer corePlayer) {
        this.corePlayer = corePlayer;
        load();
    }

    private static void create(int id, int coins) {
        try {
            if (CoreAPI.getInstance().isUsingSQL()) {
                CoreAPI.getInstance().getDatabaseManager().getStatement().executeUpdate("INSERT INTO minecode_coins (ID, COINS) VALUES (" + id + ", " + coins + ")");
            } else {
                conf.node(String.valueOf(id), "coins").set(coins);
            }
        } catch (SQLException | SerializationException throwables) {
            throwables.printStackTrace();
        }
    }

    public void load() {
        try {
            if (CoreAPI.getInstance().isUsingSQL()) {
                statement = CoreAPI.getInstance().getDatabaseManager().getStatement();
                resultSet = statement.executeQuery("SELECT * FROM minecode_coins WHERE ID = " + corePlayer.getID() + "");
                exists = resultSet.next();
            } else
                exists = !conf.node(String.valueOf(corePlayer.getID())).empty();

            if (!exists) {
                create(corePlayer.getID(), startCoins);
            }

            reload();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public boolean reload() {
        try {
            if (CoreAPI.getInstance().isUsingSQL()) {
                resultSet = statement.executeQuery("SELECT * FROM minecode_coins WHERE ID = '" + corePlayer.getID() + "'");
                if (resultSet.next()) {
                    coins = resultSet.getInt("COINS");
                    return true;
                } else load();
            } else {
                coins = conf.node(String.valueOf(corePlayer.getID()), "coins").getInt();
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean save() {
        try {
            corePlayer.reload();

            if (CoreAPI.getInstance().isUsingSQL()) {
                resultSet.updateDouble("COINS", coins);
                resultSet.updateRow();
                return true;
            } else {
                conf.node(String.valueOf(corePlayer.getID()), "coins").set(coins);
                fileObject.save();
                return true;
            }
        } catch (SQLException | SerializationException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public CorePlayer getCorePlayer() {
        return corePlayer;
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override
    public boolean setCoins(int coins) {
        if (CoinsSpigot.getInstance().isVaultEnabled()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(getCorePlayer().getUuid());
            double vaultBalance = CoinsSpigot.getInstance().getVaultManager().getEcon().getBalance(offlinePlayer);
            double diff = coins - vaultBalance;
            if (diff < 0) {
                return CoinsSpigot.getInstance().getVaultManager().getEcon().withdrawPlayer(offlinePlayer, diff * (-1)).type == EconomyResponse.ResponseType.SUCCESS;
            } else
                return CoinsSpigot.getInstance().getVaultManager().getEcon().depositPlayer(offlinePlayer, diff).type == EconomyResponse.ResponseType.SUCCESS;
        }

        CoinsAPI.getInstance().getEventManager().callEvent(new CoinsUpdateEvent(this, coins));
        return setCoinsWithoutVault(coins);
    }

    public boolean setCoinsWithoutVault(int coins) {
        if (coins < 0) return false;

        this.coins = coins;
        return true;
    }

    @Override
    public boolean addCoins(int coins) {
        if (CoinsSpigot.getInstance().isVaultEnabled())
            return CoinsSpigot.getInstance().getVaultManager().getEcon().depositPlayer(Bukkit.getOfflinePlayer(getCorePlayer().getUuid()), coins).type == EconomyResponse.ResponseType.SUCCESS;

        return setCoinsWithoutVault(this.coins + coins);
    }

    public boolean addCoinsWithoutVault(int coins) {
        return setCoinsWithoutVault(this.coins + coins);
    }

    @Override
    public boolean removeCoins(int coins) {
        if (CoinsSpigot.getInstance().isVaultEnabled())
            return CoinsSpigot.getInstance().getVaultManager().getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(getCorePlayer().getUuid()), coins).type == EconomyResponse.ResponseType.SUCCESS;

        return setCoinsWithoutVault(this.coins - coins);
    }

    public boolean removeCoinsWithoutVault(int coins) {
        return setCoinsWithoutVault(this.coins + coins);
    }
}
