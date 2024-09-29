package fr.niixoz.survivalcore.commands.storage;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.players.SurvivalPlayer;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class BackpackCommand extends AbstractCommand {

    public BackpackCommand() {
        super("backpack", "Permet d'ouvrir votre sac à dos.", "/backpack", PermissionEnum.COMMAND_BACKPACK);
    }

    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        SurvivalPlayer enderPlayer = null;
        if(args.length > 0) {
            /*
            Open player backpack as Admin
             */
        }
        else {
            enderPlayer = SurvivalPlayer.getPlayer(player);
            if (enderPlayer != null) {
                player.openInventory(enderPlayer.getBackpack());
                MessageUtils.sendPlayerMessage(enderPlayer.getPlayer(), "Vous venez d'ouvrir votre sac à dos.");
            }
        }

        return true;
    }
}
