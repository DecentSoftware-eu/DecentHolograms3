package eu.decentsoftware.holograms.conditions.impl;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.conditions.DefaultCondition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DistanceCondition extends DefaultCondition {

    private final @NotNull Location location;
    private final double maxDistanceSquared;

    public DistanceCondition(@NotNull Location location, double maxDistance) {
        this(false, location, maxDistance);
    }

    public DistanceCondition(boolean inverted, @NotNull Location location, double maxDistance) {
        super(inverted);
        this.location = location;
        this.maxDistanceSquared = maxDistance * maxDistance;
    }

    @Override
    public boolean check(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player == null) {
            return false;
        }
        Location pLocation = player.getLocation();
        World pWorld = pLocation.getWorld();
        World world = location.getWorld();
        return pWorld != null && pWorld.equals(world) && pLocation.distanceSquared(location) < maxDistanceSquared;
    }

}
