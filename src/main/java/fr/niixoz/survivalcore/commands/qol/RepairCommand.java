package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.view.AnvilView;

import java.util.Arrays;
import java.util.List;

public class RepairCommand extends AbstractCommand {

    public RepairCommand() {
        super("repair", "Permet de réparer l'item en main.", "/repair [hand|all]", PermissionEnum.COMMAND_REPAIR);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        String type = args.length == 0 ? "hand" : args[0].toLowerCase();
        switch (type){
            case "hand" -> repairHand(player);
            case "all" -> repairAll(player);
        }

        return true;
    }


    private void repairHand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType().isAir()) {
            MessageUtils.sendPlayerMessage(player, "§cVous n'avez aucun item en main.");
            return;
        }

        if (!isRepairable(item)) {
            MessageUtils.sendPlayerMessage(player, "§cCet item ne peut pas être réparé.");
            return;
        }

        if (!repair(item)) {
            MessageUtils.sendPlayerMessage(player, "§cCet item est déjà en parfait état.");
            return;
        }

        player.getInventory().setItemInMainHand(item);
        MessageUtils.sendPlayerMessage(player, "Votre item a été réparé.");
    }


    private void repairAll(Player player) {

        if (!player.hasPermission(PermissionEnum.COMMAND_REPAIR_ALL.getPermission())) {
            MessageUtils.sendPlayerMessage(player,
                    "§cVous n'avez pas la permission de réparer tout votre inventaire.");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents(); // 41 slots : stockage + armure + offhand

        int repaired = 0;
        for (int slot = 0; slot < contents.length; slot++) {
            ItemStack item = contents[slot];
            if (item == null || !isRepairable(item)) continue;

            if (repair(item)) {
                inventory.setItem(slot, item);
                repaired++;
            }
        }

        if (repaired == 0) {
            MessageUtils.sendPlayerMessage(player, "§cAucun item à réparer dans votre inventaire.");
            return;
        }

        MessageUtils.sendPlayerMessage(player, repaired + " item(s) réparé(s).");
    }


    /** Un item est réparable s'il possède une durabilité et n'est pas incassable. */
    private boolean isRepairable(ItemStack item) {
        if (item.getType().getMaxDurability() <= 0) return false;
        if (!(item.getItemMeta() instanceof Damageable meta)) return false;
        return !meta.isUnbreakable();
    }

    /** Remet la durabilité à neuf. Retourne false si l'item n'était pas endommagé. */
    private boolean repair(ItemStack item) {
        if (!(item.getItemMeta() instanceof Damageable meta)) return false;
        if (!meta.hasDamage()) return false;

        meta.setDamage(0);
        item.setItemMeta(meta);
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return null;

        if(args.length == 1) {
            return null;
        }

        return Arrays.asList("");
    }
}
