package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.homes.Home;
import fr.niixoz.survivalcore.storage.players.SurvivalPlayer;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class HomeCommand extends AbstractCommand {

    public HomeCommand() {
        super("home", "Permet de se téléporter à un home", "/home", PermissionEnum.COMMAND_HOME);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) throws Exception {

        SurvivalPlayer enderPlayer = SurvivalPlayer.getPlayer(player);
        if(enderPlayer == null) {
            player.sendMessage("§cUne erreur est survenue lors de la récupération de votre profil.");
            return true;
        }

        if(args.length == 0 && enderPlayer.hasHome("home") && enderPlayer.getHomes().size() == 1) {
            enderPlayer.teleportToHome("home");
            return true;
        }

        if(args.length == 1) {
            if(args[0].contains(":") && player.hasPermission(PermissionEnum.PERMISSION_ALL.getPermission())) {
                String[] split = args[0].split(":");
                if (split.length != 2) {
                    MessageUtils.sendPlayerMessage(player, "§c/home <joueur:home>");
                    return true;
                }
                if (!SurvivalPlayer.playersUUID.containsKey(split[0])) {
                    MessageUtils.sendPlayerMessage(player, "§cLe joueur " + split[0] + " n'existe pas.");
                    return true;
                }

                enderPlayer = new SurvivalPlayer(SurvivalPlayer.playersUUID.get(split[0]));
                if (!enderPlayer.hasHome(split[1])) {
                    MessageUtils.sendPlayerMessage(player, "§cLe joueur " + split[0] + " n'a pas de home nommé " + split[1]);
                    return true;
                }

                player.teleport(enderPlayer.getHome(split[1]).getLocation());
                MessageUtils.sendPlayerMessage(player, "Vous avez été téléporté au home " + split[1] + " de " + split[0]);
                return true;
            }
            else if(enderPlayer.hasHome(args[0])) {
                enderPlayer.teleportToHome(args[0]);
                return true;
            }
            else {
                MessageUtils.sendPlayerMessage(player, "Vous n'avez pas de home nommé " + args[0]);
                return true;
            }
        }
        else {
            MessageUtils.sendPlayerMessage(player, "/home <home>");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player p))
            return null;

        if(args.length == 1)
        {
            if(args[0].contains(":") && p.hasPermission(PermissionEnum.PERMISSION_ALL.getPermission()))
            {
                String[] split = args[0].split(":");
                if(split.length > 2)
                    return Arrays.asList("");
                if(!SurvivalPlayer.playersUUID.containsKey(split[0]))
                    return Arrays.asList("");

                SurvivalPlayer player = new SurvivalPlayer(SurvivalPlayer.playersUUID.get(split[0]));
                return player.getHomes().stream().map(home -> split[0] + ":" + home.getName()).filter(home -> home.toUpperCase().startsWith(args[0].toUpperCase())).toList();
            }
            else {
                SurvivalPlayer player = SurvivalPlayer.getPlayer((Player) sender);
                if (player == null)
                    return Arrays.asList("");
                if(p.hasPermission(PermissionEnum.PERMISSION_ALL.getPermission())) {
                    List<String> list = new java.util.ArrayList<>(player.getHomes().stream().map(Home::getName).filter(home -> home.toUpperCase().startsWith(args[0].toUpperCase())).toList());
                    list.addAll(SurvivalPlayer.players.stream().map(enderPlayer -> enderPlayer.getPlayer().getName()).filter(name -> name.toUpperCase().startsWith(args[0].toUpperCase())).toList());
                    return list;
                }
                return player.getHomes().stream().map(Home::getName).filter(home -> home.startsWith(args[0])).toList();
            }
        }

        return Arrays.asList("");
    }
}
