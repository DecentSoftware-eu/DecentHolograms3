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

import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")
class DecentHologramsAPIListener implements Listener {

    private final DecentHologramsAPIProviderImpl apiProvider;

    @Contract(pure = true)
    public DecentHologramsAPIListener(@NonNull DecentHologramsAPIProviderImpl apiProvider) {
        this.apiProvider = apiProvider;
    }

    @EventHandler
    public void onPluginDisable(@NonNull PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();
        DecentHologramsAPIImpl apiInstance = this.apiProvider.getAPI(plugin);
        if (apiInstance != null) {
            apiInstance.destroy();
        }
    }

}
