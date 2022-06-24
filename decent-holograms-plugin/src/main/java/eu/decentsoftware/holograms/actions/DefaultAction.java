package eu.decentsoftware.holograms.actions;

import eu.decentsoftware.holograms.api.actions.Action;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an action, that can be executed for a specific profile.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
public abstract class DefaultAction implements Action {

    protected final long delay;
    protected final double chance;

    /**
     * Create new {@link DefaultAction}.
     */
    public DefaultAction() {
        this(0, -1);
    }

    /**
     * Create new {@link DefaultAction}.
     *
     * @param delay Delay of this action.
     * @param chance Chance of this action.
     */
    public DefaultAction(long delay, double chance) {
        this.delay = delay;
        this.chance = chance;
    }

    /**
     * Check the chance of this action.
     *
     * @return Boolean whether this action should be executed.
     */
    public boolean checkChance() {
        final double chance = getChance();
        if (chance < 0 || chance > 100) {
            return true;
        }
        return (Math.random() * 100) < chance;
    }

    /**
     * Load an action from a configuration section.
     *
     * @param config The configuration section.
     * @return The action.
     */
    public static DefaultAction load(@NotNull ConfigurationSection config) {

        return null;
    }

}
