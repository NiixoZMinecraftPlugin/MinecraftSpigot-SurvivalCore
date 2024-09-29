package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class VisionCommand extends AbstractCommand {

    public VisionCommand() {
        super("vision", "Permet d'activer ou désactiver la vision nocturne.", "/vision", PermissionEnum.COMMAND_VISION);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION) && player.getPotionEffect(PotionEffectType.NIGHT_VISION).isInfinite()) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            MessageUtils.sendPlayerMessage(player, "§aLa vision nocturne a été désactivée.");
        }
        else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 1));
            MessageUtils.sendPlayerMessage(player, "§aLa vision nocturne a été activée.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Arrays.asList("");
    }
}
