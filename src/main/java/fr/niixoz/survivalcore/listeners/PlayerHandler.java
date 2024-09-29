package fr.niixoz.survivalcore.listeners;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.location.Spawn;
import fr.niixoz.survivalcore.storage.players.SurvivalPlayer;
import fr.niixoz.survivalcore.storage.prefix.ChatIcon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerHandler implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if(!Config.backTeleportCauses.contains(e.getCause()))
            return;
        SurvivalPlayer enderPlayer = SurvivalPlayer.getPlayer(e.getPlayer());
        if(enderPlayer != null) {
            enderPlayer.setLastLocation(e.getFrom());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        SurvivalPlayer.players.add(new SurvivalPlayer(e.getPlayer()));
        if(!SurvivalPlayer.playersUUID.containsKey(e.getPlayer().getName())) {
            SurvivalPlayer.playersUUID.put(e.getPlayer().getName(), e.getPlayer().getUniqueId());
        }
        if(!e.getPlayer().hasPlayedBefore()) {
            e.getPlayer().teleport(Spawn.location);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        SurvivalPlayer enderPlayer = SurvivalPlayer.getPlayer(e.getPlayer());
        if (enderPlayer != null) {
            enderPlayer.saveInfo();
            SurvivalPlayer.players.remove(enderPlayer);
        }
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        SurvivalPlayer player = SurvivalPlayer.getPlayer(e.getEntity());
        if(player != null) {
            if (e.getEntity().hasPermission(PermissionEnum.KEEP_EXP_ON_DEATH.getPermission())) {
                player.setExperience(e.getEntity().getExp());
                player.setLevel(e.getEntity().getLevel());
            }
            if (Config.backOnDeath) {
                player.setLastLocation(e.getEntity().getLocation());
            }
        }
        e.getEntity().teleport(Spawn.location);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(Spawn.location);

        SurvivalCore.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(SurvivalCore.getInstance(), () -> {
            SurvivalPlayer player = SurvivalPlayer.getPlayer(Bukkit.getPlayer(e.getPlayer().getName()));
            if(player != null) {
                Player p = Bukkit.getPlayer(e.getPlayer().getName());
                if(p != null) {
                    p.setExp(player.getExperience());
                    p.setLevel(player.getLevel());
                }
            }
        }, 5);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        for (ChatIcon icon : ChatIcon.icons) {
            if (e.getPlayer().hasPermission(icon.getPermission())) {
                e.setFormat(icon.getCode() + e.getFormat());
                break;
            }
        }
    }
}
