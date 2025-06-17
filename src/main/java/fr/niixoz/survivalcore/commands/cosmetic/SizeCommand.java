package fr.niixoz.survivalcore.commands.cosmetic;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.players.SurvivalPlayer;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SizeCommand extends AbstractCommand {

    public SizeCommand() {
        super("size", "Permet de changer sa taille", "", PermissionEnum.COMMAND_SIZE);
        this.usage = "/size <" + String.join(" | ", Config.sizes.keySet()) + ">";
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        AttributeInstance sizeAttribute = player.getAttribute(Attribute.SCALE);
        boolean otherPlayer = false;

        if (args.length == 0) {
            this.sendUsage(player);
            return true;
        }

        if(args.length == 2 && player.hasPermission(PermissionEnum.PERMISSION_ALL.getPermission())) {
            otherPlayer = true;
            Player p = Bukkit.getPlayer(args[1]);
            if(p == null) {
                MessageUtils.sendPlayerMessage(player, "§cErreur: Le joueur est introuvable.");
                return true;
            }
            sizeAttribute = p.getAttribute(Attribute.SCALE);
        }

        if (sizeAttribute == null) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: L'attribut de taille n'est pas disponible.");
            return true;
        }

        if (Config.sizes.get(args[0].toLowerCase()) == null) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Cette taille n'existe pas.");
            return true;
        }
        if (!player.hasPermission(PermissionEnum.COMMAND_SIZE.getPermission() + "." + args[0].toLowerCase())) {
            MessageUtils.sendPlayerMessage(player, "§cErreur: Tu n'a pas la permission pour cette taille.");
            return true;
        }

        sizeAttribute.setBaseValue(Config.getSize(args[0].toLowerCase()));
        if(otherPlayer)
            MessageUtils.sendPlayerMessage(player, "Vous avez changé la taille de " + args[1] + " en " + args[0].toLowerCase() + ".");
        else
            MessageUtils.sendPlayerMessage(player, "Votre taille a été changé en " + args[0].toLowerCase() + ".");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 2) {
            return null;
        }
        return Config.sizes.keySet().stream().filter(size -> sender.hasPermission(PermissionEnum.COMMAND_SIZE.getPermission() + "." + size)).toList();
    }
}
