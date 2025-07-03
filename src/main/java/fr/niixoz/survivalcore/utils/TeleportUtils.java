package fr.niixoz.survivalcore.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;

public class TeleportUtils {

    public static void sendTeleportAcceptationComponent(Player player, Player target) {

        TextComponent accept = new TextComponent("");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + target.getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder("Cliquez pour accepter la téléportation").color(ChatColor.of("#51D948")).create())));
        accept.addExtra(new ComponentBuilder("[").color(ChatColor.of("#2FA327")).create()[0]);
        accept.addExtra(new ComponentBuilder("Accepter").color(ChatColor.of("#51D948")).create()[0]);
        accept.addExtra(new ComponentBuilder("]").color(ChatColor.of("#2FA327")).create()[0]);

        TextComponent deny = new TextComponent("");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + target.getName()));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder("Cliquez pour refuser la téléportation").color(ChatColor.of("#E03F42")).create())));
        deny.addExtra(new ComponentBuilder("[").color(ChatColor.of("#B52225")).create()[0]);
        deny.addExtra(new ComponentBuilder("Refuser").color(ChatColor.of("#E03F42")).create()[0]);
        deny.addExtra(new ComponentBuilder("]").color(ChatColor.of("#B52225")).create()[0]);

        TextComponent message = new TextComponent();
        message.addExtra(accept);
        message.addExtra("   ");
        message.addExtra(deny);

        player.spigot().sendMessage(message);



        /*
                Component accept = Component.text("[")
                .color(TextColor.fromHexString("#2FA327"))
                .append(Component.text("Accepter")
                        .color(TextColor.fromHexString("#51D948"))
                        .clickEvent(ClickEvent.runCommand("/tpaccept " + target.getName()))
                        .hoverEvent(HoverEvent.showText(Component.text("Cliquez pour accepter la téléportation")
                                .color(TextColor.fromHexString("#51D948")))))
                .append(Component.text("]").color(TextColor.fromHexString("#2FA327")));

        Component deny = Component.text("[")
                .color(TextColor.fromHexString("#B52225"))
                .append(Component.text("Refuser")
                        .color(TextColor.fromHexString("#E03F42"))
                        .clickEvent(ClickEvent.runCommand("/tpdeny " + target.getName()))
                        .hoverEvent(HoverEvent.showText(Component.text("Cliquez pour refuser la téléportation")
                                .color(TextColor.fromHexString("#E03F42")))))
                .append(Component.text("]").color(TextColor.fromHexString("#B52225")));

        Component message = Component.text().append(accept)
                .append(Component.text("   "))
                .append(deny)
                .build();

        SurvivalCore.getInstance().getAdventure().player(player).sendMessage(message);
         */
    }

}
