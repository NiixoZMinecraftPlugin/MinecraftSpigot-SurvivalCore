package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.tasks.TeleportUtils;
import fr.niixoz.survivalcore.tasks.WaitingTeleportHereTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TpaHereCommand extends AbstractCommand {

    public TpaHereCommand() {
        super("tpahere", "Permet d'envoyer une demande de téléportation à un joueur pour le téléporter à vous", "/tpahere <joueur>", PermissionEnum.COMMAND_TP_TPAHERE);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) throws Exception {
        if(args.length == 1) {
            if(args[0].equals("all") || args[0].equals("@a") || args[0].equals("*")) {
                if(player.hasPermission(PermissionEnum.COMMAND_TP_ADMIN.getPermission())) {
                    for(Player target : Bukkit.getOnlinePlayers()) {
                        if(target == player) {
                            continue;
                        }
                        WaitingTeleportHereTask task = new WaitingTeleportHereTask(player, target, Config.tpaTeleportationRequestExpirationDelay);
                        task.start();

                        player.sendMessage("§6[ §eTéléportation §6] §eDemande de tpahere envoyé à §6" + target.getName() + "§e.");
                        target.sendMessage("§6[ §eTéléportation §6] §eLe joueur §6" + player.getName() + "§e souhaite que vous vous téléportez à lui.");
                        TeleportUtils.sendTeleportAcceptationComponent(target, player);
                    }

                }
                else {
                    player.sendMessage("§6[ §eTéléportation §6] §eVous ne pouvez pas vous téléporter à tout le monde.");
                    return true;
                }
            }

            else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§6[ §eTéléportation §6] §eLe joueur n'est pas connecté.");
                    return true;
                }

                if (target == player) {
                    player.sendMessage("§6[ §eTéléportation §6] §eVous ne pouvez pas vous téléporter à vous même.");
                    return true;
                }

                WaitingTeleportHereTask task = new WaitingTeleportHereTask(player, target, 300);
                task.start();

                player.sendMessage("§6[ §eTéléportation §6] §eDemande de tpahere envoyé à §6" + target.getName() + "§e.");
                target.sendMessage("§6[ §eTéléportation §6] §eLe joueur §6" + player.getName() + "§e souhaite que vous vous téléportez à lui.");
                TeleportUtils.sendTeleportAcceptationComponent(target, player);
                return true;
            }
        }

        else {
            player.sendMessage("§6[ §eTéléportation §6] §eUsage: §6/tpahere <joueur>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player))
            return null;

        if(args.length == 1) {
            if(player.hasPermission(PermissionEnum.COMMAND_TP_ADMIN.getPermission())) {
                ArrayList<String> players = new ArrayList<>();
                players.add("all");
                players.add("*");
                for(Player target : Bukkit.getOnlinePlayers()) {
                    if(target == player) {
                        continue;
                    }
                    players.add(target.getName());
                }
                return players;
            }
            else {
                return null;
            }
        }

        return Arrays.asList("");
    }
}
