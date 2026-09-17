package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ComponentCommand extends AbstractCommand {

    public ComponentCommand() {
        super("component", "Command helper pour les components", "/component <show>", PermissionEnum.COMMAND_COMPONENT);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            MessageUtils.sendPlayerMessage(player, "§cVous devez avoir un item dans la main.");
        }
        else {
            if(args.length >= 1) {
                if (args[0].equalsIgnoreCase("show")) {
                    MessageUtils.sendPlayerMessage(player, player.getInventory().getItemInMainHand().getItemMeta().getAsComponentString());
                } else {
                    sendUsage(player);
                }
                return true;
            }
            sendUsage(player);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return null;

        return switch (args.length) {
            case 1 -> Arrays.asList("show");
            default -> Collections.emptyList();
        };
    }
}
