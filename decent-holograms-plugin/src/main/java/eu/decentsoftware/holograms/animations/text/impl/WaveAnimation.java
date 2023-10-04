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
 * Wave text animation is a simple animation that creates a wave effect
 * on the given text by changing the color of a few characters, progressing
 * from left to right.
 *
 * @author d0by
 * @see TextAnimation
 * @see AnimationType
 * @since 3.0.0
 */
public class WaveAnimation extends TextAnimation {

    private static final String TEXT_COLOR = "&f";
    private static final String WAVE_COLOR = "&b";

    public WaveAnimation() {
        super("wave", AnimationType.INTERNAL, 0, 2, 0);
    }

    @NonNull
    @Override
    public String animate(int tick, @Nullable String frameData, String... args) {
        if (frameData == null) {
            return "";
        }

        String textColor = args != null && args.length > 0 ? args[0] : TEXT_COLOR;
        String waveColor = args != null && args.length > 1 ? args[1] : WAVE_COLOR;

        int length = frameData.length();
        int wave = tick % (length * 2);
        int waveSize = length / 10;
        if (waveSize < 1) {
            waveSize = 1;
        }
        int waveIndex = wave < length ? wave : length * 2 - wave - 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i >= waveIndex - waveSize && i <= waveIndex + waveSize) {
                builder.append(waveColor);
            } else {
                builder.append(textColor);
            }
            builder.append(frameData.charAt(i));
        }
        return builder.toString();
    }

}
