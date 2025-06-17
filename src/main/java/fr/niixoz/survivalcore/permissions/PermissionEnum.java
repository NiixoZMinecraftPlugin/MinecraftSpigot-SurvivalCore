package fr.niixoz.survivalcore.permissions;

public enum PermissionEnum {

    PERMISSION_ALL("survival.*"),
    INSTANT_TP("survival.teleport.instant"),
    COMMAND_TP_ACCEPT("survival.teleport.accept"),
    COMMAND_TP_DENY("survival.teleport.deny"),
    COMMAND_TP_TPA("survival.teleport.tpa"),
    COMMAND_TP_TPAHERE("survival.teleport.tpahere"),
    COMMAND_BACK("survival.teleport.back"),
    COMMAND_TP_ADMIN("survival.teleport.admin"),
    COMMAND_HOME("survival.homes.tp"),
    COMMAND_SETHOME("survival.homes.sethome"),
    COMMAND_DELHOME("survival.homes.delhome"),
    HOME_LIMIT_BYPASS("survival.homes.limit.bypass"),
    COMMAND_HOMES("survival.homes.homes"),
    COMMAND_HOMES_ADMIN("survival.homes.admin"),
    COMMAND_SPAWN("survival.spawn.tp"),

    // QOL
    COMMAND_RENAME_ITEM("survival.command.rename_item"),
    COMMAND_VISION("survival.command.vision"),
    COMMAND_FEED("survival.command.feed"),
    COMMAND_HEAL("survival.command.heal"),
    COMMAND_FLY("survival.command.fly"),
    COMMAND_CRAFT("survival.command.craft"),
    COMMAND_FURNACE("survival.command.furnace"),
    COMMAND_ENDERCHEST("survival.command.enderchest"),
    COMMAND_ENCHANTING_TABLE("survival.command.enchanting_table"),
    COMMAND_SPEED("survival.command.speed"),

    // STORAGE
    COMMAND_BACKPACK("survival.storage.backpack"),

    KEEP_EXP_ON_DEATH("survival.qol.keep_exp_on_death"),

    // COSMETIC
    COMMAND_HAT("survival.command.hat"),
    COMMAND_SIZE("survival.command.size"),
    COMMAND_ENTITY_SIZE("survival.command.entity_size"),
    COMMAND_ENTITY_SIZE_ANIMALS("survival.command.entity_size_animals"),
    COMMAND_ENTITY_SIZE_MONSTERS("survival.command.entity_size_monsters"),
    COMMAND_ENTITY_SIZE_OTHER("survival.command.entity_size_other"),
    COMMAND_MOUNT("survival.command.mount"),
    ;

    private String permission;

    PermissionEnum(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
