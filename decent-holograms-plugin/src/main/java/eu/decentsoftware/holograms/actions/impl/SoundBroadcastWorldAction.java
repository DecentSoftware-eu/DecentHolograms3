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
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundBroadcastWorldAction extends SoundAction {

    public SoundBroadcastWorldAction(@NotNull String sound) {
        super(sound);
    }

    public SoundBroadcastWorldAction(@NotNull String sound, float volume, float pitch) {
        super(sound, volume, pitch);
    }

    public SoundBroadcastWorldAction(long delay, double chance, @NotNull String sound, float volume, float pitch) {
        super(delay, chance, sound, volume, pitch);
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player == null) {
            return;
        }
        World world = player.getWorld();
        for (Player worldPlayer : world.getPlayers()) {
            worldPlayer.playSound(worldPlayer.getLocation(), sound, volume, pitch);
        }
    }

}
