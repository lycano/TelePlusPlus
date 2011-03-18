package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import net.sacredlabyrinth.Phaed.TelePlusPlus.TelePlusPlus;

import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijiko.permissions.PermissionHandler;

import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.plugin.Plugin;

import org.bukkit.entity.Player;
public class PermissionsManager
{
    public static PermissionHandler Permissions = null;
    public GroupManager gm;
    private TelePlusPlus plugin;
    
    public final String menu = "tpp.tp.menu";    
    public final String to = "tpp.tp.to";
    public final String coords = "tpp.tp.coords";
    public final String here = "tpp.tp.here";
    public final String others = "tpp.tp.others";
    public final String back = "tpp.tp.back";
    public final String origin = "tpp.tp.origin";
    public final String clear = "tpp.tp.clear";
    public final String worldTo = "tpp.world.to";
    public final String worldCoords = "tpp.world.coords";
    public final String top = "tpp.jump.top";
    public final String up = "tpp.jump.up";
    public final String jump = "tpp.jump.jump";
    public final String above = "tpp.mod.above";
    public final String feather = "tpp.mod.feather";
    public final String bone = "tpp.mod.bone";    
    public final String noTp = "tpp.mod.notp";    
    public final String notify = "tpp.mod.notify";    
    public final String bypassLog = "tpp.admin.bypass.log";    
    public final String bypassNotify = "tpp.admin.bypass.notify";    
    public final String bypassNoTp = "tpp.admin.bypass.notp";    
    
    public PermissionsManager(TelePlusPlus plugin)
    {
	this.plugin = plugin;
	
	startGroupManager();
	startPermissions();
    }
    
    public boolean hasPermission(Player player, String permission)
    {
	if (player == null)
	{
	    return false;
	}

	if (hasPermissionPlugin())
	{
	    return (Permissions != null && Permissions.has(player, permission)) || (gm != null && gm.getWorldsHolder().getWorldPermissions(player).has(player, permission));
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
	return gm != null || Permissions != null;
    }
    
    public void startGroupManager()
    {
	if (gm == null)
	{
	    Plugin p = plugin.getServer().getPluginManager().getPlugin("GroupManager");
	    
	    if (p != null)
	    {
		if (!plugin.getServer().getPluginManager().isPluginEnabled(p))
		{
		    plugin.getServer().getPluginManager().enablePlugin(p);
		}
		gm = (GroupManager) p;
	    }
	}
    }
    
    public void startPermissions()
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
