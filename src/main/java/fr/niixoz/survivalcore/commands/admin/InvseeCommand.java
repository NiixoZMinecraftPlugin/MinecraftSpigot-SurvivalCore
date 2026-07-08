package fr.niixoz.survivalcore.commands.admin;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class InvseeCommand extends AbstractCommand {

    public InvseeCommand() {
        super("invsee", "Permet d'ouvrir l'inventaire d'un joueur.", "/invsee <player>", PermissionEnum.COMMAND_ADMIN_INVSEE);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(args.length != 1) {
            sendUsage(player);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            MessageUtils.sendPlayerMessage(player, "Le joueur " + args[0] + " n'existe pas !");
            return true;
        }
        player.openInventory(target.getInventory());
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
