package eu.decent.holograms;

import eu.decent.holograms.api.DecentHolograms;
import eu.decent.holograms.api.DecentHologramsAPI;
import eu.decent.holograms.api.nms.NMSProvider;
import eu.decent.holograms.api.placeholders.PlaceholderRegistry;
import eu.decent.holograms.api.profile.ProfileRegistry;
import eu.decent.holograms.api.server.ServerRegistry;
import eu.decent.holograms.api.utils.reflect.Version;
import eu.decent.holograms.nms.NMSProviderImpl;
import eu.decent.holograms.placeholders.DefaultPlaceholderRegistry;
import eu.decent.holograms.profile.DefaultProfileRegistry;
import eu.decent.holograms.profile.ProfileListener;
import eu.decent.holograms.server.DefaultServerRegistry;
import eu.decent.holograms.utils.BungeeUtils;
import eu.decent.holograms.utils.UpdateChecker;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;

/**
 * A lightweight yet very powerful hologram plugin.
 *
 * @author d0by
 */
@Getter
public final class DecentHologramsPlugin extends DecentHolograms {

    @Getter(AccessLevel.NONE)
    private NMSProvider nmsProvider;
    private ProfileRegistry profileRegistry;
    private ServerRegistry serverRegistry;
    private PlaceholderRegistry placeholderRegistry;

    /**
     * Default constructor.
     */
    @SuppressWarnings("deprecation")
    public DecentHologramsPlugin() {
        // -- Register the API
        DecentHologramsAPI.setInstance(this);
    }

    @Override
    public void onEnable() {
        Config.reload();

        // -- Attempt to initialize the NMS adapter.
        try {
            this.nmsProvider = new NMSProviderImpl();
        } catch (IllegalStateException e) {
            getLogger().severe("Your version (" + Version.CURRENT.name() + ") is not supported!");
            getLogger().severe("Disabling...");
            getPluginLoader().disablePlugin(this);
            return;
        }

        this.profileRegistry = new DefaultProfileRegistry();
        this.serverRegistry = new DefaultServerRegistry();
        this.placeholderRegistry = new DefaultPlaceholderRegistry();

        BungeeUtils.init();

        // -- Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ProfileListener(), this);

        // -- Setup update checker if enabled
        if (Config.CHECK_FOR_UPDATES) {
            new UpdateChecker(96927).check((s) -> {
                // Split the version string into 3 parts: major, minor, patch
                String[] split = s.split("\\.");
                int[] latest = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
                int[] current = Arrays.stream(getDescription().getVersion().split("\\.")).mapToInt(Integer::parseInt).toArray();
                // Compare the versions
                Config.setUpdateAvailable(
                        (latest[0] > current[0]) ||
                        (latest[0] == current[0] && latest[1] > current[1]) ||
                        (latest[0] == current[0] && latest[1] == current[1] && latest[2] > current[2])
                );
                // Notify if an update is available
                if (Config.isUpdateAvailable()) {
                    Config.sendUpdateMessage(Bukkit.getConsoleSender());
                }
            });
        }
    }

    @Override
    public void onDisable() {
        this.placeholderRegistry.shutdown();
        this.serverRegistry.shutdown();
        this.profileRegistry.shutdown();

        BungeeUtils.shutdown();
        HandlerList.unregisterAll(this);
    }

    @Override
    public void reload() {
        Config.reload();

        this.serverRegistry.reload();
        this.profileRegistry.reload();
    }

    @Override
    public NMSProvider getNMSProvider() {
        return nmsProvider;
    }

}
