package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class EnderchestCommand extends AbstractCommand {

    public EnderchestCommand() {
        super("enderchest", "Permet d'ouvrir l'enderchest.", "/enderchest", PermissionEnum.COMMAND_ENDERCHEST);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        player.openInventory(player.getEnderChest());
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
