package fr.niixoz.survivalcore.managers;

import org.bukkit.entity.Player;

/**
 * Point d'entrée commun aux cosmétiques de posture : /sit, /lay, /crawl et /spin.
 *
 * Les quatre sont exclusifs (on ne rampe pas assis sur un tabouret invisible) et
 * doivent tous être coupés aux mêmes moments : déconnexion, mort, téléportation,
 * passage en spectateur. Ce manager centralise ces deux besoins pour que les
 * commandes et CosmeticHandler n'aient pas à connaître chaque mécanique.
 */
public class CosmeticManager {

    private CosmeticManager() {}

    public static boolean hasCosmetic(Player player) {
        return SeatManager.isSitting(player)
                || GroundPoseManager.isActive(player)
                || SpinManager.isSpinning(player);
    }

    /** Coupe tout ce que le joueur a en cours. Sans effet s'il n'a rien. */
    public static void stopAll(Player player) {
        SeatManager.stand(player);
        GroundPoseManager.stop(player);
        SpinManager.stop(player);
    }

    /** À appeler depuis onDisable() : siège, barrière et vrille sont tous temporaires. */
    public static void disable() {
        SeatManager.disable();
        GroundPoseManager.disable();
        SpinManager.disable();
    }
}
