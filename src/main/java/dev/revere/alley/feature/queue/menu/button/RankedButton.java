package dev.revere.alley.feature.queue.menu.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.server.ServerService;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
@AllArgsConstructor
public class RankedButton extends Button {
    protected final AlleyPlugin plugin = AlleyPlugin.getInstance();
    private final Queue queue;
    private final org.bukkit.configuration.file.FileConfiguration config;

    @Override
    public ItemStack getButtonItem(Player player) {
        Kit kit = this.queue.getKit();

        // Get customizable kit name format from config
        String kitNameFormat = config.getString("queue-button.kit-name-format", "&a&l{kit-name}");
        String kitDisplayName = kitNameFormat.replace("{kit-name}", kit.getName());

        ItemBuilder builder = new ItemBuilder(kit.getIcon())
                .name(kitDisplayName)
                .durability(kit.getDurability())
                .lore(this.getLore(kit, player))
                .hideMeta();

        // Add enchant glint if anyone is in queue
        if (this.queue.getTotalPlayerCount() >= 1) {
            builder.enchantment(org.bukkit.enchantments.Enchantment.DURABILITY);
        }

        return builder.build();
    }

    /**
     * Get the lore for the kit from configuration.
     *
     * @param kit the kit to get the lore for
     * @param player the player viewing the menu
     * @return the lore for the kit
     */
    private @NotNull List<String> getLore(Kit kit, Player player) {
        List<String> lore = new ArrayList<>();

        String separator = config.getString("queue-button.lore.separator", "&6&m--------------------");
        lore.add(CC.translate(separator));

        // Get player's ELO
        int playerElo = AlleyPlugin.getInstance().getService(ProfileService.class)
                .getProfile(player.getUniqueId())
                .getProfileData()
                .getRankedKitData()
                .get(kit.getName())
                .getElo();

        // Process each configured lore line
        if (config.contains("queue-button.lore.lines")) {
            List<?> lines = config.getList("queue-button.lore.lines");
            if (lines != null) {
                for (Object lineObj : lines) {
                    if (lineObj instanceof java.util.Map) {
                        @SuppressWarnings("unchecked")
                        java.util.Map<String, Object> lineMap = (java.util.Map<String, Object>) lineObj;

                        boolean enabled = lineMap.get("enabled") instanceof Boolean ? (Boolean) lineMap.get("enabled") : true;
                        if (!enabled) continue;

                        String text = (String) lineMap.get("text");
                        if (text == null) continue;

                        boolean showIfEmpty = lineMap.get("show-if-empty") instanceof Boolean ? (Boolean) lineMap.get("show-if-empty") : true;

                        // Skip if description is empty and show-if-empty is false
                        if (!showIfEmpty && text.contains("{kit-description}") && kit.getDescription().isEmpty()) {
                            continue;
                        }

                        // Replace placeholders
                        text = text.replace("{kit-name}", kit.getName())
                                .replace("{kit-description}", kit.getDescription())
                                .replace("{playing}", String.valueOf(this.queue.getQueueFightCount()))
                                .replace("{queueing}", String.valueOf(this.queue.getProfiles().size()))
                                .replace("{player-elo}", String.valueOf(playerElo))
                                .replace("{player}", player.getName())
                                .replace("{top1-name}", "NULL")
                                .replace("{top1-elo}", "N/A")
                                .replace("{top2-name}", "NULL")
                                .replace("{top2-elo}", "N/A")
                                .replace("{top3-name}", "NULL")
                                .replace("{top3-elo}", "N/A");

                        lore.add(CC.translate(text));
                    }
                }
            }
        }

        lore.add(CC.translate(separator));
        return lore;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;

        ServerService serverService = AlleyPlugin.getInstance().getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(AlleyPlugin.getInstance().getService(LocaleService.class).getString(GlobalMessagesLocaleImpl.QUEUE_TEMPORARILY_DISABLED));
            player.closeInventory();
            return;
        }

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getProfileData().isRankedBanned()) {
            player.closeInventory();
            Arrays.asList(
                    "",
                    "&c&lRANKED BAN",
                    "&cYou are currently banned from ranked queues.",
                    "&7You may appeal at &6&ndiscord.gg/alley-practice&7.",
                    ""
            ).forEach(line -> player.sendMessage(CC.translate(line)));
            return;
        }

        this.queue.addPlayer(player, this.queue.isRanked() ? profile.getProfileData().getRankedKitData().get(queue.getKit().getName()).getElo() : 0);
        PlayerUtil.reset(player, false, true);
        player.closeInventory();
        this.playNeutral(player);
        AlleyPlugin.getInstance().getService(HotbarService.class).applyHotbarItems(player);
    }
}
