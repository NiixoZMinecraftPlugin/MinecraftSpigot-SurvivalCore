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
import org.bukkit.entity.*;

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
        if(e instanceof Player) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Tu ne peux pas changer la taille d'un autre joueur.");
            return true;
        }
        if(e instanceof Animals && !player.hasPermission(PermissionEnum.COMMAND_ENTITY_SIZE_ANIMALS.getPermission())) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Tu ne peux pas changer la taille des animaux.");
            return true;
        }
        if(e instanceof Monster && !player.hasPermission(PermissionEnum.COMMAND_ENTITY_SIZE_MONSTERS.getPermission())) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Tu ne peux pas changer la taille des monstres.");
            return true;
        }
        if(!(e instanceof Animals) && !(e instanceof Monster) && !player.hasPermission(PermissionEnum.COMMAND_ENTITY_SIZE_OTHER.getPermission())) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Tu ne peux pas changer de ce type d'entité.");
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
        if(!player.hasPermission(PermissionEnum.COMMAND_ENTITY_SIZE.getPermission() + "." + args[0].toLowerCase())) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Tu n'a pas la permission pour cette taille.");
            return true;
        }

        sizeAttribute.setBaseValue(Config.getSize(args[0].toLowerCase()));
        MessageUtils.sendPlayerMessage(player, "La taille de l'entité visée a été changée en " + args[0].toLowerCase() + ".");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 2) {
            return null;
        }
        return Config.sizes.keySet().stream().filter(size -> sender.hasPermission(PermissionEnum.COMMAND_ENTITY_SIZE.getPermission() + "." + size)).toList();
    }
}
