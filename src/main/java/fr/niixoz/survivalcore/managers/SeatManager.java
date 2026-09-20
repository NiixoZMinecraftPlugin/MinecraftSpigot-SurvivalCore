package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gère la commande /sit.
 *
 * Minecraft n'expose aucune "pose assise" applicable à un joueur : la seule
 * façon d'obtenir l'animation vanilla est de le faire monter sur une entité.
 * On lui pose donc sous les fesses un armor stand marker, c'est à dire sans
 * hitbox du tout : invisible, insensible aux flèches, aux clics et aux
 * collisions. Le client fait le reste, exactement comme sur un cochon ou un
 * bateau, et tout le monde (le joueur compris) voit la position assise.
 */
public class SeatManager {

    public enum Result {
        SUCCESS,
        ALREADY_SITTING,
        IN_VEHICLE,
        NOT_GROUNDED,
        FAILED
    }

    /**
     * Décalage vertical du siège par rapport aux pieds du joueur.
     *
     * Un armor stand marker a une hauteur nulle : le point d'accroche du passager
     * est donc exactement la position du siège. On l'enfonce légèrement pour que
     * le joueur paraisse posé sur le bloc plutôt que flottant au-dessus.
     * C'est la seule valeur à retoucher si le rendu ne vous convient pas.
     */
    private static final double SEAT_OFFSET = -0.2D;

    private static final Map<UUID, ArmorStand> SEATS = new HashMap<>();

    private SeatManager() {}

    // ------------------------------------------------------------------ API publique

    public static Result sit(Player player) {
        if(isSitting(player))
            return Result.ALREADY_SITTING;

        if(player.isInsideVehicle())
            return Result.IN_VEHICLE;

        if(player.getGameMode() == GameMode.SPECTATOR || player.isFlying() || player.isGliding() || !PlayerUtils.isGrounded(player))
            return Result.NOT_GROUNDED;

        Location location = player.getLocation();
        Location seatLocation = new Location(
                player.getWorld(),
                location.getBlockX() + 0.5D,
                location.getY() + SEAT_OFFSET,
                location.getBlockZ() + 0.5D,
                location.getYaw(),
                0f);

        // Le consumer configure l'entité AVANT son apparition : aucun client ne
        // voit jamais l'armor stand, même le temps d'un tick.
        ArmorStand seat = player.getWorld().spawn(seatLocation, ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setBasePlate(false);
            stand.setArms(false);
            stand.setMarker(true);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setSilent(true);
            stand.setCollidable(false);
            stand.setCanPickupItems(false);
            // Jamais écrit sur le disque : pas de siège fantôme après un crash.
            stand.setPersistent(false);
        });

        if(!seat.addPassenger(player)) {
            seat.remove();
            return Result.FAILED;
        }

        SEATS.put(player.getUniqueId(), seat);
        return Result.SUCCESS;
    }

    public static boolean isSitting(Player player) {
        return SEATS.containsKey(player.getUniqueId());
    }

    /** true si l'entité est un siège du plugin (cf. EntityDismountEvent). */
    public static boolean isSeat(Entity entity) {
        return entity instanceof ArmorStand && SEATS.containsValue(entity);
    }

    /**
     * Relève le joueur et supprime son siège.
     *
     * L'entrée est retirée de la map AVANT l'éjection : celle-ci redéclenche un
     * EntityDismountEvent, qui rappellera cette méthode sans rien trouver.
     */
    public static boolean stand(Player player) {
        ArmorStand seat = SEATS.remove(player.getUniqueId());
        if(seat == null)
            return false;

        removeSeat(seat);
        return true;
    }

    /** À appeler depuis onDisable() pour ne laisser aucun siège derrière soi. */
    public static void disable() {
        SEATS.values().forEach(SeatManager::removeSeat);
        SEATS.clear();
    }

    // ------------------------------------------------------------------ interne

    private static void removeSeat(ArmorStand seat) {
        seat.eject();
        seat.remove();
    }
}
