package fr.niixoz.survivalcore.storage;

import fr.niixoz.survivalcore.SurvivalCore;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.io.File;

public class EnderChestInventory extends CustomInventory {

    private static final File CUSTOM_INVENTORY_FOLDER = new File(SurvivalCore.getInstance().getDataFolder() + "/custom_inventory");
    private Inventory inventory;
    private String folderName;

    public EnderChestInventory(int size) {
        super("ender_chest", size);
    }

    public static EnderChestInventory loadInventory(String fileName) {
        return loadInventory("ender_chest", fileName);
    }

    public static EnderChestInventory loadInventory(String folderName, String fileName) {
        File file = new File(CUSTOM_INVENTORY_FOLDER + "/" + folderName + "/" + fileName + ".yml");
        EnderChestInventory ec = new EnderChestInventory(27);
        return ec;
    }
}
