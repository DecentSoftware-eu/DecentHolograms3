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

package eu.decentsoftware.holograms.hooks;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class providing methods to interact with the PlaceholderAPI plugin.
 *
 * @author d0by
 */
@UtilityClass
public final class PAPI {

    /**
     * Check whether PlaceholderAPI is available for use.
     *
     * @return Result boolean.
     */
    public static boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Set placeholders to given String for given Player.
     *
     * @param player The player.
     * @param string The string.
     * @return The string with replaced placeholders.
     */
    public static String setPlaceholders(Player player, String string) {
        if (isAvailable()) {
            return PlaceholderAPI.setPlaceholders(player, string);
        }
        return string;
    }

    /**
     * Set placeholders to given List of Strings for given Player.
     *
     * @param player The player.
     * @param stringList The string list.
     * @return The string with replaced placeholders.
     */
    public static List<String> setPlaceholders(Player player, List<String> stringList) {
        if (isAvailable()) {
            return stringList.stream().map(s -> setPlaceholders(player, s)).collect(Collectors.toList());
        }
        return stringList;
    }

    /**
     * Check whether the given string contains PAPI placeholders.
     *
     * @param string The string.
     * @return Result boolean.
     */
    public static boolean containsPlaceholders(String string) {
        if (isAvailable()) {
            return PlaceholderAPI.containsPlaceholders(string);
        }
        return false;
    }

    /**
     * Safely register a new PlaceholderExpansion to PAPI.
     *
     * @param expansion The expansion.
     */
    public static void registerExpansion(PlaceholderExpansion expansion) {
        if (isAvailable() && expansion != null && !expansion.isRegistered()) {
            expansion.register();
        }
    }

    /**
     * Safely unregister a PlaceholderExpansion to PAPI.
     *
     * @param expansion The expansion.
     */
    public static void unregisterExpansion(PlaceholderExpansion expansion) {
        if (isAvailable() && expansion != null && expansion.isRegistered()) {
            expansion.unregister();
        }
    }

}
