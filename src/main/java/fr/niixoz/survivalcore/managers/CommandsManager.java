package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.SurvivalCore;
import fr.niixoz.survivalcore.commands.*;
import fr.niixoz.survivalcore.commands.admin.SpeedCommand;
import fr.niixoz.survivalcore.commands.cosmetic.HatCommand;
import fr.niixoz.survivalcore.commands.cosmetic.MountCommand;
import fr.niixoz.survivalcore.commands.cosmetic.SizeCommand;
import fr.niixoz.survivalcore.commands.qol.*;
import fr.niixoz.survivalcore.commands.storage.BackpackCommand;
import fr.niixoz.survivalcore.commands.teleport.*;

public class CommandsManager {

    public static void registerCommands() {
        SurvivalCore plugin = SurvivalCore.getInstance();
        plugin.getCommand("svcore").setExecutor(new CoreCommand());

        // Teleport
        plugin.getCommand("tpa").setExecutor(new TpaCommand());
        plugin.getCommand("tpaccept").setExecutor(new TpAcceptCommand());
        plugin.getCommand("tpdeny").setExecutor(new TpDenyCommand());
        plugin.getCommand("tpahere").setExecutor(new TpaHereCommand());
        plugin.getCommand("home").setExecutor(new HomeCommand());
        plugin.getCommand("sethome").setExecutor(new SethomeCommand());
        plugin.getCommand("delhome").setExecutor(new DelhomeCommand());
        plugin.getCommand("homes").setExecutor(new HomesCommand());
        plugin.getCommand("back").setExecutor(new BackCommand());
        plugin.getCommand("spawn").setExecutor(new SpawnCommand());
        plugin.getCommand("setspawn").setExecutor(new SetSpawnCommand());

        // QOL
        plugin.getCommand("rename").setExecutor(new RenameItemCommand());
        plugin.getCommand("vision").setExecutor(new VisionCommand());
        plugin.getCommand("feed").setExecutor(new FeedCommand());
        plugin.getCommand("heal").setExecutor(new HealCommand());
        plugin.getCommand("fly").setExecutor(new FlyCommand());
        plugin.getCommand("craft").setExecutor(new CraftCommand());
        plugin.getCommand("enchanting_table").setExecutor(new EnchantTableCommand());
        plugin.getCommand("furnace").setExecutor(new FurnaceCommand());
        plugin.getCommand("enderchest").setExecutor(new EnderchestCommand());

        // Storage
        plugin.getCommand("backpack").setExecutor(new BackpackCommand());

        // Admin
        plugin.getCommand("speed").setExecutor(new SpeedCommand());

        // Cosmetic
        plugin.getCommand("hat").setExecutor(new HatCommand());
        plugin.getCommand("size").setExecutor(new SizeCommand());
        plugin.getCommand("mount").setExecutor(new MountCommand());
    }
}
