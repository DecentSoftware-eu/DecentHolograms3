package eu.decent.holograms.api.actions;

import eu.decent.holograms.api.profile.Profile;
import eu.decent.holograms.api.utils.collection.DList;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a holder for actions.
 */
public abstract class ActionHolder extends DList<Action> {

    /**
     * Execute all Actions in this holder for the given {@link Profile}.
     *
     * @param profile The profile.
     */
    public abstract void execute(@NotNull Profile profile);

}
