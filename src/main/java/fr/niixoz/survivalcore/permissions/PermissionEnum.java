package fr.niixoz.survivalcore.permissions;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

public enum PermissionEnum {

    PERMISSION_ALL("survival.*", "Permet d'avoir toutes les permissions du plugin.", PermissionDefault.OP),
    INSTANT_TP("survival.teleport.instant", "Permet d'être téléporté instantanément.", PermissionDefault.OP),
    COMMAND_TP_ACCEPT("survival.teleport.accept", "Permet d'accepter une demande de téléportation.", PermissionDefault.TRUE),
    COMMAND_TP_DENY("survival.teleport.deny", "Permet de refuser une demande de téléportation.", PermissionDefault.TRUE),
    COMMAND_TP_TPA("survival.teleport.tpa", "Permet d'envoyer une demande de téléportation à un joueur.", PermissionDefault.FALSE),
    COMMAND_TP_TPAHERE("survival.teleport.tpahere", "Permet d'envoyer une demande de téléportation à un joueur pour le téléporter à vous.", PermissionDefault.FALSE),
    COMMAND_TP_ADMIN("survival.teleport.admin", "Permet de se téléporter a un joueur.", PermissionDefault.OP),
    COMMAND_TP_BACK("survival.teleport.back", "Retourne à la dernière localisation ( avant téléportation ).", PermissionDefault.FALSE),
    COMMAND_HOME("survival.homes.tp", "Permet de se téléporter à un home.", PermissionDefault.FALSE),
    COMMAND_SETHOME("survival.homes.sethome", "Permet de poser un home.", PermissionDefault.FALSE),
    COMMAND_DELHOME("survival.homes.delhome", "Permet de supprimer un home.", PermissionDefault.FALSE),
    HOME_LIMIT_BYPASS("survival.homes.limit.bypass", "Permet de depasser la limite de home.", PermissionDefault.OP),
    COMMAND_HOMES("survival.homes.homes", "Permet de voir la liste de vos homes.", PermissionDefault.FALSE),
    COMMAND_HOMES_ADMIN("survival.homes.admin", "Permet de voir la liste des homes d'un joueur.", PermissionDefault.OP),
    COMMAND_NEARHOME("survival.homes.nearhome", "Permet de voir les homes proches de sa position.", PermissionDefault.OP),
    COMMAND_SPAWN("survival.spawn.tp", "Permet de se téléporter au spawn.", PermissionDefault.FALSE),

    // QOL
    COMMAND_RENAME_ITEM("survival.command.rename_item", "Permet de renommer un item.", PermissionDefault.FALSE),
    COMMAND_RENAME_ITEM_NORMAL("survival.command.rename_item.normal", "Permet de renommer un item sans le texte en italique.", PermissionDefault.FALSE),
    COMMAND_VISION("survival.command.vision", "Permet d'activer ou désactiver la vision nocturne.", PermissionDefault.FALSE),
    COMMAND_FEED("survival.command.feed", "Permet de se nourrir.", PermissionDefault.FALSE),
    COMMAND_HEAL("survival.command.heal", "Permet de se soigner.", PermissionDefault.FALSE),
    COMMAND_FLY("survival.command.fly", "Permet d'activer ou désactiver le fly.", PermissionDefault.OP),
    COMMAND_CRAFT("survival.command.craft", "Permet d'ouvrir une table de craft.", PermissionDefault.FALSE),
    COMMAND_FURNACE("survival.command.furnace", "Permet de faire cuire les items dans la main.", PermissionDefault.FALSE),
    COMMAND_ENDERCHEST("survival.command.enderchest", "Permet d'ouvrir l'enderchest.", PermissionDefault.FALSE),
    COMMAND_ENCHANTING_TABLE("survival.command.enchanting_table", "Permet d'ouvrir une table d'enchant.", PermissionDefault.FALSE),
    COMMAND_TRASH("survival.command.trash", "Ouvre une poubelle pour détruire des items.", PermissionDefault.FALSE),
    COMMAND_ANVIL("survival.command.anvil", "Permet d'ouvrir une enclume virtuelle.", PermissionDefault.FALSE),
    COMMAND_LOOM("survival.command.loom", "Permet d'ouvrir un loom virtuelle.", PermissionDefault.FALSE),
    COMMAND_SMITHING_TABLE("survival.command.smithing_table", "Permet d'ouvrir une smithing table virtuelle.", PermissionDefault.FALSE),
    COMMAND_GRINDSTONE("survival.command.grindstone", "Permet d'ouvrir un grindstone virtuelle.", PermissionDefault.FALSE),
    COMMAND_CARTOGRAPHY_TABLE("survival.command.cartography_table", "Permet d'ouvrir une cartography table virtuelle.", PermissionDefault.FALSE),
    COMMAND_STONECUTTER("survival.command.stonecutter", "Permet d'ouvrir un stonecutter virtuelle.", PermissionDefault.FALSE),
    COMMAND_SPEED("survival.command.speed", "Permet de modifier son speed.", PermissionDefault.OP),
    COMMAND_MENDING("survival.command.mending", "Permet d'utiliser son exp pour réparer un item. (TODO)", PermissionDefault.FALSE),
    COMMAND_COMPONENT("survival.command.component", "Command helper pour les components", PermissionDefault.FALSE),
    COMMAND_SLEEP("survival.command.sleep", "Commande pour sleep.", PermissionDefault.FALSE),
    COMMAND_REPAIR("survival.command.repair", "Permet de réparer l'item en main.", PermissionDefault.FALSE),
    COMMAND_REPAIR_ALL("survival.command.repair.all", "Permet de réparer tout son inventaire.", PermissionDefault.FALSE),

