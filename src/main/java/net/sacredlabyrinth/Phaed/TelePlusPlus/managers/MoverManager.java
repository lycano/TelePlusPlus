package net.sacredlabyrinth.Phaed.TelePlusPlus.managers;

import java.util.List;
import java.util.HashSet;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.World;

public class MoverManager {
    private HashMap<String, HashSet<Integer>> MovedEntities = new HashMap<String, HashSet<Integer>>();
    private HashMap<String, Location> MovedBlock = new HashMap<String, Location>();
    private boolean entityDirty = false;
    
    public void setEntitiesDirty() {
        entityDirty = true;
    }
    
    public boolean addMovedEntity(Player player, Entity entity) {
        if (entityDirty) {
            clearMovedEntities(player);
            entityDirty = false;
        }

        clearMovedBlock(player);

        if (MovedEntities.containsKey(player.getName())) {
            HashSet<Integer> list = MovedEntities.get(player.getName());

            if (list.contains(entity.getEntityId())) {
                return false;
            }
            list.add(entity.getEntityId());
        } else {
            HashSet<Integer> list = new HashSet<Integer>();
            
            list.add(entity.getEntityId());
            MovedEntities.put(player.getName(), list);
        }

        return true;
    }
    
    public boolean addMovedBlock(Player player, Block block) {
        if (entityDirty || !MovedEntities.containsKey(player.getName())) {
            clearMovedEntities(player);

            MovedBlock.put(player.getName(), block.getLocation());
            return true;
        }

        return false;
    }
    
    public void relocateMovedBlock(Player player, Block block) {
        if (MovedBlock.containsKey(player.getName())) {
            MovedBlock.put(player.getName(), block.getLocation());
        }
    }
    
    public boolean clearMovedBlock(Player player) {
        if (MovedBlock.containsKey(player.getName())) {
            MovedBlock.remove(player.getName());        
            return true;
        }
        
        return false;
    }
    
    public boolean clearMovedEntities(Player player) {
        if (MovedEntities.containsKey(player.getName())) {
            MovedEntities.remove(player.getName());
            return true;
        }
        
        return false;
    }
    
    public int getEntityCount(Player player) {
        if (MovedEntities.containsKey(player.getName())) {
            return MovedEntities.get(player.getName()).size();
        }

        return 0;
    }
    
    public HashSet<Entity> getMovedEntities(Player player) {
        HashSet<Entity> out = new HashSet<Entity>();

        if (!MovedEntities.containsKey(player.getName())) {
            return out;
        }

        HashSet<Integer> list = MovedEntities.get(player.getName());

        for (int entityid : list) {
            Entity ent = getEntityById(player.getWorld(), entityid);

            if (ent != null) {
                out.add(ent);
            }
        }

        return out;
    }
    
    public Block getMovedBlock(Player player) {
        if (!MovedBlock.containsKey(player.getName())) {
            return null;
        }
        
        return player.getWorld().getBlockAt(MovedBlock.get(player.getName()));
    }
    
    private Entity getEntityById(World world, int id) {
        List<Entity> entities = world.getEntities();

        for (Entity entity : entities) {
            if (entity.getEntityId() == id) {
                return entity;
            }
        }

        return null;
    }
}