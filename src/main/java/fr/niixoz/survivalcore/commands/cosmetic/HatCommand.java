package fr.niixoz.survivalcore.commands.cosmetic;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class HatCommand extends AbstractCommand {

    public HatCommand() {
        super("hat", "Permet de mettre un item sur la tête", "/hat", PermissionEnum.COMMAND_HAT);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            MessageUtils.sendPlayerMessage(player, "§cVous devez avoir un item dans la main.");
            return true;
        }
        else {
            ItemStack itemHand = player.getInventory().getItemInMainHand();
            ItemStack itemHead = player.getInventory().getHelmet();
            player.getInventory().setItemInMainHand(itemHead);
            player.getInventory().setHelmet(itemHand);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Arrays.asList("");
    }
}
