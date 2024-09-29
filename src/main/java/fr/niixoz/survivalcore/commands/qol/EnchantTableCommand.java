package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class EnchantTableCommand extends AbstractCommand {

    public EnchantTableCommand() {
        super("enchanting_table", "Permet d'ouvrir une table d'enchant.", "/enchanting_table", PermissionEnum.COMMAND_ENCHANTING_TABLE);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        player.openEnchanting(player.getLocation(), true);
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
