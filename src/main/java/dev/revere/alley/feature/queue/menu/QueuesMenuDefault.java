package dev.revere.alley.feature.queue.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.constants.MessageConstant;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.feature.ffa.menu.FFAMenu;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.queue.menu.sub.UnrankedMenu;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:28
 */
@AllArgsConstructor
public class QueuesMenuDefault extends Menu {
    protected final AlleyPlugin plugin;

    public QueuesMenuDefault() {
        this.plugin = AlleyPlugin.getInstance();
    }

    /**
     * Get the title of the menu.
     *
     * @param player the player to get the title for
     * @return the title of the menu
     */
    @Override
    public String getTitle(Player player) {
        return "&6&lSolo Unranked Queues";
    }

    /**
     * Get the buttons for the menu.
     *
     * @param player the player to get the buttons for
     * @return the buttons for the menu
     */
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        QueueService queueService = AlleyPlugin.getInstance().getService(QueueService.class);

        // Moved to slot 12 (right by 1) and added enchant glint
        buttons.put(12, new QueuesButtonDefault("&6&lSolos", Material.DIAMOND_SWORD, 0, Arrays.asList(
                "&7Casual 1v1s with",
                "&7no loss penalty.",
                "",
                "&6Players: &f" + queueService.getPlayerCountOfGameType("Unranked"),
                "",
                "&aClick to play!"
        ), true));

        // Moved to slot 14 (right by 1) and added enchant glint
        buttons.put(14, new QueuesButtonDefault("&6&lBots", Material.GOLD_SWORD, 0, Arrays.asList(
                "&7Practice against bots",
                "&7to improve your skills.",
                "",
                "&6Players: &f" + queueService.getPlayerCountOfGameType("Bots"),
                "",
                "&aClick to play!"
        ), true));

        // FFA tab removed as requested

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @AllArgsConstructor
    public static class QueuesButtonDefault extends Button {
        private String displayName;
        private Material material;
        private int durability;
        private List<String> lore;
        private boolean glint;

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder = new ItemBuilder(this.material)
                    .name(this.displayName)
                    .durability(this.durability)
                    .lore(this.lore);

            if (this.glint) {
                builder.glow(true);
            }

            builder.hideMeta();

            return builder.build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (clickType != ClickType.LEFT) return;

            switch (this.material) {
                case DIAMOND_SWORD:
                    new UnrankedMenu().openMenu(player);
                    break;
                case GOLD_AXE:
                    new FFAMenu().openMenu(player);
                    break;
                case GOLD_SWORD:
                    player.sendMessage(MessageConstant.IN_DEVELOPMENT);
                    break;
            }

            this.playNeutral(player);
        }
    }
}