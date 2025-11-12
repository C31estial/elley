package dev.revere.alley.feature.command.impl.main;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.ClickableUtil;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:39
 */
public class AlleyCommand extends BaseCommand {
    @CompleterData(name = "alley")
    public List<String> alleyCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            completion.addAll(Arrays.asList(
                    "reload", "debug", "core"
            ));
        }

        return completion;
    }

    @CommandData(
            name = "alley",
            aliases = {"apractice", "aprac", "practice", "prac", "emmy", "remi", "revere"},
            inGameOnly = false,
            usage = "alley",
            description = "Displays information about the plugin."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        File configFile = new File(AlleyPlugin.getInstance().getDataFolder(), "about.yml");

        // Fallback to hardcoded message if config doesn't exist
        if (!configFile.exists()) {
            Arrays.asList(
                    "",
                    "     &6&lAlley Practice",
                    "      &6&l│ &fCreated by: &6Emmy &7(github.com/hmEmmy)",
                    "      &6&l│ &fMaintained by: &6Revere Inc. &7(github.com/RevereInc)",
                    "      &6&l│ &fPrimary Contributors: &6" + this.plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", "").replace(",", "&7,&6"),
                    "",
                    "      &6&l│ &fLicense: &6CC BY-NC-SA 4.0",
                    "      &6&l│ &fVersion: &6" + this.plugin.getDescription().getVersion(),
                    ""
            ).forEach(line -> sender.sendMessage(CC.translate(line)));

            if (sender instanceof Player) {
                TextComponent clickable = this.createLinkComponent();
                command.getPlayer().spigot().sendMessage(clickable);
                sender.sendMessage("");
            }
            return;
        }

        FileConfiguration config = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(configFile);

        // Send message lines
        List<String> lines = config.getStringList("message.lines");
        for (String line : lines) {
            String processedLine = applyPlaceholders(line, config);
            sender.sendMessage(CC.translate(processedLine));
        }

        // Send clickable links for players
        if (sender instanceof Player && config.getBoolean("links.enabled", true)) {
            TextComponent clickable = createLinkComponent(config);
            command.getPlayer().spigot().sendMessage(clickable);
            sender.sendMessage("");
        }
    }

    private @NotNull TextComponent createLinkComponent() {
        TextComponent repositoryComponent = ClickableUtil.createLinkComponent("&f&l[GITHUB]", "https://github.com/hmEmmy/alley-practice", "&aClick to open the GitHub repository.");
        TextComponent discordComponent = ClickableUtil.createLinkComponent("&9&l[DISCORD]", "https://discord.com/invite/eT4B65k5E4", "&aClick to join the Revere Discord.");
        TextComponent builtByBitComponent = ClickableUtil.createLinkComponent("&b&l[BUILTBYBIT]", "https://builtbybit.com/resources/alley-next-generation-practice-core.73088/", "&aClick to open the BuiltByBit resource page.");
        TextComponent spigotMcComponent = ClickableUtil.createLinkComponent("&e&l[SPIGOTMC]", "https://www.spigotmc.org/resources/alley-next-generation-practice-core.127500/", "&aClick to open the SpigotMC resource page.");

        String SPACING = "  ";

        TextComponent clickable = new TextComponent("     ");
        clickable.addExtra(repositoryComponent);
        clickable.addExtra(SPACING);
        clickable.addExtra(discordComponent);
        clickable.addExtra(SPACING);
        clickable.addExtra(builtByBitComponent);
        clickable.addExtra(SPACING);
        clickable.addExtra(spigotMcComponent);
        return clickable;
    }

    /**
     * Creates clickable link components from config.
     *
     * @param config the configuration file
     * @return the clickable component with all links
     */
    private @NotNull TextComponent createLinkComponent(FileConfiguration config) {
        TextComponent clickable = new TextComponent("     ");
        String spacing = config.getString("links.spacing", "  ");
        boolean firstLink = true;

        // GitHub link
        if (config.getBoolean("links.github.enabled", true)) {
            String text = applyPlaceholders(config.getString("links.github.text", "&f&l[GITHUB]"), config);
            String url = config.getString("links.github.url", "https://github.com/hmEmmy/alley-practice");
            String hover = applyPlaceholders(config.getString("links.github.hover", "&aClick to open the GitHub repository."), config);
            if (!firstLink) clickable.addExtra(spacing);
            clickable.addExtra(ClickableUtil.createLinkComponent(text, url, hover));
            firstLink = false;
        }

        // Discord link
        if (config.getBoolean("links.discord.enabled", true)) {
            String text = applyPlaceholders(config.getString("links.discord.text", "&9&l[DISCORD]"), config);
            String url = config.getString("links.discord.url", "https://discord.com/invite/eT4B65k5E4");
            String hover = applyPlaceholders(config.getString("links.discord.hover", "&aClick to join the Revere Discord."), config);
            if (!firstLink) clickable.addExtra(spacing);
            clickable.addExtra(ClickableUtil.createLinkComponent(text, url, hover));
            firstLink = false;
        }

        // BuiltByBit link
        if (config.getBoolean("links.builtbybit.enabled", true)) {
            String text = applyPlaceholders(config.getString("links.builtbybit.text", "&b&l[BUILTBYBIT]"), config);
            String url = config.getString("links.builtbybit.url", "https://builtbybit.com/resources/alley-next-generation-practice-core.73088/");
            String hover = applyPlaceholders(config.getString("links.builtbybit.hover", "&aClick to open the BuiltByBit resource page."), config);
            if (!firstLink) clickable.addExtra(spacing);
            clickable.addExtra(ClickableUtil.createLinkComponent(text, url, hover));
            firstLink = false;
        }

        // SpigotMC link
        if (config.getBoolean("links.spigotmc.enabled", true)) {
            String text = applyPlaceholders(config.getString("links.spigotmc.text", "&e&l[SPIGOTMC]"), config);
            String url = config.getString("links.spigotmc.url", "https://www.spigotmc.org/resources/alley-next-generation-practice-core.127500/");
            String hover = applyPlaceholders(config.getString("links.spigotmc.hover", "&aClick to open the SpigotMC resource page."), config);
            if (!firstLink) clickable.addExtra(spacing);
            clickable.addExtra(ClickableUtil.createLinkComponent(text, url, hover));
        }

        return clickable;
    }

    /**
     * Applies placeholders to text from config.
     *
     * @param text   the text to apply placeholders to
     * @param config the configuration file
     * @return the text with placeholders applied
     */
    private String applyPlaceholders(String text, FileConfiguration config) {
        // Apply color placeholders
        Map<String, String> colors = new HashMap<>();
        if (config.contains("colors")) {
            for (String key : config.getConfigurationSection("colors").getKeys(false)) {
                colors.put("{" + key + "}", config.getString("colors." + key));
            }
        }

        for (Map.Entry<String, String> entry : colors.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }

        // Apply plugin info placeholders
        String authors = this.plugin.getDescription().getAuthors().toString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", config.getString("colors.accent", "&7") + "," + config.getString("colors.primary", "&6"));

        text = text.replace("{version}", this.plugin.getDescription().getVersion());
        text = text.replace("{authors}", authors);
        text = text.replace("{license}", config.getString("info.license", "CC BY-NC-SA 4.0"));

        return text;
    }
}