package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import java.util.HashMap;
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
	
	if (!handitem.getType().equals(item))
	{
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
		    HashMap<Integer, ? extends ItemStack> inv_stacks = inv.all(item);
		    
		    for (int h_key: inv_stacks.keySet())
		    {
		    	ItemStack stack = inv_stacks.get( h_key );
		    	if (stack.getAmount() > 1 )
		    	{
		    		stack.setAmount( stack.getAmount() - 1 );
		    		break;
		    	}
		    	
		    	if (stack.getAmount() == 1)
		    	{
		    		inv.clear(h_key);
		    		break;
		    	}
		    }
		}
		
		player.setItemInHand(new ItemStack(item, 1));
	} else {
		player.sendMessage(ChatColor.RED + "Can't you feel it? You already have this item in hand");
		return false;
	}
	
	
	return true;
    }
}