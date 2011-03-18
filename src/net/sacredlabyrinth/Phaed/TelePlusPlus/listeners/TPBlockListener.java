package net.sacredlabyrinth.Phaed.TelePlusPlus.listeners;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;
import net.sacredlabyrinth.Phaed.TelePlusPlus.ChatBlock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TPBlockListener extends BlockListener
{
    private final TelePlusPlus plugin;
    
    public TPBlockListener(final TelePlusPlus plugin)
    {
	this.plugin = plugin;
    }
    
    @Override
    public void onBlockDamage(BlockDamageEvent event)
    {
	Block block = event.getBlock();
	Player player = event.getPlayer();
	ItemStack item = player.getItemInHand();
	
	if (item.getType().equals(Material.BONE) && plugin.pm.hasPermission(player, "tpp.mod.bone"))
	{
	    if(!plugin.bm.addBonedBlock(player, block))
	    {
		ChatBlock.sendMessage(player, ChatColor.RED + "Cannot add block, you have tagged entities");
		return;
	    }
	    player.sendMessage(ChatColor.DARK_PURPLE + "Block tagged");
	    event.setCancelled(true);
	}
	
	if (block.getType().equals(Material.GLASS))
	{
	    if (plugin.gm.isGlassedBlock(player, block))
	    {
		Block fallblock = player.getWorld().getBlockAt(block.getX(), block.getY() - plugin.sm.settingsFallBlockDistance, block.getZ());
		
		if (!plugin.gm.addGlassed(player, fallblock))
		{
		    plugin.gm.removeGlassed(player);
		}		
	    }
	}
    }
}
