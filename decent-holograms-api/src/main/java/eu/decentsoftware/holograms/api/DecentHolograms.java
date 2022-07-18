package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.actions.ActionParserManager;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
import eu.decentsoftware.holograms.api.component.line.content.ContentParserManager;
import eu.decentsoftware.holograms.api.nms.NMSProvider;
import eu.decentsoftware.holograms.api.profile.ProfileRegistry;
import eu.decentsoftware.holograms.api.replacements.ReplacementRegistry;
import eu.decentsoftware.holograms.api.server.ServerRegistry;
import eu.decentsoftware.holograms.api.ticker.Ticker;
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

    /**
     * This is used to get free entity IDs. It starts at a million
     * so that it doesn't conflict with normal entities.
     */
    private static int ENTITY_ID = 1_000_000;
    // The 'config.yml' file
    private File configFile;
    // The '/holograms' file
    private File hologramsFolder;

    /**
     * Get a free entity ID.
     *
     * @return The new entity ID.
     */
    public static int getFreeEntityId() {
        return ENTITY_ID++;
    }

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

    /**
     * Get the "/holograms" folder.
     *
     * @return The folder.
     */
    public File getHologramFolder() {
        if (hologramsFolder == null) {
            hologramsFolder = new File(getDataFolder(), "holograms");
        }
        return hologramsFolder;
    }

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
     * Get the ticker.
     *
     * @return The ticker.
     * @see Ticker
     */
    public abstract Ticker getTicker();

    /**
     * Get the profile registry.
     *
     * @return The profile registry.
     * @see ProfileRegistry
     */
    public abstract ProfileRegistry getProfileRegistry();

    /**
     * Get the server registry.
     *
     * @return The server registry.
     * @see ServerRegistry
     */
    public abstract ServerRegistry getServerRegistry();

    /**
     * Get the replacement registry.
     *
     * @return The replacement registry.
     * @see ReplacementRegistry
     */
    public abstract ReplacementRegistry getReplacementRegistry();

    /**
     * Get the content parser manager.
     *
     * @return The content parser manager.
     * @see ContentParserManager
     */
    public abstract ContentParserManager getContentParserManager();

    /**
     * Get the action parser manager.
     *
     * @return The action parser manager.
     * @see ActionParserManager
     */
    public abstract ActionParserManager getActionParserManager();

    /**
     * Get the hologram registry.
     *
     * @return The hologram registry.
     * @see HologramRegistry
     */
    public abstract HologramRegistry getHologramRegistry();

}
