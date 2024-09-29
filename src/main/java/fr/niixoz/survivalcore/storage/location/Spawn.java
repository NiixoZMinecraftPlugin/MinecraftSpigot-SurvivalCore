package fr.niixoz.survivalcore.storage.location;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.tasks.TeleportationTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Spawn {

    public static Location location;
    public static int teleportationDelay = 3;
    public static boolean freeMove = false;

    public static void loadSpawnConfig(FileConfiguration config) {

        SurvivalCore.getInstance().getLogger().info("Loading spawn config...");

        if(config.contains("spawn")) {
            if(config.contains("spawn.teleportation-delay"))
                teleportationDelay = config.getInt("spawn.teleportation-delay");
            if(config.contains("spawn.free-move"))
                freeMove = config.getBoolean("spawn.free-move");
            if(config.contains("spawn.location")) {
                if(!config.contains("spawn.location.world") && !config.contains("spawn.location.x") && !config.contains("spawn.location.y") && !config.contains("spawn.location.z") && !config.contains("spawn.location.yaw") && !config.contains("spawn.location.pitch")) {
                    Bukkit.getLogger().warning("§4[§cSurvivalCore§4] §cLe spawn n'a pas été configuré correctement, merci de vérifier votre configuration.");
                    return;
                }
                World world = Bukkit.getWorld(config.getString("spawn.location.world"));
                double x = config.getDouble("spawn.location.x");
                double y = config.getDouble("spawn.location.y");
                double z = config.getDouble("spawn.location.z");
                float yaw = (float) config.getDouble("spawn.location.yaw");
                float pitch = (float) config.getDouble("spawn.location.pitch");
                location = new Location(world, x, y, z, yaw, pitch);
            }
        }
    }

    public static void modifySpawnLocation(Location loc) {
        location = loc;
        saveConfig();
    }

    public static void saveConfig() {
        FileConfiguration config = SurvivalCore.getInstance().getConfig();
        config.set("spawn.teleportation-delay", teleportationDelay);
        config.set("spawn.free-move", freeMove);
        config.set("spawn.location.world", location.getWorld().getName());
        config.set("spawn.location.x", location.getX());
        config.set("spawn.location.y", location.getY());
        config.set("spawn.location.z", location.getZ());
        config.set("spawn.location.yaw", location.getYaw());
        config.set("spawn.location.pitch", location.getPitch());
        SurvivalCore.getInstance().saveConfig();
    }

    public static boolean teleportPlayer(Player player) {
        if(location != null) {
            TeleportationTask task = new TeleportationTask(player, location, teleportationDelay);
            task.setFreeMove(freeMove);
            task.start();
            return true;
        }
        return false;
    }

}
