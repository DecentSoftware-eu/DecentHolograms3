package eu.decent.holograms.conditions.impl;

import eu.decent.holograms.api.profile.Profile;
import eu.decent.holograms.conditions.DefaultCondition;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ExpCondition extends DefaultCondition {

    private final int minLevel;

    public ExpCondition(int minLevel) {
        this(false, minLevel);
    }

    public ExpCondition(boolean inverted, int minLevel) {
        super(inverted);
        this.minLevel = minLevel;
    }

    @Override
    public boolean check(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        return player.getLevel() >= minLevel;
    }

}
