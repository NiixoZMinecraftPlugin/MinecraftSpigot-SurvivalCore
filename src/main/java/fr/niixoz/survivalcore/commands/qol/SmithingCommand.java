package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.AnvilView;

import java.util.Arrays;
import java.util.List;

public class SmithingCommand extends AbstractCommand {

    public SmithingCommand() {
        super("smithing", "Permet d'ouvrir une Smithing Table.", "/smithing", PermissionEnum.COMMAND_SMITHING_TABLE);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        player.openInventory(MenuType.SMITHING.create(player, "Smithing Table Portable"));
        
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
