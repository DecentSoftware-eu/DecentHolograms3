package eu.decentsoftware.holograms.animations;

import eu.decentsoftware.holograms.api.component.common.PositionManager;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a positional animation.
 */
public abstract class PositionalAnimation extends AbstractAnimation {

    public PositionalAnimation(@NotNull String name, long totalSteps) {
        super(name, totalSteps);
    }

    /**
     * Update the position component with the next frame of the animation.
     *
     * @param positionManager the position manager to update
     * @param step the current tick/step of the animation
     */
    public abstract void animate(@NotNull PositionManager positionManager, long step);

}
