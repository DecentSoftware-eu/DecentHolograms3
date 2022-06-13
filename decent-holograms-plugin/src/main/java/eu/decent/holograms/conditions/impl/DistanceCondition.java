package eu.decent.holograms.conditions.impl;

import eu.decent.holograms.api.profile.Profile;
import eu.decent.holograms.conditions.DefaultCondition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DistanceCondition extends DefaultCondition {

    private final Location location;
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
        Location playerLocation = player.getLocation();
        World playerWorld = playerLocation.getWorld();
        World world = location.getWorld();
        return playerWorld != null && world != null &&
                playerWorld.getName().equals(world.getName()) &&
                playerLocation.distanceSquared(location) < maxDistanceSquared;
    }

}
