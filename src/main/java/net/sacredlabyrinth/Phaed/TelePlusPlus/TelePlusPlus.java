package net.sacredlabyrinth.Phaed.TelePlusPlus;

import java.util.logging.Logger;

import net.sacredlabyrinth.Phaed.TelePlusPlus.listeners.TPPlayerListener;
import net.sacredlabyrinth.Phaed.TelePlusPlus.listeners.TPEntityListener;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.PermissionsManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.SettingsManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.RequestsManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.TeleportManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.ToggleManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.CommandManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.GlassedManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.MoverManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.ItemManager;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;

public class TelePlusPlus extends JavaPlugin {
    private TPPlayerListener playerListener;
    private TPEntityListener entityListener;
    public PermissionsManager pm;
    public SettingsManager sm;
    public RequestsManager rm;
    public TeleportManager tm;
    public ToggleManager tgm;
    public CommandManager cm;
    public GlassedManager gm;
    public MoverManager mm;
    public ItemManager im;
    
    public static Logger log;
    public String name = "Tele++";
    
    public void onEnable() {
        playerListener = new TPPlayerListener(this);
        entityListener = new TPEntityListener(this);
        
        pm = new PermissionsManager(this);
        sm = new SettingsManager(this);
        rm = new RequestsManager(this);
        tm = new TeleportManager(this);
        tgm = new ToggleManager(this);
        cm = new CommandManager(this);
        gm = new GlassedManager(this);
        im = new ItemManager(this);
        mm = new MoverManager();

        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Monitor, this);

        log = Logger.getLogger("Minecraft");
        log.info("[" + name + "] version [" + this.getDescription().getVersion() + "] loaded");
    }

    @Override
    public void onDisable() { }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        try {
            String[] split = args;
            String commandName = command.getName().toLowerCase();
            if (sender instanceof Player) {
                if (commandName.equals("tp") || commandName.equals("tpp")) {
                    return cm.processCommand((Player) sender, split);
                }
            }
            
            return false;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return true;
        }
    }
}
