package eu.decent.holograms.actions;

import eu.decent.holograms.api.actions.Action;
import lombok.Getter;

/**
 * This class represents an action, that can be executed for a specific profile.
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

}
