package fr.niixoz.survivalcore.storage.players;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.homes.Home;
import fr.niixoz.survivalcore.tasks.TeleportationTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SurvivalPlayer {

    public static List<SurvivalPlayer> players = new ArrayList<>();
    public static HashMap<String, UUID> playersUUID = new HashMap<>();
    private Player player;
    private Location lastLocation;
    private List<Home> homes = new ArrayList<>();
    private float experience;
    private int level;

    private Inventory backpack;


    public SurvivalPlayer(UUID uuid) {
        this.player = null;
        this.loadInfo(uuid);
    }
    public SurvivalPlayer(Player player) {
        this.player = player;
        this.loadInfo(player.getUniqueId());
    }

    public Player getPlayer() {
        return player;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public void setHomes(List<Home> homes) {
        this.homes = homes;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public Home getHome(String name) {
        for (Home home : homes) {
            if(home == null)
                continue;
            if (home.getName().equals(name)) {
                return home;
            }
        }
        return null;
    }

    public boolean teleportToLastLocation() {
        if (lastLocation != null) {
            TeleportationTask teleportationTask = new TeleportationTask(player, lastLocation, Config.backTeleportationDelay);
            teleportationTask.setFreeMove(Config.backFreeMove);
            teleportationTask.start();
            return true;
        }
        return false;
    }

    public boolean hasHome(String home) {
        return getHome(home) != null;
    }

    public boolean teleportToHome(String home) {
        Home homeToTeleport = getHome(home);
        if (homeToTeleport != null) {
            TeleportationTask teleportationTask = new TeleportationTask(player, homeToTeleport.getLocation(), Config.homeTeleportationDelay);
            teleportationTask.setFreeMove(Config.homeFreeMove);
            teleportationTask.start();
            return true;
        }
        return false;
    }

    public boolean addHome(String home, Location location){

        if(homes.size() < Home.getHomeLimit(player) || player.hasPermission(PermissionEnum.HOME_LIMIT_BYPASS.getPermission()) || player.hasPermission(PermissionEnum.PERMISSION_ALL.getPermission())) {
            homes.add(new Home(home, location));
            saveInfo(player.getUniqueId());
            return true;
        }
        return false;
    }

    public boolean modifyHome(String home, Location location) {
        for (Home h : homes) {
            if (h.getName().equals(home)) {
                h.setLocation(location);
                saveInfo(player.getUniqueId());
                return true;
            }
        }
        return false;
    }

    public boolean removeHome(String home) {
        if(hasHome(home)) {
            homes.remove(getHome(home));
            saveInfo(player.getUniqueId());
            return true;
        }
        return false;
    }

    private void loadInfo(UUID uuid) {
        File playerFile = getPlayerInfoFile(uuid);

        if(playerFile.exists()) {
            FileConfiguration player = YamlConfiguration.loadConfiguration(playerFile);

            if(player.contains("homes")) {
                for (String home : player.getConfigurationSection("homes").getKeys(false)) {
                    String prefix = "homes." + home;
                    Location location = new Location(SurvivalCore.getInstance().getServer().getWorld(player.getString(prefix + ".world")), player.getDouble(prefix + ".x"), player.getDouble(prefix + ".y"), player.getDouble(prefix + ".z"), player.getLong(prefix + ".yaw"), player.getLong(prefix + ".pitch"));
                    homes.add(new Home(home, location, player.getString(prefix + ".world")));
                }
            }

            if(player.contains("lastLocation") && player.contains("lastLocation.world") && player.contains("lastLocation.x") && player.contains("lastLocation.y") && player.contains("lastLocation.z") && player.contains("lastLocation.yaw") && player.contains("lastLocation.pitch"))
            {
                Location lastLocation = new Location(SurvivalCore.getInstance().getServer().getWorld(player.getString("lastLocation.world")), player.getDouble("lastLocation.x"), player.getDouble("lastLocation.y"), player.getDouble("lastLocation.z"), player.getLong("lastLocation.yaw"), player.getLong("lastLocation.pitch"));
                this.setLastLocation(lastLocation);
            }
        }
        else {
            try {
                playerFile.createNewFile();
                FileConfiguration player = YamlConfiguration.loadConfiguration(playerFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        loadBackpack();
    }

    public void loadBackpack() {
        backpack = Bukkit.createInventory(null, 27, "Sac à dos");
    }

    public void saveInfo() {
        saveInfo(player.getUniqueId());
    }

    public void saveInfo(UUID uuid) {
        File playerFile = getPlayerInfoFile(uuid);

        if(!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileConfiguration player = YamlConfiguration.loadConfiguration(playerFile);

        if(this.player != null)
            player.set("username", this.player.getName());

        // Clear all homes
        if(player.contains("homes")) {
            for (String home : player.getConfigurationSection("homes").getKeys(false)) {
                player.set("homes." + home, null);
            }
            player.set("homes", null);
        }

        for (Home home : this.homes) {
            String prefix = "homes." + home.getName();
            if(home.getLocation().getWorld() == null) {
                player.set(prefix + ".world", home.getWorldName());
            }
            else {
                player.set(prefix + ".world", home.getLocation().getWorld().getName());
            }
            player.set(prefix + ".x", home.getLocation().getX());
            player.set(prefix + ".y", home.getLocation().getY());
            player.set(prefix + ".z", home.getLocation().getZ());
            player.set(prefix + ".yaw", home.getLocation().getYaw());
            player.set(prefix + ".pitch", home.getLocation().getPitch());
        }

        if (lastLocation != null) {
            if(lastLocation.getWorld() == null) {
                lastLocation.setWorld(SurvivalCore.getInstance().getServer().getWorlds().get(0));
            }
            player.set("lastLocation.world", lastLocation.getWorld().getName());
            player.set("lastLocation.x", lastLocation.getX());
            player.set("lastLocation.y", lastLocation.getY());
            player.set("lastLocation.z", lastLocation.getZ());
            player.set("lastLocation.yaw", lastLocation.getYaw());
            player.set("lastLocation.pitch", lastLocation.getPitch());
        }

        try {
            player.save(playerFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getInfoFolder() {
        File infoFolder = new File(SurvivalCore.getInstance().getDataFolder() + "/playerdata");
        if(!infoFolder.exists()) {
            infoFolder.mkdir();
        }
        return infoFolder;
    }

    private File getPlayerInfoFile(UUID uuid) {
        return new File(getInfoFolder() + "/" + uuid + ".yml");
    }


    public static SurvivalPlayer getPlayer(Player player) {
        for (SurvivalPlayer enderPlayer : players) {
            if (enderPlayer.getPlayer().equals(player)) {
                return enderPlayer;
            }
        }
        return null;
    }

    public float getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Inventory getBackpack() {
        return backpack;
    }
}