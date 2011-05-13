package net.sacredlabyrinth.Phaed.TelePlusPlus;

import net.sacredlabyrinth.Phaed.TelePlusPlus.listeners.TPPlayerListener;
import net.sacredlabyrinth.Phaed.TelePlusPlus.listeners.TPEntityListener;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.PermissionsManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.SettingsManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.RequestsManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.TeleportManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.ToggleManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.CommandManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.GlassedManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.HelpManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.MoverManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.ItemManager;
import net.sacredlabyrinth.Phaed.TelePlusPlus.managers.TppLogger;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.util.config.Configuration;

public class TelePlusPlus extends JavaPlugin {
    private TPPlayerListener playerListener;
    private TPEntityListener entityListener;
    
    public PermissionsManager permissionsManager;
    public SettingsManager settingsManager;
    public RequestsManager requestsManager;
    public TeleportManager teleportManager;
    public ToggleManager toggleManager;
    public CommandManager commandManager;
    public GlassedManager glassedManager;
    public MoverManager moverManager;
    public ItemManager itemManager;
    public HelpManager helpManager;
    public TppLogger tppLogger;
    
    public final String name = "Tele++";
    public String pluginName;
    public String pluginVersion;
    public Configuration config;
    
    @Override
    public void onEnable() {
        this.config = this.getConfiguration();
        playerListener = new TPPlayerListener(this);
        entityListener = new TPEntityListener(this);
        
        pluginName = this.getDescription().getName();
        pluginVersion = this.getDescription().getVersion();
        
        permissionsManager = new PermissionsManager(this);
        settingsManager = new SettingsManager(this);
        requestsManager = new RequestsManager(this);
        teleportManager = new TeleportManager(this);
        toggleManager = new ToggleManager(this);
        commandManager = new CommandManager(this);
        glassedManager = new GlassedManager(this);
        helpManager = new HelpManager(this);
        itemManager = new ItemManager(this);
        moverManager = new MoverManager();

        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
        this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Priority.Monitor, this);

        TppLogger.Log("initializing version [" + pluginVersion + "]");
        
        // setupHelpmanager
        helpManager.initialize();
        
        TppLogger.Log("version [" + pluginVersion + "] loaded");
    }

    @Override
    public void onDisable() {
        TppLogger.Log("saving configuration");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        try {
            String[] split = args;
            String commandName = command.getName().toLowerCase();
            if (sender instanceof Player) {
                if (commandName.equals("tp") || commandName.equals("tpp")) {
                    return this.commandManager.processCommand((Player) sender, split);
                }
            }
            
            return false;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return true;
        }
    }
}
