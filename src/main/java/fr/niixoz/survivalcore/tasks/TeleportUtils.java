package fr.niixoz.survivalcore.tasks;

import fr.niixoz.survivalcore.SurvivalCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import org.bukkit.entity.Player;

public class TeleportUtils {

    public static void sendTeleportAcceptationComponent(Player player, Player target) {
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
    }

}
