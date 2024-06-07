package org.epycdev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.epycdev.commands.MainCommand;
import org.epycdev.config.MainConfigManager;
import org.epycdev.listeners.PlayerListener;

import java.util.Objects;

public class EpycOffhand extends JavaPlugin {
    public static String prefix = "&8[&bEpyc Offhand&8] &r";
    private String version;
    private MainConfigManager mainConfigManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        version = getDescription().getVersion();
        mainConfigManager = new MainConfigManager(this);

        registerCommands();
        registerEvents();

        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&b    ____  &3____"));
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&B   / __/ &3/ __ \\   &b&nEpyc Offhand&r &7- &3v" + version));
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&b  / _/  &3/ /_/ /    &fAuthor: &3epycdev"));
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&b /___/  &3\\____/     &fUsing method: &3" + mainConfigManager.getMethod()));
        Bukkit.getConsoleSender().sendMessage(" ");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&b    ____  &3____"));
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&B   / __/ &3/ __ \\   &b&nEpyc Offhand&r &7- &3v" + version));
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&b  / _/  &3/ /_/ /    &fAuthor: &3epycdev"));
        Bukkit.getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', "&b /___/  &3\\____/     &cPlugin was disabled."));
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
}
