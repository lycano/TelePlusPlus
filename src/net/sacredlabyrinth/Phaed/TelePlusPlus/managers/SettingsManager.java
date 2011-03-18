package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.util.config.Configuration;

public class SettingsManager
{
    public boolean logTo;
    public boolean logCoords;
    public boolean logWorldTo;
    public boolean logWorldCoords;
    public boolean logHere;
    public boolean logOthers;
    public boolean logTop;
    public boolean logUp;
    public boolean logAbove;
    public boolean logJump;
    public boolean logBack;
    public boolean logOrigin;
    public boolean logFeater;
    public boolean logBone;
    public boolean notifyTo;
    public boolean notifyCoords;
    public boolean notifyWorldTo;
    public boolean notifyWorldCoords;
    public boolean notifyHere;
    public boolean notifyOthers;
    public boolean notifyTop;
    public boolean notifyUp;
    public boolean notifyAbove;
    public boolean notifyJump;
    public boolean notifyBack;
    public boolean notifyOrigin;
    public boolean notifyFeather;
    public boolean notifyBone;
    public int settingsFallBlockDistance;
    public List<Integer> throughItems;
    public long fallImmunitySeconds;
    public boolean fallImmunity;
    
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
	
	List<Integer> defaultThroughItems = new ArrayList<Integer>();
	defaultThroughItems.add(0);
	defaultThroughItems.add(6);
	defaultThroughItems.add(8);
	defaultThroughItems.add(9);
	defaultThroughItems.add(10);
	defaultThroughItems.add(11);
	defaultThroughItems.add(37);
	defaultThroughItems.add(38);
	defaultThroughItems.add(39);
	defaultThroughItems.add(40);
	defaultThroughItems.add(50);
	defaultThroughItems.add(51);
	defaultThroughItems.add(55);
	defaultThroughItems.add(59);
	defaultThroughItems.add(69);
	defaultThroughItems.add(76);
	
	logTo = config.getBoolean("log.tp.to", false);
	logCoords = config.getBoolean("log.tp.coords", false);
	logHere = config.getBoolean("log.tp.here", false);
	logOthers = config.getBoolean("log.tp.others", false);
	logBack = config.getBoolean("log.tp.back", false);
	logOrigin = config.getBoolean("log.tp.origin", false);
	logWorldTo = config.getBoolean("log.world.to", false);
	logWorldCoords = config.getBoolean("log.world.coords", false);
	logTop = config.getBoolean("log.jump.top", false);
	logUp = config.getBoolean("log.jump.up", false);
	logJump = config.getBoolean("log.jump.jump", false);
	logAbove = config.getBoolean("log.mod.above", false);
	logFeater = config.getBoolean("log.mod.feather", false);
	logBone = config.getBoolean("log.mod.bone", false);

	notifyTo = config.getBoolean("notify.tp.to", false);
	notifyCoords = config.getBoolean("notify.tp.coords", false);
	notifyHere = config.getBoolean("notify.tp.here", false);
	notifyOthers = config.getBoolean("notify.tp.others", false);
	notifyBack = config.getBoolean("notify.tp.back", false);
	notifyOrigin = config.getBoolean("notify.tp.origin", false);
	notifyWorldTo = config.getBoolean("notify.world.to", false);
	notifyWorldCoords = config.getBoolean("notify.world.coords", false);
	notifyTop = config.getBoolean("notify.jump.top", false);
	notifyUp = config.getBoolean("notify.jump.up", false);
	notifyJump = config.getBoolean("notify.jump.jump", false);
	notifyAbove = config.getBoolean("notify.mod.above", false);
	notifyFeather = config.getBoolean("notify.mod.feather", false);
	notifyBone = config.getBoolean("notify.mod.bone", false);
	
	throughItems = config.getIntList("settings.through-items", defaultThroughItems);
	settingsFallBlockDistance = config.getInt("glassed.fall-block-distance", 10);
	fallImmunity = config.getBoolean("glassed.fall-immunity", false);
	fallImmunitySeconds = config.getInt("glassed.fall-immunity-seconds", 5);
    }
}
