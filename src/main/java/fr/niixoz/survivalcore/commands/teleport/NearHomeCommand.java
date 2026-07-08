package fr.niixoz.survivalcore.commands.teleport;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.homes.Home;
import fr.niixoz.survivalcore.storage.players.SurvivalPlayer;
import fr.niixoz.survivalcore.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class NearHomeCommand extends AbstractCommand {

    record HomeResult(String playerName, String homeName, double distance, double x, double y, double z) {}

    public NearHomeCommand() {
        super("nearhome", "Voir les homes proches de votre position.", "/nearhome <rayon>", PermissionEnum.COMMAND_NEARHOME);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) throws Exception {
        if (args.length == 0) {
            sendUsage(player);
            return true;
        }

        double radius;
        try {
            radius = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            MessageUtils.sendPlayerMessage(player, "§cLe rayon doit être un nombre valide.");
            return true;
        }

        if (radius <= 0) {
            MessageUtils.sendPlayerMessage(player, "§cLe rayon doit être supérieur à 0.");
            return true;
        }

        Location origin = player.getLocation();
        String worldName = origin.getWorld().getName();
        double radiusSq = radius * radius;

        List<HomeResult> results = new ArrayList<>();

        Set<UUID> processedUUIDs = new HashSet<>();
        for (SurvivalPlayer sp : SurvivalPlayer.players) {
            processedUUIDs.add(sp.getPlayer().getUniqueId());
            for (Home home : sp.getHomes()) {
                Location loc = home.getLocation();
                if (loc == null || loc.getWorld() == null) continue;
                if (!loc.getWorld().getName().equals(worldName)) continue;
                double distSq = loc.distanceSquared(origin);
                if (distSq <= radiusSq) {
                    results.add(new HomeResult(sp.getPlayer().getName(), home.getName(), Math.sqrt(distSq), loc.getX(), loc.getY(), loc.getZ()));
                }
            }
        }

        File infoFolder = SurvivalPlayer.getInfoFolder();
        File[] files = infoFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String uuidStr = file.getName().replace(".yml", "");
                UUID uuid;
                try {
                    uuid = UUID.fromString(uuidStr);
                } catch (IllegalArgumentException e) {
                    continue;
                }
                if (processedUUIDs.contains(uuid)) continue;

                FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                String playerName = cfg.getString("username", uuidStr);

                if (!cfg.contains("homes")) continue;
                for (String homeName : cfg.getConfigurationSection("homes").getKeys(false)) {
                    String prefix = "homes." + homeName;
                    if (!worldName.equals(cfg.getString(prefix + ".world"))) continue;
                    double hx = cfg.getDouble(prefix + ".x");
                    double hy = cfg.getDouble(prefix + ".y");
                    double hz = cfg.getDouble(prefix + ".z");
                    double dx = hx - origin.getX();
                    double dy = hy - origin.getY();
                    double dz = hz - origin.getZ();
                    double distSq = dx * dx + dy * dy + dz * dz;
                    if (distSq <= radiusSq) {
                        results.add(new HomeResult(playerName, homeName, Math.sqrt(distSq), hx, hy, hz));
                    }
                }
            }
        }

        if (results.isEmpty()) {
            MessageUtils.sendPlayerMessage(player, "Aucun home trouvé dans un rayon de " + args[0] + " blocs.");
            return true;
        }

        results.sort(Comparator.comparingDouble(HomeResult::distance));

        MessageUtils.sendPlayerMessage(player, results.size() + " home(s) trouvé(s) dans un rayon de " + args[0] + " blocs:");
        for (HomeResult result : results) {
            sendHomeComponent(player, result);
        }

        return true;
    }

    private void sendHomeComponent(Player player, HomeResult result) {
        String hoverText = "§9Cliquez pour vous téléporter\n" +
                "§9Player: " + result.playerName() + "\n" +
                "§9Home: " + result.homeName() + "\n" +
                "§9X: " + (int) result.x() +
                " §9Y: " + (int) result.y() +
                " §9Z: " + (int) result.z();

        TextComponent line = new TextComponent("");
        line.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/home " + result.playerName() + ":" + result.homeName()));
        line.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(new ComponentBuilder(hoverText).create())));

        line.addExtra(new ComponentBuilder("- ").color(ChatColor.BLUE).create()[0]);
        line.addExtra(new ComponentBuilder(result.playerName()).color(ChatColor.DARK_AQUA).create()[0]);
        line.addExtra(new ComponentBuilder(":").color(ChatColor.BLUE).create()[0]);
        line.addExtra(new ComponentBuilder(result.homeName()).color(ChatColor.DARK_AQUA).create()[0]);
        line.addExtra(new ComponentBuilder(" - " + String.format("%.1f", result.distance()) + " blocs")
                .color(ChatColor.BLUE).create()[0]);

        player.spigot().sendMessage(line);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("50", "100", "200", "500");
        }
        return Arrays.asList("");
    }
}