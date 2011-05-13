package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.ChatBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Helper;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TargetBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TeleHistory;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Request;
import me.taylorkelly.help.Help;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.ConfigurationNode;

public class CommandManager {
    private TelePlusPlus plugin;
    
    protected Help helpPlugin;
    protected static HashMap<String, Integer> playerSettings;
    
    
    public CommandManager(TelePlusPlus plugin) {
        this.plugin = plugin;
    }
    
    public boolean processCommand(Player player, String[] split) {
        if (split.length > 0) {
            if (split.length == 3 && Helper.isNumber(split[0]) && Helper.isNumber(split[1]) && Helper.isNumber(split[2]) && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.coords) && !plugin.settingsManager.disableCoords) {
                World currentWorld = player.getWorld();
                Location loc = new Location(currentWorld, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), player.getLocation().getYaw(), player.getLocation().getPitch());

                if (!plugin.teleportManager.teleport(player, loc)) {
                    player.sendMessage(ChatColor.RED + "No free space available for teleport");
                    return true;
                }

                String msg = player.getName() + " teleported to " + "[" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";

                if (plugin.settingsManager.logCoords) {
                    logTp(player, msg);
                }
                
                if (plugin.settingsManager.notifyCoords) {
                    notifyTp(player, msg);
                }
                
                if (plugin.settingsManager.sayCoords) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Teleported");
                }
                
