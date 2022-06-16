package eu.decentsoftware.holograms.animations.impl;

import eu.decentsoftware.holograms.animations.PositionalAnimation;
import eu.decentsoftware.holograms.api.component.common.PositionManager;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class RotatePositionalAnimation extends PositionalAnimation {

    public RotatePositionalAnimation() {
        super("rotate", 360);
    }

    @Override
    public void animate(@NotNull PositionManager positionManager, long step) {
        long actualStep = getActualStep(step);
        Location location = positionManager.getLocation();
        if (location != null) {
            location.setYaw(actualStep % 360);
        }
        // update the hologram location

    }

}
