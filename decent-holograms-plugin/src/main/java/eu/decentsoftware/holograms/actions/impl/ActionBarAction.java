package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.actions.DefaultAction;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionBarAction extends DefaultAction {

    private final @NotNull String message;

    public ActionBarAction(@NotNull String message) {
        this.message = message;
    }

    public ActionBarAction(long delay, double chance, @NotNull String message) {
        super(delay, chance);
        this.message = message;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        if (player != null) {
            NMSAdapter nmsAdapter = DecentHologramsAPI.getInstance().getNMSProvider().getAdapter();
            nmsAdapter.sendPacket(player, nmsAdapter.packetActionbarMessage(Lang.formatString(message, profile)));
        }
    }

}
