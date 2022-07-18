package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundBroadcastWorldAction extends SoundAction {

    public SoundBroadcastWorldAction(@NotNull String sound) {
        super(sound);
    }

    public SoundBroadcastWorldAction(@NotNull String sound, float volume, float pitch) {
        super(sound, volume, pitch);
    }

    public SoundBroadcastWorldAction(long delay, double chance, @NotNull String sound, float volume, float pitch) {
        super(delay, chance, sound, volume, pitch);
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player == null) {
            return;
        }
        World world = player.getWorld();
        for (Player worldPlayer : world.getPlayers()) {
            worldPlayer.playSound(worldPlayer.getLocation(), sound, volume, pitch);
        }
    }

}
