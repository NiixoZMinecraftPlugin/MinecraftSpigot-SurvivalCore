package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.StonecutterView;

import java.util.Arrays;
import java.util.List;

public class StonecutterCommand extends AbstractCommand {

    public StonecutterCommand() {
        super("stonecutter", "Permet d'ouvrir un Stone Cutter.", "/stonecutter", PermissionEnum.COMMAND_STONECUTTER);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        StonecutterView view = MenuType.STONECUTTER.create(player, "Stonecutter Portable");
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
