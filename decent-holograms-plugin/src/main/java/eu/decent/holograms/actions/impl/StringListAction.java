package eu.decent.holograms.actions.impl;

import eu.decent.holograms.actions.DefaultAction;
import eu.decent.holograms.actions.ActionType;
import eu.decent.holograms.api.DecentHologramsAPI;
import eu.decent.holograms.api.profile.Profile;
import eu.decent.holograms.api.utils.Common;
import eu.decent.holograms.api.hooks.PAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StringListAction extends DefaultAction {

    private final ActionType type;
    private final List<String> stringList;

    public StringListAction(ActionType type, List<String> stringList) {
        this.type = type;
        this.stringList = stringList;
    }

    public StringListAction(long delay, double chance, ActionType type, List<String> stringList) {
        super(delay, chance);
        this.type = type;
        this.stringList = stringList;
    }

    @Override
    public void execute(@NotNull Profile profile) {
        switch (type) {
            case MESSAGE:
                Player player = profile.getPlayer();
                for (String line : stringList) {
                    line = DecentHologramsAPI.getInstance().getPlaceholderRegistry().replacePlaceholders(player, line);
                    line = PAPI.setPlaceholders(player, line);
                    Common.tell(player, line);
                }
                break;
            case BROADCAST:
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    for (String line : stringList) {
                        line = DecentHologramsAPI.getInstance().getPlaceholderRegistry().replacePlaceholders(onlinePlayer, line);
                        line = PAPI.setPlaceholders(onlinePlayer, line);
                        Common.tell(onlinePlayer, line);
                    }
                }
                break;
            default:
                break;
        }
    }

}
