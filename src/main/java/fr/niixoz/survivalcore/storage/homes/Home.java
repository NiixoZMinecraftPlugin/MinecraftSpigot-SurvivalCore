package fr.niixoz.survivalcore.storage.homes;

import fr.niixoz.survivalcore.config.Config;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicReference;

public class Home {

    private String name;
    private Location location;
    private String worldName;

    public Home(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Home(String name, Location location, String worldName) {
        this.name = name;
        this.location = location;
        this.worldName = worldName;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static int getHomeLimit(Player p) {
        AtomicReference<Integer> limit = new AtomicReference<>(Config.homeMaxHomes);
        p.getEffectivePermissions().stream().filter(perm -> perm.getPermission().startsWith("survival.homes.limit.")).forEach(perm -> {
            try {
                if(perm.getPermission().startsWith("survival.homes.limit.")) {
                    limit.set(Integer.parseInt(perm.getPermission().split("survival.homes.limit.")[1]));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        return limit.get();
    }
}
