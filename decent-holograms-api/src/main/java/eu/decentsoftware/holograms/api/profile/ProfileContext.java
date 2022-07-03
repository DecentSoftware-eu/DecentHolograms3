package eu.decentsoftware.holograms.api.profile;

import eu.decentsoftware.holograms.api.component.hologram.Hologram;
import eu.decentsoftware.holograms.api.component.line.Line;
import eu.decentsoftware.holograms.api.component.page.Page;
import org.jetbrains.annotations.Nullable;

/**
 * This is the context of a players profile. It is used to store some context
 * data, like the watched lines.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface ProfileContext {

    /**
     * Get the ID of the entity, that is used to detect interaction
     * with holograms for this profile.
     *
     * @return The entity id.
     */
    int getClickableEntityId();

    /**
     * Get the currently watched line. This is the line, that the player is
     * looking at and can interact with.
     *
     * @return The currently watched line or null if no line is watched.
     * @see Line
     */
    @Nullable
    Line getWatchedLine();

    /**
     * Set the currently watched line. This is the line, that the player is
     * looking at and can interact with.
     *
     * @param line The line to watch or null to stop watching.
     * @see Line
     */
    void setWatchedLine(@Nullable Line line);

    /**
     * Set the currently watched page. This is the page, that the player is
     * looking at and can interact with.
     *
     * @return The currently watched page or null if no page is watched.
     * @see Page
     */
    @Nullable
    default Page getWatchedPage() {
        Line line = getWatchedLine();
        return line == null ? null : line.getParent();
    }

    /**
     * Set the currently watched hologram. This is the hologram, that the player is
     * looking at and can interact with.
     *
     * @return The currently watched hologram or null if no hologram is watched.
     * @see Hologram
     */
    @Nullable
    default Hologram getWatchedHologram() {
        Page page = getWatchedPage();
        return page == null ? null : page.getParent();
    }

}
