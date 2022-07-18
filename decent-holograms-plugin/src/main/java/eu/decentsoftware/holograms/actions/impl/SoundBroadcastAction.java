package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundBroadcastAction extends SoundAction {

    public SoundBroadcastAction(@NotNull String sound) {
        super(sound);
    }

    public SoundBroadcastAction(@NotNull String sound, float volume, float pitch) {
        super(sound, volume, pitch);
    }

    public SoundBroadcastAction(long delay, double chance, @NotNull String sound, float volume, float pitch) {
        super(delay, chance, sound, volume, pitch);
    }

    @Override
    public void execute(@NotNull Profile profile) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), sound, volume, pitch);
        }
    }

}
