package net.sacredlabyrinth.Phaed.TelePlusPlus.listeners;

import net.sacredlabyrinth.Phaed.TelePlusPlus.ChatBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Helper;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TargetBlock;
import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import java.util.HashSet;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Entity;

public class TPPlayerListener extends PlayerListener {
    private TelePlusPlus plugin;
    
    public TPPlayerListener(TelePlusPlus plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (plugin.glassedManager.isGlassed(player)) {
            if (from.toVector().toBlockVector().equals(to.toVector().toBlockVector())) {
                return;
            }

            Block footblock = player.getWorld().getBlockAt(to.getBlockX(), to.getBlockY() - 1, to.getBlockZ());
            if (footblock.getType().equals(Material.AIR) || plugin.glassedManager.isGlassedBlock(player, footblock)) {
                return;
            }

            plugin.glassedManager.removeGlassed(player);
            
            return;
        }
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            ItemStack item = event.getItem();

            if (item != null) {
                if (item.getType().equals(Material.getMaterial(plugin.settingsManager.moverItem)) && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.mover) && !plugin.settingsManager.disableMover) {
                    TargetBlock aiming = new TargetBlock(player, 3000, 0.2, plugin.itemManager.getThoughBlocks());
                    Block block = aiming.getTargetBlock();

                    if (block == null || block.getY() <= 1) {
                        player.sendMessage(ChatColor.RED + "Not pointing to valid block");
                    } else {
                        if (!plugin.moverManager.addMovedBlock(player, block)) {
                            ChatBlock.sendMessage(player, ChatColor.RED + "Cannot add block, you have tagged entities");
                            return;
                        }
                        
                        if (plugin.settingsManager.sayMover) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Block tagged");
                        }
                        
                        return;
                    }
                }

