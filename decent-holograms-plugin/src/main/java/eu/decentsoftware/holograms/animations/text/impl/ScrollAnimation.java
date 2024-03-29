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

package eu.decentsoftware.holograms.animations.text.impl;

import eu.decentsoftware.holograms.animations.AnimationType;
import eu.decentsoftware.holograms.animations.text.TextAnimation;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * This animation will scroll the text by only displaying a part of it at a time.
 *
 * @author d0by
 * @see TextAnimation
 * @since 3.0.0
 */
public class ScrollAnimation extends TextAnimation {

    public ScrollAnimation() {
        super("scroll", AnimationType.INTERNAL, 0, 2, 0);
    }

    @NonNull
    @Override
    public String animate(int tick, @Nullable String frameData, String... args) {
        if (frameData == null) {
            return "";
        }

        int length = frameData.length();
        int size = length / 3 * 2;
        int index = getActualStep(length, tick);
        if (index < size) {
            return frameData.substring(0, index + size);
        }
        if (index + size < length) {
            return frameData.substring(index - size, length);
        }
        return frameData.substring(index, length) + frameData.substring(0, index + size - length);
    }

}
