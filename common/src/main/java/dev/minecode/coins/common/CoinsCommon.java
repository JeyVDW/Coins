package dev.minecode.coins.common;

import dev.minecode.coins.common.api.CoinsAPIProvider;
import dev.minecode.coins.common.object.CoinsPlayerAddon;
import dev.minecode.core.api.CoreAPI;

import java.sql.SQLException;

public class CoinsCommon {

    private static CoinsCommon instance;

    private CoinsAPIProvider coinsAPIProvider;
    private CoinsPlayerAddon coinsPlayerAddon;

    public CoinsCommon() {
        makeInstances();
    }

    public static CoinsCommon getInstance() {
        return instance;
    }

    private void makeInstances() {
        instance = this;
        coinsAPIProvider = new CoinsAPIProvider();

        if (CoreAPI.getInstance().isUsingSQL()) {
            try {
                CoreAPI.getInstance().getDatabaseManager().getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS minecode_coins (ID INT, COINS INT , PRIMARY KEY (ID))");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public CoinsPlayerAddon getCoinsPlayerAddon() {
        return coinsPlayerAddon;
    }

    public void setCoinsPlayerAddon(CoinsPlayerAddon coinsPlayerAddon) {
        this.coinsPlayerAddon = coinsPlayerAddon;
    }
}
