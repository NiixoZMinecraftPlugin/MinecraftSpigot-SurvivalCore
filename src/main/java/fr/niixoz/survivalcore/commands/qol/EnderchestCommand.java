package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
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

        if(args.length == 1) {
            if(!player.hasPermission(PermissionEnum.COMMAND_ENDERCHEST_OTHERS.getPermission())) {
                MessageUtils.sendPlayerMessage(player, "Vous n'avez pas la permission d'ouvrir l'enderchest d'un autre joueur !");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                MessageUtils.sendPlayerMessage(player, "Le joueur " + args[0] + " n'existe pas !");
                return true;
            }
            player.openInventory(target.getEnderChest());
            return true;
        }
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
