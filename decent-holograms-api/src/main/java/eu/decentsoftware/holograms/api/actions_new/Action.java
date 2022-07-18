package eu.decentsoftware.holograms.api.actions_new;

import eu.decentsoftware.holograms.api.profile.Profile;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a specific action. It can be instantiated,
 * then executed as many times as needed. This class holds all parameters
 * of the action and uses them to properly execute the action.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface Action {

    /**
     * Execute this action for the given {@link Profile}.
     *
     * @param profile The profile.
     */
    void execute(@NotNull Profile profile);

    /**
     * Check if this action should be executed according to chance.
     *
     * @return True if this action should be executed.
     */
    boolean checkChance();

    /**
     * Get the delay of this action. This is the amount of ticks
     * that should be waited before executing this action.
     *
     * @return The delay of this action.
     */
    long getDelay();

    /**
     * Get the chance of this action. This is the chance that this action
     * should be executed.
     *
     * @return The chance of this action.
     */
    double getChance();

}
