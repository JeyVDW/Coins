package dev.minecode.coins.bungeecord.command;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.coins.bungeecord.object.CoinsLanguageBungeeCord;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoinsCommand extends Command implements TabExecutor {

    private final CorePlugin corePlugin = CoinsAPI.getInstance().getThisCorePlugin();

    public CoinsCommand(String name) {
        super(name);
    }

    public CoinsCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        CoinsPlayer coinsExecuter = CoinsAPI.getInstance().getPlayerManager().getPlayer(commandSender.getName());

        if (!commandSender.hasPermission("coins.use")) {
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandNoPermission)
                    .args("coins", args, "arg").chatcolorAll().getBaseMessage());
            return;
        }

        if (args.length == 0) {
            commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandYourcoins)
                    .coinsPlayer(coinsExecuter, "executer").getCoreReplaceManager()
                    .args("coins", args, "arg").chatcolorAll().getBaseMessage());
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                syntaxMessage(commandSender, coinsExecuter);
                return;
            }

            if (!commandSender.hasPermission("coins.see")) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandNoPermission)
                        .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }

            CoinsPlayer coinsTarget = CoinsAPI.getInstance().getPlayerManager().getPlayer(args[0]);

            if (coinsTarget == null) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandPlayerNotExists)
                        .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }

            commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSee)
                    .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                    .args("coins", args, "arg").chatcolorAll().getBaseMessage());
            return;
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("pay")) {
                if (!commandSender.hasPermission("coins.pay")) {
                    commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandNoPermission)
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                    return;
                }

                CoinsPlayer coinsTarget = CoinsAPI.getInstance().getPlayerManager().getPlayer(args[0]);

                if (coinsTarget == null) {
                    commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandPlayerNotExists)
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                    return;
                }

                int amount = 0;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandNoValidNumber)
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                    return;
                }

                if (amount <= 0) {
                    commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandNoValidNumber)
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                    return;
                }

                if (coinsExecuter.getCoins() < amount) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandPayNotEnoughtCoins)
                            .coinsPlayer(coinsExecuter, "executer")
                            .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                    return;
                }

                if (!coinsExecuter.removeCoins(amount) || !coinsTarget.addCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandPayFailed)
                            .coinsPlayer(coinsTarget, "target")
                            .coinsPlayer(coinsExecuter, "executer").getCoreReplaceManager()
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                    return;
                }

                coinsExecuter.save();
                coinsTarget.save();

                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSuccessfulPaid)
                        .coinsPlayer(coinsTarget, "target")
                        .coinsPlayer(coinsExecuter, "executer").getCoreReplaceManager()
                        .replaceAll("%amount%", String.valueOf(amount))
                        .args("coins", args, "arg").chatcolorAll().getBaseMessage());

                ProxiedPlayer target;
                if ((target = ProxyServer.getInstance().getPlayer(coinsTarget.getCorePlayer().getUuid())) != null) {
                    target.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSuccessfulReceived)
                            .coinsPlayer(coinsTarget, "target")
                            .coinsPlayer(coinsExecuter, "executer").getCoreReplaceManager()
                            .replaceAll("%amount%", String.valueOf(amount))
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                }
                return;
            }

            if (!commandSender.hasPermission("coins.modify")) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandNoPermission)
                        .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }

            CoinsPlayer coinsTarget = CoinsAPI.getInstance().getPlayerManager().getPlayer(args[0]);

            if (coinsTarget == null) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandPlayerNotExists)
                        .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }

            int amount = 0;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(
                        CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandNoValidNumber).chatcolorAll().getBaseMessage());
                return;
            }

            if (args[1].equalsIgnoreCase("add")) {
                int oldCoins = coinsTarget.getCoins();

                if (!coinsTarget.addCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandAddFailed)
                            .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                    return;
                }

                coinsTarget.save();
                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandAddSuccess)
                        .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                        .replaceAll("%oldCoins%", String.valueOf(oldCoins))
                        .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }

            if (args[1].equalsIgnoreCase("remove")) {
                int oldCoins = coinsTarget.getCoins();

                if (!coinsTarget.removeCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandRemoveFailed)
                            .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                    return;
                }

                coinsTarget.save();
                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandRemoveSuccess)
                        .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                        .replaceAll("%oldCoins%", String.valueOf(oldCoins))
                        .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }

            if (args[1].equalsIgnoreCase("set")) {
                int oldCoins = coinsTarget.getCoins();

                if (!coinsTarget.setCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSetFailed)
                            .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                            .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                    return;
                }

                coinsTarget.save();
                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSetSuccess)
                        .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                        .replaceAll("%oldCoins%", String.valueOf(oldCoins))
                        .args("coins", args, "arg").chatcolorAll().getBaseMessage());
                return;
            }
        }

        syntaxMessage(commandSender, coinsExecuter);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        Set<String> tab = new HashSet();
        List<String> list = new ArrayList<>();
        String search = null;

        if (!commandSender.hasPermission("coins.use")) return tab;

        if (args.length == 1) {
            list.add("help");
            if (commandSender.hasPermission("coins.see") || commandSender.hasPermission("coins.modify"))
                for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers())
                    list.add(proxiedPlayer.getName());
            search = args[0].toLowerCase();
        }

        if (args.length == 2) {
            if (!args[0].equalsIgnoreCase("help"))
                if (commandSender.hasPermission("coins.pay"))
                    list.add("pay");
            if (commandSender.hasPermission("coins.modify")) {
                list.add("add");
                list.add("remove");
                list.add("set");
            }
            search = args[1].toLowerCase();
        }

        for (String start : list)
            if (start.toLowerCase().startsWith(search))
                tab.add(start);

        return tab;
    }

    private void syntaxMessage(CommandSender commandSender, CoinsPlayer coinsPlayer) {
        if (!commandSender.hasPermission("coins.use") && !commandSender.hasPermission("coins.see") && !commandSender.hasPermission("coins.modify")) {
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandNoPermission).chatcolorAll().getBaseMessage());
            return;
        }
        commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSyntaxInfo).chatcolorAll().getBaseMessage());
        if (commandSender.hasPermission("coins.use"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSyntaxUse).chatcolorAll().getBaseMessage());
        if (commandSender.hasPermission("coins.see"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSyntaxSee).chatcolorAll().getBaseMessage());
        if (commandSender.hasPermission("coins.pay"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSyntaxPay).chatcolorAll().getBaseMessage());
        if (commandSender.hasPermission("coins.modify"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageBungeeCord.coinsCommandSyntaxModify).chatcolorAll().getBaseMessage());
    }
}