package fr.niixoz.survivalcore.commands.admin;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class SpeedCommand extends AbstractCommand {

    public SpeedCommand() {
        super("speed", "Permet de modifier son speed.", "/speed <reset|speed> [player]", PermissionEnum.COMMAND_SPEED);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(args.length == 0) {
            MessageUtils.sendPlayerMessage(player, "§cVeuillez spécifier un speed.");
            return true;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("reset")) {
                player.setWalkSpeed(0.2f);
                player.setFlySpeed(0.1f);
                return true;
            }
            if(player.isFlying()) {
                player.setFlySpeed((float) Integer.parseInt(args[0]) / 10);
            }
            else {
                player.setWalkSpeed((float) Integer.parseInt(args[0]) / 10);
            }
        }

        if(args.length == 2) {
            Player target = player.getServer().getPlayer(args[1]);
            if(target == null) {
                MessageUtils.sendPlayerMessage(player, "§cLe joueur spécifié n'est pas connecté.");
                return true;
            }
            if(target.isFlying()) {
                target.setFlySpeed(Float.parseFloat(args[0]));
            }
            else {
                target.setWalkSpeed(Float.parseFloat(args[0]));
            }
        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))
            return null;

        if(args.length == 1) {
            return Arrays.asList("reset", "1", "2", "3", "4", "5", "6", "7", "8", "9", "1");
        }

        if(args.length == 2) {
            return Arrays.asList("");
        }

        return null;
    }
}
