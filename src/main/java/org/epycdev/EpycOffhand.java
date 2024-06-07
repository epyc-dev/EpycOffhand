package org.epycdev;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.epycdev.commands.MainCommand;
import org.epycdev.config.MainConfigManager;
import org.epycdev.listeners.PlayerListener;
import org.epycdev.utils.MessageUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class EpycOffhand extends JavaPlugin {
    public static String prefix = "&7[&#00A2FF&lE&#13A9FF&lP&#26B0FF&lY&#39B7FF&lC&#4CBEFF&l &#5FC5FF&lO&#5FC5FF&lF&#4CBEFF&lF&#39B7FF&lH&#26B0FF&lA&#13A9FF&lN&#00A2FF&lD&r&7] &r";
    private String version;
    private MainConfigManager mainConfigManager;
    private static final String UPDATE_CHECK_URL = "https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=116986";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        version = getDescription().getVersion();
        mainConfigManager = new MainConfigManager(this);

        registerCommands();
        registerEvents();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#00A2FF    ____  &#68C8FF____"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#00A2FF   / __/ &#68C8FF/ __ \\   &#00A2FF&nEpyc&r &#00A2FF&nOffhand&r &7- &#68C8FFv" + version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#00A2FF  / _/  &#68C8FF/ /_/ /    &fAuthor: &#68C8FFepycdev"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#00A2FF /___/  &#68C8FF\\____/     &fUsing method: &#68C8FF" + mainConfigManager.getMethod()));
        Bukkit.getConsoleSender().sendMessage(" ");

        checkForUpdates();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#00A2FF    ____  &#68C8FF____"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#00A2FF   / __/ &#68C8FF/ __ \\   &#00A2FF&nEpyc&r &#00A2FF&nOffhand&r &7- &#68C8FFv" + version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#00A2FF  / _/  &#68C8FF/ /_/ /    &fAuthor: &#68C8FFepycdev"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#00A2FF /___/  &#68C8FF\\____/     &cPlugin was disabled."));
        Bukkit.getConsoleSender().sendMessage(" ");
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("epycoffhand")).setExecutor(new MainCommand(this));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerListener(mainConfigManager, this), this);
    }

    public MainConfigManager getMainConfigManager() {
        return mainConfigManager;
    }

    private void checkForUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(UPDATE_CHECK_URL).openConnection();
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                JSONParser parser = new JSONParser();
                JSONObject response = (JSONObject) parser.parse(in);
                in.close();

                String latestVersion = (String) response.get("current_version");
                if (latestVersion != null & !latestVersion.isEmpty() & !latestVersion.equals(version)) {
                    Bukkit.getScheduler().runTask(this, () -> {
                        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                                prefix + "&#FFFF00A new version is available: &#68C8FF" + latestVersion));
                    });
                } else {
                    Bukkit.getScheduler().runTask(this, () -> {
                        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                                prefix + "&#00FF00You are using the latest version."));
                    });
                }
            } catch (Exception e) {
                getLogger().warning("&#FFFF00Failed to check for updates: " + e.getMessage());
            }
        });
    }
}
