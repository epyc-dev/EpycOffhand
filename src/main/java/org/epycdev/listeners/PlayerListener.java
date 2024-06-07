package org.epycdev.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.epycdev.EpycOffhand;
import org.epycdev.config.MainConfigManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerListener implements Listener {

    private final MainConfigManager configManager;
    private final Set<UUID> recentlyLeftCobweb;
    private final EpycOffhand plugin;

    public PlayerListener(MainConfigManager configManager, EpycOffhand plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
        this.recentlyLeftCobweb = new HashSet<>();
    }

    // METHOD 1: Handling Player Interactions
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (configManager.getMethod() == 2) {
            preventFireworkInOffhand(player);
        } else if (configManager.getMethod() == 4) {
            event.setCancelled(true);
        }
    }

    // METHOD 2 and 4: Handling Item Swapping in Hands
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        int method = configManager.getMethod();
        switch (method) {
            case 1:
                preventSwappingFireworkInOffhand(event);
                break;
            case 2:
            case 4:
                event.setCancelled(true);
                break;
            case 3:
                preventSwappingInCobweb(event);
                break;
            default:
                break;
        }
    }

    // METHOD 3 and 4: Handling Inventory Click Events
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        int method = configManager.getMethod();
        if ((method == 2 || method == 4) && event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();

            if (event.getSlot() == 40 || event.getRawSlot() == 40) {
                event.setCancelled(true);
            }

            if (event.getClick().isKeyboardClick() && event.getHotbarButton() != -1 &&
                    (event.getSlot() == 40 || event.getRawSlot() == 40)) {
                event.setCancelled(true);
            }

            if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR &&
                    (event.getSlot() == 40 || event.getRawSlot() == 40)) {
                event.setCancelled(true);
            }

            if (event.getSlot() == 40 || (event.getAction() == InventoryAction.HOTBAR_SWAP &&
                    !event.getClick().isKeyboardClick())) {
                event.setCancelled(true);
            }
        }
    }

    // METHOD 4: Handling Inventory Drag Events
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (configManager.getMethod() == 4 && event.getWhoClicked() instanceof Player) {
            if (event.getNewItems().containsKey(40)) {
                event.setCancelled(true);
            }
        }
    }

    // Prevent swapping a firework to the offhand (METHOD 1)
    private void preventSwappingFireworkInOffhand(PlayerSwapHandItemsEvent event) {
        if (isFirework(event.getMainHandItem())) {
            event.setCancelled(true);
        }
    }

    // Remove firework from the offhand if present (METHOD 1)
    private void preventFireworkInOffhand(Player player) {
        if (isFirework(player.getInventory().getItemInOffHand())) {
            player.getInventory().setItemInOffHand(null);
        }
    }

    // Prevent item swapping while in a cobweb (METHOD 3)
    private void preventSwappingInCobweb(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (isInCobweb(player)) {
            if (isFirework(event.getMainHandItem())) {
                event.setCancelled(true);
                recentlyLeftCobweb.add(playerUUID);
                Bukkit.getScheduler().runTaskLater(plugin, () -> recentlyLeftCobweb.remove(playerUUID), 80L); // 80 ticks = 4 seconds
            } else if (isFirework(event.getOffHandItem())) {
                event.setCancelled(true);
                recentlyLeftCobweb.add(playerUUID);
                Bukkit.getScheduler().runTaskLater(plugin, () -> recentlyLeftCobweb.remove(playerUUID), 80L); // 80 ticks = 4 seconds
            }
        } else if (recentlyLeftCobweb.contains(playerUUID)) {
            event.setCancelled(true);
        }
    }

    // Check if the player is in a cobweb
    private boolean isInCobweb(Player player) {
        return player.getLocation().getBlock().getType() == Material.COBWEB ||
                player.getLocation().add(0, 1, 0).getBlock().getType() == Material.COBWEB;
    }

    // Check if the item is a firework
    private boolean isFirework(ItemStack item) {
        return item != null && item.getType() == Material.FIREWORK_ROCKET;
    }
}
