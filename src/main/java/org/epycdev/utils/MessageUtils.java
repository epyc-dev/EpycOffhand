package org.epycdev.utils;

import org.bukkit.ChatColor;

public class MessageUtils {

    public static String getColoredMessage(String message) {
        message = translateHexCodes(message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static String translateHexCodes(String message) {
        message = message.replaceAll("&#([A-Fa-f0-9]{6})", "§x§$1".replaceAll("(.)", "§$1§"));
        message = message.replaceAll("#([A-Fa-f0-9]{6})", "§x§$1".replaceAll("(.)", "§$1§"));
        return message;
    }
}
