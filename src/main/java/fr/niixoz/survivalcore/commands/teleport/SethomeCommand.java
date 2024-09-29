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

public class SethomeCommand extends AbstractCommand {

    public SethomeCommand() {
        super("sethome", "Permet de poser un home", "/sethome", PermissionEnum.COMMAND_SETHOME);
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
                MessageUtils.sendPlayerMessage(player, "Vous avez déjà un home nommé " + args[0]);
                return true;
            }
            else {
                int maxHomes = Home.getHomeLimit(player);
                if(enderPlayer.getHomes().size() < maxHomes || player.hasPermission(PermissionEnum.PERMISSION_ALL.getPermission()) || player.hasPermission(PermissionEnum.HOME_LIMIT_BYPASS.getPermission())) {
                    enderPlayer.addHome(args[0], player.getLocation());
                    MessageUtils.sendPlayerMessage(player, "Votre home " + args[0] + " a été créé avec succès !");
                }
                else {
                    MessageUtils.sendPlayerMessage(player, "Vous avez atteint le nombre maximum de homes (" + maxHomes + ")");
                }
                return true;
            }
        }
        else {
            MessageUtils.sendPlayerMessage(player, "/sethome <home>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))
            return null;

        return Arrays.asList("");
    }
}
