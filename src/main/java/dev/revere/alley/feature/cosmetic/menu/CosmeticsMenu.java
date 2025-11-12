package dev.revere.alley.feature.cosmetic.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.menu.shop.button.ShopCategoryButton;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.library.menu.impl.BackButton;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import dev.revere.alley.feature.cosmetic.menu.button.CosmeticCategoryButton;
import dev.revere.alley.core.profile.menu.setting.PracticeSettingsMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@AllArgsConstructor
public class CosmeticsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        File configFile = new File(AlleyPlugin.getInstance().getDataFolder(), "menus/cosmetics-menu.yml");

        if (!configFile.exists()) {
            return "&6&lCosmetics";
        }

        FileConfiguration config = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(configFile);
        String title = config.getString("menu.title", "&6&lCosmetics");
        return CC.translate(applyColors(title, config));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new PracticeSettingsMenu()));
        buttons.put(10, new CosmeticCategoryButton(CosmeticType.KILL_EFFECT, Material.DIAMOND_SWORD));
        buttons.put(11, new CosmeticCategoryButton(CosmeticType.SOUND_EFFECT, Material.NOTE_BLOCK));
        buttons.put(12, new CosmeticCategoryButton(CosmeticType.CLOAK, Material.BLAZE_POWDER));
        buttons.put(13, new CosmeticCategoryButton(CosmeticType.SUIT, Material.GOLD_CHESTPLATE));
        buttons.put(14, new CosmeticCategoryButton(CosmeticType.PROJECTILE_TRAIL, Material.ARROW));
        buttons.put(15, new CosmeticCategoryButton(CosmeticType.KILL_MESSAGE, Material.BOOK_AND_QUILL));

        this.addBorder(buttons, 15, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
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
