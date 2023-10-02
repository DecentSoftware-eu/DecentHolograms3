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

package eu.decentsoftware.holograms.api.hologram;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a hologram config. It is responsible for loading and saving the parent hologram.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface HologramConfig {

    /**
     * Get the parent hologram.
     *
     * @return The parent hologram.
     */
    @NotNull
    Hologram getParent();

    /**
     * Get the hologram file.
     *
     * @return The hologram file.
     */
    @NotNull
    File getFile();

    /**
     * Save the hologram file.
     *
     * @return CompletableFuture that completes when the operation is finished.
     */
    CompletableFuture<Void> save();

    /**
     * Reload the hologram file.
     *
     * @return CompletableFuture that completes when the operation is finished.
     */
    CompletableFuture<Void> reload();

    /**
     * Delete the hologram file.
     */
    void delete();

}
