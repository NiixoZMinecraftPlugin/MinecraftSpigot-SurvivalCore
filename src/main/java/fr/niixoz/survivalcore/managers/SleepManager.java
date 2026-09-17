package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.utils.SilentBlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Gère le sommeil "sans lit" de la commande /sleep.
 *
 * Principe : HumanEntity#sleep exige un vrai lit posé à la location. On en pose
 * donc un temporairement sous le joueur, on force le sommeil, puis on masque le
 * lit côté client. À partir de là c'est la logique vanilla qui prend le relais :
 * le joueur est compté dans playersSleepingPercentage, la nuit passe, l'orage
 * s'arrête et la stat TIME_SINCE_REST (phantoms) est remise à zéro.
 */
public class SleepManager {

    public enum Result {
        SUCCESS,
        WRONG_WORLD,
        NOT_NIGHT,
        ALREADY_SLEEPING,
        NOT_GROUNDED,
        NO_SPACE,
        NO_FLOOR,
        FAILED
    }

    /** false = le lit reste visible (rendu parfaitement fiable, mais on voit un lit). */
    private static final boolean HIDE_BED = true;
    /** Rayon (en blocs) dans lequel on envoie le faux bloc aux autres joueurs. */
    private static final int FAKE_BLOCK_RADIUS = 96;

    private static final long NIGHT_START = 12542L;
    private static final long NIGHT_END = 23459L;

    private static final Map<UUID, FakeBed> BEDS = new HashMap<>();

    private static BukkitTask hideTask;

    private record FakeBed(Block foot, BlockData footOld, Block head, BlockData headOld) {}

    private SleepManager() {}

    // ------------------------------------------------------------------ API publique

    public static Result sleep(Player player) {
        World world = player.getWorld();

        if(world.getEnvironment() != World.Environment.NORMAL)
            return Result.WRONG_WORLD;

        // Forcer le sommeil en journée ne sert à rien : le serveur réveille le
        // joueur au tick suivant (cf. SPIGOT-4666).
        if(!isNight(world) && !world.isThundering())
            return Result.NOT_NIGHT;

        if(player.isSleeping() || BEDS.containsKey(player.getUniqueId()))
            return Result.ALREADY_SLEEPING;

        if(player.getGameMode() == GameMode.SPECTATOR || player.isInsideVehicle() || !player.isOnGround())
            return Result.NOT_GROUNDED;

        BlockFace facing = yawToFace(player.getLocation().getYaw());
        Block foot = player.getLocation().getBlock();
        Block head = foot.getRelative(facing);

        if(!isReplaceable(foot) || !isReplaceable(head))
            return Result.NO_SPACE;

        if(!foot.getRelative(BlockFace.DOWN).getType().isSolid())
            return Result.NO_FLOOR;

        BlockData footOld = foot.getBlockData();
        BlockData headOld = head.getBlockData();

        // occupied=true dès la pose : quand sleep() voudra passer le lit à
        // occupied=true, l'état sera déjà identique. LevelChunk#setBlockState
        // compare les états, ne voit aucun changement et ne notifie donc rien.
        // Sans ça, ce second passage rediffuserait le lit malgré la pose silencieuse.
        String bed = "minecraft:red_bed[facing=" + facing.name().toLowerCase(Locale.ROOT) + ",occupied=true,part=";
        BlockData footData = Bukkit.createBlockData(bed + "foot]");
        BlockData headData = Bukkit.createBlockData(bed + "head]");

        boolean silent = SilentBlockUtil.setSilently(foot, footData);
        silent &= SilentBlockUtil.setSilently(head, headData);

        if(!silent) {
            // Repli : le lit sera visible pendant un tick ou deux.
            // applyPhysics = false : évite que le lit se casse seul ou déclenche des updates
            foot.setBlockData(footData, false);
            head.setBlockData(headData, false);
        }

        // force = true : ignore la proximité du lit, les monstres alentour, etc.
        if(!player.sleep(foot.getLocation(), true)) {
            foot.setBlockData(footOld, false);
            head.setBlockData(headOld, false);
            return Result.FAILED;
        }

        FakeBed fake = new FakeBed(foot, footOld, head, headOld);
        BEDS.put(player.getUniqueId(), fake);

        if(HIDE_BED)
            startHideTask();

        return Result.SUCCESS;
    }

