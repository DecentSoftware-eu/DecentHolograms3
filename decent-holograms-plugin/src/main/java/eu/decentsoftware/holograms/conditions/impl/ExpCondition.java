package eu.decentsoftware.holograms.conditions.impl;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.conditions.DefaultCondition;
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
        return player != null && player.getLevel() >= minLevel;
    }

}
