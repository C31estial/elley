package dev.revere.alley.core.profile.menu.setting;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.data.types.ProfileSettingData;
import dev.revere.alley.core.profile.menu.setting.button.PracticeSettingsButton;
import dev.revere.alley.core.profile.menu.setting.enums.PracticeSettingType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
public class PracticeSettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        ConfigService configService = AlleyPlugin.getInstance().getService(ConfigService.class);
        File configFile = new File(AlleyPlugin.getInstance().getDataFolder(), "menus/settings-menu.yml");

        if (!configFile.exists()) {
            return "&6&lPractice Settings";
        }

        FileConfiguration config = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(configFile);
        String title = config.getString("menu.title", "&6&lPractice Settings");
        return CC.translate(applyColors(title, config));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        ProfileSettingData settings = profile.getProfileData().getSettingData();

        for (PracticeSettingType type : PracticeSettingType.values()) {
            buttons.put(type.slot, new PracticeSettingsButton(
                    type,
                    type.displayName,
                    type.material,
                    type.durability,
                    type.loreProvider.apply(settings)
            ));
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    /**
     * Applies color replacements from config.
     *
     * @param text   the text to apply colors to
     * @param config the configuration file
     * @return the text with colors applied
     */
    private String applyColors(String text, FileConfiguration config) {
        Map<String, String> colors = new HashMap<>();
        if (config.contains("colors")) {
            for (String key : config.getConfigurationSection("colors").getKeys(false)) {
                colors.put("{" + key + "}", config.getString("colors." + key));
            }
        }

        for (Map.Entry<String, String> entry : colors.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }

        return text;
    }
}