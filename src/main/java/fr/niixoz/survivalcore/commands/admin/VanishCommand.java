package fr.niixoz.survivalcore.commands.admin;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.managers.VanishManager;
import fr.niixoz.survivalcore.managers.VanishMode;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VanishCommand extends AbstractCommand {

    public VanishCommand() {
        super("vanish", "Permet de devenir totalement invisible.", "/vanish [quiet|fake] [player]", PermissionEnum.COMMAND_ADMIN_VANISH, List.of("v"));
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        Player target = player;
        VanishMode mode = VanishMode.getDefault();
        int index = 0;

        // Le mode est optionnel : sans lui, args[0] est directement le joueur ciblé.
        if(args.length > index) {
            VanishMode argumentMode = VanishMode.fromArgument(args[index]);
            if(argumentMode != null) {
                mode = argumentMode;
                index++;
            }
        }

        if(args.length > index) {
            if(!player.hasPermission(PermissionEnum.COMMAND_ADMIN_VANISH_OTHERS.getPermission())) {
                MessageUtils.sendPlayerMessage(player, "§cTu n'as pas la permission de modifier le vanish d'un autre joueur.");
                return true;
            }
            target = Bukkit.getPlayer(args[index]);
            if(target == null) {
                MessageUtils.sendPlayerMessage(player, "§cLe joueur " + args[index] + " n'est pas connecté.");
                return true;
            }
            index++;
        }

        if(args.length > index) {
            sendUsage(player);
            return true;
        }

        boolean vanish = !VanishManager.isVanished(target);
        VanishManager.setVanished(target, vanish, mode);

        MessageUtils.sendPlayerMessage(target, "Le vanish a été " + (vanish ? "activé" : "désactivé") + " !");
        if(!target.equals(player)) {
            MessageUtils.sendPlayerMessage(player, "Vous avez " + (vanish ? "activé" : "désactivé") + " le vanish de " + target.getName() + " !");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player) || !sender.hasPermission(getPermission()))
            return Collections.emptyList();

        // Premier argument : le mode, ou directement un joueur.
        if(args.length == 1) {
            List<String> completions = new ArrayList<>(Arrays.asList("quiet", "fake"));
            for(Player online : Bukkit.getOnlinePlayers()) {
                completions.add(online.getName());
            }
            return completions;
        }

        if(args.length == 2 && VanishMode.fromArgument(args[0]) != null) {
            return null;
        }

        return Arrays.asList("");
    }
}