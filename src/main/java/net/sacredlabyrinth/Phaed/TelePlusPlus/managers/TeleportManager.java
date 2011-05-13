package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TeleHistory;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

public class TeleportManager {
    private TelePlusPlus plugin;

    public TeleportManager(TelePlusPlus plugin) {
        this.plugin = plugin;
    }
    
    public boolean teleport(Entity entity, Player player) {
        ArrayList<Entity> entities = new ArrayList<Entity>();
        entities.add(entity);

        Location smartLocation = calculateSmartLocation(player);    
        return teleport(entities, smartLocation);
    }
    
    public boolean teleport(Entity entity, Location destination) {
        ArrayList<Entity> entities = new ArrayList<Entity>();
        entities.add(entity);
        
        return teleport(entities, destination);
    }
    
    public boolean teleport(Player[] players, Location destination) {
        ArrayList<Entity> entities = new ArrayList<Entity>();

        for(Player pl : players) {
            entities.add(pl);
        }

        return teleport(entities, destination);
    }
    
    public boolean teleport(ArrayList<Entity> entities, Player player) {
        Location smartLocation = calculateSmartLocation(player);
        return teleport(entities, smartLocation);
    }
    
    public boolean teleport(ArrayList<Entity> entities, Location destination) {
        World world = destination.getWorld();
        double x = destination.getBlockX();
        double y = destination.getBlockY();
        double z = destination.getBlockZ();

        x = x + .5D;
        z = z + .5D;

        if (y < 1.0D) {
            y = 1.0D;
        }
        
        if (!world.isChunkLoaded(destination.getBlockX() >> 4, destination.getBlockZ() >> 4)) {
            world.loadChunk(destination.getBlockX() >> 4, destination.getBlockZ() >> 4);
        }

        while (blockIsAboveAir(world, x, y, z)) {
            y -= 1.0D;

            if (y < -512) {
                return false;
            }
        }
        
        while (!blockIsSafe(world, x, y, z)) {
            y += 1.0D;

            if (y > 512) {
                return false;
            }
        }

        for (Entity entity : entities) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                TeleHistory.pushLocation(player, player.getLocation());
            }
            
            Location targetLocation = new Location(world, x, y, z, destination.getYaw(), destination.getPitch());
            
            entity.teleport(targetLocation);
            try {
                String methodName = world.getClass().getMethod("strikeLightningEffect", Location.class).getName();
                if ((!methodName.isEmpty()) && (entity instanceof Player) && (!plugin.settingsManager.disableLightning) && (plugin.permissionsManager.hasPermission((Player) entity, plugin.permissionsManager.lightning))) {
                    world.strikeLightningEffect(targetLocation);
                }
            } catch (Exception ex) { 
                if (plugin.settingsManager.logSleNotFound) {
                    TppLogger.Log("strikeLightningEffect() not found. Is your craftbukkit build up to date?");
                }
            }
        }

        return true;
    }
        
    private boolean blockIsAboveAir(World world, double x, double y, double z) {
        Material mat = world.getBlockAt((int) Math.floor(x), (int) Math.floor(y - 1.0D), (int) Math.floor(z)).getType();

        return plugin.settingsManager.throughBlocks.contains(mat.getId());
    }
    
    public boolean blockIsSafe(Block block) {
        return blockIsSafe(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }
    
    public boolean blockIsSafe(World world, double x, double y, double z) {
        Material mat1 = world.getBlockAt((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z)).getType();
        Material mat2 = world.getBlockAt((int) Math.floor(x), (int) Math.floor(y + 1.0D), (int) Math.floor(z)).getType();
    
        return (plugin.settingsManager.throughBlocks.contains(mat1.getId())) && (plugin.settingsManager.throughBlocks.contains(mat2.getId()));
    }
    
    public Location calculateSmartLocation(Player player) {
        return player.getLocation();
    }
}