    // STORAGE
    COMMAND_BACKPACK("survival.storage.backpack", "Permet d'ouvrir le sac à dos.", PermissionDefault.FALSE),

    KEEP_EXP_ON_DEATH("survival.qol.keep_exp_on_death", "Permet de garder l'expérience à la mort.", PermissionDefault.FALSE),

    // COSMETIC
    COMMAND_HAT("survival.command.hat", "Permet de mettre un item sur la tête.", PermissionDefault.FALSE),
    COMMAND_SIZE("survival.command.size", "Permet de changer de taille.", PermissionDefault.FALSE),
    COMMAND_ENTITY_SIZE("survival.command.entity_size", "Permet de changer la taille de l'entité visée.", PermissionDefault.FALSE),
    COMMAND_ENTITY_SIZE_ANIMALS("survival.command.entity_size_animals", "Permet de changer la taille de l'entité visée de type Animals.", PermissionDefault.FALSE),
    COMMAND_ENTITY_SIZE_MONSTERS("survival.command.entity_size_monsters", "Permet de changer la taille de l'entité visée de type Monsters.", PermissionDefault.FALSE),
    COMMAND_ENTITY_SIZE_OTHER("survival.command.entity_size_other", "Permet de changer la taille de l'entité visée de type Others.", PermissionDefault.FALSE),
    COMMAND_MOUNT("survival.command.mount", "Permet de monter sur l'entité visée.", PermissionDefault.FALSE),
    COMMAND_SIT("survival.command.sit", "Permet de s'asseoir sur place.", PermissionDefault.FALSE),
    COMMAND_LAY("survival.command.lay", "Permet de s'allonger au sol.", PermissionDefault.FALSE),
    COMMAND_CRAWL("survival.command.crawl", "Permet de ramper au sol.", PermissionDefault.FALSE),
    COMMAND_SPIN("survival.command.spin", "Permet de faire la vrille du trident.", PermissionDefault.FALSE),


    // ADMIN
    COMMAND_ADMIN_INVSEE("survival.admin.command.invsee", "Admin invsee command", PermissionDefault.OP),
    COMMAND_ADMIN_GOD("survival.admin.command.god", "Admin god mode command", PermissionDefault.OP),
    COMMAND_ENDERCHEST_OTHERS("survival.admin.command.enderchest.others", "See other players enderchest.", PermissionDefault.OP),
    COMMAND_ADMIN_VANISH("survival.admin.command.vanish", "Permet de devenir totalement invisible.", PermissionDefault.OP),
    COMMAND_ADMIN_VANISH_OTHERS("survival.admin.command.vanish.others", "Permet d'activer le vanish d'un autre joueur.", PermissionDefault.OP),
    COMMAND_ADMIN_VANISH_SEE("survival.admin.command.vanish.see", "Permet de voir les joueurs en vanish.", PermissionDefault.OP),
    ;

    private String permission;
    private String description;
    private PermissionDefault value;

    PermissionEnum(String permission, String description, PermissionDefault value) {
        this.permission = permission;
        this.description = description;
        this.value = value;
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
            pm.addPermission(new Permission(p.permission, p.description, p.value));
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
