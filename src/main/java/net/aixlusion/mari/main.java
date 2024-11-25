package net.aixlusion.mari;

import net.aixlusion.mari.functions.monitor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class main extends JavaPlugin {

    private config configManager;
    private messages messageManager;

    @Override
    public void onEnable() {
        this.configManager = new config(this);
        this.messageManager = new messages(this);
        new monitor(this);
        getLogger().info("MARI Version " + getDescription().getVersion());
        getLogger().info("========================");
        getLogger().info("__  __   _   ___ ___");
        getLogger().info("|  \\/  | /_\\ | _ \\_ _|");
        getLogger().info("| |\\/| |/ _ \\|   /| |");
        getLogger().info("|_|  |_/_/ \\_\\_|_\\___|");
    }


    @Override
    public void onDisable() {
        getLogger().info("MARI Disabled!");
    }

    public config getConfigManager() {
        return this.configManager;
    }

    public messages getMessageManager() {
        return this.messageManager;
    }
}

