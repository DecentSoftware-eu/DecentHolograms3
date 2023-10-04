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

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.actions.Action;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class SoundAction extends Action {

    protected final String sound;
    protected final float volume;
    protected final float pitch;

    public SoundAction(@NonNull DecentHolograms plugin, @NonNull String sound) {
        this(plugin, sound, 1.0f, 1.0f);
    }

    public SoundAction(@NonNull DecentHolograms plugin, @NonNull String sound, float volume, float pitch) {
        super(plugin);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundAction(@NonNull DecentHolograms plugin, long delay, double chance, @NonNull String sound, float volume, float pitch) {
        super(plugin, delay, chance);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(@NonNull Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

}
