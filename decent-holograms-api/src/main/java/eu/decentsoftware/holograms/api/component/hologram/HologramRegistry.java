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

package eu.decentsoftware.holograms.api.component.hologram;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * This class represents a registry of holograms. It holds all holograms that are currently registered.
 *
 * @author d0by
 * @see Hologram
 * @since 3.0.0
 */
public interface HologramRegistry {

    /**
     * Reload the registry. This method is called when the plugin is reloaded.
     */
    void reload();

    /**
     * Shutdown the registry. This method is called when the plugin is disabled.
     */
    void shutdown();

    /**
     * Register a new hologram.
     *
     * @param hologram The hologram.
     * @see Hologram
     */
    void registerHologram(@NotNull Hologram hologram);

    /**
     * Get a hologram by its name.
     *
     * @param name The name of the hologram.
     * @return The hologram.
     * @see Hologram
     */
    @Nullable
    Hologram getHologram(@NotNull String name);

    /**
     * Remove a hologram by its name.
     *
     * @param name The name of the hologram.
     * @see Hologram
     */
    @Nullable
    Hologram removeHologram(@NotNull String name);

    /**
     * Get the names of all registered holograms.
     *
     * @return The set of all hologram names.
     * @see Hologram
     */
    Set<String> getHologramNames();

    /**
     * Get all registered holograms.
     *
     * @return The collection of all holograms.
     * @see Hologram
     */
    Collection<Hologram> getHolograms();

}
