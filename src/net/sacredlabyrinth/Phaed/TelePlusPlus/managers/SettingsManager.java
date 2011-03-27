package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.util.config.Configuration;

public class SettingsManager
{
    public boolean logPlayer;
    public boolean logCoords;
    public boolean logWorld;
    public boolean logHere;
    public boolean logMass;
    public boolean logTop;
    public boolean logUp;
    public boolean logOthersPlayer;
    public boolean logOthersCoords;
    public boolean logAbove;
    public boolean logJump;
    public boolean logToggle;
    public boolean logBack;
    public boolean logOrigin;
    public boolean logFeater;
    public boolean logBone;
    public boolean logRequest;
    public boolean notifyPlayer;
    public boolean notifyCoords;
    public boolean notifyWorld;
    public boolean notifyHere;
    public boolean notifyMass;
    public boolean notifyTop;
    public boolean notifyUp;
    public boolean notifyOthersPlayer;
    public boolean notifyOthersCoords;
    public boolean notifyAbove;
    public boolean notifyToggle;
    public boolean notifyJump;
    public boolean notifyBack;
    public boolean notifyOrigin;
    public boolean notifyFeather;
    public boolean notifyBone;
    public boolean notifyRequest;
    public int settingsFallBlockDistance;
    public List<Integer> throughBlocks;
    public long fallImmunitySeconds;
    public boolean fallImmunity;
    public boolean showNotifications;
    public int purgeRequestMinutes;
    
    private TelePlusPlus plugin;
    
    public SettingsManager(TelePlusPlus plugin)
    {
	this.plugin = plugin;
	
	loadConfiguration();
    }
    
    /**
     * Load the configuration
     */
    @SuppressWarnings("unchecked")
    public void loadConfiguration()
    {
	Configuration config = plugin.getConfiguration();
	config.load();
	
	List<Integer> defaultThroughBlocks = new ArrayList<Integer>();
	defaultThroughBlocks.add(0);
	defaultThroughBlocks.add(6);
	defaultThroughBlocks.add(8);
	defaultThroughBlocks.add(9);
	defaultThroughBlocks.add(10);
	defaultThroughBlocks.add(11);
	defaultThroughBlocks.add(37);
	defaultThroughBlocks.add(38);
	defaultThroughBlocks.add(39);
	defaultThroughBlocks.add(40);
	defaultThroughBlocks.add(50);
	defaultThroughBlocks.add(51);
	defaultThroughBlocks.add(55);
	defaultThroughBlocks.add(59);
	defaultThroughBlocks.add(69);
	defaultThroughBlocks.add(76);
	
	logPlayer = config.getBoolean("log.tp.player", false);
	logCoords = config.getBoolean("log.tp.coords", false);
	logHere = config.getBoolean("log.tp.here", false);
	logToggle = config.getBoolean("log.tp.toggle", false);
	logBack = config.getBoolean("log.tp.back", false);
	logOrigin = config.getBoolean("log.tp.origin", false);
	logOthersPlayer = config.getBoolean("log.others.player", false);
	logOthersCoords = config.getBoolean("log.others.coords", false);
	logWorld = config.getBoolean("log.world.tp", false);
	logTop = config.getBoolean("log.jump.top", false);
	logUp = config.getBoolean("log.jump.up", false);
	logJump = config.getBoolean("log.jump.jump", false);
	logAbove = config.getBoolean("log.mod.above", false);
	logMass = config.getBoolean("log.mod.mass", false);
	logFeater = config.getBoolean("log.mod.feather", false);
	logBone = config.getBoolean("log.mod.bone", false);
	logRequest = config.getBoolean("log.request", false);

	notifyPlayer = config.getBoolean("notify.tp.player", false);
	notifyCoords = config.getBoolean("notify.tp.coords", false);
	notifyHere = config.getBoolean("notify.tp.here", false);
	notifyToggle = config.getBoolean("notify.tp.toggle", false);
	notifyBack = config.getBoolean("notify.tp.back", false);
	notifyOrigin = config.getBoolean("notify.tp.origin", false);
	notifyOthersPlayer = config.getBoolean("notify.others.player", false);
	notifyOthersCoords = config.getBoolean("notify.others.coords", false);
	notifyWorld = config.getBoolean("notify.world.tp", false);
	notifyTop = config.getBoolean("notify.jump.top", false);
	notifyUp = config.getBoolean("notify.jump.up", false);
	notifyJump = config.getBoolean("notify.jump.jump", false);
	notifyAbove = config.getBoolean("notify.mod.above", false);
	notifyMass = config.getBoolean("notify.mod.mass", false);
	notifyFeather = config.getBoolean("notify.mod.feather", false);
	notifyBone = config.getBoolean("notify.mod.bone", false);
	notifyRequest = config.getBoolean("notify.request", false);
	
	settingsFallBlockDistance = config.getInt("glassed.fall-block-distance", 10);
	fallImmunity = config.getBoolean("glassed.fall-immunity", false);
	fallImmunitySeconds = config.getInt("glassed.fall-immunity-seconds", 5);
	throughBlocks = config.getIntList("settings.through-blocks", defaultThroughBlocks);
	showNotifications = config.getBoolean("settings.show-notifications", false);
	purgeRequestMinutes = config.getInt("settings.purge-requests-minutes", 5);
    }
}
