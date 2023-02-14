/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.profile;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.hologram.line.HologramLine;
import eu.decentsoftware.holograms.api.hologram.page.HologramPage;
import org.jetbrains.annotations.Nullable;

/**
 * This is the context of a players profile. It is used to store some context
 * data, like the watched lines.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ProfileContext {

    private final int clickableEntityId;
    private HologramLine watchedLine;

    /**
     * Create a new instance of {@link ProfileContext}.
     */
    public ProfileContext() {
        this.watchedLine = null;
        this.clickableEntityId = DecentHolograms.getInstance().getNMSManager().getAdapter().getFreeEntityId();
    }

    /**
     * Get the ID of the entity, that is used to detect interaction
     * with holograms for this profile.
     *
     * @return The entity id.
     */
    public int getClickableEntityId() {
        return clickableEntityId;
    }

    /**
     * Get the currently watched line. This is the line, that the player is
     * looking at and can interact with.
     *
     * @return The currently watched line or null if no line is watched.
     * @see HologramLine
     */
    @Nullable
    public HologramLine getWatchedLine() {
        return watchedLine;
    }

    /**
     * Set the currently watched line. This is the line, that the player is
     * looking at and can interact with.
     *
     * @param line The line to watch or null to stop watching.
     * @see HologramLine
     */
    public void setWatchedLine(@Nullable HologramLine line) {
        this.watchedLine = line;
    }

    /**
     * Set the currently watched page. This is the page, that the player is
     * looking at and can interact with.
     *
     * @return The currently watched page or null if no page is watched.
     * @see HologramPage
     */
    @Nullable
    public HologramPage getWatchedPage() {
        HologramLine line = getWatchedLine();
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
    public Hologram getWatchedHologram() {
        HologramPage page = getWatchedPage();
        return page == null ? null : page.getParent();
    }

}
