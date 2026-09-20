package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.SurvivalCore;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Gère la commande /spin, la vrille du trident (pose SPIN_ATTACK).
 *
 * HumanEntity#startRiptideAttack déclenche exactement l'animation vanilla du
 * riptide, sans l'impulsion de vitesse que donne le trident : le joueur tourne
 * sur lui-même et garde ses déplacements. La durée demandée est courte et
 * réamorcée en boucle, ce qui permet d'arrêter la vrille immédiatement quand le
 * joueur refait la commande (l'API n'expose aucun "stopRiptide").
 *
 * Le drapeau de vrille est une donnée synchronisée par le serveur : tout le
 * monde voit l'animation, y compris le joueur en vue à la troisième personne.
 */
public class SpinManager {

    /** Durée demandée à chaque relance, en ticks. */
    private static final int SPIN_DURATION = 30;
    /** Période de relance : plus courte que la durée, pour ne jamais laisser de trou. */
    private static final long REFRESH_PERIOD = 20L;

    /**
     * Le riptide vanilla a besoin d'un item pour la source de dégâts. On lui
     * donne un trident sans l'imposer dans la main du joueur : c'est l'item
     * réellement tenu qui reste affiché pendant la vrille.
     */
    private static final ItemStack TRIDENT = new ItemStack(Material.TRIDENT);

    private static final Set<UUID> SPINNING = new HashSet<>();

    private static BukkitTask task;

    private SpinManager() {}

    // ------------------------------------------------------------------ API publique

    public static boolean start(Player player) {
        if(player.getGameMode() == GameMode.SPECTATOR)
            return false;

        SPINNING.add(player.getUniqueId());
        spin(player, SPIN_DURATION);
        startTask();

        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.PLAYERS, 0.7f, 1f);
        return true;
    }

    public static boolean isSpinning(Player player) {
        return SPINNING.contains(player.getUniqueId());
    }

    /** Arrête la vrille. La relance sur 1 tick sert de "stop" : elle expire au tick suivant. */
    public static boolean stop(Player player) {
        if(!SPINNING.remove(player.getUniqueId()))
            return false;

        if(player.isOnline() && !player.isDead())
            spin(player, 1);

        if(SPINNING.isEmpty())
            stopTask();

        return true;
    }

    /** À appeler depuis onDisable() pour ne laisser personne tourner dans le vide. */
    public static void disable() {
        stopTask();

        for(UUID uuid : new ArrayList<>(SPINNING)) {
            Player player = Bukkit.getPlayer(uuid);
            if(player != null && !player.isDead())
                spin(player, 1);
        }

        SPINNING.clear();
    }

    // ------------------------------------------------------------------ interne

    private static void startTask() {
        if(task != null)
            return;

        task = Bukkit.getScheduler().runTaskTimer(SurvivalCore.getInstance(), () -> {
            if(SPINNING.isEmpty()) {
                stopTask();
                return;
            }

            // Copie : stop() modifie l'ensemble pendant l'itération.
            List<UUID> players = new ArrayList<>(SPINNING);

            for(UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);

                if(player == null || player.isDead() || player.getGameMode() == GameMode.SPECTATOR) {
                    SPINNING.remove(uuid);
                    continue;
                }

                spin(player, SPIN_DURATION);
            }
        }, REFRESH_PERIOD, REFRESH_PERIOD);
    }

    private static void stopTask() {
        if(task != null) {
            task.cancel();
            task = null;
        }
    }

    /** Dégâts à 0 : la vrille est cosmétique (cf. aussi CosmeticHandler). */
    private static void spin(Player player, int duration) {
        player.startRiptideAttack(duration, 0f, TRIDENT);
    }
}
