package fr.niixoz.survivalcore.commands.cosmetic;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SizeCommand extends AbstractCommand {

    public SizeCommand() {
        super("size", "Permet de changer sa taille", "/size <xs | small | normal | big | xl>", PermissionEnum.COMMAND_SIZE);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        if (args.length != 1) {
            this.sendUsage(player);
            return true;
        }

        AttributeInstance sizeAttribute = player.getAttribute(Attribute.SCALE);
        if( sizeAttribute == null) {
            MessageUtils.sendPlayerMessage(player, "§cErreur : L'attribut de taille n'est pas disponible.");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "xs":
                sizeAttribute.setBaseValue(0.5);
                break;
            case "small":
                sizeAttribute.setBaseValue(0.75);
                break;
            case "big":
                sizeAttribute.setBaseValue(1.25);
                break;
            case "xl":
                sizeAttribute.setBaseValue(1.5);
                break;
            case "normal":
            case "reset":
            case "default":
                sizeAttribute.setBaseValue(1.0);
                break;
        }
        MessageUtils.sendPlayerMessage(player, "Votre taille a été changé en " + args[0].toLowerCase() + ".");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Arrays.asList("xs", "small", "big", "xl", "normal", "reset", "default");
    }
}
