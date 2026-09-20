package fr.niixoz.survivalcore.commands;

import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand extends Command implements TabCompleter {

    protected boolean executeOnConsole;

    protected AbstractCommand(String name, String description, String usage, PermissionEnum permission) {
        this(name, description, usage, permission, List.of());
    }

    protected AbstractCommand(String name, String description, String usage,
                              PermissionEnum permission, List<String> aliases) {
        super(name, description, usage, aliases);   // constructeur protected de Command
        setPermission(permission.getPermission());
        this.executeOnConsole = false;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            if (!executeOnConsole) sender.sendMessage("§cCette commande n'est utilisable qu'en jeu.");
            return true;
        }
        try {
            if (!sender.hasPermission(getPermission())) {
                player.sendMessage("§cTu n'as pas la permission pour exécuter cette commande.");
                return true;
            }
            return executeCommand(player, this, label, args);
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Config.errorPrefix + Config.errorMessage);
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        List<String> result = onTabComplete(sender, this, alias, args);
        return result != null ? result : super.tabComplete(sender, alias, args); // noms de joueurs
    }

    public abstract boolean executeCommand(Player player, Command command, String s, String[] args) throws Exception;

    public void sendUsage(CommandSender sender) {
        sender.sendMessage("§cUtilisation de la commande: " + getUsage());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null; // -> fallback noms de joueurs
    }
}