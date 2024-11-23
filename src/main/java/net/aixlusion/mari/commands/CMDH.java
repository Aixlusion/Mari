package net.aixlusion.mari;

import org.bukkit.command.PluginCommand;

public class CMDH {
    public CMDH(Main plugin) {
        registerCommand(plugin, "warp", new WarpCommand(plugin));
        registerCommand(plugin, "home", new HomeCommand(plugin));
        registerCommand(plugin, "rtp", new RTPCommand(plugin));
    }

    private void registerCommand(Main plugin, String name, Object executor) {
        PluginCommand command = plugin.getCommand(name);
        if (command != null) {
            command.setExecutor((org.bukkit.command.CommandExecutor) executor);
        } else {
            plugin.getLogger().warning("Command '" + name + "' is not defined in plugin.yml!");
        }
    }
}
