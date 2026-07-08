package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class TrashCommand extends AbstractCommand {

    public TrashCommand() {
        super("trash", "Ouvre une poubelle pour détruire des items.", "/trash", PermissionEnum.COMMAND_TRASH);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        Inventory trashInv = Bukkit.createInventory(null, 27, "Poubelle");
        player.openInventory(trashInv);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return null;

        if(args.length == 1) {
            return null;
        }

        return Arrays.asList("");
    }
}
