package eu.decentsoftware.holograms.profile;

import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.profile.ProfileContext;
import org.jetbrains.annotations.Nullable;

public class DefaultProfileContext implements ProfileContext {

    private Line line;

    /**
     * Create a new instance of {@link DefaultProfileContext}.
     */
    public DefaultProfileContext() {
        this.line = null;
    }

    @Nullable
    @Override
    public Line getWatchedLine() {
        return line;
    }

    @Override
    public void setWatchedLine(@Nullable Line line) {
        this.line = line;
    }

}
