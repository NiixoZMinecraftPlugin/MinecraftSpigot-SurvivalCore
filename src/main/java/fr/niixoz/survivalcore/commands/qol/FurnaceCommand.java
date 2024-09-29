package fr.niixoz.survivalcore.commands.qol;

import fr.niixoz.survivalcore.commands.AbstractCommand;
import fr.niixoz.survivalcore.permissions.PermissionEnum;
import fr.niixoz.survivalcore.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Furnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FurnaceCommand extends AbstractCommand {

    public FurnaceCommand() {
        super("furnace", "Permet de faire cuire les items dans la main.", "/furnace", PermissionEnum.COMMAND_FURNACE);
    }

    @Override
    public boolean executeCommand(Player player, Command command, String s, String[] args) {

        ItemStack result;
        final ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType().isAir()) {
            MessageUtils.sendPlayerMessage(player, "§cVous devez avoir un item dans la main.");
            return true;
        }
        final Iterator<Recipe> it = Bukkit.recipeIterator();
        while(it.hasNext()) {
            final Recipe recipe = it.next();
            if(!(recipe instanceof FurnaceRecipe)) continue;
            FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
            if(!furnaceRecipe.getInput().isSimilar(item))
                continue;

            result = furnaceRecipe.getResult();
            result.setAmount(item.getAmount());
            player.getInventory().setItemInMainHand(result);
            player.giveExp((int) (furnaceRecipe.getExperience()*item.getAmount()));
            player.updateInventory();
            MessageUtils.sendPlayerMessage(player, "§aLes items ont été cuit.");
            return true;
        }
        MessageUtils.sendPlayerMessage(player, "§cCet item ne peut pas être cuit.");
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
