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
import cloud.commandframework.exceptions.NoPermissionException;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.SimpleCommandMeta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.component.hologram.HologramRegistry;
import eu.decentsoftware.holograms.commands.TestCommand;
import eu.decentsoftware.holograms.components.hologram.DefaultHologramRegistry;
import eu.decentsoftware.holograms.components.line.content.ContentParserManager;
import eu.decentsoftware.holograms.components.serialization.LocationSerializer;
import eu.decentsoftware.holograms.hooks.PAPI;
import eu.decentsoftware.holograms.listener.PlayerListener;
import eu.decentsoftware.holograms.nms.NMSManager;
import eu.decentsoftware.holograms.nms.PacketListenerImpl;
import eu.decentsoftware.holograms.nms.Version;
import eu.decentsoftware.holograms.profile.ProfileRegistry;
import eu.decentsoftware.holograms.replacements.ReplacementRegistry;
import eu.decentsoftware.holograms.server.ServerRegistry;
import eu.decentsoftware.holograms.ticker.Ticker;
import eu.decentsoftware.holograms.utils.BungeeUtils;
import eu.decentsoftware.holograms.utils.UpdateChecker;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
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
public final class DecentHologramsPlugin extends DecentHolograms {

    @Getter(AccessLevel.NONE)
    private NMSManager nmsManager;
    private Gson gson;
    private Ticker ticker;
    private ProfileRegistry profileRegistry;
    private ServerRegistry serverRegistry;
    private ReplacementRegistry replacementRegistry;
    private ContentParserManager contentParserManager;
    private HologramRegistry hologramRegistry;

    @Override
    public void onEnable() {
        Config.reload();
        Lang.reload();

        // -- Attempt to initialize the NMS adapter.
        try {
            this.nmsManager = new NMSManager();
            this.nmsManager.setPacketListener(new PacketListenerImpl());
        } catch (IllegalStateException e) {
            getLogger().severe("*** Your version (" + Version.CURRENT + ") is not supported!");
            getLogger().severe("*** Disabling...");
            getPluginLoader().disablePlugin(this);
            return;
        }

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .setPrettyPrinting()
                .create();
        this.ticker = new Ticker();
        this.profileRegistry = new ProfileRegistry();
        this.serverRegistry = new ServerRegistry();
        this.replacementRegistry = new ReplacementRegistry();
        this.contentParserManager = new ContentParserManager();
        this.hologramRegistry = new DefaultHologramRegistry();

        BungeeUtils.init();

        // -- Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), this);

        // -- Commands
        setupCommands();

        // -- Setup update checker if enabled
        setupUpdateChecker();

        if (PAPI.isAvailable()) {
            BootMessenger.log("Using PlaceholderAPI for placeholder support!");
        }
        BootMessenger.sendAndFinish();
    }

    @Override
    public void onDisable() {
        this.nmsManager.shutdown();
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
        Lang.reload();

        this.serverRegistry.reload();
        this.profileRegistry.reload();
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
            manager.registerExceptionHandler(NoPermissionException.class, (sender, e) -> sender.sendMessage(Lang.NO_PERM));

            // -- Register commands
            annotationParser.parse(new TestCommand());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Contract(pure = true)
    public NMSManager getNMSManager() {
        return nmsManager;
    }

    @Contract(pure = true)
    public static DecentHologramsPlugin getInstance() {
        return (DecentHologramsPlugin) instance;
    }

}
