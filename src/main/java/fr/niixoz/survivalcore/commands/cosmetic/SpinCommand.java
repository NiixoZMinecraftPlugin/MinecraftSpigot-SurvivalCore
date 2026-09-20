package fr.niixoz.survivalcore.commands.cosmetic;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.managers.CosmeticManager;
import fr.niixoz.survivalcore.managers.SpinManager;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SpinCommand extends AbstractCommand {

    public SpinCommand() {
        super("spin", "Permet de faire la vrille du trident.", "/spin", PermissionEnum.COMMAND_SPIN);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        if(SpinManager.isSpinning(player)) {
            SpinManager.stop(player);
            MessageUtils.sendPlayerMessage(player, "Tu arrêtes de tourner.");
            return true;
        }

        // Une seule posture à la fois.
        CosmeticManager.stopAll(player);

        if(!SpinManager.start(player)) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Impossible de faire la vrille en spectateur.");
            return true;
        }

        MessageUtils.sendPlayerMessage(player, "Tu fais la vrille du trident. Refais /spin pour t'arrêter.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
