package dev.revere.alley.feature.queue.menu.sub;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.library.menu.impl.BackButton;
import dev.revere.alley.library.menu.impl.CloseButton;
import dev.revere.alley.feature.kit.KitCategory;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueType;
import dev.revere.alley.feature.queue.menu.QueuesMenuDefault;
import dev.revere.alley.feature.queue.menu.button.UnrankedButton;
import dev.revere.alley.feature.queue.menu.extra.button.QueueModeSwitcherButton;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 10:28
 */
public class UnrankedMenu extends Menu {
    private final FileConfiguration config;

    public UnrankedMenu() {
        File configFile = new File(AlleyPlugin.getInstance().getDataFolder(), "menus/unranked.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate(config.getString("menu.title", "&6&lUnranked Queue"));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        // Add back button if enabled
        if (config.getBoolean("menu.back-button.enabled", true)) {
            int backSlot = config.getInt("menu.back-button.slot", 0);
            buttons.put(backSlot, new BackButton(new QueuesMenuDefault()));
        }

        // Add queue buttons
        int startSlot = config.getInt("queue-button.start-slot", 10);
        boolean skipBorder = config.getBoolean("queue-button.skip-border-slots", true);
        int slot = startSlot;

        for (Queue queue : AlleyPlugin.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && queue.getKit().getCategory() == KitCategory.NORMAL) {
                if (skipBorder) {
                    slot = this.skipIfSlotCrossingBorder(slot);
                }
                buttons.put(slot++, new UnrankedButton(queue, config));
            }
        }

        // Add close button if enabled
        if (config.getBoolean("menu.close-button.enabled", true)) {
            int closeSlot = config.getInt("menu.close-button.slot", 40);
            String material = config.getString("menu.close-button.material", "INK_SACK");
            int data = config.getInt("menu.close-button.data", 1);
            String name = config.getString("menu.close-button.name", "&cClose");
            List<String> lore = config.getStringList("menu.close-button.lore");

            buttons.put(closeSlot, new CloseButton(material, data, name, lore));
        }

        // Add border if enabled
        if (config.getBoolean("menu.border.enabled", true)) {
            int size = this.getSize();
            int rows = size / 9;
            this.addBorder(buttons, 15, rows);
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return config.getInt("menu.size", 45);
    }
}