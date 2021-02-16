package dev.minecode.coins.common;

import dev.minecode.coins.common.api.CoinsAPIProvider;
import dev.minecode.coins.common.object.CoinsPlayerAddon;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.manager.UpdateManager;

import java.sql.SQLException;

public class CoinsCommon {

    private static CoinsCommon instance;

    private CoinsAPIProvider coinsAPIProvider;
    private CoinsPlayerAddon coinsPlayerAddon;

    public CoinsCommon() {
        makeInstances();

        if (CoreAPI.getInstance().isUsingSQL()) {
            try {
                CoreAPI.getInstance().getDatabaseManager().getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS minecode_coins (ID INT, COINS INT , PRIMARY KEY (ID))");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        UpdateManager updateManager = CoreAPI.getInstance().getUpdateManager();
        if (updateManager.updateAvailable()) {
            System.out.println("[" + CoreAPI.getInstance().getPluginName() + "] There is a newer Version available! You can download it at " + updateManager.getReleaseURL(updateManager.getMatchingRelease()));
        }
    }

    public static CoinsCommon getInstance() {
        return instance;
    }

    private void makeInstances() {
        instance = this;
        coinsAPIProvider = new CoinsAPIProvider();
    }

    public CoinsPlayerAddon getCoinsPlayerAddon() {
        return coinsPlayerAddon;
    }

    public void setCoinsPlayerAddon(CoinsPlayerAddon coinsPlayerAddon) {
        this.coinsPlayerAddon = coinsPlayerAddon;
    }
}
