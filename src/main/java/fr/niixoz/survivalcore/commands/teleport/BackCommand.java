package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.players.SurvivalPlayer;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BackCommand extends AbstractCommand {

    public BackCommand() {
        super("back", "Permet d'envoyer une demande de téléportation à un joueur", "/back", PermissionEnum.COMMAND_BACK);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        SurvivalPlayer enderPlayer = null;

        if(args.length > 0) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                enderPlayer = SurvivalPlayer.getPlayer(target);
                if(enderPlayer != null) {
                    enderPlayer.teleportToLastLocation();
                    MessageUtils.sendPlayerMessage(player, "Le joueur a été téléporté à sa dernière position.");
                    MessageUtils.sendPlayerMessage(enderPlayer.getPlayer(), "Vous avez été téléporté à votre dernière position.");
                }
                else {
                    MessageUtils.sendPlayerMessage(player, "Le joueur n'existe pas ou n'est pas connecté.");
                }
            }
        }
        else {
            enderPlayer = SurvivalPlayer.getPlayer(player);
            if (enderPlayer != null) {
                enderPlayer.teleportToLastLocation();
                MessageUtils.sendPlayerMessage(enderPlayer.getPlayer(), "Vous avez été téléporté à votre dernière position.");
            }
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
