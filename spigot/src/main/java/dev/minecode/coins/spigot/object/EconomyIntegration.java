package dev.minecode.coins.spigot.object;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.spigot.CoinsSpigot;
import dev.minecode.coins.spigot.api.object.CoinsPlayerProvider;
import dev.minecode.core.api.CoreAPI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class EconomyIntegration implements Economy {
    @Override
    public boolean isEnabled() {
        return CoinsSpigot.getInstance().getVaultManager().isVaultEnabled();
    }

    @Override
    public String getName() {
        return "Coins";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        int round = Integer.parseInt(String.valueOf(Math.round(amount)));
        if (amount == 1.00 || amount == 0.01)
            return String.format("%d %s", round, currencyNameSingular());
        return String.format("%d %s", round, currencyNamePlural());
    }

    @Override
    public String currencyNamePlural() {
        return "Coins";
    }

    @Override
    public String currencyNameSingular() {
        return "Coin";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return CoinsAPI.getInstance().getPlayerManager().getPlayer(playerName) != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return CoinsAPI.getInstance().getPlayerManager().getPlayer(offlinePlayer.getUniqueId()) != null;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String playerName) {
        if (!hasAccount(playerName)) return 0.00;
        return CoinsAPI.getInstance().getPlayerManager().getPlayer(playerName).getCoins();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        if (!hasAccount(offlinePlayer)) return 0.00;
        return CoinsAPI.getInstance().getPlayerManager().getPlayer(offlinePlayer.getUniqueId()).getCoins();
    }

    @Override
    public double getBalance(String playerName, String worldName) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return CoinsAPI.getInstance().getPlayerManager().getPlayer(playerName).getCoins() <= amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return CoinsAPI.getInstance().getPlayerManager().getPlayer(offlinePlayer.getUniqueId()).getCoins() <= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return has(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        double balance = getBalance(playerName);
        double finalBalance = balance - amount;

        if (amount < 0)
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
        if (finalBalance < 0)
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");

        CoinsPlayerProvider corePlayer = (CoinsPlayerProvider) CoinsAPI.getInstance().getPlayerManager().getPlayer(playerName);
        corePlayer.removeCoinsWithoutVault(Integer.parseInt(String.valueOf(Math.round(amount))));
        corePlayer.save();
        return new EconomyResponse(amount, finalBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        double balance = getBalance(offlinePlayer);
        double finalBalance = balance - amount;

        if (amount < 0)
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
        if (finalBalance < 0)
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");

        CoinsPlayerProvider corePlayer = (CoinsPlayerProvider) CoinsAPI.getInstance().getPlayerManager().getPlayer(offlinePlayer.getUniqueId());
        corePlayer.removeCoinsWithoutVault(Integer.parseInt(String.valueOf(Math.round(amount))));
        corePlayer.save();
        return new EconomyResponse(amount, finalBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        double balance = getBalance(playerName);
        double finalBalance = balance + amount;

        if (amount < 0)
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        if (finalBalance < 0)
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");

        CoinsPlayerProvider corePlayer = (CoinsPlayerProvider) CoinsAPI.getInstance().getPlayerManager().getPlayer(playerName);
        corePlayer.addCoinsWithoutVault(Integer.parseInt(String.valueOf(Math.round(amount))));
        corePlayer.save();
        return new EconomyResponse(amount, finalBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        double balance = getBalance(offlinePlayer);
        double finalBalance = balance + amount;

        if (amount < 0)
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        if (finalBalance < 0)
            return new EconomyResponse(0, balance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");

        CoinsPlayerProvider corePlayer = (CoinsPlayerProvider) CoinsAPI.getInstance().getPlayerManager().getPlayer(offlinePlayer.getUniqueId());
        corePlayer.addCoinsWithoutVault(Integer.parseInt(String.valueOf(Math.round(amount))));
        corePlayer.save();
        return new EconomyResponse(amount, finalBalance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse createBank(String bankName, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse createBank(String bankName, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse deleteBank(String bankName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse bankBalance(String bankName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse bankHas(String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse bankWithdraw(String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse bankDeposit(String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse isBankMember(String bankName, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public EconomyResponse isBankMember(String bankName, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banks are not supported");
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return CoreAPI.getInstance().getPlayerManager().getPlayer(playerName) != null;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return CoreAPI.getInstance().getPlayerManager().getPlayer(offlinePlayer.getUniqueId()) != null;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        return createPlayerAccount(offlinePlayer);
    }
}
