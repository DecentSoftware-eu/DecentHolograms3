package eu.decentsoftware.holograms.animations.impl;

import eu.decentsoftware.holograms.animations.DefaultAnimation;
import eu.decentsoftware.holograms.api.animations.AnimationType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomAnimation extends DefaultAnimation {

    private final @NotNull List<String> steps;

    public CustomAnimation(@NotNull String name, @NotNull AnimationType type, @NotNull List<String> steps, int speed, int pause) {
        super(name, type, steps.size(), speed, pause);
        this.steps = steps;
    }

    @NotNull
    @Override
    public String animate(@NotNull String text, int step, String... args) {
        return steps.get(getActualStep(step));
    }

}
