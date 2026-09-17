package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.commands.*;
import fr.niixoz.survivalcore.commands.admin.InvseeCommand;
import fr.niixoz.survivalcore.commands.admin.SpeedCommand;
import fr.niixoz.survivalcore.commands.admin.VanishCommand;
import fr.niixoz.survivalcore.commands.cosmetic.HatCommand;
import fr.niixoz.survivalcore.commands.cosmetic.MountCommand;
import fr.niixoz.survivalcore.commands.cosmetic.SizeCommand;
import fr.niixoz.survivalcore.commands.cosmetic.SizeEntityCommand;
import fr.niixoz.survivalcore.commands.qol.*;
import fr.niixoz.survivalcore.commands.storage.BackpackCommand;
import fr.niixoz.survivalcore.commands.teleport.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

public class CommandsManager {

    public static void registerCommands() {
        // Admin
        registerCommand("svcore", new CoreCommand());
        registerCommand("invsee",  new InvseeCommand());
        registerCommand("vanish", new VanishCommand());

        // Teleport
        registerCommand("tpa", new TpaCommand());
        registerCommand("tpaccept", new TpAcceptCommand());
        registerCommand("tpdeny", new TpDenyCommand());
        registerCommand("tpahere", new TpaHereCommand());
        registerCommand("home", new HomeCommand());
        registerCommand("sethome", new SethomeCommand());
        registerCommand("delhome", new DelhomeCommand());
        registerCommand("homes", new HomesCommand());
        registerCommand("nearhome", new NearHomeCommand());
        registerCommand("back", new BackCommand());
        registerCommand("spawn", new SpawnCommand());
        registerCommand("setspawn", new SetSpawnCommand());

        // QOL
        registerCommand("rename", new RenameItemCommand());
        registerCommand("vision", new VisionCommand());
        registerCommand("feed", new FeedCommand());
        registerCommand("heal", new HealCommand());
        registerCommand("fly", new FlyCommand());
        registerCommand("craft", new CraftCommand());
        registerCommand("enchanting_table", new EnchantTableCommand());
        registerCommand("furnace", new FurnaceCommand());
        registerCommand("enderchest", new EnderchestCommand());
        registerCommand("mending", new MendingCommand());
        registerCommand("anvil", new AnvilCommand());
        registerCommand("loom", new LoomCommand());
        registerCommand("smithing", new SmithingCommand());
        registerCommand("cartography", new CartographyCommand());
        registerCommand("stonecutter", new StonecutterCommand());
        registerCommand("grindstone", new GrindstoneCommand());
        registerCommand("trash", new TrashCommand());
        registerCommand("component", new ComponentCommand());
        registerCommand("sleep", new SleepCommand());


        // Storage
        registerCommand("backpack", new BackpackCommand());

        // Admin
        registerCommand("speed", new SpeedCommand());

        // Cosmetic
        registerCommand("hat", new HatCommand());
        registerCommand("size", new SizeCommand());
        registerCommand("mount", new MountCommand());
        registerCommand("entitysize", new SizeEntityCommand());
    }

    private static void registerCommand(String commandName, CommandExecutor executor) {
        SurvivalCore plugin = SurvivalCore.getInstance();
        // getCommand() (et pas Bukkit.getPluginCommand()) : retombe sur "survivalcore:<cmd>"
        // et ne retourne que les commandes qui nous appartiennent, même si un autre
        // plugin (Essentials) a déjà réservé le label.
        PluginCommand pluginCommand = plugin.getCommand(commandName);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(executor);
            return;
        }
        plugin.getLogger().warning("[CommandManager] " + commandName + " not found! Verify plugin.yml! "
                + "(label \"" + commandName + "\" -> " + describeOwner(commandName)
                + ", label \"survivalcore:" + commandName + "\" -> " + describeOwner("survivalcore:" + commandName) + ")");
    }

    /** Décrit qui possède un label dans le CommandMap, pour diagnostiquer les conflits entre plugins. */
    private static String describeOwner(String label) {
        try {
            Object commandMap = Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
            Command command = (Command) commandMap.getClass()
                    .getMethod("getCommand", String.class).invoke(commandMap, label);
            if (command == null) {
                return "absent";
            }
            String owner = (command instanceof PluginCommand pc) ? pc.getPlugin().getName() : "?";
            return command.getClass().getSimpleName() + " (plugin: " + owner + ")";
        } catch (Exception e) {
            return "introuvable: " + e.getClass().getSimpleName();
        }
    }
}
