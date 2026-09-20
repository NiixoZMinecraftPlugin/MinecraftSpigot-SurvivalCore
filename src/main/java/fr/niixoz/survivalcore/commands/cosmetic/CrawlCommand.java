package fr.niixoz.survivalcore.commands.cosmetic;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.managers.CosmeticManager;
import fr.niixoz.survivalcore.managers.GroundPoseManager;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CrawlCommand extends AbstractCommand {

    public CrawlCommand() {
        super("crawl", "Permet de ramper au sol.", "/crawl", PermissionEnum.COMMAND_CRAWL, List.of("ramper"));
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        if(GroundPoseManager.isMode(player, GroundPoseManager.Mode.CRAWL)) {
            GroundPoseManager.stop(player);
            MessageUtils.sendPlayerMessage(player, "Tu te relèves.");
            return true;
        }

        // Une seule posture à la fois.
        CosmeticManager.stopAll(player);

        switch(GroundPoseManager.start(player, GroundPoseManager.Mode.CRAWL)) {
            case SUCCESS -> MessageUtils.sendPlayerMessage(player, "Tu rampes. Refais /crawl pour te relever.");
            case ALREADY_DOWN -> MessageUtils.sendPlayerMessage(player, "§cErreur: Tu es déjà au sol.");
            case IN_VEHICLE -> MessageUtils.sendPlayerMessage(player, "§cErreur: Tu ne peux pas ramper sur une monture.");
            case NOT_GROUNDED -> MessageUtils.sendPlayerMessage(player, "§cErreur: Tu dois avoir les pieds au sol.");
            case NO_SPACE -> MessageUtils.sendPlayerMessage(player, "§cErreur: Pas assez de place au-dessus de ta tête.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
