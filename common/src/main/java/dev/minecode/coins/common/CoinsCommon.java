package dev.minecode.coins.common;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.common.api.CoinsAPIProvider;
import dev.minecode.coins.common.api.manager.PlayerManagerProvider;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.manager.UpdateManager;
import dev.minecode.core.api.object.CorePlugin;

import java.sql.SQLException;

public class CoinsCommon {

    private static CoinsCommon instance;

    private PlayerManagerProvider playerManagerProvider;

    public CoinsCommon() {
        makeInstances();

        CorePlugin corePlugin = CoinsAPI.getInstance().getThisCorePlugin();
        UpdateManager updateManager = CoreAPI.getInstance().getUpdateManager(corePlugin);

        if (updateManager.updateAvailable())
            System.out.println("[" + corePlugin.getName() + "] There is a newer Version available! You can download it at " + updateManager.getReleaseURL(updateManager.getMatchingRelease()));

        if (CoreAPI.getInstance().isUsingSQL()) {
            try {
                CoreAPI.getInstance().getDatabaseManager().getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS minecode_coins (UUID VARCHAR (37), COINS INT , PRIMARY KEY (UUID))");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static CoinsCommon getInstance() {
        if (instance == null) new CoinsCommon();
        return instance;
    }

    private void makeInstances() {
        instance = this;
        new CoinsAPIProvider();
    }

    public PlayerManagerProvider getPlayerManagerProvider() {
        return playerManagerProvider;
    }

    public void setPlayerManagerProvider(PlayerManagerProvider playerManagerProvider) {
        this.playerManagerProvider = playerManagerProvider;
    }
}