    /** true si ce joueur dort via /sleep (et pas dans un vrai lit). */
    public static boolean isFakeSleeping(Player player) {
        return BEDS.containsKey(player.getUniqueId());
    }

    /** Appelé au réveil : reset des phantoms puis restauration différée des blocs. */
    public static void handleWakeUp(Player player) {
        FakeBed fake = BEDS.remove(player.getUniqueId());
        if(fake == null)
            return;

        // Vanilla remet déjà TIME_SINCE_REST à 0 quand la nuit est passée ;
        // ce filet de sécurité garantit que les phantoms sont bien reset.
        if(player.getWorld().getTime() < NIGHT_START)
            player.setStatistic(Statistic.TIME_SINCE_REST, 0);

        // Restauration dans le MÊME tick que le réveil. Vanilla vient de repasser
        // le lit à occupied=false, ce qui a marqué la position comme modifiée :
        // la diffusion de fin de tick relira l'état courant, donc le bloc
        // d'origine. Différer d'un tick laisserait au contraire apparaître le lit.
        restore(fake);
    }

    /** Restauration immédiate (déconnexion, kick). */
    public static void forceRestore(Player player) {
        FakeBed fake = BEDS.remove(player.getUniqueId());
        if(fake != null)
            restore(fake);
    }

    /** À appeler depuis onDisable() pour ne laisser aucun lit fantôme derrière soi. */
    public static void disable() {
        stopHideTask();
        BEDS.values().forEach(SleepManager::restore);
        BEDS.clear();
    }

    // ------------------------------------------------------------------ interne

    /**
     * Filet de sécurité : un joueur qui entre dans la zone ou qui recharge sa
     * chunk reçoit les données réelles, donc le lit. On lui renvoie donc
     * régulièrement le bloc d'origine.
     *
     * Le délai de 2 ticks sert au repli sans NMS : le scheduler Bukkit tourne
     * AVANT le tick du monde, donc un délai de 1 enverrait encore le faux bloc
     * avant la diffusion du vrai.
     */
    private static void startHideTask() {
        if(hideTask != null)
            return;

        hideTask = Bukkit.getScheduler().runTaskTimer(SurvivalCore.getInstance(), () -> {
            if(BEDS.isEmpty()) {
                stopHideTask();
                return;
            }
            BEDS.values().forEach(SleepManager::hideBed);
        }, 2L, 10L);
    }

    private static void stopHideTask() {
        if(hideTask != null) {
            hideTask.cancel();
            hideTask = null;
        }
    }

    private static void hideBed(FakeBed fake) {
        World world = fake.foot().getWorld();
        double radiusSquared = (double) FAKE_BLOCK_RADIUS * FAKE_BLOCK_RADIUS;

        for(Player viewer : world.getPlayers()) {
            if(viewer.getLocation().distanceSquared(fake.foot().getLocation()) > radiusSquared)
                continue;

            viewer.sendBlockChange(fake.foot().getLocation(), fake.footOld());
            viewer.sendBlockChange(fake.head().getLocation(), fake.headOld());
        }
    }

    private static void restore(FakeBed fake) {
        if(Tag.BEDS.isTagged(fake.foot().getType()))
            fake.foot().setBlockData(fake.footOld(), false);

        if(Tag.BEDS.isTagged(fake.head().getType()))
            fake.head().setBlockData(fake.headOld(), false);
    }

    private static boolean isNight(World world) {
        long time = world.getTime();
        return time >= NIGHT_START && time <= NIGHT_END;
    }

    private static boolean isReplaceable(Block block) {
        return block.getType().isAir() || Tag.REPLACEABLE.isTagged(block.getType());
    }

    /** La propriété "facing" d'un lit pointe du pied vers la tête. */
    private static BlockFace yawToFace(float yaw) {
        return switch(Math.round(yaw / 90f) & 3) {
            case 0 -> BlockFace.SOUTH;
            case 1 -> BlockFace.WEST;
            case 2 -> BlockFace.NORTH;
            default -> BlockFace.EAST;
        };
    }
}