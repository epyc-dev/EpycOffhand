package org.epycdev.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.epycdev.EpycOffhand;

public class MainConfigManager {

    private final CustomConfig configFile;
    private final EpycOffhand plugin;

    private int method;

    public MainConfigManager(EpycOffhand plugin) {
        this.plugin = plugin;
        configFile = new CustomConfig("config.yml", null, plugin);
        configFile.registerConfig();
        loadConfig();
    }

    public void loadConfig() {
        FileConfiguration config = configFile.getConfig();
        method = config.getInt("config.function_method.method", 1);
    }

    public void reloadConfig() {
        configFile.reloadConfig();
        loadConfig();
    }

    public int getMethod() {
        return method;
    }
}
