package eu.decentsoftware.holograms;

import eu.decentsoftware.holograms.actions.DefaultActionParserManager;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.ActionParserManager;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
import eu.decentsoftware.holograms.api.component.line.content.ContentParserManager;
import eu.decentsoftware.holograms.api.nms.NMSProvider;
import eu.decentsoftware.holograms.api.profile.ProfileRegistry;
import eu.decentsoftware.holograms.api.replacements.ReplacementRegistry;
import eu.decentsoftware.holograms.api.server.ServerRegistry;
import eu.decentsoftware.holograms.api.ticker.Ticker;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.components.hologram.DefaultHologramRegistry;
import eu.decentsoftware.holograms.components.line.content.DefaultContentParserManager;
import eu.decentsoftware.holograms.nms.NMSProviderImpl;
import eu.decentsoftware.holograms.profile.DefaultProfileRegistry;
import eu.decentsoftware.holograms.listener.PlayerListener;
import eu.decentsoftware.holograms.replacements.DefaultReplacementRegistry;
import eu.decentsoftware.holograms.server.DefaultServerRegistry;
import eu.decentsoftware.holograms.ticker.DefaultTicker;
import eu.decentsoftware.holograms.utils.BungeeUtils;
import eu.decentsoftware.holograms.utils.UpdateChecker;
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
    private Ticker ticker;
    private ProfileRegistry profileRegistry;
    private ServerRegistry serverRegistry;
    private ReplacementRegistry replacementRegistry;
    private ContentParserManager contentParserManager;
    private ActionParserManager actionParserManager;
    private HologramRegistry hologramRegistry;

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

        this.ticker = new DefaultTicker();
        this.profileRegistry = new DefaultProfileRegistry();
        this.serverRegistry = new DefaultServerRegistry();
        this.replacementRegistry = new DefaultReplacementRegistry();
        this.contentParserManager = new DefaultContentParserManager();
        this.actionParserManager = new DefaultActionParserManager();
        this.hologramRegistry = new DefaultHologramRegistry();

        BungeeUtils.init();

        // -- Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), this);

        // -- Setup update checker if enabled
        setupUpdateChecker();
    }

    @Override
    public void onDisable() {
        this.nmsProvider.shutdown();
        this.hologramRegistry.shutdown();
        this.replacementRegistry.shutdown();
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

    /**
     * Set up the update checker and check for updates.
     */
    private void setupUpdateChecker() {
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
                    Lang.sendUpdateMessage(Bukkit.getConsoleSender());
                }
            });
        }
    }

}
