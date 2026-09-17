package fr.niixoz.survivalcore.listeners;

import fr.niixoz.survivalcore.managers.SleepManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SleepHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();

        if(!SleepManager.isFakeSleeping(player))
            return;

        // Le lit est temporaire : hors de question qu'il devienne le point de respawn.
        event.setSpawnLocation(false);

        SleepManager.handleWakeUp(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        SleepManager.forceRestore(event.getPlayer());
    }
}
