package eu.decentsoftware.holograms.animations.impl;

import eu.decentsoftware.holograms.animations.DefaultAnimation;
import eu.decentsoftware.holograms.api.animations.AnimationType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class RainbowAnimation extends DefaultAnimation {

    private static final List<String> DEFAULT_COLORS = Arrays.asList("&c", "&6", "&e", "&a", "&b", "&d");

    public RainbowAnimation() {
        super("rainbow", AnimationType.ASCEND, DEFAULT_COLORS.size(), 60, 0);
    }

    @NotNull
    @Override
    public String animate(@NotNull String text, int step, String... args) {
        return DEFAULT_COLORS.get(getActualStep(step)) + text;
    }

}
