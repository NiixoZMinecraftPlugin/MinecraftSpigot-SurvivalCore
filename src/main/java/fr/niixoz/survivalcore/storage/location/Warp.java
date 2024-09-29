package fr.niixoz.survivalcore.storage.location;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.tasks.TeleportationTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Warp {

    public static List<Warp> warps = new ArrayList<>();
    public static int teleportationDelay = 3;
    public static boolean freeMove = false;

    private String name;
    private Location location;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public boolean teleportPlayer(Player player) {
        if(location != null) {
            TeleportationTask task = new TeleportationTask(player, location, teleportationDelay);
            task.setFreeMove(freeMove);
            task.start();
            return true;
        }
        return false;
    }

    public static Warp getWarp(String name) {
        return warps.stream().filter(warp -> warp.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void loadWarpConfig() {

        SurvivalCore.getInstance().getLogger().info("Loading warps...");
        warps.clear();
    }

    public static void saveConfig() {

    }
}
