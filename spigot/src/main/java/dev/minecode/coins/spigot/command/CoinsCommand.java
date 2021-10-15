package dev.minecode.coins.spigot.command;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.coins.spigot.CoinsSpigot;
import dev.minecode.coins.spigot.object.CoinsLanguageSpigot;
import dev.minecode.core.api.CoreAPI;
import dev.minecode.core.api.object.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CoinsCommand implements CommandExecutor, TabCompleter {

    private final CorePlugin corePlugin = CoinsAPI.getInstance().getThisCorePlugin();

    public CoinsCommand() {
        PluginCommand command = CoinsSpigot.getInstance().getCommand("coins");
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        CoinsPlayer coinsExecuter = CoinsAPI.getInstance().getPlayerManager().getPlayer(commandSender.getName());

        if (!commandSender.hasPermission("coins.use")) {
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandNoPermission)
                    .args(command.getName(), args, "arg").chatcolorAll().getMessage());
            return true;
        }

        if (args.length == 0) {
            commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandYourcoins)
                    .coinsPlayer(coinsExecuter, "executer").getCoreReplaceManager()
                    .args(command.getName(), args, "arg").chatcolorAll().getMessage());
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                syntaxMessage(commandSender, coinsExecuter);
                return true;
            }

            if (!commandSender.hasPermission("coins.see")) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandNoPermission)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            CoinsPlayer coinsTarget = CoinsAPI.getInstance().getPlayerManager().getPlayer(args[0]);

            if (coinsTarget == null) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandPlayerNotExists)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSee)
                    .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                    .args(command.getName(), args, "arg").chatcolorAll().getMessage());
            return true;
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("pay")) {
                if (!commandSender.hasPermission("coins.pay")) {
                    commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandNoPermission)
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                CoinsPlayer coinsTarget = CoinsAPI.getInstance().getPlayerManager().getPlayer(args[0]);

                if (coinsTarget == null) {
                    commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandPlayerNotExists)
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                int amount = 0;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandNoValidNumber)
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                if (amount <= 0) {
                    commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandNoValidNumber)
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                if (coinsExecuter.getCoins() < amount) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandPayNotEnoughtCoins)
                            .coinsPlayer(coinsExecuter, "executer")
                            .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                if (!coinsExecuter.removeCoins(amount) || !coinsTarget.addCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandPayFailed)
                            .coinsPlayer(coinsTarget, "target")
                            .coinsPlayer(coinsExecuter, "executer").getCoreReplaceManager()
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                if (CoinsSpigot.getInstance().getVaultManager().isVaultEnabled()) {
                    coinsExecuter.reload();
                    coinsTarget.reload();
                } else {
                    coinsExecuter.save();
                    coinsTarget.save();
                }

                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSuccessfulPaid)
                        .coinsPlayer(coinsTarget, "target")
                        .coinsPlayer(coinsExecuter, "executer").getCoreReplaceManager()
                        .replaceAll("%amount%", String.valueOf(amount))
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());

                Player target;
                if ((target = Bukkit.getPlayer(coinsTarget.getCorePlayer().getUuid())) != null) {
                    target.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSuccessfulReceived)
                            .coinsPlayer(coinsTarget, "target")
                            .coinsPlayer(coinsExecuter, "executer").getCoreReplaceManager()
                            .replaceAll("%amount%", String.valueOf(amount))
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                }
                return true;
            }

            if (!commandSender.hasPermission("coins.modify")) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandNoPermission)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            CoinsPlayer coinsTarget = CoinsAPI.getInstance().getPlayerManager().getPlayer(args[0]);

            if (coinsTarget == null) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandPlayerNotExists)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            int amount = 0;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(
                        CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandNoValidNumber).chatcolorAll().getMessage());
                return true;
            }

            if (args[1].equalsIgnoreCase("add")) {
                int oldCoins = coinsTarget.getCoins();

                if (!coinsTarget.addCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandAddFailed)
                            .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                if (CoinsSpigot.getInstance().getVaultManager().isVaultEnabled())
                    coinsTarget.reload();
                else
                    coinsTarget.save();

                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandAddSuccess)
                        .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                        .replaceAll("%oldCoins%", String.valueOf(oldCoins))
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            if (args[1].equalsIgnoreCase("remove")) {
                int oldCoins = coinsTarget.getCoins();

                if (!coinsTarget.removeCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandRemoveFailed)
                            .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                if (CoinsSpigot.getInstance().getVaultManager().isVaultEnabled())
                    coinsTarget.reload();
                else
                    coinsTarget.save();

                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandRemoveSuccess)
                        .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                        .replaceAll("%oldCoins%", String.valueOf(oldCoins))
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            if (args[1].equalsIgnoreCase("set")) {
                int oldCoins = coinsTarget.getCoins();

                if (!coinsTarget.setCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSetFailed)
                            .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                if (CoinsSpigot.getInstance().getVaultManager().isVaultEnabled())
                    coinsTarget.reload();
                else
                    coinsTarget.save();
                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSetSuccess)
                        .coinsPlayer(coinsTarget, "target").getCoreReplaceManager()
                        .replaceAll("%oldCoins%", String.valueOf(oldCoins))
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }
        }

        syntaxMessage(commandSender, coinsExecuter);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        ArrayList<String> tab = new ArrayList<>();
        List<String> list = new ArrayList<>();
        String search = null;

        if (!commandSender.hasPermission("coins.use")) return tab;

        if (args.length == 1) {
            list.add("help");
            if (commandSender.hasPermission("coins.see") || commandSender.hasPermission("coins.modify"))
                for (Player player : Bukkit.getOnlinePlayers())
                    list.add(player.getName());
            search = args[0];
        }

        if (args.length == 2) {
            if (!args[0].equalsIgnoreCase("help")) {
                if (commandSender.hasPermission("coins.pay"))
                    list.add("pay");
                if (commandSender.hasPermission("coins.modify")) {
                    list.add("add");
                    list.add("remove");
                    list.add("set");
                }
            }
            search = args[1];
        }

        for (String start : list)
            if (start.toLowerCase().startsWith(search.toLowerCase()))
                tab.add(start);

        return tab;
    }

    private void syntaxMessage(CommandSender commandSender, CoinsPlayer coinsPlayer) {
        if (!commandSender.hasPermission("coins.use") && !commandSender.hasPermission("coins.see") && !commandSender.hasPermission("coins.modify")) {
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandNoPermission).chatcolorAll().getMessage());
            return;
        }
        commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSyntaxInfo).chatcolorAll().getMessage());
        if (commandSender.hasPermission("coins.use"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSyntaxUse).chatcolorAll().getMessage());
        if (commandSender.hasPermission("coins.see"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSyntaxSee).chatcolorAll().getMessage());
        if (commandSender.hasPermission("coins.pay"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSyntaxPay).chatcolorAll().getMessage());
        if (commandSender.hasPermission("coins.modify"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(corePlugin), CoinsLanguageSpigot.coinsCommandSyntaxModify).chatcolorAll().getMessage());
    }
}