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

package eu.decentsoftware.holograms.animations.item;

import com.cryptomorin.xseries.XMaterial;
import eu.decentsoftware.holograms.animations.Animation;
import eu.decentsoftware.holograms.animations.AnimationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This animation is used to animate items. It takes a list of materials and
 * returns the material that should be displayed at the current tick.
 *
 * @author d0by
 * @see Animation
 * @see AnimationType
 * @see XMaterial
 * @since 3.0.0
 */
public class MaterialAnimation extends Animation<XMaterial> {

    private final XMaterial[] frames;

    public MaterialAnimation(@NotNull String name, @NotNull AnimationType type, int totalSteps, int interval, int pause, @NotNull XMaterial... frames) {
        super(name, type, totalSteps, interval, pause);
        this.frames = frames;
    }

    public MaterialAnimation(@NotNull String name, @NotNull AnimationType type, int totalSteps, int interval, int pause, @NotNull List<XMaterial> frames) {
        this(name, type, totalSteps, interval, pause, frames.toArray(new XMaterial[0]));
    }

    @NotNull
    @Override
    public XMaterial animate(int tick, @Nullable XMaterial argument) {
        return frames[tick % (frames.length * interval) / interval];
    }

}
