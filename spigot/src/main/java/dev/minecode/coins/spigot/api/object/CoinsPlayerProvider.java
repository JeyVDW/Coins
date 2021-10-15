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
import java.util.UUID;

public class CoinsPlayerProvider implements CoinsPlayer {

    private static final FileObject playersFileObject = CoinsAPI.getInstance().getFileManager().getPlayers();
    private static final int startCoins = CoinsAPI.getInstance().getFileManager().getConfig().getConf().node("startCoins").getInt();
    private static ConfigurationNode playersConf;

    private int coins;
    private final CorePlayer corePlayer;
    private boolean exists;
    private Statement statement;
    private ResultSet resultSet;

    public CoinsPlayerProvider(CorePlayer corePlayer) {
        this.corePlayer = corePlayer;
        makeInstances();
        load();
    }

    private static void create(UUID uuid, int coins) {
        try {
            if (CoreAPI.getInstance().isUsingSQL()) {
                CoreAPI.getInstance().getDatabaseManager().getStatement().executeUpdate("INSERT INTO minecode_coins (UUID, COINS) VALUES ('" + uuid + "', " + coins + ")");
            } else {
                playersConf.node(String.valueOf(uuid), "coins").set(coins);
            }
        } catch (SQLException | SerializationException throwables) {
            throwables.printStackTrace();
        }
    }

    public void makeInstances() {
        if (CoreAPI.getInstance().isUsingSQL())
            statement = CoreAPI.getInstance().getDatabaseManager().getStatement();
        else
            playersConf = playersFileObject.getConf();
    }

    public void load() {
        try {
            if (CoreAPI.getInstance().isUsingSQL()) {
                statement = CoreAPI.getInstance().getDatabaseManager().getStatement();
                resultSet = statement.executeQuery("SELECT * FROM minecode_coins WHERE UUID = '" + corePlayer.getUuid() + "'");
                exists = resultSet.next();
            } else
                exists = !playersConf.node(String.valueOf(corePlayer.getUuid())).empty();

            if (!exists) {
                create(corePlayer.getUuid(), startCoins);
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
                resultSet = statement.executeQuery("SELECT * FROM minecode_coins WHERE UUID = '" + corePlayer.getUuid() + "'");
                if (resultSet.next()) {
                    coins = resultSet.getInt("COINS");
                    return true;
                } else load();
            } else {
                coins = playersConf.node(String.valueOf(corePlayer.getUuid()), "coins").getInt();
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
                playersConf.node(String.valueOf(corePlayer.getUuid()), "coins").set(coins);
                playersFileObject.save();
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
        if (CoinsSpigot.getInstance().getVaultManager().isVaultEnabled()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(getCorePlayer().getUuid());
            double vaultBalance = CoinsSpigot.getInstance().getVaultManager().getEconomy().getBalance(offlinePlayer);
            double diff = coins - vaultBalance;
            if (diff < 0) {
                return CoinsSpigot.getInstance().getVaultManager().getEconomy().withdrawPlayer(offlinePlayer, diff * (-1)).type == EconomyResponse.ResponseType.SUCCESS;
            } else
                return CoinsSpigot.getInstance().getVaultManager().getEconomy().depositPlayer(offlinePlayer, diff).type == EconomyResponse.ResponseType.SUCCESS;
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
        if (CoinsSpigot.getInstance().getVaultManager().isVaultEnabled())
            return CoinsSpigot.getInstance().getVaultManager().getEconomy().depositPlayer(Bukkit.getOfflinePlayer(getCorePlayer().getUuid()), coins).type == EconomyResponse.ResponseType.SUCCESS;

        return setCoinsWithoutVault(this.coins + coins);
    }

    public boolean addCoinsWithoutVault(int coins) {
        return setCoinsWithoutVault(this.coins + coins);
    }

    @Override
    public boolean removeCoins(int coins) {
        if (CoinsSpigot.getInstance().getVaultManager().isVaultEnabled())
            return CoinsSpigot.getInstance().getVaultManager().getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(getCorePlayer().getUuid()), coins).type == EconomyResponse.ResponseType.SUCCESS;

        return setCoinsWithoutVault(this.coins - coins);
    }

    public boolean removeCoinsWithoutVault(int coins) {
        return setCoinsWithoutVault(this.coins - coins);
    }
}
