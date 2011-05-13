package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemManager {
    private TelePlusPlus plugin;
    
    public ItemManager(TelePlusPlus plugin) {
        this.plugin = plugin;
    }
    
    public List<Integer> getThoughBlocks() {
        return plugin.settingsManager.throughBlocks;
    }
    
    public boolean isThroughBlock(int itemid) {
        return plugin.settingsManager.throughBlocks.contains(itemid);
    }
    
    public boolean PutItemInHand(Player player, Material item) {
        ItemStack handitem = player.getItemInHand();
        Inventory inv = player.getInventory();
        
        if (!handitem.getType().equals(item)) {
            if (!handitem.getType().equals(Material.AIR)) {
                if(inv.firstEmpty() == -1) {
                    player.sendMessage(ChatColor.RED + "No space in your inventory");
                    return false;
                }
                    
                inv.setItem(inv.firstEmpty(), handitem);
            }
        
            if (inv.contains(item)) {
                Integer slotId = inv.first(item);
                ItemStack stack = inv.getItem(slotId);
                Integer stackAmount = stack.getAmount();

                if (stackAmount > 1 ) { 
                    stack.setAmount( stackAmount - 1 );
                } else { 
                    inv.clear(slotId);
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