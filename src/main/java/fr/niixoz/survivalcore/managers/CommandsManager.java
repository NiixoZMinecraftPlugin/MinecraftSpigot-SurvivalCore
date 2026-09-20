package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.commands.*;
import fr.niixoz.survivalcore.commands.admin.GodCommand;
import fr.niixoz.survivalcore.commands.admin.InvseeCommand;
import fr.niixoz.survivalcore.commands.admin.SpeedCommand;
import fr.niixoz.survivalcore.commands.admin.VanishCommand;
import fr.niixoz.survivalcore.commands.cosmetic.CrawlCommand;
import fr.niixoz.survivalcore.commands.cosmetic.HatCommand;
import fr.niixoz.survivalcore.commands.cosmetic.LayCommand;
import fr.niixoz.survivalcore.commands.cosmetic.MountCommand;
import fr.niixoz.survivalcore.commands.cosmetic.SizeCommand;
import fr.niixoz.survivalcore.commands.cosmetic.SizeEntityCommand;
import fr.niixoz.survivalcore.commands.cosmetic.SitCommand;
import fr.niixoz.survivalcore.commands.cosmetic.SpinCommand;
import fr.niixoz.survivalcore.commands.qol.*;
import fr.niixoz.survivalcore.commands.storage.BackpackCommand;
import fr.niixoz.survivalcore.commands.teleport.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandsManager {

    private static final String FALLBACK_PREFIX = "survivalcore";
    private static final List<AbstractCommand> registered = new ArrayList<>();

    public static void registerCommands() {
        register(
                // Admin
                new CoreCommand(),
                new InvseeCommand(),
                new VanishCommand(),
                new GodCommand(),
                new SpeedCommand(),

                // Teleport
                new TpaCommand(),
                new TpAcceptCommand(),
                new TpDenyCommand(),
                new TpaHereCommand(),
                new HomeCommand(),
                new SethomeCommand(),
                new DelhomeCommand(),
                new HomesCommand(),
                new NearHomeCommand(),
                new BackCommand(),
                new SpawnCommand(),
                new SetSpawnCommand(),

                // QOL
                new RenameItemCommand(),
                new VisionCommand(),
                new FeedCommand(),
                new HealCommand(),
                new FlyCommand(),
                new CraftCommand(),
                new EnchantTableCommand(),
                new FurnaceCommand(),
                new EnderchestCommand(),
                new MendingCommand(),
                new AnvilCommand(),
                new LoomCommand(),
                new SmithingCommand(),
                new CartographyCommand(),
                new StonecutterCommand(),
                new GrindstoneCommand(),
                new TrashCommand(),
                new ComponentCommand(),
                new SleepCommand(),
                new RepairCommand(),

                // Storage
                new BackpackCommand(),

                // Cosmetic
                new HatCommand(),
                new SizeCommand(),
                new MountCommand(),
                new SizeEntityCommand(),
                new SitCommand(),
                new LayCommand(),
                new CrawlCommand(),
                new SpinCommand()
        );
    }

    private static void register(AbstractCommand... commands) {
        SurvivalCore plugin = SurvivalCore.getInstance();
        CommandMap map = commandMap();
        if (map == null) return;

        for (AbstractCommand cmd : commands) {
            Command conflict = map.getCommand(cmd.getName());
            if (conflict != null) {
                plugin.getLogger().warning("[Commands] /" + cmd.getName() + " déjà pris par "
                        + describeOwner(conflict) + " -> accessible via /"
                        + FALLBACK_PREFIX + ":" + cmd.getName());
            }
            map.register(FALLBACK_PREFIX, cmd);
            registered.add(cmd);
        }

        plugin.getLogger().info("[Commands] " + registered.size() + " commande(s) enregistrée(s).");
        syncCommands();
    }

    public static void unregisterCommands() {
        CommandMap map = commandMap();
        Map<String, Command> known = knownCommands(map);

        if (known != null) {
            for (AbstractCommand cmd : registered) {
                cmd.unregister(map);
                known.entrySet().removeIf(entry -> entry.getValue() == cmd);
            }
        } else {
            // on désenregistre quand même : la commande ne répondra plus,
            // même si son label reste visible dans le CommandMap
            for (AbstractCommand cmd : registered) {
                cmd.unregister(map);
            }
        }

        registered.clear();
        syncCommands();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Command> knownCommands(CommandMap map) {
        if (map == null) return null;
        try {
            // remonte la hiérarchie : CraftCommandMap -> SimpleCommandMap
            for (Class<?> clazz = map.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                try {
                    Field field = clazz.getDeclaredField("knownCommands");
                    field.setAccessible(true);
                    return (Map<String, Command>) field.get(map);
                } catch (NoSuchFieldException ignored) {
                    // champ sur la classe parente
                }
            }
        } catch (Exception e) {
            SurvivalCore.getInstance().getLogger()
                    .warning("[Commands] knownCommands inaccessible : " + e);
        }
        return null;
    }

    private static CommandMap commandMap() {
        try {
            return (CommandMap) Bukkit.getServer().getClass()
                    .getMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (Exception e) {
            SurvivalCore.getInstance().getLogger()
                    .severe("[Commands] CommandMap inaccessible : " + e);
            return null;
        }
    }

    /** Pousse les changements vers Brigadier, sinon le tab-complete client reste périmé. */
    private static void syncCommands() {
        try {
            Method sync = Bukkit.getServer().getClass().getDeclaredMethod("syncCommands");
            sync.setAccessible(true);
            sync.invoke(Bukkit.getServer());
        } catch (Exception ignored) {
            // syncCommands() n'existe pas sur toutes les implémentations, ce n'est pas bloquant
        }
        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }

    private static String describeOwner(Command command) {
        String owner = (command instanceof PluginCommand pc) ? pc.getPlugin().getName() : "?";
        return command.getClass().getSimpleName() + " (plugin: " + owner + ")";
    }
}
