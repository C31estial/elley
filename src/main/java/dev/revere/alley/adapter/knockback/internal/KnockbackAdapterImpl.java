package dev.revere.alley.adapter.knockback.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.adapter.knockback.Knockback;
import dev.revere.alley.adapter.knockback.KnockbackAdapter;
import dev.revere.alley.adapter.knockback.KnockbackType;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
@Getter
@Service(provides = KnockbackAdapter.class, priority = 50)
public class KnockbackAdapterImpl implements KnockbackAdapter {
    private final AlleyPlugin plugin;
    private Knockback knockbackImplementation;

    /**
     * Constructor for DI. Receives the main bootstrap instance.
     */
    public KnockbackAdapterImpl(AlleyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.knockbackImplementation = this.determineKnockback();
    }

    @Override
    public Knockback getKnockbackImplementation() {
        if (this.knockbackImplementation == null) {
            throw new IllegalStateException("KnockbackAdapter has not been initialized yet.");
        }
        return this.knockbackImplementation;
    }

    /**
     * Determines the knockback implementation to use based on the server's name.
     *
     * @return The selected knockback implementation.
     */
    private Knockback determineKnockback() {
        // Always use default knockback implementation for standard Spigot
        return new DefaultKnockbackImpl();
    }
}