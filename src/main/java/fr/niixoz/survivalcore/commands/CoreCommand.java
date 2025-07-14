package fr.niixoz.survivalcore.commands;

import fr.niixoz.survivalcore.config.Config;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.storage.location.Spawn;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.*;
import java.util.stream.Collectors;

public class CoreCommand extends AbstractCommand {

    public CoreCommand() {
        super("svcore", "Commande de SurvivalCore", "/svcore", PermissionEnum.PERMISSION_ALL);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        if(args.length >= 1) {
            switch (args[0]) {
                case "reload" -> {
                    Config.reload();
                    MessageUtils.sendPlayerMessage(player, "§aLe plugin a été rechargé avec succès !");
                    return true;
                }
                case "logger" -> MessageUtils.sendPlayerMessage(player, "§cComing Soon...");
                case "debug" -> handleDebug(player, Arrays.copyOfRange(args, 1, args.length));
                default -> sendPlayerInfo(player);
            }
        }
        else {
            sendPlayerInfo(player);
        }

        return true;
    }

    private void sendPlayerInfo(Player p) {
        MessageUtils.sendPlayerMessage(p, "/svcore <reload | logger | debug>");
    }

    private void handleDebug(Player player, String[] args) {

        if (!player.hasPermission("survivalcore.debug")) {
            MessageUtils.sendPlayerMessage(player, "§cVous n’avez pas la permission pour /svcore debug");
            return;
        }

        if (args.length == 0) {
            MessageUtils.sendPlayerMessage(player,
                    "Usage : /svcore debug <entitycount | loadedchunk> ...");
            return;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "entitycount" -> debugEntityCount(player, Arrays.copyOfRange(args, 1, args.length));
            case "loadedchunk" -> debugLoadedChunk(player, Arrays.copyOfRange(args, 1, args.length));
            default            -> MessageUtils.sendPlayerMessage(player,
                    "§cSous-commande inconnue : " + args[0]);
        }
    }

    private void debugEntityCount(Player player, String[] args) {

        if (args.length < 1) {
            MessageUtils.sendPlayerMessage(player,
                    "Usage : /svcore debug entitycount <radius> [animals|!monsters|!golem|!CREEPER…]");
            return;
        }

        int radius;
        try {
            radius = Integer.parseInt(args[0]);
            if (radius < 1) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            MessageUtils.sendPlayerMessage(player, "§cLe radius doit être un entier positif.");
            return;
        }

        Set<String> include = new HashSet<>();
        Set<String> exclude = new HashSet<>();

        Arrays.stream(args).skip(1).forEach(token -> {
            (token.startsWith("!") ? exclude : include)
                    .add(token.replace("!", "").toLowerCase(Locale.ROOT));
        });

        int count = 0;
        for (Entity e : player.getNearbyEntities(radius, radius, radius)) {
            String key = classify(e);

            if (!include.isEmpty() && !include.contains(key)) continue;
            if (exclude.contains(key)) continue;
            count++;
        }

        MessageUtils.sendPlayerMessage(player,
                count + " entité(s) dans un rayon de " + radius + " bloc(s).");
    }

    private String classify(Entity e) {
        if (e instanceof Animals) return "animals";
        if (e instanceof Monster) return "monsters";
        if (e instanceof Golem)   return "golem";
        return e.getType().name().toLowerCase(Locale.ROOT);   // exemple : creeper
    }

    private void debugLoadedChunk(Player player, String[] args) {
        boolean list = false;
        if (args.length >= 1) {
            if(args[0].equalsIgnoreCase("listed"))
                list = true;
        }
        MessageUtils.sendPlayerMessage(player, "Nombre de chunks chargés: ");
        for (World world : Bukkit.getWorlds()) {

            Chunk[] chunks = world.getLoadedChunks();
            if (chunks.length == 0) continue;

            if (!list) {
                MessageUtils.sendPlayerMessage(player, world.getName() + " (" + chunks.length + ")");
                continue;
            }

            String prefix = world.getName() + " (" + chunks.length + "): ";

            StringBuilder line = new StringBuilder(prefix);
            for (int i = 0; i < chunks.length; i++) {
                String coord = "(" + chunks[i].getX() + "," + chunks[i].getZ() + ")";
                if (i != 0) coord = ", " + coord;

                line.append(coord);
            }
            MessageUtils.sendPlayerMessage(player, line.toString());
        }
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();

        return switch (args.length) {
            case 1 -> Arrays.asList("reload", "logger", "debug")
                    .stream().filter(a -> a.startsWith(args[0].toLowerCase())).toList();
            case 2 -> "debug".equalsIgnoreCase(args[0])
                    ? Arrays.asList("entitycount", "loadedchunk").stream()
                    .filter(a -> a.startsWith(args[1].toLowerCase())).toList()
                    : Collections.emptyList();
            case 3 ->  // /svcore debug loadedchunk <arg>
                    "loadedchunk".equalsIgnoreCase(args[1])
                            ? Arrays.asList("listed")
                            .stream().filter(a -> a.startsWith(args[2].toLowerCase())).toList()
                            : Collections.emptyList();
            default -> Collections.emptyList();
        };
    }
}
