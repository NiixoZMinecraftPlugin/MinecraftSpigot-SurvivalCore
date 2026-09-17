package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VanishManager {

    /** Équipe utilisée uniquement pour supprimer les collisions et les tags des joueurs en vanish. */
    private static final String TEAM_NAME = "svcore_vanish";

    // Le ping du serveur (ServerListPingEvent) est déclenché hors du thread principal.
    private static final Set<UUID> vanished = ConcurrentHashMap.newKeySet();

    public static boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public static void setVanished(Player player, boolean vanish, VanishMode mode) {
        if (vanish) {
            vanished.add(player.getUniqueId());
        } else {
            vanished.remove(player.getUniqueId());
        }
        save();

        applyEffects(player, vanish);
        updateVisibility(player);

        if (mode == VanishMode.FAKE) {
            sendFakeConnectionMessage(player, vanish);
        }
    }

    /**
     * Simule un message de déconnexion (activation) ou de connexion (désactivation).
     * Composant traduisible : chaque joueur le reçoit dans sa langue, comme un vrai message.
     */
    private static void sendFakeConnectionMessage(Player player, boolean quit) {
        TranslatableComponent message = new TranslatableComponent(
                quit ? "multiplayer.player.left" : "multiplayer.player.joined", player.getDisplayName());
        message.setColor(ChatColor.YELLOW);

        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (!viewer.equals(player)) {
                viewer.spigot().sendMessage(message);
            }
        }
    }

    /** Restaure le vanish du joueur qui se connecte, et lui cache les joueurs déjà en vanish. */
    public static void handleJoin(Player player) {
        if (isVanished(player)) {
            applyEffects(player, true);
            updateVisibility(player);
        }
        refreshFor(player);
    }

    /** Charge l'état sauvegardé et le réapplique aux joueurs déjà connectés (reload du plugin). */
    public static void load() {
        vanished.clear();

        FileConfiguration config = YamlConfiguration.loadConfiguration(getFile());
        for (String uuid : config.getStringList("vanished")) {
            try {
                vanished.add(UUID.fromString(uuid));
            } catch (IllegalArgumentException e) {
                SurvivalCore.getInstance().getLogger().warning("[VanishManager] UUID invalide ignoré : " + uuid);
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isVanished(player)) {
                applyEffects(player, true);
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            refreshFor(player);
        }
    }

    /**
     * Retire les effets du vanish sans toucher à l'état sauvegardé : sans ça, les joueurs cachés
     * le resteraient après un reload du plugin. {@link #load()} les réapplique.
     */
    public static void disable() {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!isVanished(target)) {
                continue;
            }
            applyEffects(target, false);
            for (Player viewer : Bukkit.getOnlinePlayers()) {
                if (!viewer.equals(target)) {
                    viewer.showPlayer(SurvivalCore.getInstance(), target);
                }
            }
        }
    }

    /** Cache au joueur les joueurs en vanish qu'il n'est pas autorisé à voir. */
    private static void refreshFor(Player viewer) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!viewer.equals(target) && isVanished(target)) {
                applyVisibility(viewer, target);
            }
        }
    }

    private static void updateVisibility(Player target) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (!viewer.equals(target)) {
                applyVisibility(viewer, target);
            }
        }
    }

    private static void applyVisibility(Player viewer, Player target) {
        if (isVanished(target) && !viewer.hasPermission(PermissionEnum.COMMAND_ADMIN_VANISH_SEE.getPermission())) {
            viewer.hidePlayer(SurvivalCore.getInstance(), target);
        } else {
            viewer.showPlayer(SurvivalCore.getInstance(), target);
        }
    }

    private static void applyEffects(Player player, boolean vanish) {
        if (vanish) {
            // Sans particules ni icône : les joueurs autorisés à voir le vanish n'ont aucun indice visuel.
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                    PotionEffect.INFINITE_DURATION, 0, false, false, false));
            getCollisionTeam().addEntry(player.getName());
        } else {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            getCollisionTeam().removeEntry(player.getName());
        }
    }

    private static Team getCollisionTeam() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(TEAM_NAME);
        if (team == null) {
            team = scoreboard.registerNewTeam(TEAM_NAME);
        }
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        return team;
    }

    private static void save() {
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        List<String> uuids = new ArrayList<>();
        for (UUID uuid : vanished) {
            uuids.add(uuid.toString());
        }
        config.set("vanished", uuids);

        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File getFile() {
        File dataFolder = SurvivalCore.getInstance().getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        return new File(dataFolder, "vanished.yml");
    }
}