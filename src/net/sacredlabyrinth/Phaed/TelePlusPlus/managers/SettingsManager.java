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
    public boolean logTool;
    public boolean logMover;
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
    public boolean notifyTool;
    public boolean notifyMover;
    public boolean notifyRequest;
    
    public boolean sayPlayer;
    public boolean sayCoords;
    public boolean sayWorld;
    public boolean sayHere;
    public boolean sayMass;
    public boolean sayTop;
    public boolean sayUp;
    public boolean sayOthersPlayer;
    public boolean sayOthersCoords;
    public boolean sayAbove;
    public boolean sayToggle;
    public boolean sayJump;
    public boolean sayBack;
    public boolean sayOrigin;
    public boolean sayTool;
    public boolean sayMover;
    public boolean sayRequest;
    
    public boolean disablePlayer;
    public boolean disableCoords;
    public boolean disableWorld;
    public boolean disableHere;
    public boolean disableMass;
    public boolean disableTop;
    public boolean disableUp;
    public boolean disableOthersPlayer;
    public boolean disableOthersCoords;
    public boolean disableAbove;
    public boolean disableToggle;
    public boolean disableJump;
    public boolean disableClear;
    public boolean disableBack;
    public boolean disableOrigin;
    public boolean disableTool;
    public boolean disableMover;
    public boolean disableRequest;
    
    public int settingsFallBlockDistance;
    public List<Integer> throughBlocks;
    public long fallImmunitySeconds;
    public boolean fallImmunity;
    public int purgeRequestMinutes;
    public int moverItem;
    public int toolItem;
    
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
	logTool = config.getBoolean("log.mod.tool", false);
	logMover = config.getBoolean("log.mod.mover", false);
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
	notifyTool = config.getBoolean("notify.mod.tool", false);
	notifyMover = config.getBoolean("notify.mod.mover", false);
	notifyRequest = config.getBoolean("notify.request", false);
	
	sayPlayer = config.getBoolean("say.tp.player", false);
	sayCoords = config.getBoolean("say.tp.coords", false);
	sayHere = config.getBoolean("say.tp.here", false);
	sayToggle = config.getBoolean("say.tp.toggle", false);
	sayBack = config.getBoolean("say.tp.back", false);
	sayOrigin = config.getBoolean("say.tp.origin", false);
	sayOthersPlayer = config.getBoolean("say.others.player", false);
	sayOthersCoords = config.getBoolean("say.others.coords", false);
	sayWorld = config.getBoolean("say.world.tp", false);
	sayTop = config.getBoolean("say.jump.top", false);
	sayUp = config.getBoolean("say.jump.up", false);
	sayJump = config.getBoolean("say.jump.jump", false);
	sayAbove = config.getBoolean("say.mod.above", false);
	sayMass = config.getBoolean("say.mod.mass", false);
	sayTool = config.getBoolean("say.mod.tool", false);
	sayMover = config.getBoolean("say.mod.mover", false);
	sayRequest = config.getBoolean("say.request", false);
	
	disablePlayer = config.getBoolean("disable.tp.player", false);
	disableCoords = config.getBoolean("disable.tp.coords", false);
	disableHere = config.getBoolean("disable.tp.here", false);
	disableToggle = config.getBoolean("disable.tp.toggle", false);
	disableClear = config.getBoolean("disable.tp.clear", false);
	disableBack = config.getBoolean("disable.tp.back", false);
	disableOrigin = config.getBoolean("disable.tp.origin", false);
	disableOthersPlayer = config.getBoolean("disable.others.player", false);
	disableOthersCoords = config.getBoolean("disable.others.coords", false);
	disableWorld = config.getBoolean("disable.world.tp", false);
	disableTop = config.getBoolean("disable.jump.top", false);
	disableUp = config.getBoolean("disable.jump.up", false);
	disableJump = config.getBoolean("disable.jump.jump", false);
	disableAbove = config.getBoolean("disable.mod.above", false);
	disableMass = config.getBoolean("disable.mod.mass", false);
	disableTool = config.getBoolean("disable.mod.tool", false);
	disableMover = config.getBoolean("disable.mod.mover", false);
	disableRequest = config.getBoolean("disable.request", false);
	
	settingsFallBlockDistance = config.getInt("glassed.fall-block-distance", 10);
	fallImmunity = config.getBoolean("glassed.fall-immunity", false);
	
	fallImmunitySeconds = config.getInt("glassed.fall-immunity-seconds", 5);
	throughBlocks = config.getIntList("settings.through-blocks", defaultThroughBlocks);
	purgeRequestMinutes = config.getInt("settings.purge-requests-minutes", 5);
	
	moverItem = config.getInt("settings.mover-item", 346);
	toolItem = config.getInt("settings.tool-item", 288);
    }
}
