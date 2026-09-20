package fr.niixoz.survivalcore.permissions;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public enum PermissionEnum {

    PERMISSION_ALL("survival.*", "Permet d'avoir toutes les permissions du plugin."),
    INSTANT_TP("survival.teleport.instant", "Permet d'être téléporté instantanément."),
    COMMAND_TP_ACCEPT("survival.teleport.accept", "Permet d'accepter une demande de téléportation."),
    COMMAND_TP_DENY("survival.teleport.deny", "Permet de refuser une demande de téléportation."),
    COMMAND_TP_TPA("survival.teleport.tpa", "Permet d'envoyer une demande de téléportation à un joueur."),
    COMMAND_TP_TPAHERE("survival.teleport.tpahere", "Permet d'envoyer une demande de téléportation à un joueur pour le téléporter à vous."),
    COMMAND_BACK("survival.teleport.back", "Retourne à la dernière localisation ( avant téléportation )."),
    COMMAND_TP_ADMIN("survival.teleport.admin", "Permet de se téléporter a un joueur."),
    COMMAND_HOME("survival.homes.tp", "Permet de se téléporter à un home."),
    COMMAND_SETHOME("survival.homes.sethome", "Permet de poser un home."),
    COMMAND_DELHOME("survival.homes.delhome", "Permet de supprimer un home."),
    HOME_LIMIT_BYPASS("survival.homes.limit.bypass", "Permet de depasser la limite de home."),
    COMMAND_HOMES("survival.homes.homes", "Permet de voir la liste de vos homes."),
    COMMAND_HOMES_ADMIN("survival.homes.admin", "Permet de voir la liste des homes d'un joueur."),
    COMMAND_NEARHOME("survival.homes.nearhome", "Permet de voir les homes proches de sa position."),
    COMMAND_SPAWN("survival.spawn.tp", "Permet de se téléporter au spawn."),

    // QOL
    COMMAND_RENAME_ITEM("survival.command.rename_item", "Permet de renommer un item."),
    COMMAND_RENAME_ITEM_NORMAL("survival.command.rename_item.normal", "Permet de renommer un item sans le texte en italique."),
    COMMAND_VISION("survival.command.vision", "Permet d'activer ou désactiver la vision nocturne."),
    COMMAND_FEED("survival.command.feed", "Permet de se nourrir."),
    COMMAND_HEAL("survival.command.heal", "Permet de se soigner."),
    COMMAND_FLY("survival.command.fly", "Permet d'activer ou désactiver le fly."),
    COMMAND_CRAFT("survival.command.craft", "Permet d'ouvrir une table de craft."),
    COMMAND_FURNACE("survival.command.furnace", "Permet de faire cuire les items dans la main."),
    COMMAND_ENDERCHEST("survival.command.enderchest", "Permet d'ouvrir l'enderchest."),
    COMMAND_ENCHANTING_TABLE("survival.command.enchanting_table", "Permet d'ouvrir une table d'enchant."),
    COMMAND_TRASH("survival.command.trash", "Ouvre une poubelle pour détruire des items."),
    COMMAND_ANVIL("survival.command.anvil", "Permet d'ouvrir une enclume virtuelle."),
    COMMAND_LOOM("survival.command.loom", "Permet d'ouvrir un loom virtuelle."),
    COMMAND_SMITHING_TABLE("survival.command.smithing_table", "Permet d'ouvrir une smithing table virtuelle."),
    COMMAND_GRINDSTONE("survival.command.grindstone", "Permet d'ouvrir un grindstone virtuelle."),
    COMMAND_CARTOGRAPHY_TABLE("survival.command.cartography_table", "Permet d'ouvrir une cartography table virtuelle."),
    COMMAND_STONECUTTER("survival.command.stonecutter", "Permet d'ouvrir un stonecutter virtuelle."),
    COMMAND_SPEED("survival.command.speed", "Permet de modifier son speed."),
    COMMAND_MENDING("survival.command.mending", "Permet d'utiliser son exp pour réparer un item. (TODO)"),
    COMMAND_COMPONENT("survival.command.component", "Command helper pour les components"),
    COMMAND_SLEEP("survival.command.sleep", "Commande pour sleep."),
    COMMAND_REPAIR("survival.command.repair", "Permet de réparer l'item en main."),
    COMMAND_REPAIR_ALL("survival.command.repair.all", "Permet de réparer tout son inventaire."),

    // STORAGE
    COMMAND_BACKPACK("survival.storage.backpack", "Permet d'ouvrir le sac à dos."),

    KEEP_EXP_ON_DEATH("survival.qol.keep_exp_on_death", "Permet de garder l'expérience à la mort."),

    // COSMETIC
    COMMAND_HAT("survival.command.hat", "Permet de mettre un item sur la tête."),
    COMMAND_SIZE("survival.command.size", "Permet de changer de taille."),
    COMMAND_ENTITY_SIZE("survival.command.entity_size", "Permet de changer la taille de l'entité visée."),
    COMMAND_ENTITY_SIZE_ANIMALS("survival.command.entity_size_animals", "Permet de changer la taille de l'entité visée de type Animals."),
    COMMAND_ENTITY_SIZE_MONSTERS("survival.command.entity_size_monsters", "Permet de changer la taille de l'entité visée de type Monsters."),
    COMMAND_ENTITY_SIZE_OTHER("survival.command.entity_size_other", "Permet de changer la taille de l'entité visée de type Others."),
    COMMAND_MOUNT("survival.command.mount", "Permet de monter sur l'entité visée."),
    COMMAND_SIT("survival.command.sit", "Permet de s'asseoir sur place."),
    COMMAND_LAY("survival.command.lay", "Permet de s'allonger au sol."),
    COMMAND_CRAWL("survival.command.crawl", "Permet de ramper au sol."),
    COMMAND_SPIN("survival.command.spin", "Permet de faire la vrille du trident."),


    // ADMIN
    COMMAND_ADMIN_INVSEE("survival.admin.command.invsee", "Admin invsee command"),
    COMMAND_ADMIN_GOD("survival.admin.command.god", "Admin god mode command"),
    COMMAND_ENDERCHEST_OTHERS("survival.admin.command.enderchest.others", "See other players enderchest."),
    COMMAND_ADMIN_VANISH("survival.admin.command.vanish", "Permet de devenir totalement invisible."),
    COMMAND_ADMIN_VANISH_OTHERS("survival.admin.command.vanish.others", "Permet d'activer le vanish d'un autre joueur."),
    COMMAND_ADMIN_VANISH_SEE("survival.admin.command.vanish.see", "Permet de voir les joueurs en vanish."),
    ;

    private String permission;
    private String description;

    PermissionEnum(String permission, String description) {
        this.permission = permission;
        this.description = description;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public static void registerAll() {
        PluginManager pm = Bukkit.getPluginManager();

        for (PermissionEnum p : values()) {
            if (pm.getPermission(p.permission) != null) continue;
            pm.addPermission(new Permission(p.permission, p.description, PermissionDefault.OP));
        }

        for (PermissionEnum p : values()) {
            if (!p.permission.endsWith(".*")) continue;
            String prefix = p.permission.substring(0, p.permission.length() - 1); // "survival.teleport."
            Permission parent = pm.getPermission(p.permission);
            for (PermissionEnum child : values()) {
                if (child != p && child.permission.startsWith(prefix)) {
                    parent.getChildren().put(child.permission, true);
                }
            }
            pm.recalculatePermissionDefaults(parent);
        }
    }
}
