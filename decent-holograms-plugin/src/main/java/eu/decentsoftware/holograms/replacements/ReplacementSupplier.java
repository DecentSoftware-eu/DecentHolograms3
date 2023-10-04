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

package eu.decentsoftware.holograms.replacements;

import eu.decentsoftware.holograms.profile.Profile;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This functional interface represents the supplier, that finds the replacements of placeholders.
 *
 * @author d0by
 * @since 3.0.0
 */
@FunctionalInterface
public interface ReplacementSupplier {

    /**
     * Get the replacement from this supplier.
     *
     * @param profile  The profile to get the replacement for.
     * @param argument The argument to get it for.
     * @return The replacement optional.
     */
    Optional<String> getReplacement(@Nullable Profile profile, @Nullable String argument);

}