                return true;
            } else if (split[0].equalsIgnoreCase("mass") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.mass) && !plugin.settingsManager.disableMass) {
                if (split.length == 1) {
                    ArrayList<Entity> entities = new ArrayList<Entity>();

                    Player[] players = plugin.getServer().getOnlinePlayers();

                    for (Player teleportee : players) {
                        if (!canTP(player, teleportee)) {
                            player.sendMessage(ChatColor.RED + "No rights to summon " + teleportee.getName());
                            continue;
                        }

                        entities.add(teleportee);
                    }

                    if (!plugin.teleportManager.teleport(entities, player)) {
                        player.sendMessage(ChatColor.RED + "No free space available for teleport");
                        return true;
                    }

                    String msg = player.getName() + " mass teleported all players to [" + printWorld(player.getWorld().getName()) + player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ() + "]";

                    if (plugin.settingsManager.logMass) {
                        logTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.notifyMass) {
                        notifyTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.sayMass) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Mass teleported all players to your location");
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("here") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.here) && !plugin.settingsManager.disableHere) {
                if (split.length >= 2) {
                    Location loc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

                    ArrayList<Entity> entities = new ArrayList<Entity>();

                    for (int i = 1; i < split.length; i++) {
                        Player teleportee = Helper.matchUniquePlayer(plugin, split[i]);

                        if (teleportee != null) {
                            if (!canTP(player, teleportee)) {
                                player.sendMessage(ChatColor.RED + "No rights to summon " + teleportee.getName());
                                continue;
                            }

                            entities.add(teleportee);
                        } else {
                            player.sendMessage(ChatColor.RED + split[i] + " did not match a player");
                        }
                    }

                    if (!plugin.teleportManager.teleport(entities, player)) {
                        player.sendMessage(ChatColor.RED + "No free space available for teleport");
                        return true;
                    }

                    String msg = player.getName() + " summoned " + Helper.entityArrayString(entities) + " to [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";

                    if (plugin.settingsManager.logHere) {
                        logTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.notifyHere) {
                        notifyTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.sayHere) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Summoned " + Helper.entityArrayString(entities));
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("top") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.top) && !plugin.settingsManager.disableTop) {
                if (split.length == 1) {
                    int y = player.getWorld().getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
                    Location loc = new Location(player.getWorld(), player.getLocation().getX(), y, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

                    if (!plugin.teleportManager.teleport(player, loc)) {
                        player.sendMessage(ChatColor.RED + "No free space available for teleport");
                        return true;
                    }

                    String msg = player.getName() + " moved to the top [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";

                    if (plugin.settingsManager.logTop) {
                        logTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.notifyTop) {
                        notifyTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.sayTop) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to top");
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("up") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.up) && !plugin.settingsManager.disableUp) {
                Location glassloc = player.getLocation();

                int glassHeight = glassloc.getBlockY() + 10;

                if (split.length == 2 && Helper.isInteger(split[1])) {
                    glassHeight = glassloc.getBlockY() + Integer.parseInt(split[1]);
                }

                Block targetglass = player.getWorld().getBlockAt(glassloc.getBlockX(), glassHeight, glassloc.getBlockZ());
                Location loc = new Location(player.getWorld(), glassloc.getX(), glassHeight + 1, glassloc.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

                if (!plugin.glassedManager.addGlassed(player, targetglass)) {
                    if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.top) && !plugin.settingsManager.disableTop) {
                        int y = player.getWorld().getHighestBlockYAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
                        loc = new Location(player.getWorld(), player.getLocation().getX(), y, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                        glassHeight = (int) Math.round(Helper.distance(glassloc, loc));
                    } else {
                        player.sendMessage(ChatColor.RED + "No free space above you at that height");
                        return true;
                    }
                }

                if (!plugin.teleportManager.teleport(player, loc)) {
                    player.sendMessage(ChatColor.RED + "No free space available for teleport");
                    return true;
                }

                String msg = player.getName() + " moved up " + glassHeight + " blocks [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";

                if (plugin.settingsManager.logUp) {
                    logTp(player, msg);
                }
                
                if (plugin.settingsManager.notifyUp) {
                    notifyTp(player, msg);
                }
                
                if (plugin.settingsManager.sayUp) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "Teleported up");
                }
                
                return true;
            } else if (split[0].equalsIgnoreCase("above") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.above) && !plugin.settingsManager.disableAbove) {
                if (split.length >= 2) {
                    int height = 5;

                    if (split.length == 3 && (Helper.isInteger(split[2]))) {
                        height = Integer.parseInt(split[2]);
                    }

                    Player target = Helper.matchUniquePlayer(plugin, split[1]);

                    if (target != null) {
                        if (!canTP(player, target)) {
                            player.sendMessage(ChatColor.RED + "No rights to teleport above " + target.getName());
                            return true;
                        }

                        Location targetLoc = target.getLocation();
                        int glassHeight = targetLoc.getBlockY() + height;

                        Block targetglass = target.getWorld().getBlockAt(targetLoc.getBlockX(), glassHeight, targetLoc.getBlockZ());

                        if (!plugin.glassedManager.addGlassed(player, targetglass)) {
                            player.sendMessage(ChatColor.RED + "No free space above you at that height");
                            return true;
                        }

                        Location loc = new Location(player.getWorld(), targetLoc.getX(), glassHeight + 1, targetLoc.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

                        if (!plugin.teleportManager.teleport(player, loc)) {
                            player.sendMessage(ChatColor.RED + "No free space available for teleport");
                            return true;
                        }

                        String msg = player.getName() + " teleported above " + target.getName() + " [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";

                        if (plugin.settingsManager.logAbove) {
                            logTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.notifyAbove) {
                            notifyTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.sayAbove) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Teleported above " + target.getName());
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + split[1] + " did not match a player");
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("jump") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.jump) && !plugin.settingsManager.disableJump) {
                if (split.length == 1) {
                    TargetBlock aiming = new TargetBlock(player, 1000, 0.2, plugin.itemManager.getThoughBlocks());
                    Block block = aiming.getTargetBlock();

                    if (block == null) {
                        player.sendMessage(ChatColor.RED + "Not pointing to valid block");
                    } else {
                        double x = block.getX() + 0.5D;
                        double y = block.getY() + 1;
                        double z = block.getZ() + 0.5D;
                        World world = block.getWorld();

                        Location loc = new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());

                        if (!plugin.teleportManager.teleport(player, loc)) {
                            player.sendMessage(ChatColor.RED + "No free space available for teleport");
                            return true;
                        }

                        String msg = player.getName() + " jumped to " + "[" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";

                        if (plugin.settingsManager.logJump) {
                            logTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.notifyJump) {
                            notifyTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.sayJump) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Jumped");
                        }
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("toggle") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.toggle) && !plugin.settingsManager.disableToggle) {
                if (split.length == 1) {
                    boolean toggled = plugin.toggleManager.toggle(player);

                    String msg = player.getName() + " toggled teleports " + (toggled ? "off" : "on");

                    if (plugin.settingsManager.logToggle) {
                        logTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.notifyToggle) {
                        notifyTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.sayToggle) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Toggled teleports " + (toggled ? "off" : "on"));
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("back") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.back) && !plugin.settingsManager.disableBack) {
                if (split.length == 1) {
                    Location location = TeleHistory.popLocation(player);

                    if (location == null) {
                        player.sendMessage(ChatColor.RED + "No locations in your teleport history");
                    } else {
                        player.teleport(location);
                        String msg = player.getName() + " went back to " + "[" + printWorld(location.getWorld().getName()) + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + "]";

                        if (plugin.settingsManager.logBack) {
                            logTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.notifyBack) {
                            notifyTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.sayBack) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Teleported back");
                        }
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("clear") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.clear) && !plugin.settingsManager.disableClear) {
                if (split.length == 1) {
                    boolean bonecleared = plugin.moverManager.clearMovedBlock(player);
                    bonecleared = bonecleared || plugin.moverManager.clearMovedEntities(player);

                    if (bonecleared) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Your bone selections have been cleared");
                    }

                    if (TeleHistory.clearHistory(player)) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Your history has been cleared");
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("origin") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.origin) && !plugin.settingsManager.disableOrigin) {
                if (split.length == 1) {
                    Location location = TeleHistory.origin(player);

                    if (location == null) {
                        player.sendMessage(ChatColor.RED + "No locations in your teleport history");
                    } else {
                        player.teleport(location);
                        String msg = player.getName() + " returned to his origin location " + "[" + printWorld(location.getWorld().getName()) + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + "]";

                        if (plugin.settingsManager.logOrigin) {
                            logTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.notifyOrigin) {
                            notifyTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.sayOrigin) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to origin");
                        }
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("mover") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.mover) && !plugin.settingsManager.disableMover) {
                if (split.length == 1) {
                    if (plugin.itemManager.PutItemInHand(player, Material.getMaterial(plugin.settingsManager.moverItem))) {
                        if (plugin.settingsManager.sayMover) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "You now have a " + Helper.friendlyBlockType(Material.getMaterial(plugin.settingsManager.moverItem).toString()).toLowerCase());
                        }
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("tool") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.tool) && !plugin.settingsManager.disableTool) {
                if (split.length == 1) {
                    if (plugin.itemManager.PutItemInHand(player, Material.getMaterial(plugin.settingsManager.toolItem))) {
                        if (plugin.settingsManager.sayTool) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "You now have a " + Helper.friendlyBlockType(Material.getMaterial(plugin.settingsManager.toolItem).toString()).toLowerCase());
                        }
                    }
                    
                    return true;
                }
            } else if ((split[0].equalsIgnoreCase("help") || split[0].equalsIgnoreCase("menu")) && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.menu) && !plugin.settingsManager.disableMenu) {
                if (plugin.helpManager.isHelpActive()) {
                    int pageNumber = 1;
                    
                    if ((split.length >= 2) && (Helper.isNumber(split[1]))) {
                        pageNumber = Integer.parseInt(split[1]);
                        player.performCommand("help " + plugin.pluginName + " "+pageNumber);
                        return true;
                    }
                    
                    if ((split.length >= 2) && (!Helper.isNumber(split[1]))) {
                        player.performCommand("help search tp " + split[1]);
                        return true;
                    }
                    
                    player.performCommand("help " + plugin.pluginName + " "+pageNumber);
                } else {
                    plugin.helpManager.showInternalHelp(player);
                }

                return true;
            } else if (split[0].equalsIgnoreCase("options") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.options) && !plugin.settingsManager.disableOptions) {
                if (split.length >= 2) {
                    String nodePath = split[1];
                    Object nodeValue = plugin.settingsManager.getProperty(nodePath);
                    Boolean nodeArg = false;
                    
                    if ((split.length >= 3) && (Helper.isBoolean(nodeValue))) {
                        if ((Helper.isInteger(split[2])) && (Integer.parseInt(split[2]) == 1)) {
                            nodeArg = true;
                        } else if ((Helper.isInteger(split[2])) && (Integer.parseInt(split[2]) == 0)) {
                            nodeArg = false;
                        } else if ((Helper.isString(split[2])) && split[2].equalsIgnoreCase("true")) {
                            nodeArg = true;
                        } else if ((Helper.isString(split[2])) && split[2].equalsIgnoreCase("false")) {
                            nodeArg = false;
                        } else {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Invalid Argument");
                            return true;
                        }
                        
                        plugin.settingsManager.setProperty(nodePath, nodeArg);
                        player.sendMessage(ChatColor.DARK_PURPLE + "Property '" + ChatColor.WHITE + nodePath + ChatColor.DARK_PURPLE + "' set to: " + ChatColor.WHITE + nodeArg.toString());
                        
                        return true;
                    }
                
                    
                    
                    
                    if ((nodeValue != null) && (Helper.isBoolean(nodeValue))) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Value of property '" + ChatColor.WHITE + nodePath + ChatColor.DARK_PURPLE + "' is: " + ChatColor.WHITE + nodeValue);
                        return true;
                    } else if ((nodeValue != null) && (nodeValue instanceof Object)) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Not implemented yet");
                        return true;
                    }
                    
                    player.sendMessage(ChatColor.DARK_PURPLE + "Property '" + ChatColor.WHITE + nodePath + ChatColor.DARK_PURPLE + "' not found");
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("version")) {
                player.sendMessage(ChatColor.DARK_PURPLE + plugin.name + " version: "+plugin.pluginVersion);

                return true;
            } else if (split[0].equalsIgnoreCase("request") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.request) && !plugin.settingsManager.disableRequest) {
                if (split.length > 4 && Helper.isNumber(split[1]) && Helper.isNumber(split[2]) && Helper.isNumber(split[3])) {
                    if (!plugin.requestsManager.existRequestTakers()) {
                        player.sendMessage(ChatColor.RED + "There is no one around to take your request");
                        return true;
                    }

                    String reason = "";
                    for (int i = 4; i < split.length; i++) {
                        reason += split[i] + " ";
                    }
                    reason = reason.trim();

                    plugin.requestsManager.addRequest(player, reason, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));

                    String msg = player.getName() + " requested to be tpd to " + "[" + printWorld(player.getWorld().getName()) + Integer.parseInt(split[1]) + " " + Integer.parseInt(split[2]) + " " + Integer.parseInt(split[3]) + "]";

                    if (plugin.settingsManager.logRequest) {
                        logTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.notifyRequest) {
                        notifyTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.sayRequest) {
                        player.sendMessage(ChatColor.RED + "Your tp request has been sent");
                    }
                    return true;
                }

                if (split.length > 2) {
                    Player targetplayer = Helper.matchUniquePlayer(plugin, split[1]);

                    if (targetplayer == null) {
                        player.sendMessage(ChatColor.RED + "There is no player with that name");
                        return true;
                    }

                    if (!plugin.requestsManager.existRequestTakers()) {
                        player.sendMessage(ChatColor.RED + "There is no one around to take your request");
                        return true;
                    }

                    String reason = "";
                    for (int i = 2; i < split.length; i++) {
                        reason += split[i] + " ";
                    }
                    reason = reason.trim();

                    plugin.requestsManager.addRequest(player, reason, targetplayer);

                    String msg = player.getName() + " requested to be tpd to " + targetplayer.getName() + " [" + printWorld(player.getWorld().getName()) + targetplayer.getLocation().getBlockX() + " " + targetplayer.getLocation().getBlockY() + " " + targetplayer.getLocation().getBlockZ() + "]";

                    if (plugin.settingsManager.logRequest) {
                        logTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.notifyRequest) {
                        notifyTp(player, msg);
                    }
                    
                    if (plugin.settingsManager.sayRequest) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "Your tp request has been sent");
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("take") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.request) && !plugin.settingsManager.disableRequest) {
                if (split.length == 1) {
                    Request req = plugin.requestsManager.takeRequest(player);

                    if (req == null) {
                        player.sendMessage(ChatColor.RED + "All tp requests have been taken");
                        return true;
                    }

                    Player playername = Helper.matchUniquePlayer(plugin, req.getPlayerName());
                    if (playername == null) {
                        player.sendMessage(ChatColor.DARK_PURPLE + "The player is no longer online");
                        plugin.requestsManager.finishTakenRequest(req);
                        
                        return true;
                    }

                    playername.sendMessage(ChatColor.DARK_PURPLE + "Your request has been taken");

                    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.WHITE + "[" + req.getPlayerName() + "] " + ChatColor.YELLOW + "requests tp to " + ChatColor.WHITE + (req.getLocation() != null ? Helper.formatLocation(req.getLocation()) : "[" + req.getTargetName() + "]"));
                    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.YELLOW + "Reason: " + req.getReason());
                    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.GREEN + "/tp accept" + ChatColor.YELLOW + " or " + ChatColor.RED + "/tp deny");
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("accept") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.request) && !plugin.settingsManager.disableRequest) {
                if (split.length == 1) {
                    Request req = plugin.requestsManager.retrieveTakenRequest(player);

                    if (req != null) {
                        Player playername = Helper.matchUniquePlayer(plugin, req.getPlayerName());

                        if (playername == null) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "The player is no longer online");
                            return true;
                        }

                        if (req.getLocation() != null) {
                            playername.teleport(req.getLocation());
                            playername.sendMessage(ChatColor.DARK_PURPLE + "Your tp request has been " + ChatColor.GREEN + "accepted");
                        } else {
                            Player targetplayer = Helper.matchUniquePlayer(plugin, req.getTargetName());

                            if (targetplayer == null) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "The target player is no longer online");
                                playername.sendMessage(ChatColor.DARK_PURPLE + "The target player is no longer online");
                                plugin.requestsManager.finishTakenRequest(req);
                                
                                return true;
                            }

                            playername.teleport(targetplayer);
                        }

                        plugin.requestsManager.finishTakenRequest(req);

                        String msg = player.getName() + " accepted " + playername.getName() + "'s request";

                        if (plugin.settingsManager.logRequest) {
                            logTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.notifyRequest) {
                            notifyTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.sayRequest) {
                            player.sendMessage(ChatColor.DARK_PURPLE + playername.getName() + " has been teleported.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You have not taken a request");
                        return true;
                    }
                    
                    return true;
                }
            } else if (split[0].equalsIgnoreCase("deny") && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.request) && !plugin.settingsManager.disableRequest) {
                if (split.length == 1) {
                    if (split.length == 1) {
                        Request req = plugin.requestsManager.retrieveTakenRequest(player);

                        if (req != null) {
                            Player playername = Helper.matchUniquePlayer(plugin, req.getPlayerName());
                            if (playername == null) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "The player is no longer online");
                                plugin.requestsManager.finishTakenRequest(req);
                                
                                return true;
                            }

                            plugin.requestsManager.finishTakenRequest(req);
                            String msg = player.getName() + " denied " + playername.getName() + "'s request";

                            if (plugin.settingsManager.logRequest) {
                                logTp(player, msg);
                            }
                            
                            if (plugin.settingsManager.notifyRequest) {
                                notifyTp(player, msg);
                            }
                            
                            if (plugin.settingsManager.sayRequest) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "You have denied " + playername.getName() + "'s request");
                                playername.sendMessage(ChatColor.DARK_PURPLE + "Your tp request has been " + ChatColor.RED + "denied");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You have not taken a request");
                            return true;
                        }
                        
                        return true;
                    }
                }
            } else {
                if (split.length == 1) {
                    Player target = Helper.matchUniquePlayer(plugin, split[0]);
                    if (target != null) {
                        if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.player) && !plugin.settingsManager.disablePlayer) {
                            if (!canTP(player, target)) {
                                player.sendMessage(ChatColor.RED + "No rights to teleport to " + target.getName());
                                return true;
                            }

                            Location loc = new Location(target.getWorld(), target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ(), target.getLocation().getYaw(), target.getLocation().getPitch());
                            if (!plugin.teleportManager.teleport(player, target)) {
                                player.sendMessage(ChatColor.RED + "No free space available for teleport");
                                return true;
                            }

                            String msg = player.getName() + " teleported to " + target.getName() + " [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";

                            if (plugin.settingsManager.logPlayer) {
                                logTp(player, msg);
                            }
                            
                            if (plugin.settingsManager.notifyPlayer) {
                                notifyTp(player, msg);
                            }
                            
                            if (plugin.settingsManager.sayPlayer) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to " + target.getName());
                            }
                            
                            return true;
                        }
                    } else {
                        if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.world) && !plugin.settingsManager.disableWorld) {
                            World world = plugin.getServer().getWorld(split[0]);
                            // @TODO: change warning massage
                            if (world == null) {
                                player.sendMessage(ChatColor.RED + "Not a valid world or player.");
                            } else {
                                // @TODO: make this optional player2spawnpoint instead of current location
                                Location loc = new Location(world, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                                if (!plugin.teleportManager.teleport(player, loc)) {
                                    player.sendMessage(ChatColor.RED + "No free space available for teleport");
                                    return true;
                                }

                                String msg = player.getName() + " teleported to " + world.getName() + " [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
                                if (plugin.settingsManager.logWorld) {
                                    logTp(player, msg);
                                }
                                
                                if (plugin.settingsManager.notifyWorld) {
                                    notifyTp(player, msg);
                                }
                                
                                if (plugin.settingsManager.sayWorld) {
                                    player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to " + world.getName());
                                }

                                return true;
                            }
                        }
                        
                        return true;
                    }
                }

                int toLocation = Helper.wordLocation(split, "to");
                if (toLocation > 0) {
                    ArrayList<Entity> sources = new ArrayList<Entity>();

                    for (int i = 0; i < toLocation; i++) {
                        Player target = Helper.matchUniquePlayer(plugin, split[i]);
                        if (target != null) {
                            if (!canTP(player, target)) {
                                player.sendMessage(ChatColor.RED + "No rights to teleport to " + target.getName());
                                continue;
                            }

                            sources.add(target);
                        } else {
                            player.sendMessage(ChatColor.RED + split[i] + " did not match a player");
                        }
                    }

                    if (sources.size() > 0) {
                        int targetCount = (split.length - 1) - toLocation;

                        if (targetCount == 1 && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.othersPlayer) && !plugin.settingsManager.disableOthersPlayer) {
                            if (Helper.matchUniquePlayer(plugin, split[toLocation + 1]) != null) {
                                Player target = Helper.matchUniquePlayer(plugin, split[toLocation + 1]);
                                if (!plugin.teleportManager.teleport(sources, target)) {
                                    player.sendMessage(ChatColor.RED + "No free space available for teleport");
                                    return true;
                                }

                                String msg = player.getName() + " teleported " + Helper.entityArrayString(sources) + " to " + target.getName() + " [" + printWorld(target.getWorld().getName()) + target.getLocation().getBlockX() + " " + target.getLocation().getBlockY() + " " + target.getLocation().getBlockZ() + "]";
                                if (plugin.settingsManager.logOthersPlayer) {
                                    logTp(player, msg);
                                }
                                
                                if (plugin.settingsManager.notifyOthersPlayer) {
                                    notifyTp(player, msg);
                                }
                                
                                if (plugin.settingsManager.sayOthersPlayer) {
                                    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "Teleported " + Helper.entityArrayString(sources) + " to " + target.getName());
                                }
                                
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "Target did not match any player");
                            }
                        } else if (targetCount == 3 && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.othersCoords) && !plugin.settingsManager.disableOthersCoords) {
                            if (Helper.isNumber(split[toLocation + 1]) && Helper.isNumber(split[toLocation + 2]) && Helper.isNumber(split[toLocation + 3])) {
                                int x = Integer.parseInt(split[toLocation + 1]);
                                int y = Integer.parseInt(split[toLocation + 2]);
                                int z = Integer.parseInt(split[toLocation + 3]);

                                Location loc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());

                                String msg = player.getName() + " teleported " + Helper.entityArrayString(sources) + " to [" + printWorld(player.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
                                if (plugin.settingsManager.logOthersCoords) {
                                    logTp(player, msg);
                                }
                                
                                if (plugin.settingsManager.notifyOthersCoords) {
                                    notifyTp(player, msg);
                                }
                                
                                if (plugin.settingsManager.sayOthersCoords) {
                                    ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "Teleported " + Helper.entityArrayString(sources) + " to [" + x + " " + y + " " + z + "]");
                                }
                                
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "Target are not valid oordinates");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Target did not match any player and are not coordinates");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "No one to teleport");
                    }
                } else if (split.length == 4 && Helper.isNumber(split[1]) && Helper.isNumber(split[2]) && Helper.isNumber(split[3])) {
                    if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.world) && !plugin.settingsManager.disableWorld) {
                        World world = plugin.getServer().getWorld(split[0]);

                        if (world == null) {
                            player.sendMessage(ChatColor.RED + "Not a valid world.");
                        } else {
                            int x = Integer.parseInt(split[1]);
                            int y = Integer.parseInt(split[2]);
                            int z = Integer.parseInt(split[3]);

                            Location loc = new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());

                            if (!plugin.teleportManager.teleport(player, loc)) {
                                player.sendMessage(ChatColor.RED + "No free space available for teleport");
                                return true;
                            }

                            String msg = player.getName() + " teleported across worlds to coords [" + printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";

                            if (plugin.settingsManager.logWorld) {
                                logTp(player, msg);
                            }
                            
                            if (plugin.settingsManager.notifyWorld) {
                                notifyTp(player, msg);
                            }
                            
                            if (plugin.settingsManager.sayWorld) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "Teleported to " + world.getName() + " to [" + x + " " + y + " " + z + "]");
                            }
                            
                            return true;
                        }
                    }
                }
            }
        }
        
        if (!plugin.helpManager.isHelpActive()) {
            plugin.helpManager.showInternalHelp(player);
        } else {
            player.performCommand("tpp help");
        }
    
        return true;
    }
    
    public void notifyTp(Player tool, String msg) {
        if (!plugin.permissionsManager.hasPermission(tool, plugin.permissionsManager.bypassNotify)) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.notify)) {
                    if (tool.getName().equals(player.getName())) {
                        continue;
                    }
                    
                    ChatBlock.sendMessage(player, ChatColor.DARK_GRAY + plugin.name + ": " + msg);
                }
            }
        }
    }
    
    public void logTp(Player tool, String msg) {
        if (!plugin.permissionsManager.hasPermission(tool, plugin.permissionsManager.bypassLog)) {
            TppLogger.Log(msg);
        }
    }
    
    public String printWorld(String world) {
        if (!plugin.getServer().getWorlds().get(0).getName().equals(world)) {
            return world + " ";
        }

        return "";
    }
    
    private boolean canTP(Player player, Player target) {
        if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.bypassNoTp)) {
            return true;
        }

        if (plugin.permissionsManager.hasPermission(target, plugin.permissionsManager.noTp)) {
            return false;
        }

        if (plugin.toggleManager.isDisabled(target)) {
            return false;
        }
    
        return true;
    }
}