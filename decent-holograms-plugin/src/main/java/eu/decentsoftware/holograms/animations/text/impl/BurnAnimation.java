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
 * Burn animation will "burn" the text by changing the color of the text
 * progressively from left to right.
 *
 * @author d0by
 * @see TextAnimation
 * @since 3.0.0
 */
public class BurnAnimation extends TextAnimation {

    private static final String TEXT_COLOR = "&f";
    private static final String BURN_COLOR = "&b";

    public BurnAnimation() {
        super("burn", AnimationType.INTERNAL, 0, 2, 20);
    }

    @NonNull
    @Override
    public String animate(int tick, @Nullable String frameData, String... args) {
        if (frameData == null) {
            return "";
        }
        int length = frameData.length();
        int index = getActualStep(length, tick);
        if (index >= length) {
            return frameData;
        }
        return TEXT_COLOR + frameData.substring(0, index) + BURN_COLOR + frameData.substring(index);
    }

}
