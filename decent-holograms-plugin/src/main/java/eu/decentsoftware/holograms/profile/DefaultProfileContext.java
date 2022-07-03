package eu.decentsoftware.holograms.profile;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.profile.ProfileContext;
import org.jetbrains.annotations.Nullable;

public class DefaultProfileContext implements ProfileContext {

    private final int clickableEntityId;
    private Line watchedLine;

    /**
     * Create a new instance of {@link DefaultProfileContext}.
     */
    public DefaultProfileContext() {
        this.watchedLine = null;
        this.clickableEntityId = DecentHolograms.getFreeEntityId();
    }

    @Override
    public int getClickableEntityId() {
        return clickableEntityId;
    }

    @Nullable
    @Override
    public Line getWatchedLine() {
        return watchedLine;
    }

    @Override
    public void setWatchedLine(@Nullable Line line) {
        this.watchedLine = line;
    }

}
