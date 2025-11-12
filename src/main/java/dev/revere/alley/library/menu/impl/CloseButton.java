package dev.revere.alley.library.menu.impl;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 09/11/2025
 */
@AllArgsConstructor
public class CloseButton extends Button {
    private final String materialName;
    private final int data;
    private final String name;
    private final List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        Material material;
        try {
            material = Material.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            material = Material.INK_SACK;
        }

        List<String> translatedLore = new ArrayList<>();
        if (lore != null && !lore.isEmpty()) {
            for (String line : lore) {
                translatedLore.add(CC.translate(line));
            }
        }

        return new ItemBuilder(material)
                .name(CC.translate(name))
                .durability(data)
                .lore(translatedLore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.playNeutral(player);
        player.closeInventory();
    }
}
