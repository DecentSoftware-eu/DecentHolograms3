package eu.decentsoftware.holograms.actions_new.impl;

import eu.decentsoftware.holograms.actions_new.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportAction extends DefaultAction {

    protected final @NotNull Location location;

    public TeleportAction(@NotNull Location location) {
        this.location = location;
    }

    public TeleportAction(long delay, double chance, @NotNull Location location) {
        super(delay, chance);
        this.location = location;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            player.teleport(location);
        }
    }

}
