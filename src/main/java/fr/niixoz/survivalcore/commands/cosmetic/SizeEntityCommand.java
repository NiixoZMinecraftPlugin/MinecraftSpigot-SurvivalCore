package fr.niixoz.survivalcore.commands.cosmetic;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import fr.niixoz.survivalcore.utils.PlayerUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SizeEntityCommand extends AbstractCommand {

    public SizeEntityCommand() {
        super("entitysize", "Permet de changer la taille de l'entité visée", "", PermissionEnum.COMMAND_ENTITY_SIZE);
        this.usage = "/entitysize <" + String.join(" | ", Config.sizes.keySet()) + ">";
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        if (args.length != 1) {
            this.sendUsage(player);
            return true;
        }

        Entity e = PlayerUtils.getEntityLookingAt(player, 5);
        if(!(e instanceof LivingEntity)) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Aucune entité visée.");
            return true;
        }

        AttributeInstance sizeAttribute = ((LivingEntity) e).getAttribute(Attribute.SCALE);
        if( sizeAttribute == null) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: L'attribut de taille n'est pas disponible.");
            return true;
        }
        if(Config.sizes.get(args[0].toLowerCase()) == null) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Cette taille n'existe pas.");
            return true;
        }
        if(!player.hasPermission(PermissionEnum.COMMAND_SIZE + "." + args[0].toLowerCase())) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Tu na pas la permission pour cette taille.");
            return true;
        }

        sizeAttribute.setBaseValue(Config.getSize(args[0].toLowerCase()));
        MessageUtils.sendPlayerMessage(player, "La taille de l'entité visée a été changée en " + args[0].toLowerCase() + ".");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Config.sizes.keySet().stream().toList();
    }
}
