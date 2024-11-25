package net.aixlusion.mari;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class messages {

    private final main plugin;
    private FileConfiguration messages;
    private File messagesFile;

    public messages(main plugin) {
        this.plugin = plugin;
        createMessages();
    }

    private void createMessages() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String get(String path) {
        return messages.getString(path, "Missing message: " + path);
    }

    public void saveMessages() {
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save messages.yml!");
        }
    }
}
