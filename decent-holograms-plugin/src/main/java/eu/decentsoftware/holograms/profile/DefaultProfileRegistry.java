package eu.decentsoftware.holograms.profile;

import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.profile.ProfileRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultProfileRegistry implements ProfileRegistry {

    private final Map<String, Profile> profileMap;

    /**
     * Creates a new profile registry.
     */
    public DefaultProfileRegistry() {
        this.profileMap = new ConcurrentHashMap<>();
        this.reload();
    }

    @Override
    public void reload() {
        this.shutdown();

        // -- Create profiles for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            registerProfile(player.getName());
        }
    }

    @Override
    public void shutdown() {
        this.profileMap.clear();
    }

    @Override
    public void registerProfile(@NotNull String name) {
        this.profileMap.put(name, new DefaultProfile(name));
    }

    @Override
    public Profile getProfile(@NotNull String name) {
        return this.profileMap.get(name);
    }

    @Override
    public void removeProfile(@NotNull String name) {
        this.profileMap.remove(name);
    }
}
