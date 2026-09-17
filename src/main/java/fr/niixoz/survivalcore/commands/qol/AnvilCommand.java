package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.AnvilView;

import java.util.Arrays;
import java.util.List;

public class AnvilCommand extends AbstractCommand {

    public AnvilCommand() {
        super("anvil", "Permet d'ouvrir une enclume.", "/anvil", PermissionEnum.COMMAND_ANVIL);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        AnvilView view = MenuType.ANVIL.create(player, "Enclume Portable");
        player.openInventory(view);

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
