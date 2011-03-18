package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class GlassedManager
{
    private TelePlusPlus plugin;
    private HashMap<String, Vector> glassed = new HashMap<String, Vector>();
    private Set<String> fallDamageImmune = Collections.synchronizedSet(new HashSet<String>());
    
    public GlassedManager(TelePlusPlus plugin)
    {
	this.plugin = plugin;
    }
    
    public boolean isGlassed(Player player)
    {
	return glassed.containsKey(player.getName());
    }
    
    public boolean isGlassedBlock(Player player, Block block)
    {
	if (!glassed.containsKey(player.getName()))
	{
	    return false;
	}
	
	if (glassed.get(player.getName()).equals(block.getLocation().toVector()))
	{
	    return true;
	}
	
	return false;
    }
    
    public boolean addGlassed(Player player, Block block)
    {
	plugin.gm.removeGlassedNotImmunity(player);
	
	if (!block.getType().equals(Material.AIR))
	{
	    if (plugin.sm.fallImmunity)
	    {
		startImmuneRemovalDelay(player);
	    }
	    return false;
	}
	
	block.setType(Material.GLASS);
	glassed.put(player.getName(), block.getLocation().toVector());
	
	if (plugin.sm.fallImmunity)
	{
	    fallDamageImmune.add(player.getName());
	}
	return true;
    }
    
    public void removeGlassed(Player player)
    {
	if (glassed.containsKey(player.getName()))
	{
	    removeGlassedNotImmunity(player);
	    if (plugin.sm.fallImmunity)
	    {
		startImmuneRemovalDelay(player);
	    }
	}
    }
    
    public void removeGlassedNotImmunity(Player player)
    {
	if (glassed.containsKey(player.getName()))
	{
	    Vector vec = glassed.get(player.getName());
	    Block block = player.getWorld().getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
	    if (block.getType().equals(Material.GLASS))
	    {
		block.setType(Material.AIR);
	    }
	    glassed.remove(player.getName());
	}
    }
    
    public boolean isFallDamageImmune(Player player)
    {
	return fallDamageImmune.contains(player.getName());
    }
    
    public void startImmuneRemovalDelay(Player player)
    {
	final String name = player.getName();
	
	plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable()
	{
	    public void run()
	    {
		fallDamageImmune.remove(name);
	    }
	}, plugin.sm.fallImmunitySeconds * 20L);
    }
}
