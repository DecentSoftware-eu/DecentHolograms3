package eu.decent.holograms.actions.impl;

import eu.decent.holograms.actions.DefaultAction;
import eu.decent.holograms.api.DecentHologramsAPI;
import eu.decent.holograms.api.nms.NMSAdapter;
import eu.decent.holograms.api.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TitleAction extends DefaultAction {

    private final String title;
    private final String subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public TitleAction(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public TitleAction(long delay, double chance, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        super(delay, chance);
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        NMSAdapter nmsAdapter = DecentHologramsAPI.getInstance().getNMSProvider().getAdapter();
        nmsAdapter.sendPacket(player, nmsAdapter.packetClearTitle());
        if (title != null) {
            nmsAdapter.sendPacket(player, nmsAdapter.packetTitleMessage(title));
        }
        if (subtitle != null) {
            nmsAdapter.sendPacket(player, nmsAdapter.packetSubtitleMessage(subtitle));
        }
        nmsAdapter.sendPacket(player, nmsAdapter.packetTimes(Math.max(fadeIn, 1), Math.max(stay, 1), Math.max(fadeOut, 1)));
    }

}
