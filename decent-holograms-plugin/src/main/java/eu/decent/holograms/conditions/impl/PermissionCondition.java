package eu.decent.holograms.conditions.impl;

import eu.decent.holograms.api.profile.Profile;
import eu.decent.holograms.conditions.DefaultCondition;
import eu.decent.holograms.api.hooks.PAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link DefaultCondition} that checks
 * whether the player has a specified permission.
 */
public class PermissionCondition extends DefaultCondition {

    private final String permission;

    public PermissionCondition(@NotNull String permission) {
        this(false, permission);
    }

    public PermissionCondition(boolean inverted, @NotNull String permission) {
        super(inverted);
        this.permission = permission;
    }

    @Override
    public boolean check(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        return player.hasPermission(PAPI.setPlaceholders(player, this.permission));
    }

}
