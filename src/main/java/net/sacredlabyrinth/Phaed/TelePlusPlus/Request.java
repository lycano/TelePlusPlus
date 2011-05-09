package net.sacredlabyrinth.Phaed.TelePlusPlus;

import org.bukkit.Location;

public class Request {
    private String playername;
    private Location location;
    private String targetname;
    private String reason;
    private int minutes;

    public Request(String playername, String reason, Location location) {
        this.playername = playername;
        this.targetname = null;
        this.location = location;
        this.reason = reason;
        this.minutes = 0;
    }
    
    public Request(String playername, String reason, String targetname) {
        this.playername = playername;
        this.targetname = targetname;
        this.location = null;
        this.reason = reason;
        this.minutes = 0;
    }
    
    public String getReason() {
        return reason;
    }
    
    public String getPlayerName() {
        return playername;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public String getTargetName() {
        return targetname;
    }    
    
    public int getMinutes() {
        return minutes;
    }
    
    public void incrementMinutes() {
        minutes = minutes + 1;
    }
}