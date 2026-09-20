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

public class GodCommand extends AbstractCommand {

    public GodCommand() {
        super("god", "Permet de se mettre en god mode.", "/god [player]", PermissionEnum.COMMAND_ADMIN_GOD);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        Player target = (args.length == 0) ? player : Bukkit.getPlayer(args[0]);

        if (target == null) {
            MessageUtils.sendPlayerMessage(player, "Le joueur " + args[0] + " n'existe pas !");
            return true;
        }

        boolean newState = !target.isInvulnerable();
        target.setInvulnerable(newState);

        String action = newState ? "d'activer" : "de désactiver";

        if (target.equals(player)) {
            MessageUtils.sendPlayerMessage(player, "Tu viens " + action + " le god mode.");
        } else {
            MessageUtils.sendPlayerMessage(player, "Tu viens " + action + " le god mode pour le joueur " + target.getName() + ".");
        }
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
