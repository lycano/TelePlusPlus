package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TeleHistory;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

public class TeleportManager
{
    //private TelePlusPlus plugin;

    public TeleportManager(TelePlusPlus plugin)
    {
	//this.plugin = plugin;
    }
    
    public boolean teleport(Entity entity, Player player)
    {
	ArrayList<Entity> entities = new ArrayList<Entity>();
	entities.add(entity);
	
	Location smartLocation = calculateSmartLocation(player);	
	return teleport(entities, smartLocation);
    }
    
    public boolean teleport(Entity entity, Location destination)
    {
	ArrayList<Entity> entities = new ArrayList<Entity>();
	entities.add(entity);
	return teleport(entities, destination);
    }
    
    public boolean teleport(Player[] players, Location destination)
    {
	ArrayList<Entity> entities = new ArrayList<Entity>();
	
	for(Player pl : players)
	{
	    entities.add(pl);
	}
	
	return teleport(entities, destination);
    }
    
    public boolean teleport(ArrayList<Entity> entities, Player player)
    {
	Location smartLocation = calculateSmartLocation(player);
	return teleport(entities, smartLocation);
    }
    
    public boolean teleport(ArrayList<Entity> entities, Location destination)
    {
	World world = destination.getWorld();
	double x = destination.getX();
	double y = destination.getY();
	double z = destination.getZ();
	
	if (y < 1.0D)
	{
	    y = 1.0D;
	}
	if (!world.isChunkLoaded(destination.getBlockX() >> 4, destination.getBlockZ() >> 4))
	{
	    world.loadChunk(destination.getBlockX() >> 4, destination.getBlockZ() >> 4);
	}
	
	while (blockIsAboveAir(world, x, y, z))
	{
	    y -= 1.0D;
	    
	    if (y < -512)
	    {
		return false;
	    }
	}
	while (!blockIsSafe(world, x, y, z))
	{
	    y += 1.0D;
	    
	    if (y > 512)
	    {
		return false;
	    }
	}
	
	for (Entity entity : entities)
	{
	    if (entity instanceof Player)
	    {
		Player player = (Player) entity;
		TeleHistory.pushLocation(player, player.getLocation());
	    }
	    entity.teleportTo(new Location(world, x, y, z, destination.getYaw(), destination.getPitch()));
	}
	
	return true;
    }
        
    private boolean blockIsAboveAir(World world, double x, double y, double z)
    {
	Material mat = world.getBlockAt((int) Math.floor(x), (int) Math.floor(y - 1.0D), (int) Math.floor(z)).getType();
	
	return  mat == Material.AIR || mat == Material.WATER;
    }
    
    public boolean blockIsSafe(World world, double x, double y, double z)
    {
	Material mat1 = world.getBlockAt((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z)).getType();
	Material mat2 = world.getBlockAt((int) Math.floor(x), (int) Math.floor(y + 1.0D), (int) Math.floor(z)).getType();
	
	return (mat1 == Material.AIR || mat1 == Material.WATER) && (mat2 == Material.AIR || mat2 == Material.WATER);
    }
    
    public Location calculateSmartLocation(Player player)
    {
	return player.getLocation();
    }
}
