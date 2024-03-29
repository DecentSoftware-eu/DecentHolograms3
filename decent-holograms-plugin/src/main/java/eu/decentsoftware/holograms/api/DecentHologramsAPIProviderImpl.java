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

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.internal.DecentHologramsAPIProvider;
import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DecentHologramsAPIProviderImpl extends DecentHologramsAPIProvider {

    private final DecentHolograms decentHolograms;
    private final Map<Plugin, DecentHologramsAPIImpl> apiMap = new ConcurrentHashMap<>();
    private final DecentHologramsAPIListener listener;

    public DecentHologramsAPIProviderImpl(@NonNull DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
        this.listener = new DecentHologramsAPIListener(this);
    }

    @Override
    public DecentHologramsAPIImpl getAPI(@NonNull Plugin plugin) {
        return this.apiMap.computeIfAbsent(plugin, p -> new DecentHologramsAPIImpl(this.decentHolograms));
    }

    public void shutdown() {
        HandlerList.unregisterAll(this.listener);

        this.apiMap.values().forEach(DecentHologramsAPIImpl::destroy);
        this.apiMap.clear();
    }

}
