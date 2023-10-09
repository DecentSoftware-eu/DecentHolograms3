/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.actions.ClickActionHolder;
import eu.decentsoftware.holograms.actions.serialization.ActionHolderSerializer;
import eu.decentsoftware.holograms.actions.serialization.ClickActionHolderSerializer;
import eu.decentsoftware.holograms.addons.AddonLoader;
import eu.decentsoftware.holograms.animations.AnimationRegistry;
import eu.decentsoftware.holograms.api.DecentHologramsAPIImpl;
import eu.decentsoftware.holograms.api.event.DecentHologramsReloadEvent;
import eu.decentsoftware.holograms.api.internal.DecentHologramsAPIProvider;
import eu.decentsoftware.holograms.api.util.DecentLocation;
import eu.decentsoftware.holograms.commands.RootCommand;
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import eu.decentsoftware.holograms.conditions.serialization.ClickConditionHolderSerializer;
import eu.decentsoftware.holograms.conditions.serialization.ConditionHolderSerializer;
import eu.decentsoftware.holograms.content.parser.ContentParserManager;
import eu.decentsoftware.holograms.editor.Editor;
import eu.decentsoftware.holograms.hooks.PAPI;
import eu.decentsoftware.holograms.internal.PluginHologramManager;
import eu.decentsoftware.holograms.nms.NMSManager;
import eu.decentsoftware.holograms.nms.utils.Version;
import eu.decentsoftware.holograms.profile.ProfileRegistry;
import eu.decentsoftware.holograms.replacements.ReplacementRegistry;
import eu.decentsoftware.holograms.serialization.DecentHologramsSerializer;
import eu.decentsoftware.holograms.serialization.DecentLocationSerializer;
import eu.decentsoftware.holograms.serialization.LocationSerializer;
import eu.decentsoftware.holograms.server.ServerRegistry;
import eu.decentsoftware.holograms.ticker.Ticker;
import eu.decentsoftware.holograms.utils.BungeeUtils;
import eu.decentsoftware.holograms.utils.CommandUtil;
import eu.decentsoftware.holograms.utils.UpdateChecker;
import eu.decentsoftware.holograms.utils.watcher.FileWatcher;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;

/**
 * "Introducing a powerful hologram plugin that offers a wide range
 * of features and customization options, all while maintaining
 * a lightweight design for optimal performance."
 *
 * @author d0by
 */
public final class DecentHolograms extends JavaPlugin {

    private static DecentHolograms instance;
    private Gson gson;
    private Ticker ticker;
    private ProfileRegistry profileRegistry;
    private ServerRegistry serverRegistry;
    private ReplacementRegistry replacementRegistry;
    private AnimationRegistry animationRegistry;
    private ContentParserManager contentParserManager;
    private PluginHologramManager hologramManager;
    private NMSManager nMSManager;
    private Editor editor;
    private AddonLoader addonLoader;
    private BootMessenger bootMessenger;
    private boolean enabled = false;

    public DecentHolograms() {
        DecentHolograms.instance = this;
    }

    @Override
    public void onEnable() {
        // -- Attempt to initialize the NMS adapter
        try {
            this.nMSManager = new NMSManager(this);
        } catch (IllegalStateException e) {
            getLogger().severe("*** Your version (" + Version.CURRENT + ") is not supported!");
            getLogger().severe("*** Disabling...");
            getPluginLoader().disablePlugin(this);
            return;
        }

        this.bootMessenger = new BootMessenger(this);

        Config.reload();
        Lang.reload();

        // -- Initialize Managers
        this.ticker = new Ticker();
        this.profileRegistry = new ProfileRegistry(this);
        this.serverRegistry = new ServerRegistry(this);
        this.replacementRegistry = new ReplacementRegistry(this);
        this.animationRegistry = new AnimationRegistry(this);
        this.contentParserManager = new ContentParserManager(this);
        this.hologramManager = new PluginHologramManager(this);
        this.editor = new Editor(this);

        // -- Register DecentHologramsAPI
        DecentHologramsAPIProvider.setInstance(new DecentHologramsAPIImpl(this));

        // -- Initialize Utils
        BungeeUtils.init(this);

        // -- Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new UpdateNotificationListener(), this);

        // -- Commands
        CommandUtil.register(this, new RootCommand(this));

        // -- FileWatcher
        FileWatcher.assignFolder("addons");

        // -- Setup update checker if enabled
        setupUpdateChecker();

        if (PAPI.isAvailable()) {
            logOrBoot("Using PlaceholderAPI for placeholder support!");
        }
        this.bootMessenger.sendAndFinish();

        // -- Addons
        this.addonLoader = new AddonLoader("addons");
        this.addonLoader.loadAllAddons();

        this.enabled = true;
    }

