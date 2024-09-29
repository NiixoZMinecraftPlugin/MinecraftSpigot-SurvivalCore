package fr.niixoz.survivalcore.config;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.storage.location.Spawn;
import fr.niixoz.survivalcore.storage.location.Warp;
import fr.niixoz.survivalcore.storage.prefix.ChatIcon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

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


    public static void loadConfig() {

        SurvivalCore.getInstance().getLogger().info("Loading config...");

        backTeleportCauses.add(PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
        backTeleportCauses.add(PlayerTeleportEvent.TeleportCause.COMMAND);
        backTeleportCauses.add(PlayerTeleportEvent.TeleportCause.PLUGIN);
        backTeleportCauses.add(PlayerTeleportEvent.TeleportCause.UNKNOWN);

        SurvivalCore plugin = SurvivalCore.getInstance();
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

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

        Spawn.loadSpawnConfig(config);
        Warp.loadWarpConfig();
        ChatIcon.loadIconConfig();
    }

    public static void reload() {
        SurvivalCore.getInstance().reloadConfig();
        loadConfig();
    }
}
