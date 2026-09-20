package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Gère les commandes /lay et /crawl, les deux postures au sol.
 *
 * Principe : c'est le serveur qui choisit la pose d'un joueur en fonction de la
 * place disponible (Player#updatePlayerPose). S'il ne tient ni debout (1.8 bloc)
 * ni accroupi (1.5), mais qu'il tient couché (0.6), Minecraft bascule tout seul
 * sur la pose SWIMMING : celle qu'on voit quand on rampe sous une trappe.
 *
 * On se contente donc de poser une barrière au-dessus de sa tête et de la
 * déplacer avec lui. Tout le reste est vanilla : la pose est calculée côté
 * serveur puis diffusée à tous les clients (le joueur lui-même y compris), et le
 * déplacement au sol garde exactement le comportement du jeu.
 *
 * Pourquoi une barrière : invisible en survie, elle ne bloque pas la lumière et
 * n'a aucun rendu. En contrepartie elle étouffe, d'où l'annulation des dégâts
 * SUFFOCATION dans CosmeticHandler le temps que la pose s'applique.
 *
 * Différence entre les deux modes : en LAY la posture s'arrête au premier
 * déplacement (on s'allonge, on ne se traîne pas), en CRAWL la barrière suit le
 * joueur tant qu'il rampe.
 */
public class GroundPoseManager {

    public enum Mode {
        LAY,
        CRAWL
    }

    public enum Result {
        SUCCESS,
        ALREADY_DOWN,
        IN_VEHICLE,
        NOT_GROUNDED,
        NO_SPACE
    }

    /** Bloc temporaire posé au-dessus de la tête pour forcer la pose couchée. */
    private static final Material CEILING = Material.BARRIER;

    /** Tolérance (en blocs, au carré) avant de considérer qu'un joueur allongé a bougé. */
    public static final double MOVE_THRESHOLD_SQUARED = 0.0004D;

    private static final Map<UUID, State> STATES = new HashMap<>();

    private static BukkitTask task;

    /** Posture en cours d'un joueur, et bloc qu'on lui a emprunté au-dessus de la tête. */
    private static final class State {

        private final Mode mode;

        /** Bloc actuellement remplacé par la barrière, null si le plafond est déjà bas. */
        private Block block;
        /** Contenu d'origine de ce bloc, à remettre en place ensuite. */
        private BlockData previous;

        private State(Mode mode) {
            this.mode = mode;
        }
    }

    private GroundPoseManager() {}

    // ------------------------------------------------------------------ API publique

    public static Result start(Player player, Mode mode) {
        if(isActive(player))
            return Result.ALREADY_DOWN;

        if(player.isInsideVehicle())
            return Result.IN_VEHICLE;

        if(player.getGameMode() == GameMode.SPECTATOR || player.isFlying() || player.isGliding() || !PlayerUtils.isGrounded(player))
            return Result.NOT_GROUNDED;

        Block head = head(player);

        // Pas la place de poser la barrière : soit le joueur rampe déjà sous un
        // vrai plafond bas (rien à faire), soit il y a un bloc qu'on refuse de
        // remplacer et la posture est impossible ici.
        if(!isReplaceable(head) && player.getPose() != Pose.SWIMMING)
            return Result.NO_SPACE;

        State state = new State(mode);
        STATES.put(player.getUniqueId(), state);
        setCeiling(state, head);
        startTask();

        return Result.SUCCESS;
    }

    public static boolean isActive(Player player) {
        return STATES.containsKey(player.getUniqueId());
    }

    public static boolean isMode(Player player, Mode mode) {
        State state = STATES.get(player.getUniqueId());
        return state != null && state.mode == mode;
    }

    /** Relève le joueur et rend son bloc au monde. Sans effet s'il n'est pas au sol. */
    public static boolean stop(Player player) {
        return stop(player.getUniqueId());
    }

    /** À appeler depuis onDisable() pour ne laisser aucune barrière derrière soi. */
    public static void disable() {
        stopTask();
        STATES.values().forEach(GroundPoseManager::restore);
        STATES.clear();
    }

    // ------------------------------------------------------------------ interne

    private static boolean stop(UUID uuid) {
        State state = STATES.remove(uuid);
        if(state == null)
            return false;

        restore(state);

        if(STATES.isEmpty())
            stopTask();

        return true;
    }

    /**
     * La barrière doit rester collée à la tête du joueur : un tick de retard et
     * il se relève. La tâche tourne donc à chaque tick, mais elle n'existe que
     * tant qu'au moins un joueur est au sol.
     *
     * Le scheduler Bukkit passe avant le tick des entités : la barrière est donc
     * déjà à jour quand le serveur recalcule la pose du joueur.
     */
    private static void startTask() {
        if(task != null)
            return;

        task = Bukkit.getScheduler().runTaskTimer(SurvivalCore.getInstance(), () -> {
            if(STATES.isEmpty()) {
                stopTask();
                return;
            }

            // Copie : stop() modifie la map pendant l'itération.
            List<UUID> players = new ArrayList<>(STATES.keySet());

            for(UUID uuid : players) {
                Player player = Bukkit.getPlayer(uuid);

                if(player == null || !canStayDown(player)) {
                    stop(uuid);
                    continue;
                }

                update(player, STATES.get(uuid));
            }
        }, 1L, 1L);
    }

    private static void stopTask() {
        if(task != null) {
            task.cancel();
            task = null;
        }
    }

    /** Conditions qui mettent fin à la posture d'elles-mêmes (vol, monture, mort...). */
    private static boolean canStayDown(Player player) {
        return player.isOnline()
                && !player.isDead()
                && !player.isInsideVehicle()
                && !player.isFlying()
                && !player.isGliding()
                && player.getGameMode() != GameMode.SPECTATOR;
    }

    private static void update(Player player, State state) {
        Block head = head(player);

        if(Objects.equals(state.block, head))
            return;

        setCeiling(state, head);
    }

    /**
     * Déplace la barrière sur le bloc demandé.
     *
     * La nouvelle est posée avant que l'ancienne ne soit retirée : le joueur n'a
     * jamais l'occasion de se relever entre les deux.
     */
    private static void setCeiling(State state, Block head) {
        Block previousBlock = state.block;
        BlockData previousData = state.previous;

        if(isReplaceable(head)) {
            state.block = head;
            state.previous = head.getBlockData();
            // applyPhysics = false : pas d'update de voisinage, donc pas de
            // torche qui saute ni de sable qui tombe autour de la barrière.
            head.setBlockData(CEILING.createBlockData(), false);
        }
        else {
            // Plafond déjà bas (ou bloc qu'on ne veut pas remplacer) : la pose
            // tient toute seule, on ne pose rien.
            state.block = null;
            state.previous = null;
        }

        if(previousBlock != null && !previousBlock.equals(state.block))
            restore(previousBlock, previousData);
    }

    private static void restore(State state) {
        if(state.block != null)
            restore(state.block, state.previous);
    }

    private static void restore(Block block, BlockData previous) {
        // Le bloc a pu être remplacé entre-temps (piston, autre plugin...) :
        // on ne rend son contenu d'origine que si notre barrière est encore là.
        if(block.getType() == CEILING)
            block.setBlockData(previous, false);
    }

    /** Bloc occupé par la tête du joueur, celui qu'on remplace par la barrière. */
    private static Block head(Player player) {
        return player.getLocation().getBlock().getRelative(BlockFace.UP);
    }

    private static boolean isReplaceable(Block block) {
        if(block.getY() >= block.getWorld().getMaxHeight())
            return false;

        // Les liquides sont exclus : une barrière dans l'eau assèche la colonne
        // et la restauration ne rendrait pas les courants.
        if(block.isLiquid())
            return false;

        return block.getType().isAir() || Tag.REPLACEABLE.isTagged(block.getType());
    }
}
