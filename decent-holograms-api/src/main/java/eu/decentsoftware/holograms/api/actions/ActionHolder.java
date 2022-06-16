package eu.decentsoftware.holograms.api.actions;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.utils.collection.DList;
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
    public abstract void execute(@NotNull Profile profile);

}
