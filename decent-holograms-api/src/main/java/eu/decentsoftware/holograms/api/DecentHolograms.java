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

import java.io.File;

/**
 * This class represents the main class of the plugin. It is responsible for
 * enabling and disabling the plugin. It also contains getters for all the
 * managers.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class DecentHolograms extends JavaPlugin {

    /**
     * The current running instance of {@link DecentHolograms}.
     */
    protected static DecentHolograms instance;
    /**
     * This is used to get free entity IDs. It starts at a million
     * so that it doesn't conflict with normal entities.
     */
    private static int ENTITY_ID = 1_000_000;
    /**
     * The '/holograms' directory
     */
    private File hologramsFolder;

    /**
     * Get the current running instance of {@link DecentHolograms}.
     *
     * @return The instance.
     */
    @Contract(pure = true)
    public static DecentHolograms getInstance() {
        return instance;
    }

    /**
     * Get a free entity ID.
     *
     * @return The new entity ID.
     */
    public static int getFreeEntityId() {
        return ENTITY_ID++;
    }

    public DecentHolograms() {
        instance = this;
    }

    /**
     * Get the "/holograms" folder.
     *
     * @return The folder.
     */
    public File getHologramFolder() {
        if (hologramsFolder == null) {
            hologramsFolder = new File(getDataFolder(), "holograms");
        }
        return hologramsFolder;
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
