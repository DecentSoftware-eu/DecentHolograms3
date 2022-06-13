package eu.decent.holograms.api.actions;

import eu.decent.holograms.api.profile.Profile;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an action, that can be executed for a specific profile.
 *
 * @author d0by
 */
public interface Action {

    /**
     * Execute this action for the given profile.
     *
     * @param profile The profile.
     */
    void execute(@NotNull Profile profile);

    /**
     * Check the chance of this action.
     *
     * @return Boolean whether this action should be executed.
     */
    boolean checkChance();

    /**
     * Get the delay of this action.
     *
     * @return The delay.
     */
    long getDelay();

    /**
     * Get the chance of this action.
     *
     * @return The chance.
     */
    double getChance();

}
