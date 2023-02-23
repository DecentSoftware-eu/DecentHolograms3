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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * !! INTERNAL USE ONLY !!
 * <p>
 * Internal class to provide the {@link DecentHologramsAPI} instance.
 *
 * @author d0by
 * @since 3.0.0
 */
@ApiStatus.Internal
public final class DecentHologramsAPIProvider {

    private static DecentHologramsAPI instance;

    @Contract(value = " -> fail", pure = true)
    private DecentHologramsAPIProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    @Contract(pure = true)
    public static DecentHologramsAPI getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DecentHologramsAPI instance is not set.");
        }
        return instance;
    }

    public static void setInstance(@NotNull DecentHologramsAPI api) {
        if (instance != null) {
            throw new IllegalStateException("DecentHologramsAPI instance is already set.");
        }
        instance = api;
    }

}
