package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.tasks.WaitingTeleportTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TpAcceptCommand extends AbstractCommand {

    public TpAcceptCommand() {
        super("tpaccept", "Permet d'accepter une demande de téléportation", "/tpaccept <joueur>", PermissionEnum.COMMAND_TP_ACCEPT);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) throws Exception {

        if(args.length == 0) {
            player.sendMessage("§6[ §eTéléportation §6] §eUsage: §6/tpaccept <joueur>");
            return true;
        }

        Player playerWhoWantTeleport = Bukkit.getPlayer(args[0]);
        if(playerWhoWantTeleport == null) {
            player.sendMessage("§6[ §eTéléportation §6] §eLe joueur n'est pas connecté.");
            return true;
        }

        WaitingTeleportTask task = WaitingTeleportTask.getTask(playerWhoWantTeleport, player);
        if(task == null) {
            player.sendMessage("§6[ §eTéléportation §6] §eVous n'avez aucune demande de téléportation en cours.");
            return true;
        }

        task.accept();
        player.sendMessage("§6[ §eTéléportation §6] §eVous avez accepté la demande de téléportation de §6" + task.getPlayer().getName() + "§e.");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {


        if(!(sender instanceof Player player))
            return null;

        List<WaitingTeleportTask> tasks = WaitingTeleportTask.getTargetTasks(player);

        if(args.length == 1) {
            return tasks.stream().map(WaitingTeleportTask::getPlayer).map(Player::getName).toList();
        }

        return Arrays.asList("");
    }
}
