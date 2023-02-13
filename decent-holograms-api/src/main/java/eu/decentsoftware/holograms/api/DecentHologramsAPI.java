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

import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

/**
 * This class represents the main class of the plugin. It is responsible for
 * enabling and disabling the plugin. It also contains getters for all the
 * managers.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class DecentHologramsAPI extends JavaPlugin {

    /**
     * The current running instance of {@link DecentHologramsAPI}.
     */
    protected static DecentHologramsAPI instance;

    /**
     * Get the current running instance of {@link DecentHologramsAPI}.
     *
     * @return The instance.
     */
    @Contract(pure = true)
    public static DecentHologramsAPI getInstance() {
        return instance;
    }

    public DecentHologramsAPI() {
        instance = this;
    }

    /**
     * Reloads the plugin.
     */
    public abstract void reload();

    /**
     * Get the hologram registry.
     *
     * @return The hologram registry.
     * @see HologramRegistry
     */
    public abstract HologramRegistry getHologramRegistry();

}
