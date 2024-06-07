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
    public static String prefix = "&8[&#68C8FFEpyc Offhand&8] &r";
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
                "&#68C8FF    ____  &#00A2FF____"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#68C8FF   / __/ &#00A2FF/ __ \\   &#68C8FF&nEpyc&r &#68C8FF&nOffhand&r &7- &#00A2FFv" + version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#68C8FF  / _/  &#00A2FF/ /_/ /    &7Author: &#00A2FFepycdev"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#68C8FF /___/  &#00A2FF\\____/     &7Using method: &#00A2FF" + mainConfigManager.getMethod()));
        Bukkit.getConsoleSender().sendMessage(" ");

        checkForUpdates();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#68C8FF    ____  &#00A2FF____"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#68C8FF   / __/ &#00A2FF/ __ \\   &#68C8FF&nEpyc&r &#68C8FF&nOffhand&r &7- &#00A2FFv" + version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#68C8FF  / _/  &#00A2FF/ /_/ /    &7Author: &#00A2FFepycdev"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(
                "&#68C8FF /___/  &#00A2FF\\____/     &cPlugin was disabled."));
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
                                prefix + "&#FFFF00A new version is available: &#00A2FF" + latestVersion));
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
