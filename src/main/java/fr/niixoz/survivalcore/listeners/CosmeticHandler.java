package fr.niixoz.survivalcore.listeners;

import fr.niixoz.survivalcore.managers.CosmeticManager;
import fr.niixoz.survivalcore.managers.GroundPoseManager;
import fr.niixoz.survivalcore.managers.SeatManager;
import fr.niixoz.survivalcore.managers.SpinManager;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Nettoyage et garde-fous des cosmétiques de posture (/sit, /lay, /crawl, /spin).
 *
 * Toutes ces postures posent quelque chose de temporaire dans le monde (un siège
 * invisible, une barrière au-dessus de la tête) : il faut donc les couper dès que
 * le joueur sort du cadre prévu, sous peine de laisser ces objets derrière lui.
 */
public class CosmeticHandler implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CosmeticManager.stopAll(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        CosmeticManager.stopAll(event.getEntity());
    }

    /**
     * Priorité LOWEST : le joueur doit avoir quitté son siège avant que la
     * téléportation ne parte, sinon elle échoue (on ne téléporte pas un passager).
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(PlayerTeleportEvent event) {
        Location to = event.getTo();
        if(to == null)
            return;

        // La descente d'un siège téléporte le joueur d'un demi-bloc : inutile de
        // réagir à nos propres mouvements.
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.DISMOUNT)
            return;

        if(event.getFrom().getWorld() != to.getWorld() || event.getFrom().distanceSquared(to) > 4D)
            CosmeticManager.stopAll(event.getPlayer());
    }

    /** Descente du siège à l'accroupissement : c'est le comportement vanilla des montures. */
    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if(!(event.getEntity() instanceof Player player))
            return;

        if(!SeatManager.isSeat(event.getDismounted()))
            return;

        SeatManager.stand(player);
    }

    /** /lay s'arrête au premier déplacement : on s'allonge, on ne se traîne pas. */
    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(!GroundPoseManager.isMode(player, GroundPoseManager.Mode.LAY))
            return;

        Location to = event.getTo();
        if(to == null)
            return;

        // PlayerTeleportEvent hérite de PlayerMoveEvent : les deux positions
        // peuvent être dans des mondes différents, ce que distanceSquared refuse.
        if(event.getFrom().getWorld() == to.getWorld()
                && event.getFrom().distanceSquared(to) <= GroundPoseManager.MOVE_THRESHOLD_SQUARED)
            return;  // rotation de la tête ou micro-glissement

        GroundPoseManager.stop(player);
        MessageUtils.sendPlayerMessage(player, "§aTu te relèves.");
    }

    /**
     * La barrière posée au-dessus de la tête étouffe le temps que le serveur
     * bascule le joueur en position couchée : ces dégâts sont de notre fait.
     */
    @EventHandler(ignoreCancelled = true)
    public void onSuffocate(EntityDamageEvent event) {
        if(event.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION)
            return;

        if(event.getEntity() instanceof Player player && GroundPoseManager.isActive(player))
            event.setCancelled(true);
    }

    /**
     * Le riptide vanilla frappe tout ce qu'il touche pendant la vrille. /spin
     * étant purement cosmétique, on neutralise ses dégâts.
     */
    @EventHandler(ignoreCancelled = true)
    public void onSpinTouch(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player && SpinManager.isSpinning(player))
            event.setCancelled(true);
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if(event.getNewGameMode() == GameMode.SPECTATOR)
            CosmeticManager.stopAll(event.getPlayer());
    }
}
