package net.sacredlabyrinth.Phaed.TelePlusPlus;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

public class Teleporter
{
    private Location destination;
    private ArrayList<Entity> entities;
    
    public Teleporter(Location location)
    {
	destination = location;
	entities = new ArrayList<Entity>();
    }
    
    public boolean teleport()
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
    
    public boolean teleportEntity(Entity entity)
    {
	World world = destination.getWorld();
	double x = destination.getX();
	double y = destination.getY();
	double z = destination.getZ();
	
	if (y < 1.0D)
	{
	    y = 1.0D;
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
	
	entity.teleportTo(new Location(world, x, y, z, destination.getYaw(), destination.getPitch()));
	return true;
    }
    
    private boolean blockIsAboveAir(World world, double x, double y, double z)
    {
	return world.getBlockAt((int) Math.floor(x), (int) Math.floor(y - 1.0D), (int) Math.floor(z)).getType() == Material.AIR;
    }
    
    public void addTeleportee(Entity entity)
    {
	entities.add(entity);
    }
    
    public boolean blockIsSafe(World world, double x, double y, double z)
    {
	return (world.getBlockAt((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z)).getType() == Material.AIR) && (world.getBlockAt((int) Math.floor(x), (int) Math.floor(y + 1.0D), (int) Math.floor(z)).getType() == Material.AIR);
    }
    
    public void addAll(Player[] playerList)
    {
	for (Player player : playerList)
	    addTeleportee(player);
    }
}
