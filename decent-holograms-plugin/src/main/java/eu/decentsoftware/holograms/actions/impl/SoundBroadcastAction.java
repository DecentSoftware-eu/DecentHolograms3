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

package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundBroadcastAction extends SoundAction {

    public SoundBroadcastAction(@NotNull String sound) {
        super(sound);
    }

    public SoundBroadcastAction(@NotNull String sound, float volume, float pitch) {
        super(sound, volume, pitch);
    }

    public SoundBroadcastAction(long delay, double chance, @NotNull String sound, float volume, float pitch) {
        super(delay, chance, sound, volume, pitch);
    }

    @Override
    public void execute(@NotNull Profile profile) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), sound, volume, pitch);
        }
    }

}
