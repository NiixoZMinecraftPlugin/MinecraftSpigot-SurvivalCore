package fr.niixoz.survivalcore.commands;

import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.location.Spawn;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CoreCommand extends AbstractCommand {

    public CoreCommand() {
        super("svcore", "Commande de SurvivalCore", "/svcore", PermissionEnum.PERMISSION_ALL);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(args.length == 1) {
            switch (args[0]) {
                case "reload":
                    Config.reload();
                    MessageUtils.sendPlayerMessage(player, "§aLe plugin a été rechargé avec succès !");
                    return true;
                case "logger":
                    MessageUtils.sendPlayerMessage(player, "§cComing Soon...");
                    return true;
                default:
                    sendPlayerInfo(player);
                    return true;
            }
        }
        else {
            sendPlayerInfo(player);
        }

        return true;
    }

    private void sendPlayerInfo(Player p) {
        MessageUtils.sendPlayerMessage(p, "/svcore <reload | logger>");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        return Arrays.asList("reload", "logger").stream().filter(a -> a.startsWith(args[0])).toList();
    }


}
