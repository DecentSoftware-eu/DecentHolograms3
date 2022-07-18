package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.actions.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.jetbrains.annotations.NotNull;

public class WaitAction extends DefaultAction {

    public WaitAction(long delay) {
        super(delay, -1.0);
    }

    public WaitAction(long delay, double chance) {
        super(delay, chance);
    }

    @Override
    public void execute(@NotNull Profile profile) {
        // do nothing, action holder will handle it
    }

}
