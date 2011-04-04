package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijiko.permissions.PermissionHandler;

import org.bukkit.plugin.Plugin;

import org.bukkit.entity.Player;

public class PermissionsManager
{
    public static PermissionHandler Permissions = null;
    private TelePlusPlus plugin;
    
    public final String menu = "tpp.tp.menu";    
    public final String player = "tpp.tp.player";
    public final String coords = "tpp.tp.coords";
    public final String here = "tpp.tp.here";
    public final String toggle = "tpp.tp.toggle";
    public final String back = "tpp.tp.back";
    public final String origin = "tpp.tp.origin";
    public final String clear = "tpp.tp.clear";
    public final String othersPlayer = "tpp.others.player";
    public final String othersCoords = "tpp.others.coords";
    public final String world = "tpp.world.tp";
    public final String top = "tpp.jump.top";
    public final String up = "tpp.jump.up";
    public final String jump = "tpp.jump.jump";
    public final String above = "tpp.mod.above";
    public final String mass = "tpp.mod.mass";
    public final String tool = "tpp.mod.tool";
    public final String mover = "tpp.mod.mover";    
    public final String noTp = "tpp.mod.notp";    
    public final String notify = "tpp.mod.notify";
    public final String take = "tpp.mod.take";
    public final String bypassLog = "tpp.admin.bypass.log";    
    public final String bypassNotify = "tpp.admin.bypass.notify";    
    public final String bypassNoTp = "tpp.admin.bypass.notp";
    public final String request = "tpp.request";
    
    public PermissionsManager(TelePlusPlus plugin)
    {
	this.plugin = plugin;
	
	startoolmissions();
    }
    
    public boolean hasPermission(Player player, String permission)
    {
	if (player == null)
	{
	    return false;
	}

	if (hasPermissionPlugin())
	{
	    return (Permissions != null && Permissions.has(player, permission));
	}
	else
	{
	    if (player.isOp())
	    {
		return true;
	    }
	    else
	    {
		if (permission.contains("admin"))
		{
		    return false;
		}
		
		if (permission.contains("mod"))
		{
		    return false;
		}
		
		return true;
	    }
	}	
    }
    
    private boolean hasPermissionPlugin()
    {
	return Permissions != null;
    }
    
    public void startoolmissions()
    {
	if (PermissionsManager.Permissions == null)
	{
	    Plugin test = plugin.getServer().getPluginManager().getPlugin("Permissions");
	    
	    if (test != null)
	    {
		if (!plugin.getServer().getPluginManager().isPluginEnabled(test))
		{
		    plugin.getServer().getPluginManager().enablePlugin(test);
		}
		
		PermissionsManager.Permissions = ((Permissions) test).getHandler();
	    }
	}
    }
}
