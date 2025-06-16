package fr.niixoz.survivalcore.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.Iterator;
import java.util.List;

public class PlayerUtils {

    public static Entity getEntityLookingAt(Player player, int range) {
        List<Entity> entities = player.getNearbyEntities(range, range, range);
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity next = iterator.next();
            if (!(next instanceof LivingEntity) || next == player) {
                iterator.remove();
            }
        }
        List<Block> sight = player.getLineOfSight(null, range);
        for (Block block : sight) {
            if (block.getType() != Material.AIR) {
                break;
            }
            Location low = block.getLocation();
            BoundingBox blockBox = BoundingBox.of(low, low.clone().add(1, 1, 1));

            for(Entity entity : entities) {
                if(entity.getBoundingBox().overlaps(blockBox)) {
                    return entity;
                }
            }

        }
        return null;
    }

}
