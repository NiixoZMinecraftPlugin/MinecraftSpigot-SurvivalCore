package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;

import java.util.Arrays;
import java.util.List;

public class CartographyCommand extends AbstractCommand {

    public CartographyCommand() {
        super("cartography", "Permet d'ouvrir une Cartography Table.", "/cartography", PermissionEnum.COMMAND_CARTOGRAPHY_TABLE, List.of("cartography_table"));
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        player.openInventory(MenuType.CARTOGRAPHY_TABLE.create(player, "Cartography Table"));
        
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
