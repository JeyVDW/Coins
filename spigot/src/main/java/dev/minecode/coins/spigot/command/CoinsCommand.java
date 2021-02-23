package dev.minecode.coins.spigot.command;

import dev.minecode.coins.api.CoinsAPI;
import dev.minecode.coins.api.object.CoinsPlayer;
import dev.minecode.coins.spigot.CoinsSpigot;
import dev.minecode.coins.spigot.object.CoinsLanguageSpigot;
import dev.minecode.core.api.CoreAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CoinsCommand implements CommandExecutor, TabCompleter {
    public CoinsCommand() {
        PluginCommand command = CoinsSpigot.getInstance().getCommand("coins");
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        CoinsPlayer coinsExecuter = CoinsAPI.getInstance().getCoinsPlayer(commandSender.getName());

        if (!commandSender.hasPermission("coins.use")) {
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.noPermission)
                    .args(command.getName(), args, "arg").chatcolorAll().getMessage());
            return true;
        }

        if (args.length == 0) {
            commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandYourcoins)
                    .coinsPlayer(coinsExecuter, "executer")
                    .args(command.getName(), args, "arg").chatcolorAll().getMessage());
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                syntaxMessage(commandSender, coinsExecuter);
                return true;
            }

            if (!commandSender.hasPermission("coins.see")) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.noPermission)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            CoinsPlayer coinsTarget = CoinsAPI.getInstance().getCoinsPlayer(args[0]);

            if (coinsTarget == null) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.playernotExists)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandSee)
                    .coinsPlayer(coinsTarget, "target")
                    .args(command.getName(), args, "arg").chatcolorAll().getMessage());
            return true;
        }

        if (args.length == 3) {
            if (!commandSender.hasPermission("coins.modify")) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.noPermission)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            CoinsPlayer coinsTarget = CoinsAPI.getInstance().getCoinsPlayer(args[0]);

            if (coinsTarget == null) {
                commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.playernotExists)
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            int amount = 0;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(
                        CoreAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.noValidNumber).chatcolorAll().getMessage());
                return true;
            }

            if (args[1].equalsIgnoreCase("add")) {
                int oldCoins = coinsTarget.getCoins();

                if (!coinsTarget.addCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandAddFailed)
                            .coinsPlayer(coinsTarget, "target")
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }
                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandAddSuccess)
                        .coinsPlayer(coinsTarget, "target")
                        .replaceAll("%oldCoins%", String.valueOf(oldCoins))
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                coinsTarget.save();
                return true;
            }

            if (args[1].equalsIgnoreCase("remove")) {
                int oldCoins = coinsTarget.getCoins();

                if (!coinsTarget.removeCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandRemoveFailed)
                            .coinsPlayer(coinsTarget, "target")
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                coinsTarget.save();
                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandRemoveSuccess)
                        .coinsPlayer(coinsTarget, "target")
                        .replaceAll("%oldCoins%", String.valueOf(oldCoins))
                        .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                return true;
            }

            if (args[1].equalsIgnoreCase("set")) {
                int oldCoins = coinsTarget.getCoins();

                if (!coinsTarget.setCoins(amount)) {
                    commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandSetFailed)
                            .coinsPlayer(coinsTarget, "target")
                            .args(command.getName(), args, "arg").chatcolorAll().getMessage());
                    return true;
                }

                coinsTarget.save();
                commandSender.sendMessage(CoinsAPI.getInstance().getReplaceManager(coinsExecuter.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandSetSuccess)
                        .coinsPlayer(coinsTarget, "target")
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

        if (!commandSender.hasPermission("coins.use")) {
            return tab;
        }

        if (args.length == 1) {
            list.add("help");
            for (Player player : Bukkit.getOnlinePlayers())
                list.add(player.getName());
            search = args[0].toLowerCase();
        }

        if (args.length == 2) {
            if (commandSender.hasPermission("coins.see"))
                list.add("see");
            if (commandSender.hasPermission("coins.modify")) {
                list.add("add");
                list.add("remove");
                list.add("set");
            }
            search = args[1].toLowerCase();
        }

        for (String start : list) {
            if (start.toLowerCase().startsWith(search))
                tab.add(start);
        }

        return tab;
    }

    private void syntaxMessage(CommandSender commandSender, CoinsPlayer coinsPlayer) {
        if (!commandSender.hasPermission("coins.use") && !commandSender.hasPermission("coins.see") && !commandSender.hasPermission("coins.modify")) {
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(), CoinsLanguageSpigot.noPermission).chatcolorAll().getMessage());
            return;
        }
        commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(), CoinsLanguageSpigot.syntax).chatcolorAll().getMessage());
        if (commandSender.hasPermission("coins.use"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandSyntaxUse).chatcolorAll().getMessage());
        if (commandSender.hasPermission("coins.see"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandSyntaxSee).chatcolorAll().getMessage());
        if (commandSender.hasPermission("coins.modify"))
            commandSender.sendMessage(CoreAPI.getInstance().getReplaceManager(coinsPlayer.getCorePlayer().getLanguage(), CoinsLanguageSpigot.coinsCommandSyntaxModify).chatcolorAll().getMessage());
    }
}