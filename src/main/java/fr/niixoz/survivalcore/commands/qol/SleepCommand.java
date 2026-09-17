package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.managers.SleepManager;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SleepCommand extends AbstractCommand {

    public SleepCommand() {
        super("sleep", "Permet de s'endormir.", "/sleep", PermissionEnum.COMMAND_SLEEP);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {
        SleepManager.Result result = SleepManager.sleep(player);

        switch(result) {
            case SUCCESS -> MessageUtils.sendPlayerMessage(player, "§7Tu t'endors sur le sol tel un sauvage.");
            case WRONG_WORLD -> MessageUtils.sendPlayerMessage(player, "§cImpossible de dormir dans cette dimension.");
            case NOT_NIGHT -> MessageUtils.sendPlayerMessage(player, "§cTu ne peux dormir que la nuit ou pendant un orage.");
            case ALREADY_SLEEPING -> MessageUtils.sendPlayerMessage(player, "§cTu dors déjà.");
            case NOT_GROUNDED -> MessageUtils.sendPlayerMessage(player, "§cTu dois être posé au sol pour t'allonger.");
            case NO_SPACE -> MessageUtils.sendPlayerMessage(player, "§cPas assez de place pour t'allonger ici.");
            case NO_FLOOR -> MessageUtils.sendPlayerMessage(player, "§cIl te faut un sol solide sous les pieds.");
            case FAILED -> MessageUtils.sendPlayerMessage(player, "§cImpossible de t'endormir ici.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
