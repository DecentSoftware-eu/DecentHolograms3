package eu.decent.holograms.actions.impl;

import eu.decent.holograms.actions.DefaultAction;
import eu.decent.holograms.actions.ActionType;
import eu.decent.holograms.api.DecentHologramsAPI;
import eu.decent.holograms.api.nms.NMSAdapter;
import eu.decent.holograms.api.profile.Profile;
import eu.decent.holograms.api.utils.Common;
import eu.decent.holograms.hooks.PAPI;
import eu.decent.holograms.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StringAction extends DefaultAction {

    private final ActionType type;
    private final String data;

    public StringAction(@NotNull ActionType type, @NotNull String data) {
        this.type = type;
        this.data = data;
    }

    public StringAction(long delay, double chance, @NotNull ActionType type, @NotNull String data) {
        super(delay, chance);
        this.type = type;
        this.data = data;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        Player player = profile.getPlayer();
        String processed = PAPI.setPlaceholders(player, data);
        switch (type) {
            case CHAT:
                player.chat(Common.colorize(processed));
                break;
            case COMMAND:
                Bukkit.dispatchCommand(player, processed);
                break;
            case CONSOLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), processed);
                break;
            case CONNECT:
                BungeeUtils.connect(player, processed);
                break;
            case ACTION_BAR:
                NMSAdapter nmsAdapter = DecentHologramsAPI.getInstance().getNMSProvider().getAdapter();
                nmsAdapter.sendPacket(player, nmsAdapter.packetActionbarMessage(processed));
                break;
            default: break;
        }
    }

}
