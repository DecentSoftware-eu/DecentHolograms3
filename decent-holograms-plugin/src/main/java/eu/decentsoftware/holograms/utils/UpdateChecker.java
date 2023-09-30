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

package eu.decentsoftware.holograms.utils;

import eu.decentsoftware.holograms.DecentHolograms;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * This class is used to check for updates.
 */
public class UpdateChecker {

    private final DecentHolograms plugin;
    private final String url;

    /**
     * Creates a new update checker.
     *
     * @param plugin     The plugin to check for updates.
     * @param resourceId The resource id of the plugin.
     */
    public UpdateChecker(DecentHolograms plugin, int resourceId) {
        this.plugin = plugin;
        this.url = "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId;
    }

    /**
     * Gets the latest version of the plugin from the Spigot API.
     *
     * @param consumer The consumer to call when the latest version is found.
     */
    public void check(final Consumer<String> consumer) {
        SchedulerUtil.async(() -> {
            try (InputStream inputStream = new URL(url).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().warning("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

}