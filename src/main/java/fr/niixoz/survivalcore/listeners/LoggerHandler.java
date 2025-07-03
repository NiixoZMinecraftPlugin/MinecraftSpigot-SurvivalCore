package fr.niixoz.survivalcore.listeners;

import fr.niixoz.survivalcore.SurvivalCore;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class LoggerHandler implements Listener {

    private final SurvivalCore plugin;
    private final boolean enabled;
    private final Set<Material> watchedBlocks = new HashSet<>();
    private final BukkitScheduler scheduler;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LoggerHandler() {
        this.plugin = SurvivalCore.getInstance();
        this.scheduler = plugin.getServer().getScheduler();
        FileConfiguration config = plugin.getConfig();
        this.enabled = config.getBoolean("logger.enabled", true);

        if(enabled) {
            List<String> list = config.getStringList("logger.blocks");
            for (String s : list) {
                Material m = Material.matchMaterial(s);
                if (m != null) {
                    watchedBlocks.add(m);
                } else {
                    plugin.getLogger().warning("Unknown material in logger.blocks: " + s);
                }
            }
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void playerPlaceBlock(BlockPlaceEvent e) {
        if (!shouldLog(e.getBlock())) return;
        writeAsync("PLACE", e.getPlayer(), e.getBlock());
    }

    @EventHandler
    public void playerBreakBlock(BlockBreakEvent e) {
        if (!shouldLog(e.getBlock())) return;
        writeAsync("BREAK", e.getPlayer(), e.getBlock());
    }

    private boolean shouldLog(Block block) {
        return enabled && watchedBlocks.contains(block.getType());
    }

    private void writeAsync(String action, Player player, Block block) {
        // Clone data needed for async execution
        Material type = block.getType();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        String world = block.getWorld().getName();
        String playerName = player.getName();
        UUID uuid = player.getUniqueId();
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();

        scheduler.runTaskAsynchronously(plugin, () -> {
            File logFile = getLogFileForDate(date);
            String line = String.format("[%s] %s (%s) %s %s at %d,%d,%d in %s",
                    TIME_FORMAT.format(now), playerName, uuid, action, type, x, y, z, world);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(line);
                writer.newLine();
            } catch (IOException ex) {
                plugin.getLogger().log(Level.WARNING, "Could not write to log file " + logFile.getAbsolutePath(), ex);
            }
        });
    }

    private File getLogFileForDate(LocalDate date) {
        String dateStr = DATE_FORMAT.format(date);
        File dir = new File(plugin.getDataFolder(), "logs" + File.separator + dateStr);
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        return new File(dir, dateStr + ".txt");
    }

}
