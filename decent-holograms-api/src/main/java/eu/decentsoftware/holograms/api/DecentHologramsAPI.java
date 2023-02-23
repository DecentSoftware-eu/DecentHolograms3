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

package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.hologram.Hologram;
import eu.decentsoftware.holograms.api.internal.DecentHologramsAPIProvider;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This is the main class of the API. It serves as the access point to the API.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface DecentHologramsAPI {

    /**
     * Get the instance of the API.
     *
     * @return The instance of the API.
     */
    @Contract(pure = true)
    static DecentHologramsAPI getInstance() {
        return DecentHologramsAPIProvider.getInstance();
    }

    /**
     * Create a new, empty hologram.
     *
     * @param location The location of the hologram.
     * @return The new hologram.
     * @see Hologram
     */
    @NotNull
    Hologram createHologram(@NotNull Location location);

    /**
     * Create a new, empty hologram.
     *
     * @param location   The location of the hologram.
     * @param persistent Whether the hologram should be persistent. (Saved in a file)
     * @return The new hologram.
     * @see Hologram
     */
    @NotNull
    Hologram createHologram(@NotNull Location location, boolean persistent);

    /**
     * Create a new hologram with the given lines at the first page.
     * <p>
     * You can use any kind of formatting in the lines. It will be automatically
     * parsed just like if you were using the commands.
     *
     * @param location The location of the hologram.
     * @param lines    The lines of the first page of the hologram.
     * @return The new hologram.
     * @see Hologram
     */
    @NotNull
    Hologram createHologram(@NotNull Location location, @NotNull List<String> lines);

    /**
     * Create a new hologram with the given lines at the first page.
     * <p>
     * You can use any kind of formatting in the lines. It will be automatically
     * parsed just like if you were using the commands.
     *
     * @param location   The location of the hologram.
     * @param lines      The lines of the first page of the hologram.
     * @param persistent Whether the hologram should be persistent. (Saved in a file)
     * @return The new hologram.
     * @see Hologram
     */
    @NotNull
    Hologram createHologram(@NotNull Location location, @NotNull List<String> lines, boolean persistent);

}
