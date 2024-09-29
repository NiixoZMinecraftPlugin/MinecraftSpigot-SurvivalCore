package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.location.Spawn;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class RenameItemCommand extends AbstractCommand {

    public RenameItemCommand() {
        super("rename", "Permet de renommer l'item dans la main", "/rename <name>", PermissionEnum.COMMAND_RENAME_ITEM);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            MessageUtils.sendPlayerMessage(player, "§cVous devez avoir un item dans la main.");
            return true;
        }
        else {
            if(args.length == 0) {
                MessageUtils.sendPlayerMessage(player, "§cVous devez spécifier un nom.");
                return true;
            }
            else {
                StringBuilder name = new StringBuilder();
                for (String arg : args) {
                    name.append(arg).append(" ");
                }
                ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                meta.setDisplayName(name.toString());
                player.getInventory().getItemInMainHand().setItemMeta(meta);
                MessageUtils.sendPlayerMessage(player, "§aL'item a été renommé en §e" + name.toString());
                return true;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Arrays.asList("");
    }
}
