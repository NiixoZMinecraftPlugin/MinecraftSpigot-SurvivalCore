package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.SurvivalCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public final class VirtualEnchantingManager implements Listener {

    private final Map<UUID, List<PlacedBlock>> sessions = new HashMap<>();
    private final SurvivalCore plugin;

    public VirtualEnchantingManager(SurvivalCore plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /* ---------- API publique ---------- */
    public void openTable(Player player) {
        Location tableLoc = player.getLocation().getBlock().getLocation();
        List<PlacedBlock> placed = new ArrayList<>();

        // 1) Table d'enchantement
        placed.add(PlacedBlock.replace(tableLoc, Material.ENCHANTING_TABLE));

        // 2) 15 bibliothèques autour (x/z = ±2 ou y+1)
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if ((Math.abs(dx) == 2 || Math.abs(dz) == 2)           // anneau extérieur
                        && !(Math.abs(dx) == 2 && Math.abs(dz) == 2))  // pas les 4 coins ext.
                {
                    Location shelf = tableLoc.clone().add(dx, 0, dz);
                    placed.add(PlacedBlock.replace(shelf, Material.BOOKSHELF));
                }
            }
        }
        // 3 bibliothèques manquantes : on en place 3 au-dessus
        placed.add(PlacedBlock.replace(tableLoc.clone().add(2, 1, 0), Material.BOOKSHELF));
        placed.add(PlacedBlock.replace(tableLoc.clone().add(-2, 1, 0), Material.BOOKSHELF));
        placed.add(PlacedBlock.replace(tableLoc.clone().add(0, 1, 2), Material.BOOKSHELF));

        sessions.put(player.getUniqueId(), placed);

        // Ouvre l'interface (force=false car la table existe désormais)
        player.openEnchanting(tableLoc, false);   // :contentReference[oaicite:0]{index=0}
    }

    /* ---------- Listeners ---------- */
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getType() != InventoryType.ENCHANTING) return;
        restoreBlocks(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) { restoreBlocks(e.getPlayer().getUniqueId()); }

    /* ---------- Restauration ---------- */
    private void restoreBlocks(UUID uuid) {
        List<PlacedBlock> placed = sessions.remove(uuid);
        if (placed == null) return;
        Bukkit.getScheduler().runTask(plugin, () -> placed.forEach(PlacedBlock::restore));
    }

    /* ---------- Petit record interne ---------- */
    private record PlacedBlock(Location loc, Material original, BlockData originalData) {
        static PlacedBlock replace(Location loc, Material newType) {
            Block block = loc.getBlock();
            PlacedBlock pb = new PlacedBlock(loc.clone(), block.getType(), block.getBlockData());
            block.setType(newType, false);
            return pb;
        }
        void restore() {
            loc.getBlock().setBlockData(originalData, false);
        }
    }
}