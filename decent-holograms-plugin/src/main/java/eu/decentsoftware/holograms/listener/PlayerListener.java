package eu.decentsoftware.holograms.listener;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.Lang;
import eu.decentsoftware.holograms.api.DecentHolograms;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This listener handles some events related to the player profiles.
 */
public class PlayerListener implements Listener {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PLUGIN.getProfileRegistry().registerProfile(player.getName());
//        PLUGIN.getNMSProvider().getPacketListener().hook(player);

        // -- Notify the player about a new version available
        if (Config.isUpdateAvailable() && player.hasPermission(Config.ADMIN_PERM)) {
            Lang.sendUpdateMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PLUGIN.getProfileRegistry().removeProfile(player.getName());
//        PLUGIN.getNMSProvider().getPacketListener().unhook(player);
    }

}
