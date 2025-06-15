package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class FeedCommand extends AbstractCommand {

    public FeedCommand() {
        super("feed", "Permet de se nourrir.", "/feed [player]", PermissionEnum.COMMAND_FEED);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(args.length == 1) {
            if(!player.hasPermission(PermissionEnum.PERMISSION_ALL.getPermission())) {
                MessageUtils.sendPlayerMessage(player, "Vous n'avez pas la permission d'utiliser cette commande sur un autre joueur !");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                MessageUtils.sendPlayerMessage(player, "Le joueur " + args[0] + " n'existe pas !");
                return true;
            }
            MessageUtils.sendPlayerMessage(player, "Vous avez nourri " + target.getName() + " !");
            player = target;
        }

        player.setSaturation(20);
        player.setFoodLevel(20);
        MessageUtils.sendPlayerMessage(player, "Vous avez été nourri !");
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
