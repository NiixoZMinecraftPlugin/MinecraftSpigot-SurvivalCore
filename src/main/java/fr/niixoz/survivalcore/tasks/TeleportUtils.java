package fr.niixoz.survivalcore.tasks;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class TeleportUtils {

    public static void sendTeleportAcceptationComponent(Player player, Player target) {

        TextComponent accept = new TextComponent();
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + target.getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour accepter la téléportation").color(ChatColor.of("#51D948")).create()));
        accept.addExtra(new ComponentBuilder("[").color(ChatColor.of("#2FA327")).create()[0]);
        accept.addExtra(new ComponentBuilder("Accepter").color(ChatColor.of("#51D948")).create()[0]);
        accept.addExtra(new ComponentBuilder("]").color(ChatColor.of("#2FA327")).create()[0]);

        TextComponent deny = new TextComponent();
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + target.getName()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cliquez pour refuser la téléportation").color(ChatColor.of("#E03F42")).create()));
        deny.addExtra(new ComponentBuilder("[").color(ChatColor.of("#B52225")).create()[0]);
        deny.addExtra(new ComponentBuilder("Refuser").color(ChatColor.of("#E03F42")).create()[0]);
        deny.addExtra(new ComponentBuilder("]").color(ChatColor.of("#B52225")).create()[0]);

        TextComponent message = new TextComponent();
        message.addExtra(accept);
        message.addExtra("   ");
        message.addExtra(deny);

        player.spigot().sendMessage(message);
    }

}
