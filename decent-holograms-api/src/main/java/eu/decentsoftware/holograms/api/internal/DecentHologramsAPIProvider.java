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

package eu.decentsoftware.holograms.api.internal;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * !! INTERNAL USE ONLY !!
 * <p>
 * Use {@link DecentHologramsAPI#getInstance(Plugin)} to get an instance of the API.
 *
 * @author d0by
 * @see DecentHologramsAPI#getInstance(Plugin)
 * @since 3.0.0
 */
@ApiStatus.Internal
public abstract class DecentHologramsAPIProvider {

    private static DecentHologramsAPIProvider implementation;

    @Contract(pure = true)
    @ApiStatus.Internal
    public static DecentHologramsAPIProvider getImplementation() {
        if (DecentHologramsAPIProvider.implementation == null) {
            throw new IllegalStateException("DecentHologramsAPIProvider implementation is not set.");
        }
        return DecentHologramsAPIProvider.implementation;
    }

    @ApiStatus.Internal
    public static void setImplementation(@NonNull DecentHologramsAPIProvider implementation) {
        if (DecentHologramsAPIProvider.implementation != null) {
            throw new IllegalStateException("DecentHologramsAPIProvider implementation is already set.");
        }
        DecentHologramsAPIProvider.implementation = implementation;
    }

    public abstract DecentHologramsAPI getAPI(@NonNull Plugin plugin);

}
