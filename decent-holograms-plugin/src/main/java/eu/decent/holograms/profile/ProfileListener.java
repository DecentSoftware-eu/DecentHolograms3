package eu.decent.holograms.profile;

import eu.decent.holograms.Config;
import eu.decent.holograms.api.DecentHolograms;
import eu.decent.holograms.api.DecentHologramsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This listener handles some events related to the player profiles.
 */
public class ProfileListener implements Listener {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PLUGIN.getProfileRegistry().register(player.getName());

        // -- Notify the player about a new version available
        if (player.hasPermission(Config.ADMIN_PERM)) {
            Config.sendUpdateMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PLUGIN.getProfileRegistry().remove(player.getName());
    }

}
