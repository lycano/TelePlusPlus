package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.ChatBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Helper;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TargetBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TeleHistory;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Teleporter;
import me.taylorkelly.help.Help;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandManager
{
    private TelePlusPlus plugin;
    
    public Help helpPlugin;
    
    public CommandManager(TelePlusPlus plugin)
    {
	this.plugin = plugin;
    }
    
    public void registerHelpCommands()
    {
	Plugin test = plugin.getServer().getPluginManager().getPlugin("Help");
	
	if (helpPlugin == null)
	{
	    if (test != null)
	    {
		helpPlugin = ((Help) test);
	    }
	}
	
	if (helpPlugin != null)
	{
	    helpPlugin.registerCommand("tp [player]", "Teleport to another player", plugin, true, plugin.pm.to);
	    helpPlugin.registerCommand("tp [x] [y] [z]", "Teleport to coordinates", plugin, true, plugin.pm.coords);
	    helpPlugin.registerCommand("tp [world]", "Teleport to another player", plugin, true, plugin.pm.worldTo);
	    helpPlugin.registerCommand("tp [world] [x] [y] [z]", "Teleport to coordinates", plugin, true, plugin.pm.worldCoords);
	    helpPlugin.registerCommand("tp here [player(s)|*]", "Teleports players to you", plugin, true, plugin.pm.here);
	    helpPlugin.registerCommand("tp to [target] [player(s)|*]", "Teleports players to target", plugin, true, plugin.pm.others);
	    helpPlugin.registerCommand("tp top", "Teleports you to the block highest above", plugin, true, plugin.pm.top);
	    helpPlugin.registerCommand("tp up [height]", "Teleports you up on a glass block", plugin, true, plugin.pm.up);
	    helpPlugin.registerCommand("tp above [player]", "Teleports 10 blocks above the player", plugin, true, plugin.pm.above);
	    helpPlugin.registerCommand("tp above [player] [height]", "Teleports above the player", plugin, true, plugin.pm.above);
	    helpPlugin.registerCommand("tp jump", "Teleports you to the block you're looking at", plugin, true, plugin.pm.jump);
	    helpPlugin.registerCommand("tp back", "Teleports you back to previous locations", plugin, true, plugin.pm.back);
	    helpPlugin.registerCommand("tp origin", "Go back to where you were before all tps", plugin, true, plugin.pm.origin);
	    helpPlugin.registerCommand("tp clear", "Clear your entire tp history", plugin, true, plugin.pm.clear);
	    helpPlugin.registerCommand("tp feather", "Get a feather to tp yourself around", plugin, true, plugin.pm.feather);
	    helpPlugin.registerCommand("tp bone", "Get a bone to tp others around", plugin, true, plugin.pm.bone);
	    
	    TelePlusPlus.log.info("[" + plugin.getDescription().getName() + "] 'Help' support enabled");
	}
    }
    
    public boolean processCommand(Player player, String[] split)
    {
	if (split.length > 0)
	{
	    if (split.length == 3 && Helper.isNumber(split[0]) && Helper.isNumber(split[1]) && Helper.isNumber(split[2]) && plugin.pm.hasPermission(player, plugin.pm.coords))
	    {
		World currentWorld = player.getWorld();
		Location loc = new Location(currentWorld, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), player.getLocation().getYaw(), player.getLocation().getPitch());
		Teleporter tp = new Teleporter(loc);
		tp.addTeleportee(player);
		if (!tp.teleport())
		{
		    player.sendMessage(ChatColor.RED + "No free space available for teleport");
		    return true;
		}
		
		String msg = player.getName() + " teleported to " + "[" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
		
		if (plugin.sm.logCoords)
		{
		    logTp(player, msg);
		}
		if (plugin.sm.notifyCoords)
		{
		    notifyTp(player, msg);
		}
	    }
	    else if (split[0].equalsIgnoreCase("here") && plugin.pm.hasPermission(player, plugin.pm.here))
	    {
		if (split.length >= 2)
		{
		    Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
		    Teleporter tp = new Teleporter(loc);
		    
		    if ((split.length == 2) && (split[1].equalsIgnoreCase("*")))
		    {
			tp.addAll(plugin.getServer().getOnlinePlayers());
		    }
		    else
		    {
			for (int i = 1; i < split.length; i++)
			{
			    List<Player> targets = plugin.getServer().matchPlayer(split[i]);
			    
			    if (targets.size() == 1)
			    {
				Player teleportee = (Player) targets.get(0);
				
				if (!canTP(player, teleportee))
				{
				    player.sendMessage(ChatColor.RED + "No rights to summon " + teleportee.getName());
				    continue;
				}
				
				tp.addTeleportee(teleportee);
				
				String msg = player.getName() + " summoned " + teleportee.getName() + " [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
				
				if (plugin.sm.logHere)
				{
				    logTp(player, msg);
				}
				if (plugin.sm.notifyHere)
				{
				    notifyTp(player, msg);
				}
			    }
			    else
			    {
				player.sendMessage(ChatColor.RED + split[i] + " did not match a player");
			    }
			}
		    }
		    if (!tp.teleport())
		    {
			player.sendMessage(ChatColor.RED + "No free space available for teleport");
			return true;
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("to") && plugin.pm.hasPermission(player, plugin.pm.others))
	    {
		if (split.length > 2)
		{
		    String teleportees = "";
		    List<Player> targets = plugin.getServer().matchPlayer(split[1]);
		    
		    if (targets.size() == 1)
		    {
			Player target = (Player) targets.get(0);
			Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), target.getLocation().getYaw(), target.getLocation().getPitch());
			Teleporter tp = new Teleporter(loc);
			
			if ((split.length == 3) && (split[2].equalsIgnoreCase("*")))
			{
			    tp.addAll(plugin.getServer().getOnlinePlayers());
			}
			else
			{
			    for (int i = 2; i < split.length; i++)
			    {
				targets = plugin.getServer().matchPlayer(split[i]);
				
				if (targets.size() == 1)
				{
				    Player teleportee = (Player) targets.get(0);
				    
				    if (!canTP(player, teleportee))
				    {
					player.sendMessage(ChatColor.RED + "No rights to teleport to " + teleportee.getName());
					continue;
				    }
				    
				    tp.addTeleportee(teleportee);
				    teleportees += ", " + teleportee.getName();
				}
				else
				{
				    player.sendMessage(ChatColor.RED + split[i] + " did not match a player");
				}
			    }
			}
			if (!tp.teleport())
			{
			    player.sendMessage(ChatColor.RED + "No free space available for teleport");
			    return true;
			}
			
			String msg = player.getName() + " teleported " + teleportees.substring(1) + " to " + target.getName() + " [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
			
			if (plugin.sm.logOthers)
			{
			    logTp(player, msg);
			}
			if (plugin.sm.notifyOthers)
			{
			    notifyTp(player, msg);
			}
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + split[1] + " did not match a player, cancelling teleport");
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("top") && plugin.pm.hasPermission(player, plugin.pm.top))
	    {
		if (split.length == 1)
		{
		    int y = player.getWorld().getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
		    Location loc = new Location(player.getWorld(), player.getLocation().getX(), y, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
		    Teleporter tp = new Teleporter(loc);
		    tp.addTeleportee(player);
		    if (!tp.teleport())
		    {
			player.sendMessage(ChatColor.RED + "No free space available for teleport");
			return true;
		    }
		    
		    String msg = player.getName() + " moved to the top [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
		    
		    if (plugin.sm.logTop)
		    {
			logTp(player, msg);
		    }
		    if (plugin.sm.notifyTop)
		    {
			notifyTp(player, msg);
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("up") && plugin.pm.hasPermission(player, plugin.pm.up))
	    {
		if (split.length == 2 && Helper.isInteger(split[1]))
		{
		    Location glassloc = player.getLocation();
		    int glassHeight = glassloc.getBlockY() + Integer.parseInt(split[1]);
		    
		    Block targetglass = player.getWorld().getBlockAt(glassloc.getBlockX(), glassHeight, glassloc.getBlockZ());
		    
		    if (!plugin.gm.addGlassed(player, targetglass))
		    {
			player.sendMessage(ChatColor.RED + "No free space above you at that height");
			return true;
		    }
		    
		    Location loc = new Location(player.getWorld(), glassloc.getX(), glassHeight + 1, glassloc.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
		    Teleporter tp = new Teleporter(loc);
		    tp.addTeleportee(player);
		    if (!tp.teleport())
		    {
			player.sendMessage(ChatColor.RED + "No free space available for teleport");
			return true;
		    }
		    
		    String msg = player.getName() + " moved up " + glassHeight + " blocks [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
		    
		    if (plugin.sm.logUp)
		    {
			logTp(player, msg);
		    }
		    if (plugin.sm.notifyUp)
		    {
			notifyTp(player, msg);
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("above") && plugin.pm.hasPermission(player, plugin.pm.above))
	    {
		if (split.length >= 2)
		{
		    int height = 5;
		    
		    if (split.length == 3 && (Helper.isInteger(split[2])))
		    {
			height = Integer.parseInt(split[2]);
		    }
		    List<Player> targets = plugin.getServer().matchPlayer(split[1]);
		    
		    if (targets.size() == 1)
		    {
			Player target = targets.get(0);
			
			if (!canTP(player, target))
			{
			    player.sendMessage(ChatColor.RED + "No rights to teleport above " + target.getName());
			    return true;
			}
			
			Location targetLoc = target.getLocation();
			int glassHeight = targetLoc.getBlockY() + height;
			
			Block targetglass = target.getWorld().getBlockAt(targetLoc.getBlockX(), glassHeight, targetLoc.getBlockZ());
			
			if (!plugin.gm.addGlassed(player, targetglass))
			{
			    player.sendMessage(ChatColor.RED + "No free space above you at that height");
			    return true;
			}
			
			Location loc = new Location(player.getWorld(), targetLoc.getX(), glassHeight + 1, targetLoc.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
			Teleporter tp = new Teleporter(loc);
			tp.addTeleportee(player);
			if (!tp.teleport())
			{
			    player.sendMessage(ChatColor.RED + "No free space available for teleport");
			    return true;
			}
			
			String msg = player.getName() + " teleported above " + target.getName() + " [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
			
			if (plugin.sm.logAbove)
			{
			    logTp(player, msg);
			}
			if (plugin.sm.notifyAbove)
			{
			    notifyTp(player, msg);
			}
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + split[1] + " did not match a player");
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("jump") && plugin.pm.hasPermission(player, plugin.pm.jump))
	    {
		if (split.length == 1)
		{
		    TargetBlock aiming = new TargetBlock(player, 1000, 0.2, plugin.im.getThoughItems());
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
			Teleporter tp = new Teleporter(loc);
			tp.addTeleportee(player);
			if (!tp.teleport())
			{
			    player.sendMessage(ChatColor.RED + "No free space available for teleport");
			    return true;
			}
			
			String msg = player.getName() + " jumped to " + "[" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
			
			if (plugin.sm.logJump)
			{
			    logTp(player, msg);
			}
			if (plugin.sm.notifyJump)
			{
			    notifyTp(player, msg);
			}
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("back") && plugin.pm.hasPermission(player, plugin.pm.back))
	    {
		if (split.length == 1)
		{
		    Location location = TeleHistory.popLocation(player);
		    if (location == null)
		    {
			player.sendMessage(ChatColor.RED + "No locations in your teleport history");
		    }
		    else
		    {
			player.teleportTo(location);
			
			String msg = player.getName() + " went back to " + "[" + printWorld(location.getWorld().getName()) + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + "]";
			
			if (plugin.sm.logBack)
			{
			    logTp(player, msg);
			}
			if (plugin.sm.notifyBack)
			{
			    notifyTp(player, msg);
			}
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("clear") && plugin.pm.hasPermission(player, plugin.pm.clear))
	    {
		if (split.length == 1)
		{
		    if (TeleHistory.clearHistory(player))
		    {
			player.sendMessage(ChatColor.AQUA + "Your history has been cleared");
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + "No locations in your history to clear");
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("origin") && plugin.pm.hasPermission(player, plugin.pm.origin))
	    {
		if (split.length == 1)
		{
		    Location location = TeleHistory.origin(player);
		    if (location == null)
		    {
			player.sendMessage(ChatColor.RED + "No locations in your teleport history");
		    }
		    else
		    {
			player.teleportTo(location);
			
			String msg = player.getName() + " returned to his origin location " + "[" + printWorld(location.getWorld().getName()) + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + "]";
			
			if (plugin.sm.logOrigin)
			{
			    logTp(player, msg);
			}
			if (plugin.sm.notifyOrigin)
			{
			    notifyTp(player, msg);
			}
		    }
		    return true;
		}
	    }
	    else if (split.length == 4 && Helper.isNumber(split[1]) && Helper.isNumber(split[2]) && Helper.isNumber(split[3]) && plugin.pm.hasPermission(player, plugin.pm.worldCoords))
	    {
		World world = plugin.getServer().getWorld(split[0]);
		
		if (world == null)
		{
		    player.sendMessage(ChatColor.RED + "Not a valid world.");
		}
		else
		{
		    Location loc = new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), player.getLocation().getYaw(), player.getLocation().getPitch());
		    
		    Teleporter tp = new Teleporter(loc);
		    tp.addTeleportee(player);
		    if (!tp.teleport())
		    {
			player.sendMessage(ChatColor.RED + "No free space available for teleport");
			return true;
		    }
		    
		    String msg = player.getName() + " teleported across worlds to " + "[" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
		    
		    if (plugin.sm.logWorldCoords)
		    {
			logTp(player, msg);
		    }
		    if (plugin.sm.notifyWorldCoords)
		    {
			notifyTp(player, msg);
		    }
		}
		return true;
	    }
	    else if (split[0].equalsIgnoreCase("feather") && plugin.pm.hasPermission(player, plugin.pm.feather))
	    {
		if (split.length == 1)
		{
		    plugin.im.PutItemInHand(player, Material.FEATHER);
		    player.sendMessage(ChatColor.DARK_PURPLE + "You now have a feather");
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("bone") && plugin.pm.hasPermission(player, plugin.pm.bone))
	    {
		if (split.length == 1)
		{
		    plugin.im.PutItemInHand(player, Material.BONE);
		    player.sendMessage(ChatColor.DARK_PURPLE + "You now have a bone");
		    return true;
		}
	    }
	    else if (plugin.pm.hasPermission(player, plugin.pm.to))
	    {
		if (split.length == 1)
		{
		    List<Player> targets = plugin.getServer().matchPlayer(split[0]);
		    
		    if (targets.size() == 1)
		    {
			Player target = (Player) targets.get(0);
			
			if (!canTP(player, target))
			{
			    player.sendMessage(ChatColor.RED + "No rights to teleport to " + target.getName());
			    return true;
			}
			
			Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), target.getLocation().getYaw(), target.getLocation().getPitch());
			Teleporter tp = new Teleporter(loc);
			tp.addTeleportee(player);
			if (!tp.teleport())
			{
			    player.sendMessage(ChatColor.RED + "No free space available for teleport");
			    return true;
			}
			
			String msg = player.getName() + " teleported to " + "[" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
			
			if (plugin.sm.logTo)
			{
			    logTp(player, msg);
			}
			if (plugin.sm.notifyTo)
			{
			    notifyTp(player, msg);
			}
		    }
		    else
		    {
			World world = plugin.getServer().getWorld(split[0]);
			
			if (world == null && plugin.pm.hasPermission(player, plugin.pm.worldTo))
			{
			    player.sendMessage(ChatColor.RED + "Not a valid world or player.");
			}
			else if (plugin.pm.hasPermission(player, plugin.pm.worldTo))
			{
			    Location loc = new Location(world, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
			    Teleporter tp = new Teleporter(loc);
			    tp.addTeleportee(player);
			    if (!tp.teleport())
			    {
				player.sendMessage(ChatColor.RED + "No free space available for teleport");
				return true;
			    }
			    
			    String msg = player.getName() + " teleported across worlds to " + "[" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
			    
			    if (plugin.sm.logWorldTo)
			    {
				logTp(player, msg);
			    }
			    if (plugin.sm.notifyWorldTo)
			    {
				notifyTp(player, msg);
			    }
			}
			else
			{
			    player.sendMessage(ChatColor.RED + split[0] + " did not match a player, cancelling teleport");
			}
		    }
		    return true;
		}
	    }
	}
	
	if (plugin.pm.hasPermission(player, plugin.pm.menu))
	{
	    ChatBlock.sendBlank(player);
	    ChatBlock.saySingle(player, ChatColor.LIGHT_PURPLE + plugin.name + " " + plugin.getDescription().getVersion() + ChatColor.DARK_GRAY + " ----------------------------------------------------------------------------------");
	    ChatBlock.sendBlank(player);
	    
	    if (plugin.pm.hasPermission(player, plugin.pm.to))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp [player]" + ChatColor.DARK_PURPLE + " - Teleport to another player");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.coords))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp [x] [y] [z]" + ChatColor.DARK_PURPLE + " - Teleport to coordinates");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.worldTo))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp [world]" + ChatColor.DARK_PURPLE + " - Teleport to another world");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.worldCoords))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp [world] [x] [y] [z]" + ChatColor.DARK_PURPLE + " - Teleport to world coordinates");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.here))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp here [player(s)|*]" + ChatColor.DARK_PURPLE + " - Teleports players to you");
	    }
	    if (plugin.pm.hasPermission(player,plugin.pm.others))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp to [target] [player(s)|*]" + ChatColor.DARK_PURPLE + " - Teleports players to target");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.top))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp top" + ChatColor.DARK_PURPLE + " - Teleports you to the block highest above");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.up))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp up [height]" + ChatColor.DARK_PURPLE + " - Teleports you up on a glass block");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.above))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp above [player]" + ChatColor.DARK_PURPLE + " - Teleports 10 blocks above the player ");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.above))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp above [player] [height]" + ChatColor.DARK_PURPLE + " - Teleports above the player ");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.jump))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp jump" + ChatColor.DARK_PURPLE + " - Teleports you to the block you're looking at");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.back))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp back" + ChatColor.DARK_PURPLE + " - Teleports you back to previous locations");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.origin))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp origin" + ChatColor.DARK_PURPLE + " - Go back to where you were before all tps");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.clear))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp clear" + ChatColor.DARK_PURPLE + " - Clear your entire tp history");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.feather))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp feather" + ChatColor.DARK_PURPLE + " - Get a feather to tp yourself around");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.bone))
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp bone" + ChatColor.DARK_PURPLE + " - Get a bone to tp others around");
	    }
	    ChatBlock.sendBlank(player);
	}
	return true;
    }
    
    public void notifyTp(Player tper, String msg)
    {
	if (!plugin.pm.hasPermission(tper, plugin.pm.bypassNotify))
	{
	    for (Player player : plugin.getServer().getOnlinePlayers())
	    {
		if (plugin.pm.hasPermission(player, plugin.pm.notify))
		{
		    if (tper.getName().equals(player.getName()))
		    {
			continue;
		    }
		    player.sendMessage(ChatColor.DARK_GRAY + plugin.name + ": " + msg);
		}
	    }
	}
    }
    
    public void logTp(Player tper, String msg)
    {
	if (!plugin.pm.hasPermission(tper, plugin.pm.bypassLog))
	{
	    TelePlusPlus.log.info(plugin.name + ": " + msg);
	}
    }
    
    public String printWorld(String world)
    {
	if (!plugin.getServer().getWorlds().get(0).getName().equals(world))
	{
	    return world + " ";
	}
	
	return "";
    }
    
    private boolean canTP(Player player, Player target)
    {
	if (plugin.pm.hasPermission(player, plugin.pm.bypassNoTp))
	{
	    return true;
	}
	
	if (plugin.pm.hasPermission(target, plugin.pm.noTp))
	{
	    return false;
	}
	
	return true;
    }
}
