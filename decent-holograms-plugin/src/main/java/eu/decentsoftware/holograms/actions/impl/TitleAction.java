package eu.decentsoftware.holograms.actions.impl;

import eu.decentsoftware.holograms.actions.DefaultAction;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import eu.decentsoftware.holograms.api.profile.Profile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TitleAction extends DefaultAction {

    protected final String title;
    protected final String subtitle;
    protected final int fadeIn;
    protected final int stay;
    protected final int fadeOut;

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
        if (player == null) {
            return;
        }
        NMSAdapter nmsAdapter = DecentHolograms.getInstance().getNMSProvider().getAdapter();
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
