package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MendingCommand extends AbstractCommand {

    public MendingCommand() {
        super("mending", "Permet de réparer les outils mending avec ton exp.", "/mending", PermissionEnum.COMMAND_HEAL);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        MessageUtils.sendPlayerMessage(player, "Coming Soon...");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return null;

        if(args.length == 1) {
            return null;
        }

        return Arrays.asList("");
    }
}
