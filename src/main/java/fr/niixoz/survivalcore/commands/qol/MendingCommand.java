package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Arrays;
import java.util.List;

public class MendingCommand extends AbstractCommand {

    public MendingCommand() {
        super("mending", "Permet de réparer les outils mending avec ton exp.", "/mending", PermissionEnum.COMMAND_MENDING);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(player.getInventory().getItemInMainHand().getType().isAir()) {
            MessageUtils.sendPlayerMessage(player, "§cVous devez avoir un item dans la main.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if(!item.getItemMeta().hasEnchant(Enchantment.MENDING)) {
            MessageUtils.sendPlayerMessage(player, "§cCet objet ne possède pas l'enchantement Mending.");
            return true;
        }

        Damageable meta = (Damageable) item.getItemMeta();
        int damage = meta.getDamage();
        int maxDurability = item.getType().getMaxDurability();

        int expUsed = 0;

        while(damage > 0 && player.getTotalExperience() > 0) {
            // Consommer 1 point d'expérience
            player.giveExp(-1);
            expUsed++;
            // Réparer 2 points de durabilité
            damage -= 2;
        }
        if(damage < 0) damage = 0; // éviter valeur négative
        meta.setDamage(damage);
        item.setItemMeta(meta);


        if(damage == 0) {
            MessageUtils.sendPlayerMessage(player, "§aVotre objet a été entièrement réparé (+" + expUsed + " XP utilisés).");
        } else {
            MessageUtils.sendPlayerMessage(player, "§aVotre objet a été partiellement réparé (" + expUsed + " XP utilisés), mais il reste des dégâts.");
        }
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
