package me.willis.permissions.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PConfig {

    private JavaPlugin plugin;

    private File folder;
    private File file;
    private FileConfiguration config;

    public void createPConfig(JavaPlugin plugin, String name) {

        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        folder = new File(plugin.getDataFolder() + File.separator + "players");

        if (!folder.exists()) {
            try {
                folder.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        file = new File(folder, name + ".yml");

        config = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            try {
                file.createNewFile();
                config.set(name + ".Prefix", "");
                config.set(name + ".Suffix", "");
                config.set(name + ".Permissions", "");
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
