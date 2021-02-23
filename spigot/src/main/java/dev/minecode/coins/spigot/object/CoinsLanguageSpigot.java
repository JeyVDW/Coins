package dev.minecode.coins.spigot.object;

import dev.minecode.core.api.object.LanguageAbstract;

public enum CoinsLanguageSpigot implements LanguageAbstract {

    noPermission("noPermission"),
    noPlayers("noPlayers"),
    playerNotOnline("playerNotOnline"),
    playernotExists("playerNotExists"),
    noValidNumber("noValidNumber"),
    syntax("syntax"),

    coinsCommandSyntaxUse("coins", "command", "syntax", "use"),
    coinsCommandSyntaxSee("coins", "command", "syntax", "see"),
    coinsCommandSyntaxModify("coins", "command", "syntax", "modify"),
    coinsCommandYourcoins("coins", "command", "yourcoins"),
    coinsCommandSee("coins", "command", "see"),
    coinsCommandAddSuccess("coins", "command", "add", "success"),
    coinsCommandAddFailed("coins", "command", "add", "failed"),
    coinsCommandRemoveSuccess("coins", "command", "remove", "success"),
    coinsCommandRemoveFailed("coins", "command", "remove", "failed"),
    coinsCommandSetSuccess("coins", "command", "set", "success"),
    coinsCommandSetFailed("coins", "command", "set", "failed");

    private String[] path;

    CoinsLanguageSpigot(String... path) {
        this.path = path;
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
