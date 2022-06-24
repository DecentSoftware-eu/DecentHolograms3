package eu.decentsoftware.holograms.api.hooks;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class providing methods to interact with the HeadDatabaseAPI plugin.
 */
public final class HDB {

    public static final HeadDatabaseAPI API;

    static {
        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            API = new HeadDatabaseAPI();
        } else {
            API = null;
        }
    }

    /**
     * Check whether this hook is available for use.
     *
     * @return The requested boolean.
     */
    public static boolean isAvailable() {
        return API != null;
    }

    /**
     * Get the head item with the given id from HDB.
     *
     * @param id The id.
     * @return The head item.
     */
    @Nullable
    public static ItemStack getHeadItemStackById(@NotNull String id) {
        if (isAvailable()) {
            return API.getItemHead(id);
        }
        return null;
    }

}
