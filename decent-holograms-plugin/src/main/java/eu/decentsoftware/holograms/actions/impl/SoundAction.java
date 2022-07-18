package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.actions.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction extends DefaultAction {

    protected final @NotNull String sound;
    protected final float volume;
    protected final float pitch;

    public SoundAction(@NotNull String sound) {
        this(sound, 1.0f, 1.0f);
    }

    public SoundAction(@NotNull String sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundAction(long delay, double chance, @NotNull String sound, float volume, float pitch) {
        super(delay, chance);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

}
