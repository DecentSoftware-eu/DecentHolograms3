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
import eu.decentsoftware.holograms.commands.RootCommand;
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
import eu.decentsoftware.holograms.conditions.serialization.ClickConditionHolderSerializer;
import eu.decentsoftware.holograms.conditions.serialization.ConditionHolderSerializer;
import eu.decentsoftware.holograms.editor.Editor;
import eu.decentsoftware.holograms.hologram.DefaultHologramRegistry;
import eu.decentsoftware.holograms.hologram.line.content.ContentParserManager;
import eu.decentsoftware.holograms.hologram.serialization.LocationSerializer;
import eu.decentsoftware.holograms.hooks.PAPI;
import eu.decentsoftware.holograms.listener.PlayerListener;
import eu.decentsoftware.holograms.nms.NMSManager;
import eu.decentsoftware.holograms.nms.PacketListener;
import eu.decentsoftware.holograms.nms.utils.Version;
import eu.decentsoftware.holograms.profile.ProfileRegistry;
import eu.decentsoftware.holograms.replacements.ReplacementRegistry;
import eu.decentsoftware.holograms.server.ServerRegistry;
import eu.decentsoftware.holograms.ticker.Ticker;
import eu.decentsoftware.holograms.utils.BungeeUtils;
import eu.decentsoftware.holograms.utils.CommandUtil;
import eu.decentsoftware.holograms.utils.UpdateChecker;
import eu.decentsoftware.holograms.utils.watcher.FileWatcher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * "Introducing a powerful hologram plugin that offers a wide range
 * of features and customization options, all while maintaining
 * a lightweight design for optimal performance."
 *
 * @author d0by
 */
@Getter
public final class DecentHolograms extends JavaPlugin {

    @Getter
    private static DecentHolograms instance;
    private Gson gson;
    private Ticker ticker;
    private ProfileRegistry profileRegistry;
    private ServerRegistry serverRegistry;
    private ReplacementRegistry replacementRegistry;
    private AnimationRegistry animationRegistry;
    private ContentParserManager contentParserManager;
    private DefaultHologramRegistry hologramRegistry;
    private NMSManager nMSManager;
    private Editor editor;
    private AddonLoader addonLoader;

    private BootMessenger bootMessenger;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean enabled = false;

    public DecentHolograms() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // -- Attempt to initialize the NMS adapter
        try {
            nMSManager = new NMSManager(this);
        } catch (IllegalStateException e) {
            getLogger().severe("*** Your version (" + Version.CURRENT + ") is not supported!");
            getLogger().severe("*** Disabling...");
            getPluginLoader().disablePlugin(this);
            return;
        }

        bootMessenger = new BootMessenger(this);

        Config.reload();
        Lang.reload();

        // -- Initialize Custom Gson Instance
        setupGson();

        // -- Initialize Managers
        ticker = new Ticker();
        profileRegistry = new ProfileRegistry(this);
        serverRegistry = new ServerRegistry(this);
        replacementRegistry = new ReplacementRegistry(this);
        animationRegistry = new AnimationRegistry(this);
        contentParserManager = new ContentParserManager(this);
        hologramRegistry = new DefaultHologramRegistry(this);
        editor = new Editor(this);

        // -- Register DecentHologramsAPI
        DecentHologramsAPIProvider.setInstance(new DecentHologramsAPIImpl());

        // -- Initialize Utils
        BungeeUtils.init(this);

        // -- Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new PacketListener(this), this);

        // -- Commands
        CommandUtil.register(this, new RootCommand(this));

        // -- FileWatcher
        FileWatcher.assignFolder("addons");

        // -- Setup update checker if enabled
        setupUpdateChecker();

        if (PAPI.isAvailable()) {
            bootMessenger.log("Using PlaceholderAPI for placeholder support!");
        }
        bootMessenger.sendAndFinish();

        // -- Addons
        addonLoader = new AddonLoader("addons");
        addonLoader.loadAllAddons();

        enabled = true;
    }

    @Override
    public void onDisable() {
        if (enabled) {
            editor.shutdown();
            ticker.shutdown();
            nMSManager.shutdown();
            hologramRegistry.shutdown();
            animationRegistry.shutdown();
            replacementRegistry.shutdown();
            serverRegistry.shutdown();
            profileRegistry.shutdown();
            addonLoader.unloadAllAddons();
        }

        BungeeUtils.shutdown();
        HandlerList.unregisterAll(this);
        FileWatcher.close();
    }

    /**
     * Reload the plugins configuration and all holograms.
     */
    public void reload() {
        Config.reload();
        Lang.reload();

        hologramRegistry.reload();
        replacementRegistry.reload();
        animationRegistry.reload();
        serverRegistry.reload();
        profileRegistry.reload();
        editor.reload();
        addonLoader.reload();

        if (PAPI.isAvailable()) {
            bootMessenger.log("Using PlaceholderAPI for placeholder support!");
        }
        bootMessenger.sendAndFinish();

        // Call the reload event
        Bukkit.getPluginManager().callEvent(new DecentHologramsReloadEvent());
    }

    /**
     * Set up the update checker and check for updates.
     */
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
                    bootMessenger.log(Lang.formatString(Lang.UPDATE_MESSAGE));
                }
            });
        }
    }

    private void setupGson() {
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .registerTypeAdapter(ActionHolder.class, new ActionHolderSerializer())
                .registerTypeAdapter(ClickActionHolder.class, new ClickActionHolderSerializer())
                .registerTypeAdapter(ConditionHolder.class, new ConditionHolderSerializer())
                .registerTypeAdapter(ClickConditionHolder.class, new ClickConditionHolderSerializer())
                .create();
    }

}
