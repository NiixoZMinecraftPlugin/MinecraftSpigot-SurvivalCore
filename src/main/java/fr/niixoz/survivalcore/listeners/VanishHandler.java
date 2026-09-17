package fr.niixoz.survivalcore.listeners;

import fr.niixoz.survivalcore.managers.VanishManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Iterator;

public class VanishHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        VanishManager.handleJoin(e.getPlayer());

        if (VanishManager.isVanished(e.getPlayer())) {
            e.setJoinMessage(null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (VanishManager.isVanished(e.getPlayer())) {
            e.setQuitMessage(null);
        }
    }

    /** Retire les joueurs en vanish de l'aperçu des joueurs connectés dans la liste des serveurs. */
    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        try {
            Iterator<Player> iterator = e.iterator();
            while (iterator.hasNext()) {
                if (VanishManager.isVanished(iterator.next())) {
                    iterator.remove();
                }
            }
        } catch (UnsupportedOperationException ignored) {
            // Le client qui ping n'attend pas d'aperçu des joueurs.
        }
    }
}