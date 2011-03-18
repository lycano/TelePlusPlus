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
