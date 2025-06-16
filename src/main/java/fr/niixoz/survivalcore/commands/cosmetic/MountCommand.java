package fr.niixoz.survivalcore.commands.cosmetic;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import fr.niixoz.survivalcore.utils.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MountCommand extends AbstractCommand {

    public MountCommand() {
        super("mount", "Permet de monter sur l'entité visée", "/mount", PermissionEnum.COMMAND_MOUNT);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        if (args.length != 0) {
            this.sendUsage(player);
            return true;
        }

        if (player.getVehicle() != null) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Vous êtes déjà sur un véhicule.");
            return true;
        }

        Entity e = PlayerUtils.getEntityLookingAt(player, 5);
        if(e instanceof LivingEntity) {
            e.addPassenger(player);
            return true;
        }

        MessageUtils.sendPlayerMessage(player, "§cErreur: Aucune entité visée.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Arrays.asList("");
    }
}