    @Override
    public void onDisable() {
        if (this.enabled) {
            this.editor.shutdown();
            this.ticker.shutdown();
            this.nMSManager.shutdown();
            this.hologramManager.shutdown();
            this.animationRegistry.shutdown();
            this.replacementRegistry.shutdown();
            this.serverRegistry.shutdown();
            this.profileRegistry.shutdown();
            this.addonLoader.unloadAllAddons();
        }

        BungeeUtils.shutdown();
        HandlerList.unregisterAll(this);
        FileWatcher.close();
    }

    public void reload() {
        Config.reload();
        Lang.reload();

        this.replacementRegistry.reload();
        this.animationRegistry.reload();
        this.serverRegistry.reload();
        this.hologramManager.reload();
        this.profileRegistry.reload();
        this.editor.reload();
        this.addonLoader.reload();

        if (PAPI.isAvailable()) {
            logOrBoot("Using PlaceholderAPI for placeholder support!");
        }
        this.bootMessenger.sendAndFinish();

        // Call the reload event
        Bukkit.getPluginManager().callEvent(new DecentHologramsReloadEvent());
    }

    public void log(@NonNull String message, Object... args) {
        getLogger().info(String.format(message, args));
    }

    public void warn(@NonNull String message, Object... args) {
        getLogger().warning(String.format(message, args));
    }

    public void error(@NonNull String message, Object... args) {
        getLogger().severe(String.format(message, args));
    }

    public void logOrBoot(@NonNull String message, Object... args) {
        if (this.bootMessenger != null) {
            this.bootMessenger.log(String.format(message, args));
        } else {
            log(message, args);
        }
    }

    public void warnOrBoot(@NonNull String message, Object... args) {
        if (this.bootMessenger != null) {
            this.bootMessenger.log("&e" + String.format(message, args));
        } else {
            warn(message, args);
        }
    }

    public void errorOrBoot(@NonNull String message, Object... args) {
        if (this.bootMessenger != null) {
            this.bootMessenger.log("&c" + String.format(message, args));
        } else {
            error(message, args);
        }
    }

    private void setupUpdateChecker() {
        if (Config.CHECK_FOR_UPDATES) {
            new UpdateChecker(this, 96927).check(s -> {
                // Split the version string into 3 parts: major, minor, patch
                String[] split = s.split("\\.", 3);
                int[] latest = Arrays.stream(split)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                int[] current = Arrays.stream(getDescription().getVersion().split("\\.", 3))
                        .mapToInt(Integer::parseInt)
                        .toArray();

                // Compare the versions
                Config.setUpdateAvailable(
                        (latest[0] > current[0]) || // Major
                                (latest[0] == current[0] && latest[1] > current[1]) || // Minor
                                (latest[0] == current[0] && latest[1] == current[1] && latest[2] > current[2]) // Patch
                );

                // Notify if an update is available
                if (Config.isUpdateAvailable()) {
                    Config.setUpdateVersion(s);
                    logOrBoot(Lang.formatString(Lang.UPDATE_MESSAGE));
                }
            });
        }
    }

    @NonNull
    public Gson getGson() {
        if (this.gson == null) {
            this.gson = new GsonBuilder()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .registerTypeAdapter(DecentHolograms.class, new DecentHologramsSerializer(this))
                    .registerTypeAdapter(Location.class, new LocationSerializer())
                    .registerTypeAdapter(DecentLocation.class, new DecentLocationSerializer())
                    .registerTypeAdapter(ActionHolder.class, new ActionHolderSerializer())
                    .registerTypeAdapter(ClickActionHolder.class, new ClickActionHolderSerializer())
                    .registerTypeAdapter(ConditionHolder.class, new ConditionHolderSerializer())
                    .registerTypeAdapter(ClickConditionHolder.class, new ClickConditionHolderSerializer())
                    .create();
        }
        return this.gson;
    }

    @Contract(pure = true)
    public Ticker getTicker() {
        return this.ticker;
    }

    @Contract(pure = true)
    public ProfileRegistry getProfileRegistry() {
        return this.profileRegistry;
    }

    @Contract(pure = true)
    public ServerRegistry getServerRegistry() {
        return this.serverRegistry;
    }

    @Contract(pure = true)
    public ReplacementRegistry getReplacementRegistry() {
        return this.replacementRegistry;
    }

    @Contract(pure = true)
    public AnimationRegistry getAnimationRegistry() {
        return this.animationRegistry;
    }

    @Contract(pure = true)
    public ContentParserManager getContentParserManager() {
        return this.contentParserManager;
    }

    @Contract(pure = true)
    public PluginHologramManager getHologramManager() {
        return this.hologramManager;
    }

    @Contract(pure = true)
    public NMSManager getNMSManager() {
        return this.nMSManager;
    }

    @Contract(pure = true)
    public Editor getEditor() {
        return this.editor;
    }

    @Contract(pure = true)
    public static DecentHolograms getInstance() {
        return DecentHolograms.instance;
    }

}
