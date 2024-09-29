package fr.niixoz.survivalcore.storage.prefix;

import fr.niixoz.survivalcore.SurvivalCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ChatIcon {

    public static List<ChatIcon> icons = new ArrayList<>();

    private String code;
    private String name;

    public ChatIcon(String name, String code) {
        Properties p = new Properties();
        try {
            p.load(new StringReader("code = " + code));
            code = p.getProperty("code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static ChatIcon getIcon(String name) {
        return icons.stream().filter(icon -> icon.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void loadIconConfig() {
        ChatIcon.icons.clear();
        SurvivalCore.getInstance().getLogger().info("Loading icons...");
        // Load icons.yml in icons folder
        File icons = new File(SurvivalCore.getInstance().getDataFolder() + "/icons/icons.yml");
        if(!icons.exists()) {
            icons.getParentFile().mkdirs();
            try {
                icons.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileConfiguration conf = YamlConfiguration.loadConfiguration(icons);
        conf.getKeys(false).forEach(key -> {
            ChatIcon icon = new ChatIcon(key, conf.getString(key));
            ChatIcon.icons.add(icon);
        });
    }

    public String getPermission() {
        return "survival.icon." + name;
    }
}
