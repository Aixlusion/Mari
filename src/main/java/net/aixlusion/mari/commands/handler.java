package net.aixlusion.mari.commands;

import net.aixlusion.mari.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class handler implements CommandExecutor {

    private final main plugin;

    public handler(main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /mr reload");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigManager().saveConfig();
            sender.sendMessage("Config reloaded!");
            return true;
        }

        sender.sendMessage("Unknown command. Use /mr reload.");
        return true;
    }
}
