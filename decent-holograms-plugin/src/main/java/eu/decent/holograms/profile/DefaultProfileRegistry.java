package eu.decent.holograms.profile;

import eu.decent.holograms.api.profile.ProfileRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DefaultProfileRegistry extends ProfileRegistry {

    /**
     * Creates a new profile registry.
     */
    public DefaultProfileRegistry() {
        this.reload();
    }

    @Override
    public void reload() {
        this.clear();

        // -- Create profiles for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            register(player.getName());
        }
    }

    @Override
    public void register(@NotNull String name) {
        register(name, new DefaultProfile(name));
    }

}
