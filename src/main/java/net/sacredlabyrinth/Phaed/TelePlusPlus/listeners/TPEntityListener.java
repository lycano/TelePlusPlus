package net.sacredlabyrinth.Phaed.TelePlusPlus.listeners;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

public class TPEntityListener extends EntityListener {
    private final TelePlusPlus plugin;
    
    public TPEntityListener(TelePlusPlus plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause().equals(DamageCause.FALL)) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();

                if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.tool) && !plugin.settingsManager.disableTool && player.getItemInHand().getType().equals(Material.getMaterial(plugin.settingsManager.toolItem))) {
                    event.setCancelled(true);
                }

                if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.mover) && !plugin.settingsManager.disableMover && player.getItemInHand().getType().equals(Material.getMaterial(plugin.settingsManager.moverItem))) {
                    event.setCancelled(true);
                }

                if (plugin.glassedManager.isFallDamageImmune(player)) {
                    event.setCancelled(true);
                }
            }
        }

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent) event;
            if (sub.getDamager() instanceof Player) {
                Player player = (Player) sub.getDamager();
                Entity entity = sub.getEntity();
                ItemStack item = player.getItemInHand();

                if (item.getType().equals(Material.getMaterial(plugin.settingsManager.moverItem)) && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.mover) && !plugin.settingsManager.disableMover) {
                    event.setCancelled(true);

                    if (plugin.moverManager.addMovedEntity(player, entity)) {
                        int count = plugin.moverManager.getEntityCount(player);

                        if (plugin.settingsManager.sayMover) {
                            if (entity instanceof Player) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "Player tagged (" + count + ")");
                            } else if (entity instanceof Monster) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "Mob tagged (" + count + ")");
                            } else if (entity instanceof Animals) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "Animal tagged (" + count + ")");
                            } else if (entity instanceof Vehicle) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "Vehicle tagged (" + count + ")");
                            } else if (entity instanceof Vehicle) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "Entity tagged (" + count + ")");
                            }
                        }
                    } else {
                        if (plugin.settingsManager.sayMover) {
                            player.sendMessage(ChatColor.RED + "Already tagged");
                        }
                    }
                }
            }
        }
    }
}
