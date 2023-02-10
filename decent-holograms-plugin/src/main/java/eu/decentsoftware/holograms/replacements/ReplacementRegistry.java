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

package eu.decentsoftware.holograms.replacements;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.DecentHologramsPlugin;
import eu.decentsoftware.holograms.utils.config.FileConfig;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.server.Server;
import eu.decentsoftware.holograms.utils.DatetimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents the replacement registry. It is responsible for managing
 * all replacements of internal placeholders.
 * <p>
 * There are three types of replacements:
 * <ul>
 *     <li>Default Replacements: Replaced by the plugin.</li>
 *     <li>Normal Replacements: Replaced with a configurable value.</li>
 * </ul>
 *
 * @author d0by
 * @since 1.0.0
 */
public class ReplacementRegistry {

    private static final DecentHologramsPlugin PLUGIN = DecentHologramsPlugin.getInstance();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\S+(:\\S+)?)}");
    private final Map<String, Replacement> defaultReplacementMap;
    private final Map<String, Replacement> normalReplacementMap;

    /**
     * Create a new instance of {@link ReplacementRegistry}.
     */
    public ReplacementRegistry() {
        this.defaultReplacementMap = new ConcurrentHashMap<>();
        this.normalReplacementMap = new ConcurrentHashMap<>();
        this.reload();
    }

    /**
     * Reload all replacements from the config.
     */
    public void reload() {
        this.defaultReplacementMap.clear();
        this.normalReplacementMap.clear();
        this.registerDefaultReplacements();

        // Reload custom replacements
        String path = "replacements";
        FileConfig config = Config.getConfig();
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section != null) {
            section.getKeys(false).forEach((key) -> {
                String value = config.getString(path + "." + key);
                Replacement replacement = new Replacement((player, argument) -> Optional.ofNullable(value));
                this.normalReplacementMap.put(key, replacement);
            });
        }
    }

    /**
     * Shutdown this manager, removing all registered replacements.
     */
    public void shutdown() {
        this.defaultReplacementMap.clear();
        this.normalReplacementMap.clear();
    }

    /**
     * Replace all registered placeholders, that the given String contains.
     *
     * @param string  The string.
     * @param profile The profile to replace the placeholders for.
     * @return The resulting String.
     */
    public String replace(@NotNull String string, @Nullable Profile profile) {
        // Replace default replacements
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(string);
        while (matcher.find()) {
            String key = matcher.group();
            Optional<String> replacement = getDefaultReplacement(profile, matcher.group(1).trim());
            string = string.replace(key, replacement.orElse(key));
            matcher = PLACEHOLDER_PATTERN.matcher(string);
        }

        // Replace normal replacements
        for (String key : normalReplacementMap.keySet()) {
            if (string.contains(key)) {
                Optional<String> replacement = normalReplacementMap.get(key).getReplacement(profile, null);
                string = string.replace(key, replacement.orElse(key));
            }
        }
        return string;
    }

    /**
     * Get the replacement for the given placeholder string.
     *
     * @param profile           The profile to get the replacement for.
     * @param placeholderString The placeholder string.
     * @return The replacement for the placeholder string or null
     * if the given placeholder string cannot be replaced.
     */
    private Optional<String> getDefaultReplacement(@Nullable Profile profile, @NotNull String placeholderString) {
        String[] spl = placeholderString.split(":", 2);
        Replacement replacement = defaultReplacementMap.get(spl[0]);
        if (replacement != null) {
            return replacement.getReplacement(profile, spl.length > 1 ? spl[1] : null);
        }
        return Optional.empty();
    }

    /**
     * Register the default built-in replacements.
     */
    private void registerDefaultReplacements() {
        // -- Player placeholders

        this.defaultReplacementMap.put("player", new Replacement((profile, argument) -> {
            if (profile != null) {
                return Optional.of(profile.getName());
            }
            return Optional.empty();
        }));
        this.defaultReplacementMap.put("displayname", new Replacement((profile, argument) -> {
            Player player;
            if (profile == null || (player = profile.getPlayer()) == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(player.getDisplayName());
        }));
        this.defaultReplacementMap.put("uuid", new Replacement((profile, argument) -> {
            Player player;
            if (profile == null || (player = profile.getPlayer()) == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(player.getUniqueId().toString());
        }));

        // -- Global placeholders

        this.defaultReplacementMap.put("time", new Replacement(
                (profile, argument) -> Optional.of(DatetimeUtils.getTimeFormatted()))
        );
        this.defaultReplacementMap.put("date", new Replacement(
                (profile, argument) -> Optional.of(DatetimeUtils.getDateFormatted()))
        );

        // -- World placeholders

        this.defaultReplacementMap.put("world", new Replacement(
                (profile, argument) -> {
                    int online;
                    if (argument != null) {
                        // -- Given worlds
                        online = ReplacementCommons.getFromWorldOrWorldsInt(
                                argument, (world) -> world.getPlayers().size()
                        );
                    } else if (profile != null) {
                        // -- Player world
                        Player player = profile.getPlayer();
                        if (player != null) {
                            online = profile.getPlayer().getWorld().getPlayers().size();
                        } else {
                            online = -1;
                        }
                    } else {
                        online = -1;
                    }

                    if (online >= 0) {
                        return Optional.of(String.valueOf(online));
                    } else {
                        return Optional.empty();
                    }
                })
        );

        // -- Server & Pinger placeholders

        this.defaultReplacementMap.put("online", new Replacement(
                (profile, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online;
                        Player player;
                        if (profile != null && (player = profile.getPlayer()) != null) {
                            online = ReplacementCommons.getFromServerOrServersInt(
                                    player, argument, (server) -> server.getData().getPlayers().getOnline()
                            );
                        } else {
                            online = -1;
                        }
                        if (online >= 0) {
                            return Optional.of(online + "");
                        }
                    } else {
                        // -- This server
                        return Optional.of(Bukkit.getOnlinePlayers().size() + "");
                    }
                    return Optional.empty();
                })
        );
        this.defaultReplacementMap.put("max_players", new Replacement(
                (profile, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online;
                        Player player;
                        if (profile != null && (player = profile.getPlayer()) != null) {
                            online = ReplacementCommons.getFromServerOrServersInt(
                                    player, argument, (server) -> server.getData().getPlayers().getMax()
                            );
                        } else {
                            online = -1;
                        }
                        if (online >= 0) {
                            return Optional.of(online + "");
                        }
                    } else {
                        // -- This server
                        return Optional.of(Bukkit.getServer().getMaxPlayers() + "");
                    }
                    return Optional.empty();
                })
        );
        this.defaultReplacementMap.put("motd", new Replacement(
                (profile, argument) -> {
                    String motd = null;
                    if (argument != null) {
                        // -- Pinged server
                        Server server = PLUGIN.getServerRegistry().getServer(argument);
                        if (server != null && server.isOnline()) {
                            motd = server.getData().getDescription();
                        }
                    } else {
                        // -- This server
                        motd = Bukkit.getServer().getMotd();
                    }
                    if (Config.PINGER_TRIM_MOTD && motd != null) {
                        return Optional.of(motd.trim());
                    }
                    return Optional.ofNullable(motd);
                })
        );
        this.defaultReplacementMap.put("status", new Replacement(
                (profile, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        Server server = PLUGIN.getServerRegistry().getServer(argument);
                        if (server != null && server.isOnline()) {
                            if (server.isFull()) {
                                return Optional.ofNullable(Config.PINGER_STATUS_FULL);
                            }
                            return Optional.ofNullable(Config.PINGER_STATUS_ONLINE);
                        }
                    } else {
                        // -- This server
                        return Optional.ofNullable(Config.PINGER_STATUS_ONLINE);
                    }
                    return Optional.ofNullable(Config.PINGER_STATUS_OFFLINE);
                })
        );
    }

}
