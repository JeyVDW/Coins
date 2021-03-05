package dev.minecode.coins.bungeecord.api.object;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.api.event.CoinsUpdateEvent;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlayer;
import dev.minecode.core.api.object.FileObject;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class CoinsPlayerProvider implements CoinsPlayer {

    private static HashMap<CorePlayer, CoinsPlayer> coinsPlayers = new HashMap<>();

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
            if (CoreAPI.getInstance().getPluginManager().isUsingSQL()) {
                CoreAPI.getInstance().getDatabaseManager().getStatement().executeUpdate("INSERT INTO minecode_coins (ID, COINS) VALUES (" + id + ", " + coins + ")");
            } else {
                conf.node(String.valueOf(id), "coins").set(coins);
            }
        } catch (SQLException | SerializationException throwables) {
            throwables.printStackTrace();
        }
    }

    public static HashMap<CorePlayer, CoinsPlayer> getCoinsPlayers() {
        return coinsPlayers;
    }

    public void load() {
        try {
            if (CoreAPI.getInstance().getPluginManager().isUsingSQL()) {
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
            if (CoreAPI.getInstance().getPluginManager().isUsingSQL()) {
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

            if (CoreAPI.getInstance().getPluginManager().isUsingSQL()) {
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
        if (coins < 0) return false;

        CoinsAPI.getInstance().getEventManager().callEvent(new CoinsUpdateEvent(this, coins));
        this.coins = coins;
        return true;
    }

    @Override
    public boolean addCoins(int coins) {
        return setCoins(this.coins + coins);
    }

    @Override
    public boolean removeCoins(int coins) {
        return setCoins(this.coins - coins);
    }
}