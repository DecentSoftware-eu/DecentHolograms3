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

package eu.decentsoftware.holograms.animations.text;

import eu.decentsoftware.holograms.animations.AnimationType;
import eu.decentsoftware.holograms.utils.Common;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomTextAnimation extends TextAnimation {

    protected final @NotNull List<String> frameList;

    public CustomTextAnimation(@NotNull String name, @NotNull AnimationType type, int speed, int pause, @NotNull List<String> frameList) {
        super(name, type, frameList.size(), speed, pause);
        this.frameList = frameList;
    }

    @NotNull
    public String animate(int tick, @Nullable String string) {
        if (type == AnimationType.RANDOM) {
            return frameList.get(Common.irand(0, totalSteps - 1));
        }

        int actualStep = getActualStep(tick);
        switch (type) {
            case INTERNAL:
            case ASCEND:
                return frameList.get(actualStep);
            case DESCEND:
                return frameList.get(totalSteps - actualStep - 1);
            case ASCEND_DESCEND:
                if (actualStep < totalSteps / 2) {
                    return frameList.get(actualStep);
                } else {
                    // Count from max to min
                    return frameList.get(totalSteps - actualStep - 1);
                }
        }
        return ""; // Should never happen
    }

}
