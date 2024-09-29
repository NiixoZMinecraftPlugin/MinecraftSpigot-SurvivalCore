package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class FlyCommand extends AbstractCommand {

    public FlyCommand() {
        super("fly", "Permet d'activer ou désactiver le fly.", "/fly [player]", PermissionEnum.COMMAND_FLY);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                MessageUtils.sendPlayerMessage(player, "Le joueur " + args[0] + " n'existe pas !");
                return true;
            }
            MessageUtils.sendPlayerMessage(player, "Vous avez " + (!target.getAllowFlight() ? "activé" : "désactivé") + " le fly de " + target.getName() + " !");
            player = target;
        }

        player.setAllowFlight(!player.getAllowFlight());
        MessageUtils.sendPlayerMessage(player, "Le fly a été " + (player.getAllowFlight() ? "activé" : "désactivé") + " !");
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
