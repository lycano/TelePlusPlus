package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.ChatBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Helper;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TargetBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TeleHistory;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Request;
import me.taylorkelly.help.Help;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
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
	    helpPlugin.registerCommand("tp [player]", "Teleport to another player", plugin, true, plugin.pm.player);
	    helpPlugin.registerCommand("tp [player(s)] to [player]", "Teleport players to player", plugin, true, plugin.pm.othersPlayer);
	    helpPlugin.registerCommand("tp [player(s)] to [x] [y] [z]", "Teleport players to coords", plugin, true, plugin.pm.othersCoords);
	    helpPlugin.registerCommand("tp [x] [y] [z]", "Teleport to coordinates", plugin, true, plugin.pm.coords);
	    helpPlugin.registerCommand("tp [world] <x> <y> <z>", "Teleport to world", plugin, true, plugin.pm.world);
	    helpPlugin.registerCommand("tp here [player(s)]", "Teleport players to you", plugin, true, plugin.pm.here);
	    helpPlugin.registerCommand("tp mass", "Teleport all players to you", plugin, true, plugin.pm.mass);
	    helpPlugin.registerCommand("tp top", "Teleport to the block highest above you", plugin, true, plugin.pm.top);
	    helpPlugin.registerCommand("tp up <height>", "Teleport up on a glass block", plugin, true, plugin.pm.up);
	    helpPlugin.registerCommand("tp above [player] <height>", "Teleport above a player", plugin, true, plugin.pm.above);
	    helpPlugin.registerCommand("tp jump", "Teleport to the block you're looking at", plugin, true, plugin.pm.jump);
	    helpPlugin.registerCommand("tp toggle", "Toggle teleporting to you on/off", plugin, true, plugin.pm.toggle);
	    helpPlugin.registerCommand("tp back", "Teleport back to your previous locations", plugin, true, plugin.pm.back);
	    helpPlugin.registerCommand("tp origin", "Go back to where you were before all tps", plugin, true, plugin.pm.origin);
	    helpPlugin.registerCommand("tp clear", "Clear your tp history and " + Helper.friendlyBlockType(Material.getMaterial(plugin.sm.moverItem).toString()).toLowerCase() + " selections", plugin, true, plugin.pm.clear);
	    helpPlugin.registerCommand("tp tool", "Get a " + Helper.friendlyBlockType(Material.getMaterial(plugin.sm.toolItem).toString()).toLowerCase() + " to tp yourself around", plugin, true, plugin.pm.tool);
	    helpPlugin.registerCommand("tp mover", "Get a " + Helper.friendlyBlockType(Material.getMaterial(plugin.sm.moverItem).toString()).toLowerCase() + " to tp others around", plugin, true, plugin.pm.mover);
	    helpPlugin.registerCommand("tp request [player|x y z] [reason]", "Request tp", plugin, true, plugin.pm.request);
	    
	    TelePlusPlus.log.info("[" + plugin.getDescription().getName() + "] 'Help' support enabled");
	}
    }
    
    public boolean processCommand(Player player, String[] split)
    {
	if (split.length > 0)
	{
	    if (split.length == 3 && Helper.isNumber(split[0]) && Helper.isNumber(split[1]) && Helper.isNumber(split[2]) && plugin.pm.hasPermission(player, plugin.pm.coords) && !plugin.sm.disableCoords)
	    {
		World currentWorld = player.getWorld();
		Location loc = new Location(currentWorld, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), player.getLocation().getYaw(), player.getLocation().getPitch());
		
		if (!plugin.tm.teleport(player, loc))
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
		if (plugin.sm.sayCoords)
		{
		    player.sendMessage(ChatColor.DARK_PURPLE + "Teleported");
		}
		return true;
	    }
	    else if (split[0].equalsIgnoreCase("mass") && plugin.pm.hasPermission(player, plugin.pm.mass) && !plugin.sm.disableMass)
	    {
		if (split.length == 1)
		{
		    ArrayList<Entity> entities = new ArrayList<Entity>();
		    
		    Player[] players = plugin.getServer().getOnlinePlayers();
		    
		    for (Player teleportee : players)
		    {
			if (!canTP(player, teleportee))
			{
			    player.sendMessage(ChatColor.RED + "No rights to summon " + teleportee.getName());
			    continue;
			}
			
			entities.add(teleportee);
		    }
		    
		    if (!plugin.tm.teleport(entities, player))
		    {
			player.sendMessage(ChatColor.RED + "No free space available for teleport");
			return true;
		    }
		    
		    String msg = player.getName() + " mass teleported all players to [" + printWorld(player.getWorld().getName()) + player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ() + "]";
		    
		    if (plugin.sm.logMass)
		    {
			logTp(player, msg);
		    }
		    if (plugin.sm.notifyMass)
		    {
			notifyTp(player, msg);
		    }
		    if (plugin.sm.sayMass)
		    {
			player.sendMessage(ChatColor.DARK_PURPLE + "Mass teleported all players to your location");
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("here") && plugin.pm.hasPermission(player, plugin.pm.here) && !plugin.sm.disableHere)
	    {
		if (split.length >= 2)
		{
		    Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
		    
		    ArrayList<Entity> entities = new ArrayList<Entity>();
		    
		    for (int i = 1; i < split.length; i++)
		    {
			Player teleportee = Helper.matchUniquePlayer(plugin, split[i]);
			
			if (teleportee != null)
			{
			    if (!canTP(player, teleportee))
			    {
				player.sendMessage(ChatColor.RED + "No rights to summon " + teleportee.getName());
				continue;
			    }
			    
			    entities.add(teleportee);
			}
			else
			{
			    player.sendMessage(ChatColor.RED + split[i] + " did not match a player");
			}
		    }
		    
		    if (!plugin.tm.teleport(entities, player))
		    {
			player.sendMessage(ChatColor.RED + "No free space available for teleport");
			return true;
		    }
		    
		    String msg = player.getName() + " summoned " + Helper.entityArrayString(entities) + " to [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
		    
		    if (plugin.sm.logHere)
		    {
			logTp(player, msg);
		    }
		    if (plugin.sm.notifyHere)
		    {
			notifyTp(player, msg);
		    }
		    if (plugin.sm.sayHere)
		    {
			player.sendMessage(ChatColor.DARK_PURPLE + "Summoned " + Helper.entityArrayString(entities));
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("top") && plugin.pm.hasPermission(player, plugin.pm.top) && !plugin.sm.disableTop)
	    {
		if (split.length == 1)
		{
		    int y = player.getWorld().getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
		    Location loc = new Location(player.getWorld(), player.getLocation().getX(), y, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
		    
		    if (!plugin.tm.teleport(player, loc))
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
		    if (plugin.sm.sayTop)
		    {
			player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to top");
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("up") && plugin.pm.hasPermission(player, plugin.pm.up) && !plugin.sm.disableUp)
	    {
		Location glassloc = player.getLocation();
		
		int glassHeight = glassloc.getBlockY() + 10;
		
		if (split.length == 2 && Helper.isInteger(split[1]))
		{
		    glassHeight = glassloc.getBlockY() + Integer.parseInt(split[1]);
		}
		
		Block targetglass = player.getWorld().getBlockAt(glassloc.getBlockX(), glassHeight, glassloc.getBlockZ());
		Location loc = new Location(player.getWorld(), glassloc.getX(), glassHeight + 1, glassloc.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
		
		if (!plugin.gm.addGlassed(player, targetglass))
		{
		    if (plugin.pm.hasPermission(player, plugin.pm.top) && !plugin.sm.disableTop)
		    {
			int y = player.getWorld().getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
			loc = new Location(player.getWorld(), player.getLocation().getX(), y, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
			glassHeight = (int) Math.round(Helper.distance(glassloc, loc));
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + "No free space above you at that height");
			return true;
		    }
		}
		
		if (!plugin.tm.teleport(player, loc))
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
		if (plugin.sm.sayUp)
		{
		    player.sendMessage(ChatColor.DARK_PURPLE + "Teleported up");
		}
		return true;
	    }
	    else if (split[0].equalsIgnoreCase("above") && plugin.pm.hasPermission(player, plugin.pm.above) && !plugin.sm.disableAbove)
	    {
		if (split.length >= 2)
		{
		    int height = 5;
		    
		    if (split.length == 3 && (Helper.isInteger(split[2])))
		    {
			height = Integer.parseInt(split[2]);
		    }
		    
		    Player target = Helper.matchUniquePlayer(plugin, split[1]);
		    
		    if (target != null)
		    {
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
			
			if (!plugin.tm.teleport(player, loc))
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
			if (plugin.sm.sayAbove)
			{
			    player.sendMessage(ChatColor.DARK_PURPLE + "Teleported above " + target.getName());
			}
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + split[1] + " did not match a player");
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("jump") && plugin.pm.hasPermission(player, plugin.pm.jump) && !plugin.sm.disableJump)
	    {
		if (split.length == 1)
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
			if (plugin.sm.sayJump)
			{
			    player.sendMessage(ChatColor.DARK_PURPLE + "Jumped");
			}
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("toggle") && plugin.pm.hasPermission(player, plugin.pm.toggle) && !plugin.sm.disableToggle)
	    {
		if (split.length == 1)
		{
		    boolean toggled = plugin.tgm.toggle(player);
		    
		    String msg = player.getName() + " toggled teleports " + (toggled ? "off" : "on");
		    
		    if (plugin.sm.logToggle)
		    {
			logTp(player, msg);
		    }
		    if (plugin.sm.notifyToggle)
		    {
			notifyTp(player, msg);
		    }
		    if (plugin.sm.sayToggle)
		    {
			player.sendMessage(ChatColor.DARK_PURPLE + "Toggled teleports " + (toggled ? "off" : "on"));
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("back") && plugin.pm.hasPermission(player, plugin.pm.back) && !plugin.sm.disableBack)
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
			player.teleport(location);
			
			String msg = player.getName() + " went back to " + "[" + printWorld(location.getWorld().getName()) + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + "]";
			
			if (plugin.sm.logBack)
			{
			    logTp(player, msg);
			}
			if (plugin.sm.notifyBack)
			{
			    notifyTp(player, msg);
			}
			if (plugin.sm.sayBack)
			{
			    player.sendMessage(ChatColor.DARK_PURPLE + "Teleported back");
			}
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("clear") && plugin.pm.hasPermission(player, plugin.pm.clear) && !plugin.sm.disableClear)
	    {
		if (split.length == 1)
		{
		    boolean bonecleared = plugin.mm.clearMovedBlock(player);
		    bonecleared = bonecleared || plugin.mm.clearMovedEntities(player);
		    
		    if (bonecleared)
		    {
			player.sendMessage(ChatColor.DARK_PURPLE + "Your bone selections have been cleared");
		    }
		    
		    if (TeleHistory.clearHistory(player))
		    {
			player.sendMessage(ChatColor.DARK_PURPLE + "Your history has been cleared");
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("origin") && plugin.pm.hasPermission(player, plugin.pm.origin) && !plugin.sm.disableOrigin)
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
			player.teleport(location);
			
			String msg = player.getName() + " returned to his origin location " + "[" + printWorld(location.getWorld().getName()) + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + "]";
			
			if (plugin.sm.logOrigin)
			{
			    logTp(player, msg);
			}
			if (plugin.sm.notifyOrigin)
			{
			    notifyTp(player, msg);
			}
			if (plugin.sm.sayOrigin)
			{
			    player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to origin");
			}
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("mover") && plugin.pm.hasPermission(player, plugin.pm.mover) && !plugin.sm.disableMover)
	    {
		if (split.length == 1)
		{
		    if (plugin.im.PutItemInHand(player, Material.getMaterial(plugin.sm.moverItem)))
		    {
			if (plugin.sm.sayMover)
			{
			    player.sendMessage(ChatColor.DARK_PURPLE + "You now have a " + Helper.friendlyBlockType(Material.getMaterial(plugin.sm.moverItem).toString()).toLowerCase());
			}
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("tool") && plugin.pm.hasPermission(player, plugin.pm.tool) && !plugin.sm.disableTool)
	    {
		if (split.length == 1)
		{
		    if (plugin.im.PutItemInHand(player, Material.getMaterial(plugin.sm.toolItem)))
		    {
			if (plugin.sm.sayTool)
			{
			    player.sendMessage(ChatColor.DARK_PURPLE + "You now have a " + Helper.friendlyBlockType(Material.getMaterial(plugin.sm.toolItem).toString()).toLowerCase());
			}
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("request") && plugin.pm.hasPermission(player, plugin.pm.request) && !plugin.sm.disableRequest)
	    {
		if (split.length > 4 && Helper.isNumber(split[1]) && Helper.isNumber(split[2]) && Helper.isNumber(split[3]))
		{
		    if (!plugin.rm.existRequestTakers())
		    {
			player.sendMessage(ChatColor.RED + "There is no one around to take your request");
			return true;
		    }
		    
		    String reason = "";
		    
		    for (int i = 4; i < split.length; i++)
		    {
			reason += split[i] + " ";
		    }
		    reason = reason.trim();
		    
		    plugin.rm.addRequest(player, reason, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
		    
		    String msg = player.getName() + " requested to be tpd to " + "[" + printWorld(player.getWorld().getName()) + Integer.parseInt(split[1]) + " " + Integer.parseInt(split[2]) + " " + Integer.parseInt(split[3]) + "]";
		    
		    if (plugin.sm.logRequest)
		    {
			logTp(player, msg);
		    }
		    if (plugin.sm.notifyRequest)
		    {
			notifyTp(player, msg);
		    }
		    if (plugin.sm.sayRequest)
		    {
			player.sendMessage(ChatColor.RED + "Your tp request has been sent");
		    }
		    return true;
		}
		
		if (split.length > 2)
		{
		    Player targetplayer = Helper.matchUniquePlayer(plugin, split[1]);
		    
		    if (targetplayer == null)
		    {
			player.sendMessage(ChatColor.RED + "There is no player with that name");
			return true;
		    }
		    
		    if (!plugin.rm.existRequestTakers())
		    {
			player.sendMessage(ChatColor.RED + "There is no one around to take your request");
			return true;
		    }
		    
		    String reason = "";
		    
		    for (int i = 2; i < split.length; i++)
		    {
			reason += split[i] + " ";
		    }
		    reason = reason.trim();
		    
		    plugin.rm.addRequest(player, reason, targetplayer);
		    
		    String msg = player.getName() + " requested to be tpd to " + targetplayer.getName() + " [" + printWorld(player.getWorld().getName()) + targetplayer.getLocation().getBlockX() + " " + targetplayer.getLocation().getBlockY() + " " + targetplayer.getLocation().getBlockZ() + "]";
		    
		    if (plugin.sm.logRequest)
		    {
			logTp(player, msg);
		    }
		    if (plugin.sm.notifyRequest)
		    {
			notifyTp(player, msg);
		    }
		    if (plugin.sm.sayRequest)
		    {
			player.sendMessage(ChatColor.DARK_PURPLE + "Your tp request has been sent");
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("take") && plugin.pm.hasPermission(player, plugin.pm.request) && !plugin.sm.disableRequest)
	    {
		if (split.length == 1)
		{
		    Request req = plugin.rm.takeRequest(player);
		    
		    if (req == null)
		    {
			player.sendMessage(ChatColor.RED + "All tp requests have been taken");
			return true;
		    }
		    
		    Player playername = Helper.matchUniquePlayer(plugin, req.getPlayerName());
		    
		    if (playername == null)
		    {
			player.sendMessage(ChatColor.DARK_PURPLE + "The player is no longer online");
			plugin.rm.finishTakenRequest(req);
			return true;
		    }
		    
		    playername.sendMessage(ChatColor.DARK_PURPLE + "Your request has been taken");
		    
		    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.WHITE + "[" + req.getPlayerName() + "] " + ChatColor.YELLOW + "requests tp to " + ChatColor.WHITE + (req.getLocation() != null ? Helper.formatLocation(req.getLocation()) : "[" + req.getTargetName() + "]"));
		    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.YELLOW + "Reason: " + req.getReason());
		    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.GREEN + "/tp accept" + ChatColor.YELLOW + " or " + ChatColor.RED + "/tp deny");
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("accept") && plugin.pm.hasPermission(player, plugin.pm.request) && !plugin.sm.disableRequest)
	    {
		if (split.length == 1)
		{
		    Request req = plugin.rm.retrieveTakenRequest(player);
		    
		    if (req != null)
		    {
			Player playername = Helper.matchUniquePlayer(plugin, req.getPlayerName());
			
			if (playername == null)
			{
			    player.sendMessage(ChatColor.DARK_PURPLE + "The player is no longer online");
			    return true;
			}
			
			if (req.getLocation() != null)
			{
			    playername.teleport(req.getLocation());
			    playername.sendMessage(ChatColor.DARK_PURPLE + "Your tp request has been " + ChatColor.GREEN + "accepted");
			}
			else
			{
			    Player targetplayer = Helper.matchUniquePlayer(plugin, req.getTargetName());
			    
			    if (targetplayer == null)
			    {
				player.sendMessage(ChatColor.DARK_PURPLE + "The target player is no longer online");
				playername.sendMessage(ChatColor.DARK_PURPLE + "The target player is no longer online");
				plugin.rm.finishTakenRequest(req);
				return true;
			    }
			    
			    playername.teleport(targetplayer);
			}
			
			plugin.rm.finishTakenRequest(req);
			
			String msg = player.getName() + " accepted " + playername.getName() + "'s request";
			
			if (plugin.sm.logRequest)
			{
			    logTp(player, msg);
			}
			if (plugin.sm.notifyRequest)
			{
			    notifyTp(player, msg);
			}
			if (plugin.sm.sayRequest)
			{
			    player.sendMessage(ChatColor.DARK_PURPLE + playername.getName() + " has been teleported.");
			}
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + "You have not taken a request");
			return true;
		    }
		    return true;
		}
	    }
	    else if (split[0].equalsIgnoreCase("deny") && plugin.pm.hasPermission(player, plugin.pm.request) && !plugin.sm.disableRequest)
	    {
		if (split.length == 1)
		{
		    if (split.length == 1)
		    {
			Request req = plugin.rm.retrieveTakenRequest(player);
			
			if (req != null)
			{
			    Player playername = Helper.matchUniquePlayer(plugin, req.getPlayerName());
			    
			    if (playername == null)
			    {
				player.sendMessage(ChatColor.DARK_PURPLE + "The player is no longer online");
				plugin.rm.finishTakenRequest(req);
				return true;
			    }
			    
			    plugin.rm.finishTakenRequest(req);
			    String msg = player.getName() + " denied " + playername.getName() + "'s request";
			    
			    if (plugin.sm.logRequest)
			    {
				logTp(player, msg);
			    }
			    if (plugin.sm.notifyRequest)
			    {
				notifyTp(player, msg);
			    }
			    if (plugin.sm.sayRequest)
			    {
				player.sendMessage(ChatColor.DARK_PURPLE + "You have denied " + playername.getName() + "'s request");
				playername.sendMessage(ChatColor.DARK_PURPLE + "Your tp request has been " + ChatColor.RED + "denied");
			    }
			}
			else
			{
			    player.sendMessage(ChatColor.RED + "You have not taken a request");
			    return true;
			}
			return true;
		    }
		}
	    }
	    else
	    {
		if (split.length == 1)
		{
		    Player target = Helper.matchUniquePlayer(plugin, split[0]);
		    
		    if (target != null)
		    {
			if (plugin.pm.hasPermission(player, plugin.pm.player) && !plugin.sm.disablePlayer)
			{
			    if (!canTP(player, target))
			    {
				player.sendMessage(ChatColor.RED + "No rights to teleport to " + target.getName());
				return true;
			    }
			    
			    Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), target.getLocation().getYaw(), target.getLocation().getPitch());
			    
			    if (!plugin.tm.teleport(player, target))
			    {
				player.sendMessage(ChatColor.RED + "No free space available for teleport");
				return true;
			    }
			    
			    String msg = player.getName() + " teleported to " + target.getName() + " [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
			    
			    if (plugin.sm.logPlayer)
			    {
				logTp(player, msg);
			    }
			    if (plugin.sm.notifyPlayer)
			    {
				notifyTp(player, msg);
			    }
			    if (plugin.sm.sayPlayer)
			    {
				player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to " + target.getName());
			    }
			    return true;
			}
		    }
		    else
		    {
			if (plugin.pm.hasPermission(player, plugin.pm.world) && !plugin.sm.disableWorld)
			{
			    World world = plugin.getServer().getWorld(split[0]);
			    
			    if (world == null)
			    {
				player.sendMessage(ChatColor.RED + "Not a valid world or player.");
			    }
			    else
			    {
				Location loc = new Location(world, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
				
				if (!plugin.tm.teleport(player, loc))
				{
				    player.sendMessage(ChatColor.RED + "No free space available for teleport");
				    return true;
				}
				
				String msg = player.getName() + " teleported to " + world.getName() + " [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
				
				if (plugin.sm.logWorld)
				{
				    logTp(player, msg);
				}
				if (plugin.sm.notifyWorld)
				{
				    notifyTp(player, msg);
				}
				if (plugin.sm.sayWorld)
				{
				    player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to " + world.getName());
				}
				return true;
			    }
			}
			return true;
		    }
		}
		
		int toLocation = Helper.wordLocation(split, "to");
		
		if (toLocation > 0)
		{
		    ArrayList<Entity> sources = new ArrayList<Entity>();
		    
		    for (int i = 0; i < toLocation; i++)
		    {
			Player target = Helper.matchUniquePlayer(plugin, split[i]);
			
			if (target != null)
			{
			    if (!canTP(player, target))
			    {
				player.sendMessage(ChatColor.RED + "No rights to teleport to " + target.getName());
				continue;
			    }
			    
			    sources.add(target);
			}
			else
			{
			    player.sendMessage(ChatColor.RED + split[i] + " did not match a player");
			}
		    }
		    
		    if (sources.size() > 0)
		    {
			int targetCount = (split.length - 1) - toLocation;
			
			if (targetCount == 1 && plugin.pm.hasPermission(player, plugin.pm.othersPlayer) && !plugin.sm.disableOthersPlayer)
			{
			    if (Helper.matchUniquePlayer(plugin, split[toLocation + 1]) != null)
			    {
				Player target = Helper.matchUniquePlayer(plugin, split[toLocation + 1]);
				
				if (!plugin.tm.teleport(sources, target))
				{
				    player.sendMessage(ChatColor.RED + "No free space available for teleport");
				    return true;
				}
				
				String msg = player.getName() + " teleported " + Helper.entityArrayString(sources) + " to " + target.getName() + " [" + printWorld(target.getWorld().getName()) + target.getLocation().getBlockX() + " " + target.getLocation().getBlockY() + " " + target.getLocation().getBlockZ() + "]";
				
				if (plugin.sm.logOthersPlayer)
				{
				    logTp(player, msg);
				}
				if (plugin.sm.notifyOthersPlayer)
				{
				    notifyTp(player, msg);
				}
				if (plugin.sm.sayOthersPlayer)
				{
				    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "Teleported " + Helper.entityArrayString(sources) + " to " + target.getName());
				}
				return true;
			    }
			    else
			    {
				player.sendMessage(ChatColor.RED + "Target did not match any player");
			    }
			}
			else if (targetCount == 3 && plugin.pm.hasPermission(player, plugin.pm.othersCoords) && !plugin.sm.disableOthersCoords)
			{
			    if (Helper.isNumber(split[toLocation + 1]) && Helper.isNumber(split[toLocation + 2]) && Helper.isNumber(split[toLocation + 3]))
			    {
				int x = Integer.parseInt(split[toLocation + 1]);
				int y = Integer.parseInt(split[toLocation + 2]);
				int z = Integer.parseInt(split[toLocation + 3]);
				
				Location loc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
				
				String msg = player.getName() + " teleported " + Helper.entityArrayString(sources) + " to [" + printWorld(player.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
				
				if (plugin.sm.logOthersCoords)
				{
				    logTp(player, msg);
				}
				if (plugin.sm.notifyOthersCoords)
				{
				    notifyTp(player, msg);
				}
				if (plugin.sm.sayOthersCoords)
				{
				    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "Teleported " + Helper.entityArrayString(sources) + " to [" + x + " " + y + " " + z + "]");
				}
				return true;
			    }
			    else
			    {
				player.sendMessage(ChatColor.RED + "Target are not valid oordinates");
			    }
			}
			else
			{
			    player.sendMessage(ChatColor.RED + "Target did not match any player and are not coordinates");
			}
		    }
		    else
		    {
			player.sendMessage(ChatColor.RED + "No one to teleport");
		    }
		}
		else if (split.length == 4 && Helper.isNumber(split[1]) && Helper.isNumber(split[2]) && Helper.isNumber(split[3]))
		{
		    if (plugin.pm.hasPermission(player, plugin.pm.world) && !plugin.sm.disableWorld)
		    {
			World world = plugin.getServer().getWorld(split[0]);
			
			if (world == null)
			{
			    player.sendMessage(ChatColor.RED + "Not a valid world.");
			}
			else
			{
			    int x = Integer.parseInt(split[1]);
			    int y = Integer.parseInt(split[2]);
			    int z = Integer.parseInt(split[3]);
			    
			    Location loc = new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
			    
			    if (!plugin.tm.teleport(player, loc))
			    {
				player.sendMessage(ChatColor.RED + "No free space available for teleport");
				return true;
			    }
			    
			    String msg = player.getName() + " teleported across worlds to coords [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
			    
			    if (plugin.sm.logWorld)
			    {
				logTp(player, msg);
			    }
			    if (plugin.sm.notifyWorld)
			    {
				notifyTp(player, msg);
			    }
			    if (plugin.sm.sayWorld)
			    {
				player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to " + world.getName() + " to [" + x + " " + y + " " + z + "]");
			    }
			    return true;
			}
		    }
		}
	    }
	}
	
	if (plugin.pm.hasPermission(player, plugin.pm.menu))
	{
	    ChatBlock.sendBlank(player);
	    ChatBlock.saySingle(player, ChatColor.LIGHT_PURPLE + plugin.name + " " + plugin.getDescription().getVersion() + ChatColor.DARK_GRAY + " ----------------------------------------------------------------------------------");
	    ChatBlock.sendBlank(player);
	    
	    if (plugin.pm.hasPermission(player, plugin.pm.player) && !plugin.sm.disablePlayer)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp [player]" + ChatColor.DARK_PURPLE + " - Teleport to another player");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.othersPlayer) && !plugin.sm.disableOthersPlayer)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp [player(s)] to [player]" + ChatColor.DARK_PURPLE + " - Teleport players to player");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.othersCoords) && !plugin.sm.disableOthersCoords)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp [player(s)] to [x] [y] [z]" + ChatColor.DARK_PURPLE + " - Teleport players to coords");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.coords) && !plugin.sm.disableCoords)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp [x] [y] [z]" + ChatColor.DARK_PURPLE + " - Teleport to coordinates");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.world) && !plugin.sm.disableWorld)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp [world] <x> <y> <z>" + ChatColor.DARK_PURPLE + " - Teleport to world");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.here) && !plugin.sm.disableHere)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp here [player(s)]" + ChatColor.DARK_PURPLE + " - Teleport players to you");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.mass) && !plugin.sm.disableMass)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp mass" + ChatColor.DARK_PURPLE + " - Teleport all players to you");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.top) && !plugin.sm.disableTop)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp top" + ChatColor.DARK_PURPLE + " - Teleport to the block highest above you");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.up) && !plugin.sm.disableUp)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp up <height>" + ChatColor.DARK_PURPLE + " - Teleport up on a glass block");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.above) && !plugin.sm.disableAbove)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp above [player] <height>" + ChatColor.DARK_PURPLE + " - Teleport above a player");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.jump) && !plugin.sm.disableJump)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp jump" + ChatColor.DARK_PURPLE + " - Teleport to the block you're looking at");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.toggle) && !plugin.sm.disableToggle)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp toggle" + ChatColor.DARK_PURPLE + " - Toggle teleporting to you on/off");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.back) && !plugin.sm.disableBack)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp back" + ChatColor.DARK_PURPLE + " - Teleport back to your previous locations");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.origin) && !plugin.sm.disableOrigin)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp origin" + ChatColor.DARK_PURPLE + " - Go back to where you were before all tps");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.clear) && !plugin.sm.disableClear)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp clear" + ChatColor.DARK_PURPLE + " - Clear your tp history and " + Helper.friendlyBlockType(Material.getMaterial(plugin.sm.moverItem).toString()).toLowerCase() + " selections");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.tool) && !plugin.sm.disableTool)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp tool" + ChatColor.DARK_PURPLE + " - Get a " + Helper.friendlyBlockType(Material.getMaterial(plugin.sm.toolItem).toString()).toLowerCase() + " to tp yourself around");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.mover) && !plugin.sm.disableMover)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp mover" + ChatColor.DARK_PURPLE + " - Get a " + Helper.friendlyBlockType(Material.getMaterial(plugin.sm.moverItem).toString()).toLowerCase() + " to tp others around");
	    }
	    if (plugin.pm.hasPermission(player, plugin.pm.request) && !plugin.sm.disableRequest)
	    {
		ChatBlock.sendMessage(player, "  ", ChatColor.WHITE + "/tp request [player|x y z] [reason]" + ChatColor.DARK_PURPLE + " - Request tp");
	    }
	    ChatBlock.sendBlank(player);
	}
	return true;
    }
    
    public void notifyTp(Player tool, String msg)
    {
	if (!plugin.pm.hasPermission(tool, plugin.pm.bypassNotify))
	{
	    for (Player player : plugin.getServer().getOnlinePlayers())
	    {
		if (plugin.pm.hasPermission(player, plugin.pm.notify))
		{
		    if (tool.getName().equals(player.getName()))
		    {
			continue;
		    }
		    ChatBlock.sendMessage(player, ChatColor.DARK_GRAY + plugin.name + ": " + msg);
		}
	    }
	}
    }
    
    public void logTp(Player tool, String msg)
    {
	if (!plugin.pm.hasPermission(tool, plugin.pm.bypassLog))
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
	
	if (plugin.tgm.isDisabled(target))
	{
	    return false;
	}
	
	return true;
    }
}
