package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.location.Spawn;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetSpawnCommand extends AbstractCommand {

    public SetSpawnCommand() {
        super("setspawn", "Permet de poser le spawn", "/setspawn", PermissionEnum.PERMISSION_ALL);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) throws Exception {
        Spawn.modifySpawnLocation(player.getLocation());
        MessageUtils.sendPlayerMessage(player, "La position du spawn a été modifié !");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))
            return null;

        return Arrays.asList("");
    }
}
