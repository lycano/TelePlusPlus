package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import java.util.Vector;
import java.util.HashMap;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Request;
import net.sacredlabyrinth.Phaed.TelePlusPlus.Helper;
import net.sacredlabyrinth.Phaed.TelePlusPlus.ChatBlock;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RequestsManager {
    private TelePlusPlus plugin;
    private Vector<Request> requests = new Vector<Request>();
    private Vector<Request> purged = new Vector<Request>();
    private HashMap<String, Request> taken = new HashMap<String, Request>();
    
    public RequestsManager(TelePlusPlus plugin) {
        this.plugin = plugin;
        requestAgeCounter();
    }
    
    public void addRequest(Player player, String reason, int x, int y, int z) {
        Location loc = player.getLocation();
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);

        Request req = new Request(player.getName(), reason, loc);

        requests.add(req);
        shoutRequest(req);
    }
    
    public void addRequest(Player player, String reason, Player targetplayer) {
        Request req = new Request(player.getName(), reason, targetplayer.getName());

        requests.add(req);
        shoutRequest(req);
    }
    
    public Request takeRequest(Player player) {
        if (taken.containsKey(player.getName())) {
            return taken.get(player.getName());
        }

        if (requests.size() > 0) {
            Request req = requests.get(0);
            requests.remove(0);
            
            taken.put(player.getName(), req);
            
            return req;
        }

        return null;
    }
    
    public void finishTakenRequest(Request req) {
        taken.remove(req);
    }
    
    public Request retrieveTakenRequest(Player player) {
        if (taken.containsKey(player.getName())) {
            Request req = taken.get(player.getName());
            taken.remove(req);
            
            return req;
        }
        
        return null;
    }
    
    public void flush() {
        for (Request req : purged) {
            requests.remove(req);
        }
        
        purged.clear();
    }
    
    public int requestAgeCounter() {
        return plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Request req : requests) {
                    req.incrementMinutes();

                    if (req.getMinutes() == plugin.settingsManager.purgeRequestMinutes) {
                        purged.add(req);
                    }
                }

                flush();
            }
        }, 60 * 20L);
    }
    
    public boolean existRequestTakers() {
        Player[] online = plugin.getServer().getOnlinePlayers();

        for (Player player : online) {
            if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.take) && !plugin.settingsManager.disableRequest) {
                return true;
            }
        }
        
        return false;
    }
    
    public void shoutRequest(Request req) {
        Player[] online = plugin.getServer().getOnlinePlayers();

        for (Player player : online) {
            if (plugin.permissionsManager.hasPermission(player, plugin.permissionsManager.take) && !plugin.settingsManager.disableRequest) {
                ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.WHITE + "[" + req.getPlayerName() + "] " + ChatColor.YELLOW + "requests tp to " + ChatColor.WHITE + (req.getLocation() != null ? Helper.formatLocation(req.getLocation()) : "[" + req.getTargetName() + "]"));
                ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.YELLOW + "Reason: " + req.getReason());
                ChatBlock.sendMessage(player, ChatColor.DARK_PURPLE + "[tp] " + ChatColor.YELLOW + "/tp take");
            }
        }
    }
}