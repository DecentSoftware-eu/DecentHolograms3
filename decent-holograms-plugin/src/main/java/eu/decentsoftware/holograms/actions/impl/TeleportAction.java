package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.actions.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportAction extends DefaultAction {

    private final Location location;

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
        player.teleport(location);
    }

}