                if (item.getType().equals(Material.getMaterial(plugin.settingsManager.toolItem)) && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.tool) && !plugin.settingsManager.disableTool) {
                    TargetBlock aiming = new TargetBlock(player, 3000, 0.2, plugin.itemManager.getThoughBlocks());
                    Block block = aiming.getTargetBlock();

                    if (block == null || block.getY() <= 1) {
                        player.sendMessage(ChatColor.RED + "Not pointing to valid block");
                    } else {
                        Location loc = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

                        if (!plugin.teleportManager.teleport(player, loc)) {
                            player.sendMessage(ChatColor.RED + "No free space available for teleport");
                            return;
                        }

                        String msg = player.getName() + " tool jumped to " + "[" + plugin.commandManager.printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";
                        if (plugin.settingsManager.logTool) {
                            plugin.commandManager.logTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.notifyTool) {
                            plugin.commandManager.notifyTp(player, msg);
                        }
                        
                        if (plugin.settingsManager.sayTool) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Jumped");
                        }

                        event.setCancelled(true);
                        return;
                    }
                }
            }

            Block clicked = event.getClickedBlock();

            if (clicked != null) {
                if (clicked.getType().equals(Material.GLASS)) {
                    if (plugin.glassedManager.isGlassedBlock(player, clicked)) {
                        Block fallblock = player.getWorld().getBlockAt(clicked.getX(), clicked.getY() - plugin.settingsManager.settingsFallBlockDistance, clicked.getZ());

                        if (!plugin.glassedManager.addGlassed(player, fallblock)) {
                            plugin.glassedManager.removeGlassed(player);
                        }
                    }
                }
            }
        } else if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack item = event.getItem();

            if (item != null) {
                if (item.getType().equals(Material.getMaterial(plugin.settingsManager.toolItem)) && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.tool) && !plugin.settingsManager.disableTool) {
                    TargetBlock aiming = new TargetBlock(player, 3000, 0.2, plugin.itemManager.getThoughBlocks());
                    Block block = aiming.getTargetBlock();

                    if (block == null || block.getY() <= 1) {
                        player.sendMessage(ChatColor.RED + "Not pointing to valid block");
                    } else {
                        boolean passed = false;
                        Location from = block.getLocation();

                        while ((block = aiming.getNextBlock()) != null) {
                            if (block.getY() <= 1) {
                                player.sendMessage(ChatColor.RED + "No free space available for teleport");
                                return;
                            }

                            if (plugin.teleportManager.blockIsSafe(block)) {
                                Location to = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

                                to.setX(to.getX() + .5D);
                                to.setZ(to.getZ() + .5D);

                                if (!block.getWorld().isChunkLoaded(to.getBlockX() >> 4, to.getBlockZ() >> 4)) {
                                    block.getWorld().loadChunk(to.getBlockX() >> 4, to.getBlockZ() >> 4);
                                }

                                player.teleport(to);

                                String msg = player.getName() + " passed through " + Math.round(Helper.distance(from, to)) + " blocks to " + "[" + plugin.commandManager.printWorld(to.getWorld().getName()) + to.getBlockX() + " " + to.getBlockY() + " " + to.getBlockZ() + "]";

                                if (plugin.settingsManager.logTool) {
                                    plugin.commandManager.logTp(player, msg);
                                }
                                
                                if (plugin.settingsManager.notifyTool) {
                                    plugin.commandManager.notifyTp(player, msg);
                                }
                                
                                if (plugin.settingsManager.sayTool) {
                                    player.sendMessage(ChatColor.DARK_PURPLE + "Passed through " + Math.round(Helper.distance(from, to)) + " blocks");
                                }

                                passed = true;
                                break;
                            }
                        }

                        if (!passed) {
                            player.sendMessage(ChatColor.RED + "No free space available for teleport");
                        }

                        return;
                    }
                }

                if (item.getType().equals(Material.getMaterial(plugin.settingsManager.moverItem)) && plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.mover) && !plugin.settingsManager.disableMover) {
                    HashSet<Entity> entities = plugin.moverManager.getMovedEntities(player);

                    if (entities.size() > 0) {
                        TargetBlock aiming = new TargetBlock(player, 3000, 0.2, plugin.itemManager.getThoughBlocks());
                        Block block = aiming.getTargetBlock();

                        if (block == null) {
                            player.sendMessage(ChatColor.RED + "Not pointing to valid block");
                        } else {
                            Location loc = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

                            ArrayList<Entity> tps = new ArrayList<Entity>();

                            for (Entity entity : entities) {
                                tps.add(entity);
                            }

                            if (!plugin.teleportManager.teleport(tps, loc)) {
                                player.sendMessage(ChatColor.RED + "No free space available for teleport");
                                return;
                            }

                            plugin.moverManager.setEntitiesDirty();
                            
                            String msg = player.getName() + " moved " + Helper.entityArrayString(tps) + " to [" + plugin.commandManager.printWorld(loc.getWorld().getName()) + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "]";

                            if (plugin.settingsManager.logMover) {
                                plugin.commandManager.logTp(player, msg);
                            }
                            
                            if (plugin.settingsManager.notifyMover) {
                                plugin.commandManager.notifyTp(player, msg);
                            }
                            
                            if (plugin.settingsManager.sayMover) {
                                player.sendMessage(ChatColor.DARK_PURPLE + "Moved");
                            }

                            event.setCancelled(true);
                        }
                    }

                    Block block = plugin.moverManager.getMovedBlock(player);

                    if (block != null) {
                        Material mat = block.getType();
                        byte data = block.getData();

                        TargetBlock aiming = new TargetBlock(player, 3000, 0.2, plugin.itemManager.getThoughBlocks());
                        Block target = aiming.getFaceBlock();

                        if (target != null) {
                            if (plugin.itemManager.isThroughBlock(target.getTypeId())) {
                                block.setType(Material.AIR);
                                target.setType(mat);
                                target.setData(data);

                                if (plugin.settingsManager.sayMover) {
                                    player.sendMessage(ChatColor.DARK_PURPLE + "Moved");
                                }

                                plugin.moverManager.relocateMovedBlock(player, target);
                            } else {
                                player.sendMessage(ChatColor.RED + "There is something in the way");
                                return;
                            }
                        }
                    }

                    if (entities.isEmpty() && block == null) {
                        player.sendMessage(ChatColor.RED + "Nothing has been tagged");
                    }
                }
            }
        }
    }
}