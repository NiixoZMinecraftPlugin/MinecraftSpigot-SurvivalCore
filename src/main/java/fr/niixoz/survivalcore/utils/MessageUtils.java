package fr.niixoz.survivalcore.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static void sendPlayerMessage(Player player, String message) {

        TextComponent comp = new TextComponent();
        comp.addExtra(new ComponentBuilder("[").color(ChatColor.of("#5D1AA1")).create()[0]);
        comp.addExtra(new ComponentBuilder("EnderCube").color(ChatColor.of("#A320E6")).create()[0]);
        comp.addExtra(new ComponentBuilder("]").color(ChatColor.of("#5D1AA1")).create()[0]);
        comp.addExtra(new ComponentBuilder(" " + message).color(ChatColor.of("#B5B5B5")).create()[0]);

        player.spigot().sendMessage(comp);

    }
}
