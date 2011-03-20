package net.sacredlabyrinth.Phaed.TelePlusPlus.listeners;

import net.sacredlabyrinth.Phaed.TelePlusPlus.Helper;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TargetBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import java.util.HashSet;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Entity;

public class TPPlayerListener extends PlayerListener
{
    private TelePlusPlus plugin;
    
    public TPPlayerListener(TelePlusPlus plugin)
    {
	this.plugin = plugin;
    }
    
    @Override
    public void onPlayerMove(PlayerMoveEvent event)
    {
	Player player = event.getPlayer();
	Location from = event.getFrom();
	Location to = event.getTo();
	
	if (plugin.gm.isGlassed(player))
	{
	    if (from.toVector().toBlockVector().equals(to.toVector().toBlockVector()))
	    {
		return;
	    }
	    
	    Block footblock = player.getWorld().getBlockAt(to.getBlockX(), to.getBlockY() - 1, to.getBlockZ());
	    
	    if (footblock.getType().equals(Material.AIR) || plugin.gm.isGlassedBlock(player, footblock))
	    {
		return;
	    }
	    
	    plugin.gm.removeGlassed(player);
	    return;
	}
    }
    
    @Override
    public void onPlayerItem(PlayerItemEvent event)
    {
	Player player = event.getPlayer();
	ItemStack item = player.getItemInHand();
	
	if (item.getType().equals(Material.FEATHER) && plugin.pm.hasPermission(player, plugin.pm.feather))
	{
	    TargetBlock aiming = new TargetBlock(player, 1000, 0.2, plugin.im.getThoughBlocks());
	    Block block = aiming.getTargetBlock();
	    
	    if (block == null)
	    {
		player.sendMessage(ChatColor.RED + "Not pointing to valid block");
	    }
	    else
	    {
		double x = block.getX() + 0.5D;
		double y = block.getY() + 1;
		double z = block.getZ() + 0.5D;
		World world = block.getWorld();
		Location loc = new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
		
		if (!plugin.tm.teleport(player, loc))
		{
		    player.sendMessage(ChatColor.RED + "No free space available for teleport");
		    return;
		}
		
		String msg = player.getName() + " feather jumped to " + "[" + plugin.cm.printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
		
		if (plugin.sm.logWorld)
		{
		    plugin.cm.logTp(player, msg);
		}
		if (plugin.sm.notifyWorld)
		{
		    plugin.cm.notifyTp(player, msg);
		}
		if (plugin.sm.showNotifications)
		{
		    player.sendMessage(ChatColor.DARK_PURPLE + "Feather jumped");
		}
		
		event.setCancelled(true);
	    }
	}
	
	if (item.getType().equals(Material.BONE) && plugin.pm.hasPermission(player, plugin.pm.bone))
	{
	    HashSet<Entity> entities = plugin.bm.getBonedEntities(player);
	    
	    if (entities.size() > 0)
	    {
		TargetBlock aiming = new TargetBlock(player, 1000, 0.2, plugin.im.getThoughBlocks());
		Block block = aiming.getTargetBlock();
		
		if (block == null)
		{
		    player.sendMessage(ChatColor.RED + "Not pointing to valid block");
		}
		else
		{
		    double x = block.getX() + 0.5D;
		    double y = block.getY() + 1;
		    double z = block.getZ() + 0.5D;
		    World world = block.getWorld();
		    Location loc = new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
		    
		    ArrayList<Entity> tps = new ArrayList<Entity>();
		    
		    for (Entity entity : entities)
		    {
			tps.add(entity);
		    }
		    
		    if (!plugin.tm.teleport(tps, loc))
		    {
			player.sendMessage(ChatColor.RED + "No free space available for teleport");
			return;
		    }
		    
		    plugin.bm.setEntitiesDirty();
		    
		    String msg = player.getName() + " bone teleported " + Helper.entityArrayString(tps) + " to [" + plugin.cm.printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
		    
		    if (plugin.sm.logWorld)
		    {
			plugin.cm.logTp(player, msg);
		    }
		    if (plugin.sm.notifyWorld)
		    {
			plugin.cm.notifyTp(player, msg);
		    }
		    if (plugin.sm.showNotifications)
		    {
			player.sendMessage(ChatColor.DARK_PURPLE + "Boned");
		    }
		    
		    event.setCancelled(true);
		}
	    }
	    
	    Block block = plugin.bm.getBonedBlock(player);
	    
	    if (block != null)
	    {
		Material mat = block.getType();
		
		TargetBlock aiming = new TargetBlock(player, 1000, 0.2, plugin.im.getThoughBlocks());
		Block target = aiming.getFaceBlock();
		
		if (target != null)
		{
		    if (plugin.im.isThroughBlock(target.getTypeId()))
		    {
			block.setType(Material.AIR);
			target.setType(mat);
			player.sendMessage(ChatColor.DARK_PURPLE + "Boned");
			plugin.bm.relocateBonedBlock(player, target);
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + "There is something in the way");
			return;
		    }
		}
	    }
	    
	    if (entities.size() == 0 && block == null)
	    {
		player.sendMessage(ChatColor.RED + "Nothing has been tagged");
	    }
	}
    }
}
