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
    public boolean logJumpTop;
    public boolean logJumpUp;
    public boolean logJumpAbove;
    public boolean logJumpJump;
    public boolean logTpHistoryBack;
    public boolean logTpHistoryOrigin;
    public boolean logModFeater;
    public boolean logModBone;
    public boolean notifyTo;
    public boolean notifyCoords;
    public boolean notifyWorldTo;
    public boolean notifyWorldCoords;
    public boolean notifyHere;
    public boolean notifyOthers;
    public boolean notifyJumpTop;
    public boolean notifyJumpUp;
    public boolean notifyJumpAbove;
    public boolean notifyJumpJump;
    public boolean notifyTpHistoryBack;
    public boolean notifyTpHistoryOrigin;
    public boolean notifyModFeater;
    public boolean notifyModBone;
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
	logWorldTo = config.getBoolean("log.tp.world-to", false);
	logWorldCoords = config.getBoolean("log.tp.world-coords", false);
	logHere = config.getBoolean("log.tp.here", false);
	logOthers = config.getBoolean("log.tp.others", false);
	logJumpTop = config.getBoolean("log.jump.top", false);
	logJumpUp = config.getBoolean("log.jump.up", false);
	logJumpAbove = config.getBoolean("log.jump.above", false);
	logJumpJump = config.getBoolean("log.jump.jump", false);
	logTpHistoryBack = config.getBoolean("log.tp.history-back", false);
	logTpHistoryOrigin = config.getBoolean("log.tp.history-origin", false);
	logModFeater = config.getBoolean("log.mod-feather", false);
	logModBone = config.getBoolean("log.mod-bone", false);
	notifyTo = config.getBoolean("notify.tp.to", false);
	notifyCoords = config.getBoolean("notify.tp.coords", false);
	notifyWorldTo = config.getBoolean("notify.tp.world-to", false);
	notifyWorldCoords = config.getBoolean("notify.tp.world-coords", false);
	notifyHere = config.getBoolean("notify.tp.here", false);
	notifyOthers = config.getBoolean("notify.tp.others", false);
	notifyJumpTop = config.getBoolean("notify.jump.top", false);
	notifyJumpUp = config.getBoolean("notify.jump.up", false);
	notifyJumpAbove = config.getBoolean("notify.jump.above", false);
	notifyJumpJump = config.getBoolean("notify.jump.jump", false);
	notifyTpHistoryBack = config.getBoolean("notify.tp.history-back", false);
	notifyTpHistoryOrigin = config.getBoolean("notify.tp.history-origin", false);
	notifyModFeater = config.getBoolean("notify.mod-feather", false);
	notifyModBone = config.getBoolean("notify.mod-bone", false);
	throughItems = config.getIntList("settings.through-items", defaultThroughItems);
	settingsFallBlockDistance = config.getInt("glassed.fall-block-distance", 10);
	fallImmunity = config.getBoolean("glassed.fall-immunity", false);
	fallImmunitySeconds = config.getInt("glassed.fall-immunity-seconds", 5);
    }
}
