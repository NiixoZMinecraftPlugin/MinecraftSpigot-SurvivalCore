package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.location.Spawn;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SpawnCommand extends AbstractCommand {

    public SpawnCommand() {
        super("spawn", "Permet de se téléporter au spawn", "/spawn [player]", PermissionEnum.COMMAND_SPAWN);
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
            MessageUtils.sendPlayerMessage(player, "Vous avez téléporté " + target.getName() + " au spawn !");
            player = target;
        }

        Spawn.teleportPlayer(player);
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
