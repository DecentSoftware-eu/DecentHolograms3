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
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class providing methods to interact with the HeadDatabaseAPI plugin.
 */
@UtilityClass
public final class HDB {

    public static final HeadDatabaseAPI API;

    static {
        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            API = new HeadDatabaseAPI();
        } else {
            API = null;
        }
    }

    /**
     * Check whether this hook is available for use.
     *
     * @return The requested boolean.
     */
    public static boolean isAvailable() {
        return API != null;
    }

    /**
     * Get the head item with the given id from HDB.
     *
     * @param id The id.
     * @return The head item.
     */
    @Nullable
    public static ItemStack getHeadItemStackById(@NotNull String id) {
        if (isAvailable()) {
            return API.getItemHead(id);
        }
        return null;
    }

}
