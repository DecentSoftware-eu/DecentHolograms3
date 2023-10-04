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
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DistanceCondition extends Condition {

    private final Location location;
    private final double maxDistanceSquared;

    public DistanceCondition(@NonNull Location location, double maxDistance) {
        this(false, location, maxDistance);
    }

    public DistanceCondition(boolean inverted, @NonNull Location location, double maxDistance) {
        super(inverted);
        this.location = location;
        this.maxDistanceSquared = maxDistance * maxDistance;
    }

    @Override
    public boolean check(@NonNull Player player) {
        Location pLocation = player.getLocation();
        World pWorld = pLocation.getWorld();
        World world = location.getWorld();
        return pWorld != null && pWorld.equals(world) && pLocation.distanceSquared(location) < maxDistanceSquared;
    }

}
