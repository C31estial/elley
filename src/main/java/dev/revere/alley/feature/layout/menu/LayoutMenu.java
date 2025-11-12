package dev.revere.alley.feature.layout.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.library.menu.impl.CloseButton;
import dev.revere.alley.feature.kit.KitCategory;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.layout.menu.button.LayoutButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 02/05/2025
 */
@AllArgsConstructor
public class LayoutMenu extends Menu {
    private KitCategory kitCategory;

    @Override
    public String getTitle(Player player) {
        return "&6&lLayout Editor";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;

        for (Queue queue : AlleyPlugin.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && !queue.isDuos() && queue.getKit().getCategory() == this.kitCategory && queue.getKit().isEditable()) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new LayoutButton(queue.getKit()));
            }
        }

        // Add border only (not full background)
        int size = this.getSize();
        int rows = size / 9;
        this.addBorder(buttons, 15, rows);

        // Add close button at bottom center (dynamic position based on menu size)
        int closeButtonSlot = (rows * 9) - 5; // Bottom row, center slot
        buttons.put(closeButtonSlot, new CloseButton("INK_SACK", 1, "&cClose", Arrays.asList("&7Click to close this menu")));

        return buttons;
    }

    @Override
    public int getSize() {
        if (this.kitCategory == KitCategory.EXTRA) {
            return 9 * 4;
        }

        return 9 * 5;
    }
}