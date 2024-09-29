package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.homes.Home;
import fr.niixoz.survivalcore.storage.players.SurvivalPlayer;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class HomesCommand extends AbstractCommand {

    public HomesCommand() {
        super("home", "Permet de voir la liste de vos homes.", "/home", PermissionEnum.COMMAND_HOMES);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) throws Exception {

        SurvivalPlayer enderPlayer = null;
        if(args.length == 0) {
            enderPlayer = SurvivalPlayer.getPlayer(player);
            if(enderPlayer == null) {
                player.sendMessage("§cUne erreur est survenue lors de la récupération de votre profil.");
                return true;
            }

            String message = "";
            int homes = enderPlayer.getHomes().size();
            if(homes == 0) {
                message = "Vous n'avez aucun home.";
            }
            else {
                message += "Liste de vos homes: ";
                for(int i = 0; i < homes; i++) {
                    message += enderPlayer.getHomes().get(i).getName();
                    if(i != homes - 1)
                        message += ", ";
                }
            }

            MessageUtils.sendPlayerMessage(player, message);
        }
        else {
            if(args[0].equals("info"))
            {
                if (player.hasPermission(PermissionEnum.COMMAND_HOMES_ADMIN.getPermission()) && args.length == 2)
                {
                    if (Bukkit.getPlayer(args[1]) == null)
                    {
                        if(SurvivalPlayer.playersUUID.containsKey(args[1]))
                        {
                            SurvivalPlayer target = new SurvivalPlayer(SurvivalPlayer.playersUUID.get(args[1]));
                            MessageUtils.sendPlayerMessage(player, "§9Les homes de " + args[1] + ":");
                            for (Home home : target.getHomes()) {
                                player.sendMessage("§9• " + home.getName() + " - x:§3" + home.getLocation().getX() + "§9, y: §3" + home.getLocation().getY() + "§9, z: §3" + home.getLocation().getZ());
                            }
                        }
                        else {
                            MessageUtils.sendPlayerMessage(player, "§cLe joueur " + args[1] + " est introuvable.");
                        }
                        return true;
                    }
                    else
                    {
                        enderPlayer = SurvivalPlayer.getPlayer(Bukkit.getPlayer(args[1]));
                        if (enderPlayer == null)
                        {
                            MessageUtils.sendPlayerMessage(player, "§cUne erreur est survenue lors de la récupération du profil.");
                            return true;
                        }

                        player.sendMessage("§9Homes de " + args[1] + ":");
                        for (Home home : enderPlayer.getHomes())
                        {
                            player.sendMessage("§9" + home.getName() + " - x:§3" + home.getLocation().getX() + "§9, y: §3" + home.getLocation().getY() + "§9, z: §3" + home.getLocation().getZ());
                        }
                    }
                }
                else
                {
                    enderPlayer = SurvivalPlayer.getPlayer(player);
                    if (enderPlayer == null)
                    {
                        MessageUtils.sendPlayerMessage(player, "§cUne erreur est survenue lors de la récupération de votre profil.");
                        return true;
                    }

                    MessageUtils.sendPlayerMessage(player, "§9Vos homes:");
                    for (Home home : enderPlayer.getHomes()) {
                        player.sendMessage("§9• " + home.getName() + " - x:§3" + home.getLocation().getX() + "§9, y: §3" + home.getLocation().getY() + "§9, z: §3" + home.getLocation().getZ());
                    }
                }
            }

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return null;

        if (args.length == 1) {
            return Arrays.asList("info");
        }

        if(args.length == 2) {
            return SurvivalPlayer.playersUUID.keySet().stream().filter(name -> name.toUpperCase().startsWith(args[1].toUpperCase())).toList();
        }

        return Arrays.asList("");
    }
}
