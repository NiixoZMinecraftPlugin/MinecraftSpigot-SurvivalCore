package fr.niixoz.survivalcore.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MobProtectionHandler implements Listener {

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        if(e.getEntityType() == EntityType.DOLPHIN) {
            LivingEntity livingEntity = (LivingEntity) e.getEntity();
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, PotionEffect.INFINITE_DURATION, 1, false, false));
        }
    }

    @EventHandler
    public void onMobDamage(EntityDamageByBlockEvent e) {
        if(e.getEntityType() == EntityType.CAMEL && e.getCause() == EntityDamageEvent.DamageCause.CONTACT && e.getDamager().getType() == Material.CACTUS) {
            e.setCancelled(true);
        }
    }
}
