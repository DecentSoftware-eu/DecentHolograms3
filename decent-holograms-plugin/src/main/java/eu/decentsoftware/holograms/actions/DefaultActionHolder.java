package eu.decentsoftware.holograms.actions;

import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.utils.S;
import org.jetbrains.annotations.NotNull;

public class DefaultActionHolder extends ActionHolder {

    @Override
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



}
