package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.inventory.view.LoomView;

import java.util.Arrays;
import java.util.List;

public class LoomCommand extends AbstractCommand {

    public LoomCommand() {
        super("loom", "Permet d'ouvrir un Loom.", "/lom", PermissionEnum.COMMAND_LOOM);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        LoomView view = MenuType.LOOM.create(player, "Loom Portable");
        player.openInventory(view);

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
