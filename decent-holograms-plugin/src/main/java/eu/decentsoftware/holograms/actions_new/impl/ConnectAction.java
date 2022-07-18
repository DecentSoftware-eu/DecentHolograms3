package eu.decentsoftware.holograms.actions_new.impl;

import eu.decentsoftware.holograms.actions_new.DefaultAction;
import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.utils.BungeeUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConnectAction extends DefaultAction {

    protected final @NotNull String server;

    public ConnectAction(@NotNull String server) {
        this.server = server;
    }

    public ConnectAction(long delay, double chance, @NotNull String server) {
        super(delay, chance);
        this.server = server;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            BungeeUtils.connect(player, server);
        }
    }

}
