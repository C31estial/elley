package dev.revere.alley.visual.nametag.internal;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dev.revere.alley.visual.nametag.NametagAdapter;
import dev.revere.alley.visual.nametag.NametagView;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@Getter
public class NametagRegistry {
    // Use a simple map for caching to avoid Guava version compatibility issues
    private final Map<String, NametagAdapter> adapterCache = new ConcurrentHashMap<>();
    private final NametagServiceImpl service;
    private final AtomicInteger teamIdCounter = new AtomicInteger(0);

    public NametagRegistry(NametagServiceImpl service) {
        this.service = service;
    }

    /**
     * Gets or creates a NametagAdapter for a given style.
     *
     * @param view The nametag view.
     * @return The cached or newly created NametagAdapter.
     */
    public NametagAdapter getAdapter(NametagView view) {
        String key = view.getPrefix() + "|" + view.getSuffix() + "|" + view.getVisibility().name();
        return adapterCache.computeIfAbsent(key, k -> {
            String teamName = "nt" + teamIdCounter.getAndIncrement();
            return new NametagAdapter(service, teamName, view.getPrefix(), view.getSuffix(), view.getVisibility());
        });
    }

    /**
     * Sends creation packets for all active adapters to a specific player.
     *
     * @param player The player to receive the packets.
     */
    public void sendAllAdapters(Player player) {
        for (NametagAdapter adapter : adapterCache.values()) {
            adapter.sendCreationPacket(player);
        }
    }

    /**
     * Cleans up a player's data from all perspectives when they quit.
     *
     * @param player The player who quit.
     */
    public void cleanupPlayer(Player player) {
        service.getPlayerPerspectives().values().forEach(p -> p.getDisplayedAdapters().remove(player.getUniqueId()));
    }
}