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
import eu.decentsoftware.holograms.api.hologram.HologramLine;
import eu.decentsoftware.holograms.core.CoreHologram;
import eu.decentsoftware.holograms.core.CoreHologramPage;
import eu.decentsoftware.holograms.core.line.CoreHologramLine;
import eu.decentsoftware.holograms.nms.NMSAdapter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * This is the context of a players profile. It is used to store some context
 * data, like the watched lines.
 *
 * @author d0by
 * @since 3.0.0
 */
public class ProfileContext {

    private final DecentHolograms plugin;
    private final int clickableEntityId;
    private CoreHologramLine watchedLine = null;
    private boolean clickableEntitySpawned = false;

    /**
     * Create a new instance of {@link ProfileContext}.
     *
     * @param plugin The plugin instance.
     */
    public ProfileContext(@NonNull DecentHolograms plugin) {
        this.plugin = plugin;
        this.clickableEntityId = this.plugin.getNMSManager().getAdapter().getFreeEntityId();
    }

    /**
     * Move the clickable entity to the given location or create it, if it is not spawned yet. This
     * entity is used to detect interaction with holograms.
     *
     * @param player   The player to move the entity for.
     * @param location The location to move the entity to.
     */
    public void moveOrCreateClickableEntity(@NonNull Player player, @NonNull Location location) {
        NMSAdapter nmsAdapter = this.plugin.getNMSManager().getAdapter();
        if (this.clickableEntitySpawned) {
            nmsAdapter.teleportEntity(player, this.clickableEntityId, location, false);
        } else {
            nmsAdapter.spawnEntityLiving(player, this.clickableEntityId, UUID.randomUUID(), EntityType.SLIME, location);
            Object metaProperties = nmsAdapter.getMetaEntityProperties(
                    false,
                    false,
                    false,
                    false,
                    true,
                    false,
                    false
            );
            nmsAdapter.sendEntityMetadata(player, this.clickableEntityId, metaProperties);
            this.clickableEntitySpawned = true;
        }
    }

    /**
     * Destroy the clickable entity for the given player.
     *
     * @param player The player to destroy the entity for.
     */
    public void destroyClickableEntity(@NonNull Player player) {
        if (this.clickableEntitySpawned) {
            this.plugin.getNMSManager().getAdapter().removeEntity(player, this.clickableEntityId);
            this.clickableEntitySpawned = false;
        }
    }

    /**
     * Get the ID of the entity, that is used to detect interaction
     * with holograms for this profile.
     *
     * @return The entity id.
     */
    public int getClickableEntityId() {
        return this.clickableEntityId;
    }

    /**
     * Get the currently watched line. This is the line, that the player is
     * looking at and can interact with.
     *
     * @return The currently watched line or null if no line is watched.
     * @see CoreHologramLine
     */
    @Nullable
    public CoreHologramLine getWatchedLine() {
        return this.watchedLine;
    }

    /**
     * Set the currently watched line. This is the line, that the player is
     * looking at and can interact with.
     *
     * @param line The line to watch or null to stop watching.
     * @see HologramLine
     */
    public void setWatchedLine(@Nullable CoreHologramLine line) {
        this.watchedLine = line;
    }

    /**
     * Set the currently watched page. This is the page, that the player is
     * looking at and can interact with.
     *
     * @return The currently watched page or null if no page is watched.
     * @see CoreHologramPage
     */
    @Nullable
    public CoreHologramPage<?> getWatchedPage() {
        CoreHologramLine line = getWatchedLine();
        return line == null ? null : line.getParent();
    }

    /**
     * Set the currently watched hologram. This is the hologram, that the player is
     * looking at and can interact with.
     *
     * @return The currently watched hologram or null if no hologram is watched.
     * @see CoreHologram
     */
    @Nullable
    public CoreHologram<?> getWatchedHologram() {
        CoreHologramPage<?> page = getWatchedPage();
        return page == null ? null : page.getParent();
    }

}
