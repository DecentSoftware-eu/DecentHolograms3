package eu.decentsoftware.holograms.profile;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.profile.ProfileContext;
import org.jetbrains.annotations.NotNull;

public class DefaultProfile implements Profile {

    private final @NotNull String name;
    private final @NotNull ProfileContext context;

    /**
     * Create a new profile for the given player.
     *
     * @param name The player's nickname.
     */
    public DefaultProfile(@NotNull String name) {
        this.name = name;
        this.context = new DefaultProfileContext();
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public ProfileContext getContext() {
        return context;
    }
}
