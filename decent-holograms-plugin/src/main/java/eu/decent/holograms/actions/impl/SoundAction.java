package eu.decent.holograms.actions.impl;

import eu.decent.holograms.actions.DefaultAction;
import eu.decent.holograms.actions.ActionType;
import eu.decent.holograms.api.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction extends DefaultAction {

    private final ActionType type;
    private final Sound sound;
    private final float volume;
    private final float pitch;

    public SoundAction(@NotNull ActionType type, @NotNull Sound sound, float volume, float pitch) {
        this.type = type;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundAction(long delay, double chance, @NotNull ActionType type, @NotNull Sound sound, float volume, float pitch) {
        super(delay, chance);
        this.type = type;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        World world = player.getWorld();
        switch (type) {
            case SOUND:
                player.playSound(player.getLocation(), sound, volume, pitch);
                break;
            case BROADCAST_SOUND:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), sound, volume, pitch);
                }
                break;
            case BROADCAST_SOUND_WORLD:
                for (Player worldPlayer : world.getPlayers()) {
                    worldPlayer.playSound(worldPlayer.getLocation(), sound, volume, pitch);
                }
                break;
            default: break;
        }
    }

}
