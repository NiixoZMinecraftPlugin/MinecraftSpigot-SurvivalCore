package fr.niixoz.survivalcore;

import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.listeners.MobProtectionHandler;
import fr.niixoz.survivalcore.listeners.PlantHandler;
import fr.niixoz.survivalcore.listeners.PlayerHandler;
import fr.niixoz.survivalcore.managers.CommandsManager;
import fr.niixoz.survivalcore.storage.players.SurvivalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

import java.io.File;
import java.util.UUID;

public final class SurvivalCore extends JavaPlugin {

    private static SurvivalCore instance;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        instance = this;
        adventure = BukkitAudiences.create(this);
        Config.loadConfig();

        registerEvents();
        initPlugin();
    }

    @Override
    public void onDisable() {
        for(SurvivalPlayer enderPlayer : SurvivalPlayer.players) {
            enderPlayer.saveInfo();
        }
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }

    private void initPlugin() {
        CommandsManager.registerCommands();
        checkForOnlinePlayer();
        loadPlayersUUID();
    }

    public void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new MobProtectionHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlantHandler(), this);
    }

    private void loadPlayersUUID() {
        for(File file : SurvivalPlayer.getInfoFolder().listFiles()) {
            if(file.getName().endsWith(".yml")) {
                FileConfiguration player = YamlConfiguration.loadConfiguration(file);
                try {
                    if(!player.contains("username")) {
                        System.out.println("Error while loading UUID for player " + file.getName().replace(".yml", ""));
                        continue;
                    }
                    SurvivalPlayer.playersUUID.put(player.getString("username"), UUID.fromString(file.getName().replace(".yml", "")));
                }
                catch(Exception e) {
                    System.out.println("Error while loading UUID for player " + file.getName().replace(".yml", ""));
                }
            }
        }
    }

    private void checkForOnlinePlayer() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            SurvivalPlayer.players.add(new SurvivalPlayer(player));
            if(!SurvivalPlayer.playersUUID.containsKey(player.getName())) {
                SurvivalPlayer.playersUUID.put(player.getName(), player.getUniqueId());
            }
        }
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public static SurvivalCore getInstance() {
        return instance;
    }
}
