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

import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.exceptions.InvalidSyntaxException;
import cloud.commandframework.exceptions.NoPermissionException;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.SimpleCommandMeta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.decentsoftware.holograms.actions.ActionHolder;
import eu.decentsoftware.holograms.actions.serialization.ActionHolderSerializer;
import eu.decentsoftware.holograms.addons.AddonLoader;
import eu.decentsoftware.holograms.animations.AnimationRegistry;
import eu.decentsoftware.holograms.api.DecentHologramsAPIImpl;
import eu.decentsoftware.holograms.api.event.DecentHologramsReloadEvent;
import eu.decentsoftware.holograms.api.internal.DecentHologramsAPIProvider;
import eu.decentsoftware.holograms.commands.DecentHologramsCommand;
import eu.decentsoftware.holograms.conditions.ConditionHolder;
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
import eu.decentsoftware.holograms.utils.UpdateChecker;
import eu.decentsoftware.holograms.utils.watcher.FileWatcher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.function.Function;

/**
 * "Introducing a powerful hologram plugin that offers a wide range
 * of features and customization options, all while maintaining
 * a lightweight design for optimal performance."
 *
 * @author d0by
 */
@Getter
public final class DecentHolograms extends JavaPlugin {

    private static DecentHolograms instance;
    private Gson gson;
    private Ticker ticker;
    private ProfileRegistry profileRegistry;
    private ServerRegistry serverRegistry;
    private ReplacementRegistry replacementRegistry;
    private AnimationRegistry animationRegistry;
    private ContentParserManager contentParserManager;
    private DefaultHologramRegistry hologramRegistry;
    @Getter(AccessLevel.NONE)
    private NMSManager nmsManager;
    private Editor editor;
    private AddonLoader addonLoader;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private boolean enabled = false;

    public DecentHolograms() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Config.reload();
        Lang.reload();

        // -- Attempt to initialize the NMS adapter.
        try {
            this.nmsManager = new NMSManager();
        } catch (IllegalStateException e) {
            getLogger().severe("*** Your version (" + Version.CURRENT + ") is not supported!");
            getLogger().severe("*** Disabling...");
            getPluginLoader().disablePlugin(this);
            return;
        }

        // -- Initialize Custom Gson Instance
        setupGson();

        // -- Initialize Managers
        this.ticker = new Ticker();
        this.profileRegistry = new ProfileRegistry();
        this.serverRegistry = new ServerRegistry();
        this.replacementRegistry = new ReplacementRegistry();
        this.animationRegistry = new AnimationRegistry();
        this.contentParserManager = new ContentParserManager();
        this.hologramRegistry = new DefaultHologramRegistry();
        this.editor = new Editor();

        // -- Register DecentHologramsAPI
        DecentHologramsAPIProvider.setInstance(new DecentHologramsAPIImpl());

        // -- Initialize Utils
        BungeeUtils.init();

        // -- Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new PacketListener(), this);

        // -- Commands
        setupCommands();

        // -- FileWatcher
        FileWatcher.assignFolder("addons");

        // -- Setup update checker if enabled
        setupUpdateChecker();

        if (PAPI.isAvailable()) {
            BootMessenger.log("Using PlaceholderAPI for placeholder support!");
        }
        BootMessenger.sendAndFinish();

        // -- Addons
        addonLoader = new AddonLoader("addons");
        addonLoader.loadAllAddons();

        this.enabled = true;
    }

    @Override
    public void onDisable() {
        if (this.enabled) {
            this.editor.shutdown();
            this.ticker.shutdown();
            this.nmsManager.shutdown();
            this.hologramRegistry.shutdown();
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

    /**
     * Reload the plugins configuration and all holograms.
     */
    public void reload() {
        Config.reload();
        Lang.reload();

        this.hologramRegistry.reload();
        this.replacementRegistry.reload();
        this.animationRegistry.reload();
        this.serverRegistry.reload();
        this.profileRegistry.reload();
        this.editor.reload();
        this.addonLoader.reload();

        if (PAPI.isAvailable()) {
            BootMessenger.log("Using PlaceholderAPI for placeholder support!");
        }
        BootMessenger.sendAndFinish();

        // Call the reload event
        Bukkit.getPluginManager().callEvent(new DecentHologramsReloadEvent());
    }

    /**
     * Set up the update checker and check for updates.
     */
    private void setupUpdateChecker() {
        if (Config.CHECK_FOR_UPDATES) {
            new UpdateChecker(96927).check((s) -> {
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
                    BootMessenger.log(Lang.formatString(Lang.UPDATE_MESSAGE));
                }
            });
        }
    }

    private void setupCommands() {
        try {
            // -- Setup command manager
            final BukkitCommandManager<CommandSender> manager = new BukkitCommandManager<>(
                    this,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );

            // -- Setup command parser
            final AnnotationParser<CommandSender> annotationParser = new AnnotationParser<>(
                    manager,
                    CommandSender.class,
                    parameters -> SimpleCommandMeta.empty()
            );

            // -- Register exception handlers
            manager.registerExceptionHandler(
                    NoPermissionException.class,
                    (sender, e) -> Lang.confTell(sender, "no_permission")
            );
            manager.registerExceptionHandler(
                    InvalidSyntaxException.class,
                    (sender, e) -> Lang.confTell(sender, "plugin.help")
            );

            // -- Register commands
            annotationParser.parse(new DecentHologramsCommand());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupGson() {
        this.gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .registerTypeAdapter(ActionHolder.class, new ActionHolderSerializer())
                .registerTypeAdapter(ConditionHolder.class, new ConditionHolderSerializer())
                .create();
    }

    @Contract(pure = true)
    public NMSManager getNMSManager() {
        return nmsManager;
    }

    @Contract(pure = true)
    public static DecentHolograms getInstance() {
        return instance;
    }

}
