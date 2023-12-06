package com.veeonthewall.tabby.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.veeonthewall.tabby.Tabby;
import com.veeonthewall.tabby.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class TabbyCommand implements CommandExecutor {
    private Tabby plugin;

    // Constructor
    public TabbyCommand(Tabby plugin) {
        this.plugin = plugin;
    }

    @Override
    // The most basic command handler ever, if you wanted to you could split this into multiple classes but since it's only two commands I didn't bother
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission("tabby.help")) {
                sender.sendMessage(MessageUtil.createColoredMessage("&6&lTabby Help"));
                sender.sendMessage(MessageUtil.createColoredMessage("&e/tabby reload &7- Reloads the config file"));
                sender.sendMessage(MessageUtil.createColoredMessage("&e/tabby help &7- Shows this message"));
            } else {
                sender.sendMessage(MessageUtil.createColoredMessage(plugin.getPrefix() + "&cYou do not have permission to use this command."));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("tabby.reload")) {
                int res = plugin.refreshConfig();
                if (res == 0) {
                    sender.sendMessage(MessageUtil.createColoredMessage(plugin.getPrefix() + "&aSuccessfully reloaded config, expect update shortly."));
                } else {
                    sender.sendMessage(MessageUtil.createColoredMessage(plugin.getPrefix() + "&cFailed to reload config file. Config issue?"));
                }
            } else {
                sender.sendMessage(MessageUtil.createColoredMessage(plugin.getPrefix() + "&cYou do not have permission to use this command."));
            }
            return true;
        }

        sender.sendMessage(MessageUtil.createColoredMessage(plugin.getPrefix() + "&cUnknown command. Use /tabby help for help."));
        return true;
    }

    // Tab completion, not really needed but it's nice to have
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            List<String> tabComplete = new ArrayList<>();
            tabComplete.add("help");
            tabComplete.add("reload");
            return tabComplete;
        }
        return null;
    }

    // Getters and setters
    public Tabby getPlugin() {
        return plugin;
    }
}