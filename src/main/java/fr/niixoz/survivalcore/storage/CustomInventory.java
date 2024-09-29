package fr.niixoz.survivalcore.storage;

import fr.niixoz.survivalcore.SurvivalCore;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.io.File;

public class CustomInventory  {

    private static final File CUSTOM_INVENTORY_FOLDER = new File(SurvivalCore.getInstance().getDataFolder() + "/custom_inventory");
    private Inventory inventory;
    private String folderName;


    public CustomInventory() {
        this("custom", 27);
    }

    public CustomInventory(int size) {
        this("custom", size);
    }

    public CustomInventory(String folderName, int size) {
        this.folderName = folderName;
        this.inventory = Bukkit.createInventory(null, size);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inv) {
        this.inventory = inv;
    }

    public void saveInventory(String fileName) {
        if(!CUSTOM_INVENTORY_FOLDER.exists()) {
            CUSTOM_INVENTORY_FOLDER.mkdirs();
        }

        File file = new File(CUSTOM_INVENTORY_FOLDER + "/" + this.folderName + "/", fileName + ".yml");
    }

    public static CustomInventory loadInventory(String fileName) {
        return loadInventory("custom", fileName);
    }

    public static CustomInventory loadInventory(String folderName, String fileName) {
        File file = new File(CUSTOM_INVENTORY_FOLDER + "/" + folderName + "/" + fileName + ".yml");
        CustomInventory ci = new CustomInventory(27);
        return ci;
    }
}
