package fr.niixoz.survivalcore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CommandHandler implements Listener {

    @EventHandler
    public void onAnvilClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        if (!event.getView().getTitle().equals("Enclume portable")) return;

        Inventory inv = event.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType().isAir()) continue;

            Map<Integer, ItemStack> restant = player.getInventory().addItem(item);
            restant.values().forEach(drop -> player.getWorld().dropItemNaturally(player.getLocation(), drop));
        }
        inv.clear();
    }
}
