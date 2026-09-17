package fr.niixoz.survivalcore.managers;

import fr.niixoz.survivalcore.config.Config;

public enum VanishMode {

    QUIET,
    FAKE;

    public static VanishMode getDefault() {
        return Config.vanishDefaultFake ? FAKE : QUIET;
    }

    /** @return VanishMode */
    public static VanishMode fromArgument(String argument) {
        for (VanishMode mode : values()) {
            if (mode.name().equalsIgnoreCase(argument)) {
                return mode;
            }
        }
        return null;
    }
}