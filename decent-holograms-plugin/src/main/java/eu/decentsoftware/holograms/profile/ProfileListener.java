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

package eu.decentsoftware.holograms.profile;

import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")
class ProfileListener implements Listener {

    private final ProfileRegistry profileRegistry;

    @Contract(pure = true)
    public ProfileListener(@NonNull ProfileRegistry profileRegistry) {
        this.profileRegistry = profileRegistry;
    }

    @EventHandler
    public void onJoin(@NonNull PlayerJoinEvent event) {
        profileRegistry.registerProfile(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(@NonNull PlayerQuitEvent event) {
        profileRegistry.removeProfile(event.getPlayer().getUniqueId());
    }

}
