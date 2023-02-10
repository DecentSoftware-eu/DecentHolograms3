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

package eu.decentsoftware.holograms.conditions.impl;

import eu.decentsoftware.holograms.conditions.Condition;
import eu.decentsoftware.holograms.hooks.PAPI;
import eu.decentsoftware.holograms.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a {@link Condition} that checks
 * whether the player has a specific permission.
 */
public class PermissionCondition extends Condition {

    private final @NotNull String permission;

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
        return player != null && player.hasPermission(PAPI.setPlaceholders(player, this.permission));
    }

}
