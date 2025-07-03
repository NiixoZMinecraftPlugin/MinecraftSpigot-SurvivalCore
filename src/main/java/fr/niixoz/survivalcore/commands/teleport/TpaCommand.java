package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.TeleportUtils;
import fr.niixoz.survivalcore.tasks.TeleportationTask;
import fr.niixoz.survivalcore.tasks.WaitingTeleportTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TpaCommand extends AbstractCommand {

    public TpaCommand() {
        super("tpa", "Permet d'envoyer une demande de téléportation à un joueur", "/tpa", PermissionEnum.COMMAND_TP_TPA);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) throws Exception {
        if(args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                player.sendMessage("§6[ §eTéléportation §6] §eLe joueur n'est pas connecté.");
                return true;
            }

            if(target == player) {
                player.sendMessage("§6[ §eTéléportation §6] §eVous ne pouvez pas vous téléporter à vous même.");
                return true;
            }

            if(TeleportationTask.isInTeleportation(player)) {
                player.sendMessage("§6[ §eTéléportation §6] §eVous avez déjà une téléportation en cours.");
                return true;
            }

            WaitingTeleportTask task = new WaitingTeleportTask(player, target, Config.tpaTeleportationRequestExpirationDelay);
            task.start();

            player.sendMessage("§6[ §eTéléportation §6] §eDemande de téléportation envoyé à §6" + target.getName() + "§e.");
            target.sendMessage("§6[ §eTéléportation §6] §eLe joueur §6" + player.getName() + "§e souhaite se téléporter à vous.");
            TeleportUtils.sendTeleportAcceptationComponent(target, player);
            return true;
        }

        else {
            player.sendMessage("§6[ §eTéléportation §6] §eUsage: §6/tpa <joueur>");
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
