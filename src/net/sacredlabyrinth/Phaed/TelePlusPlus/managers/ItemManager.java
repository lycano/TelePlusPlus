package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemManager
{
    private TelePlusPlus plugin;
    
    public ItemManager(TelePlusPlus plugin)
    {
	this.plugin = plugin;
    }
    
    public List<Integer> getThoughBlocks()
    {
	return plugin.sm.throughBlocks;
    }
    
    public boolean isThroughBlock(int itemid)
    {
	return plugin.sm.throughBlocks.contains(itemid);
    }
    
    public boolean PutItemInHand(Player player, Material item)
    {
	ItemStack handitem = player.getItemInHand();
	Inventory inv = player.getInventory();
	
	if (!handitem.getType().equals(Material.AIR))
	{
	    if(inv.firstEmpty() == -1)
	    {
		player.sendMessage(ChatColor.RED + "No space in your inventory");
		return false;
	    }
	    
	    inv.setItem(inv.firstEmpty(), handitem);
	}
	
	if (inv.contains(item))
	{
	    ItemStack[] stacks = inv.getContents();
	    
	    for (int i = 0; i < stacks.length; i++)
	    {
		if (stacks[i].getType().equals(item))
		{		    
		    stacks[i].setAmount(stacks[i].getAmount() - 1);
		    inv.setContents(stacks);
		    break;
		}
	    }
	}
	
	player.setItemInHand(new ItemStack(item, 1));
	return true;
    }
}
