package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.nms.NMSProvider;
import eu.decentsoftware.holograms.api.placeholders.PlaceholderRegistry;
import eu.decentsoftware.holograms.api.profile.ProfileRegistry;
import eu.decentsoftware.holograms.api.server.ServerRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * This class represents the main class of the plugin. It is responsible for
 * enabling and disabling the plugin. It also contains getters for all the
 * managers.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class DecentHolograms extends JavaPlugin {

    // The 'config.yml' file
    private File configFile;

    /**
     * Reloads the plugin.
     */
    public abstract void reload();

    /**
     * Gets the NMS adapter provider.
     *
     * @return The NMS adapter provider.
     * @see NMSProvider
     */
    public abstract NMSProvider getNMSProvider();

    /**
     * Get the profile registry.
     *
     * @return The profile registry.
     */
    public abstract ProfileRegistry getProfileRegistry();

    /**
     * Get the server registry.
     *
     * @return The server registry.
     */
    public abstract ServerRegistry getServerRegistry();

    /**
     * Get the placeholder registry.
     *
     * @return The placeholder registry.
     */
    public abstract PlaceholderRegistry getPlaceholderRegistry();

    /**
     * Get the "config.yml" file.
     *
     * @return The file.
     */
    public File getConfigFile() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        return configFile;
    }

}
