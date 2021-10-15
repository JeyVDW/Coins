package dev.minecode.coins.bungeecord.object;

import dev.minecode.core.api.object.LanguageAbstract;

public enum CoinsLanguageBungeeCord implements LanguageAbstract {

    coinsCommandNoPermission("coins", "command", "noPermission"),
    coinsCommandSyntaxInfo("coins", "command", "syntax", "info"),
    coinsCommandSyntaxUse("coins", "command", "syntax", "use"),
    coinsCommandSyntaxSee("coins", "command", "syntax", "see"),
    coinsCommandSyntaxPay("coins", "command", "syntax", "pay"),
    coinsCommandSyntaxModify("coins", "command", "syntax", "modify"),
    coinsCommandPlayerNotExists("coins", "command", "playerNotExists"),
    coinsCommandNoValidNumber("coins", "command", "noValidNumber"),
    coinsCommandYourcoins("coins", "command", "yourcoins"),
    coinsCommandSee("coins", "command", "see"),
    coinsCommandAddSuccess("coins", "command", "add", "success"),
    coinsCommandAddFailed("coins", "command", "add", "failed"),
    coinsCommandRemoveSuccess("coins", "command", "remove", "success"),
    coinsCommandRemoveFailed("coins", "command", "remove", "failed"),
    coinsCommandSetSuccess("coins", "command", "set", "success"),
    coinsCommandSetFailed("coins", "command", "set", "failed"),
    coinsCommandPayNotEnoughtCoins("coins", "command", "pay", "notEnoughtCoins"),
    coinsCommandPayFailed("coins", "command", "pay", "failed"),
    coinsCommandSuccessfulPaid("coins", "command", "pay", "successfulPaid"),
    coinsCommandSuccessfulReceived("coins", "command", "pay", "successfullyReceived");

    private final String[] path;

    CoinsLanguageBungeeCord(String... path) {
        this.path = path;
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
