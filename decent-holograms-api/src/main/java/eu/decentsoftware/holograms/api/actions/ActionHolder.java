package eu.decentsoftware.holograms.api.actions;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.utils.S;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a holder for actions.
 *
 * @author d0by
 */
public abstract class ActionHolder extends DList<Action> {

    /**
     * Execute all Actions in this holder for the given {@link Profile}.
     *
     * @param profile The profile.
     */
    public void execute(@NotNull Profile profile) {
        for (Action action : this) {
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
     * Load all Actions from a configuration section.
     *
     * @param config The configuration section.
     */
    public abstract void load(@NotNull ConfigurationSection config);

}
