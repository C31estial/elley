package dev.revere.alley.feature.command.impl.main.impl;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * @author Emmy
 * @project Alley
 * @date 06/06/2024 - 17:34
 */
public class AlleyReloadCommand extends BaseCommand {

    @CommandData(
            name = "alley.reload",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "alley.reload",
            description = "Reload Alley plugin configurations."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate("&6&lAlley &freloading..."));

        // Reload main configs
        this.plugin.getService(ConfigService.class).reloadConfigs();

        // Reload menu configs
        reloadMenuConfigs();

        player.sendMessage(CC.translate("&6&lAlley &a&lreloaded&f."));
        player.sendMessage(CC.translate("&7All menus, configs, and caches have been reloaded."));
    }

    /**
     * Reload menu configuration files.
     */
    private void reloadMenuConfigs() {
        File menusFolder = new File(AlleyPlugin.getInstance().getDataFolder(), "menus");

        // Reload queue menu configs
        File unrankedFile = new File(menusFolder, "unranked.yml");
        if (unrankedFile.exists()) {
            YamlConfiguration.loadConfiguration(unrankedFile);
        }

        File rankedFile = new File(menusFolder, "ranked.yml");
        if (rankedFile.exists()) {
            YamlConfiguration.loadConfiguration(rankedFile);
        }

        // Reload other menu configs
        File settingsFile = new File(menusFolder, "settings-menu.yml");
        if (settingsFile.exists()) {
            YamlConfiguration.loadConfiguration(settingsFile);
        }

        File cosmeticsFile = new File(menusFolder, "cosmetics-menu.yml");
        if (cosmeticsFile.exists()) {
            YamlConfiguration.loadConfiguration(cosmeticsFile);
        }

        File leaderboardFile = new File(menusFolder, "leaderboard-menu.yml");
        if (leaderboardFile.exists()) {
            YamlConfiguration.loadConfiguration(leaderboardFile);
        }

        File currentMatchesFile = new File(menusFolder, "current-matches-menu.yml");
        if (currentMatchesFile.exists()) {
            YamlConfiguration.loadConfiguration(currentMatchesFile);
        }

        // Reload about.yml
        File aboutFile = new File(AlleyPlugin.getInstance().getDataFolder(), "about.yml");
        if (aboutFile.exists()) {
            YamlConfiguration.loadConfiguration(aboutFile);
        }

        // Note: The actual configs will be reloaded when the menus are next opened
        // since each menu loads its config dynamically
    }
}