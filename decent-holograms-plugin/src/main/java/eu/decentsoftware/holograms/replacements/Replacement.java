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
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This class represents a custom placeholder.
 *
 * @author d0by
 * @since 3.0.0
 */
public class Replacement {

    private final ReplacementSupplier replacementSupplier;

    /**
     * Create new {@link Replacement}.
     *
     * @param replacementSupplier The supplier, that finds a replacement for this placeholder.
     */
    @Contract(pure = true)
    public Replacement(@NonNull ReplacementSupplier replacementSupplier) {
        this.replacementSupplier = replacementSupplier;
    }

    /**
     * Get the replacement for this placeholder.
     *
     * @param profile  The profile to get the replacement for.
     * @param argument The argument to get the replacement for.
     * @return The replacement optional.
     */
    public Optional<String> getReplacement(@Nullable Profile profile, @Nullable String argument) {
        return replacementSupplier.getReplacement(profile, argument);
    }

    /**
     * Get the replacement supplier for this placeholder.
     *
     * @return The replacement supplier.
     * @see ReplacementSupplier
     */
    @NonNull
    public ReplacementSupplier getReplacementSupplier() {
        return replacementSupplier;
    }

}