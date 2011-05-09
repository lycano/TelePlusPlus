package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import java.util.ArrayList;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import org.bukkit.entity.Player;

public class ToggleManager {
    //private TelePlusPlus plugin;
    private ArrayList<String> toggled = new ArrayList<String>();
    
    public ToggleManager(TelePlusPlus plugin) {
        //this.plugin = plugin;
    }
    
    public boolean toggle(Player player) {
        if(toggled.contains(player.getName())) {
            toggled.remove(player.getName());
            return false;
        }

        toggled.add(player.getName());
        return true;
    }
    
    public boolean isDisabled(Player player) {
        return toggled.contains(player.getName());
    }
}
