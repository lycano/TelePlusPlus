package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import java.util.List;
import java.util.HashSet;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.World;

public class BonedManager
{
    private HashMap<String, HashSet<Integer>> bonedEntities = new HashMap<String, HashSet<Integer>>();
    private HashMap<String, Location> bonedBlock = new HashMap<String, Location>();
    private boolean entityDirty = false;
    
    public void setEntitiesDirty()
    {
	entityDirty = true;
    }
    
    public boolean addBonedEntity(Player player, Entity entity)
    {
	if (entityDirty)
	{
	    clearBonedEntities(player);
	    entityDirty = false;
	}
	
	clearBonedBlock(player);
	
	if (bonedEntities.containsKey(player.getName()))
	{
	    HashSet<Integer> list = bonedEntities.get(player.getName());
	    
	    if (list.contains(entity.getEntityId()))
	    {
		return false;
	    }
	    list.add(entity.getEntityId());
	}
	else
	{
	    HashSet<Integer> list = new HashSet<Integer>();
	    list.add(entity.getEntityId());
	    bonedEntities.put(player.getName(), list);
	}
	
	return true;
    }
    
    public boolean addBonedBlock(Player player, Block block)
    {
	if (entityDirty || !bonedEntities.containsKey(player.getName()))
	{
	    clearBonedEntities(player);
	    
	    bonedBlock.put(player.getName(), block.getLocation());
	    return true;
	}
	
	return false;
    }
    
    public void relocateBonedBlock(Player player, Block block)
    {
	if (bonedBlock.containsKey(player.getName()))
	{
	    bonedBlock.put(player.getName(), block.getLocation());
	}
    }
    
    public boolean clearBonedBlock(Player player)
    {
	if (bonedBlock.containsKey(player.getName()))
	{
	    bonedBlock.remove(player.getName());	    
	    return true;
	}	
	return false;
    }
    
    public boolean clearBonedEntities(Player player)
    {
	if (bonedEntities.containsKey(player.getName()))
	{
	    bonedEntities.remove(player.getName());
	    return true;
	}
	return false;
    }
    
    public int getEntityCount(Player player)
    {
	if (bonedEntities.containsKey(player.getName()))
	{
	    return bonedEntities.get(player.getName()).size();
	}
	
	return 0;
    }
    
    public HashSet<Entity> getBonedEntities(Player player)
    {
	HashSet<Entity> out = new HashSet<Entity>();
	
	if (!bonedEntities.containsKey(player.getName()))
	{
	    return out;
	}
	
	HashSet<Integer> list = bonedEntities.get(player.getName());
	
	for (int entityid : list)
	{
	    Entity ent = getEntityById(player.getWorld(), entityid);
	    
	    if (ent != null)
	    {
		out.add(ent);
	    }
	}
	
	return out;
    }
    
    public Block getBonedBlock(Player player)
    {
	if (!bonedBlock.containsKey(player.getName()))
	{
	    return null;
	}
	return player.getWorld().getBlockAt(bonedBlock.get(player.getName()));
    }
    
    private Entity getEntityById(World world, int id)
    {
	List<Entity> entities = world.getEntities();
	
	for (Entity entity : entities)
	{
	    if (entity.getEntityId() == id)
	    {
		return entity;
	    }
	}
	
	return null;
    }
}
