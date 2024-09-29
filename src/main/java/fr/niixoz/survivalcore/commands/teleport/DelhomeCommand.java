package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.homes.Home;
import fr.niixoz.survivalcore.storage.players.SurvivalPlayer;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class DelhomeCommand extends AbstractCommand {

    public DelhomeCommand() {
        super("delhome", "Permet de supprimer un home", "/delhome", PermissionEnum.COMMAND_DELHOME);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) throws Exception {

        SurvivalPlayer enderPlayer = SurvivalPlayer.getPlayer(player);
        if(enderPlayer == null) {
            player.sendMessage("§cUne erreur est survenue lors de la récupération de votre profil.");
            return true;
        }

        if(args.length == 1) {
            if(enderPlayer.hasHome(args[0])) {
                enderPlayer.removeHome(args[0]);
                MessageUtils.sendPlayerMessage(player, "Le home " + args[0] + " a bien été supprimé.");
                return true;
            }
            else {
                MessageUtils.sendPlayerMessage(player, "Vous n'avez pas de home nommé " + args[0]);
            }
        }
        else {
            MessageUtils.sendPlayerMessage(player, "/delhome <home>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))
            return null;

        if(args.length == 1) {
            SurvivalPlayer player = SurvivalPlayer.getPlayer((Player) sender);
            if(player == null)
                return Arrays.asList("");

            return player.getHomes().stream().map(Home::getName).toList();
        }

        return Arrays.asList("");
    }
}
