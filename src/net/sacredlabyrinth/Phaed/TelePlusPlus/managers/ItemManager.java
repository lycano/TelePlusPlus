package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import java.util.List;

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
    
    public List<Integer> getThoughItems()
    {
	return plugin.sm.throughItems;
    }
    
    public boolean isThroughItem(int itemid)
    {
	return plugin.sm.throughItems.contains(itemid);
    }
    
    public void PutItemInHand(Player player, Material item)
    {
	ItemStack handitem = player.getItemInHand();
	Inventory inv = player.getInventory();
	
	if (!handitem.getType().equals(Material.AIR))
	{
	    ItemStack inhand = player.getItemInHand();
	    inv.setItem(inv.firstEmpty(), inhand);
	}
	
	if (inv.contains(item))
	{
	    ItemStack[] stacks = inv.getContents();
	    
	    for (ItemStack stack : stacks)
	    {
		if (stack.getType().equals(item))
		{
		    stack.setAmount(stack.getAmount() - 1);
		    break;
		}
	    }
	    inv.setContents(stacks);
	}
	
	player.setItemInHand(new ItemStack(item, 1));
    }    
}
