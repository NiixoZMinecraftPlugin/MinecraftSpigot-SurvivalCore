package fr.niixoz.survivalcore.commands.admin;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        Player target = (args.length >= 2) ? Bukkit.getPlayer(args[1]) : player;
        if (target == null) {
            MessageUtils.sendPlayerMessage(player, "§cLe joueur spécifié n'est pas connecté.");
            return true;
        }

        if(args[0].equalsIgnoreCase("reset")) {
            target.setWalkSpeed(0.2f);
            target.setFlySpeed(0.1f);
            MessageUtils.sendPlayerMessage(player, "Vitesse reset.");
            return true;
        }

        try {
            float speed = Float.parseFloat(args[0]) / 10f;
            if (speed < -1.0f || speed > 1.0f) {
                MessageUtils.sendPlayerMessage(player, "§cLa vitesse doit être comprise entre -10 et 10.");
                return true;
            }
            if(target.isFlying())
                target.setFlySpeed(speed);
            else
                target.setWalkSpeed(speed);

            MessageUtils.sendPlayerMessage(player, "Vitesse modifié avec succès.");
        }
        catch(NumberFormatException e) {
            MessageUtils.sendPlayerMessage(player, "§cValeur invalide. Veuillez entrer un nombre.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player))
            return null;

        if(args.length == 1) {
            return Arrays.asList("reset", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "-10", "-9", "-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1");
        }

        if(args.length == 2) {
            return Arrays.asList("");
        }

        return null;
    }
}
