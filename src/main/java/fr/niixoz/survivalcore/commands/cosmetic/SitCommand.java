package fr.niixoz.survivalcore.commands.cosmetic;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.managers.CosmeticManager;
import fr.niixoz.survivalcore.managers.SeatManager;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SitCommand extends AbstractCommand {

    public SitCommand() {
        super("sit", "Permet de s'asseoir sur place.", "/sit", PermissionEnum.COMMAND_SIT, List.of("assis"));
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        if(SeatManager.isSitting(player)) {
            SeatManager.stand(player);
            MessageUtils.sendPlayerMessage(player, "Tu te relèves.");
            return true;
        }

        // Une seule posture à la fois.
        CosmeticManager.stopAll(player);

        switch(SeatManager.sit(player)) {
            case SUCCESS -> MessageUtils.sendPlayerMessage(player, "Tu t'assieds. Accroupis-toi ou refais /sit pour te relever.");
            case ALREADY_SITTING -> MessageUtils.sendPlayerMessage(player, "§cErreur: Tu es déjà assis.");
            case IN_VEHICLE -> MessageUtils.sendPlayerMessage(player, "§cErreur: Tu es déjà sur une monture.");
            case NOT_GROUNDED -> MessageUtils.sendPlayerMessage(player, "§cErreur: Tu dois avoir les pieds au sol.");
            case FAILED -> MessageUtils.sendPlayerMessage(player, "§cErreur: Impossible de s'asseoir ici.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
