package eu.decentsoftware.holograms.api.actions;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.utils.S;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class represents a holder for actions.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface ActionHolder {

    /**
     * Execute all Actions in this holder for the given {@link Profile}.
     *
     * @param profile The profile.
     */
    default void execute(@NotNull Profile profile) {
        for (Action action : getActions()) {
            // Check the chance
            if (!action.checkChance()) {
                continue;
            }
            // Execute with delay if needed
            long delay = action.getDelay();
            if (delay > 0) {
                S.run(() -> action.execute(profile), delay);
            } else {
                S.run(() -> action.execute(profile));
            }
        }
    }

    /**
     * Add the given action to this holder.
     *
     * @param action The action.
     */
    void addAction(@NotNull Action action);

    /**
     * Remove the given action from this holder.
     *
     * @param action The action.
     */
    void removeAction(@NotNull Action action);

    /**
     * Remove the action at the given index from this holder.
     *
     * @param index The index.
     */
    void removeAction(int index);

    /**
     * Remove all actions from this holder.
     */
    void clearActions();

    /**
     * Get all actions in this holder.
     *
     * @return All actions.
     */
    List<Action> getActions();

}