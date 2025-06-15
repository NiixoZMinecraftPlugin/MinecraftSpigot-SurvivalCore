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

public class HealCommand extends AbstractCommand {

    public HealCommand() {
        super("heal", "Permet de se nourrir.", "/heal [player]", PermissionEnum.COMMAND_HEAL);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                MessageUtils.sendPlayerMessage(player, "Le joueur " + args[0] + " n'existe pas !");
                return true;
            }
            MessageUtils.sendPlayerMessage(player, "Vous avez heal " + target.getName() + " !");
            player = target;
        }

        player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
        MessageUtils.sendPlayerMessage(player, "Vous avez été heal !");
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
