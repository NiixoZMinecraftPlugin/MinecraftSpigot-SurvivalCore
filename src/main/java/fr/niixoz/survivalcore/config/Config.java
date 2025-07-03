package fr.niixoz.survivalcore.config;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.storage.location.Spawn;
import fr.niixoz.survivalcore.storage.location.Warp;
import fr.niixoz.survivalcore.storage.prefix.ChatIcon;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class Config {

    public static String errorPrefix = "§4[§cSurvivalCore§4] §c";
    public static String errorMessage = "Une erreur est survenue, merci de contacter un administrateur.";
    public static int tpaTeleportationDelay = 3;
    public static int tpaTeleportationRequestExpirationDelay = 300;
    public static boolean tpaFreeMove = true;
    public static int homeTeleportationDelay = 3;
    public static boolean homeFreeMove = false;
    public static int homeMaxHomes = 20;

    public static int backTeleportationDelay = 0;
    public static boolean backFreeMove = false;
    public static List<PlayerTeleportEvent.TeleportCause> backTeleportCauses = new ArrayList<>();
    public static boolean backOnDeath = true;

    public static Map<String, Double> sizes = new HashMap<>();


    public static void loadConfig() {

        SurvivalCore.getInstance().getLogger().info("Loading config...");

        backTeleportCauses.add(PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
        backTeleportCauses.add(PlayerTeleportEvent.TeleportCause.COMMAND);
        backTeleportCauses.add(PlayerTeleportEvent.TeleportCause.PLUGIN);
        backTeleportCauses.add(PlayerTeleportEvent.TeleportCause.UNKNOWN);

        SurvivalCore plugin = SurvivalCore.getInstance();
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        loadDefaultConfig(config);

        tpaTeleportationDelay = config.getInt("teleport.teleportation-delay");
        tpaTeleportationRequestExpirationDelay = config.getInt("teleport.teleportation-request-expiration-delay");
        tpaFreeMove = config.getBoolean("teleport.free-move");

        homeMaxHomes = config.getInt("homes.max-homes");
        homeTeleportationDelay = config.getInt("homes.teleportation-delay");
        homeFreeMove = config.getBoolean("homes.free-move");

        if(config.contains("back")) {
            if (config.contains("back.teleportation-delay"))
                backTeleportationDelay = config.getInt("back.teleportation-delay");
            if (config.contains("back.free-move"))
                backFreeMove = config.getBoolean("back.free-move");
            if (config.contains("back.teleport-causes")) {
                backTeleportCauses.clear();
                List<String> causes = (List<String>) config.getList("back.teleport-causes");
                for(String cause : causes) {
                    if(cause.equals("DEATH"))
                        backOnDeath = true;
                    else {
                        PlayerTeleportEvent.TeleportCause teleportCause = PlayerTeleportEvent.TeleportCause.valueOf(cause);
                        if(teleportCause != null)
                            backTeleportCauses.add(teleportCause);
                    }
                }
            }
        }

        if(config.contains("sizes")) {
            sizes.clear();
            sizes.put("normal", 1.0d);
            sizes.put("reset", 1.0d);
            ConfigurationSection sizeSection = config.getConfigurationSection("sizes");
            if (sizeSection != null) {
                for (String key : sizeSection.getKeys(false)) {
                    double value = sizeSection.getDouble(key);
                    sizes.put(key, value);
                }
            }
        }
        else {
            sizes.clear();
            sizes.put("reset", 1.0d);
            sizes.put("xs", 0.5d);
            sizes.put("small", 0.75d);
            sizes.put("normal", 1.0d);
            sizes.put("big", 1.25d);
            sizes.put("xl", 1.5d);
            config.addDefault("sizes.xs", 0.5d);
            config.addDefault("sizes.small", 0.75d);
            config.addDefault("sizes.big", 1.25d);
            config.addDefault("sizes.xl", 1.5d);
            plugin.saveDefaultConfig();
        }

        Spawn.loadSpawnConfig(config);
        Warp.loadWarpConfig();
        ChatIcon.loadIconConfig();
    }

    private static void loadDefaultConfig(FileConfiguration config){
        config.addDefault("logger.enabled", true);
        config.addDefault("logger.blocks", Arrays.asList(
                "DIAMOND_BLOCK",
                "DIAMOND_ORE",
                "ANCIENT_DEBRIS"
        ));
    }

    public static void reload() {
        SurvivalCore.getInstance().reloadConfig();
        loadConfig();
    }

    public static Double getSize(String size) {
        return sizes.get(size);
    }
}
