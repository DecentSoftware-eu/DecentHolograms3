package eu.decentsoftware.holograms.replacements;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.profile.Profile;
import eu.decentsoftware.holograms.api.replacements.Replacement;
import eu.decentsoftware.holograms.api.replacements.ReplacementRegistry;
import eu.decentsoftware.holograms.api.server.Server;
import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import eu.decentsoftware.holograms.utils.DatetimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultReplacementRegistry implements ReplacementRegistry {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\S+(:\\S+)?)}");
    private final Map<String, Replacement> defaultReplacementMap;
    private final Map<String, Replacement> normalReplacementMap;

    /**
     * Create a new instance of {@link DefaultReplacementRegistry}.
     */
    public DefaultReplacementRegistry() {
        this.defaultReplacementMap = new ConcurrentHashMap<>();
        this.normalReplacementMap = new ConcurrentHashMap<>();
        this.reload();
    }

    @Override
    public void reload() {
        this.defaultReplacementMap.clear();
        this.normalReplacementMap.clear();
        this.registerDefaultReplacements();

        // Reload custom replacements
        String path = "replacements";
        FileConfig config = Config.getConfig();
        if (config.isConfigurationSection(path)) {
            config.getConfigurationSection(path).getKeys(false).forEach((key) -> {
                String value = config.getString(path + "." + key);
                Replacement replacement = new DefaultReplacement((player, argument) -> value);
                this.normalReplacementMap.put(key, replacement);
            });
        }
    }

    @Override
    public void shutdown() {
        this.defaultReplacementMap.clear();
        this.normalReplacementMap.clear();
    }

    @Override
    public String replace(@NotNull String string, @Nullable Profile profile) {
        // Replace default replacements
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(string);
        while (matcher.find()) {
            String replacement = getDefaultReplacement(profile, matcher.group(1).trim());
            if (replacement != null) {
                string = string.replace(matcher.group(), replacement);
            }
            matcher = PLACEHOLDER_PATTERN.matcher(string);
        }

        // Replace normal replacements
        for (String key : normalReplacementMap.keySet()) {
            if (string.contains(key)) {
                string = string.replace(key, normalReplacementMap.get(key).getReplacement(profile, null));
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
    @Nullable
    private String getDefaultReplacement(@Nullable Profile profile, @NotNull String placeholderString) {
        String[] spl = placeholderString.split(":", 2);
        Replacement replacement = defaultReplacementMap.get(spl[0]);
        if (replacement != null) {
            return replacement.getReplacement(profile, spl.length > 1 ? spl[1] : null);
        }
        return null;
    }

    /**
     * Register the default built-in replacements.
     */
    private void registerDefaultReplacements() {
        // -- Player placeholders

        this.defaultReplacementMap.put("player", new DefaultReplacement((profile, argument) -> {
            if (profile != null) {
                return profile.getName();
            }
            return null;
        }));
        this.defaultReplacementMap.put("displayname", new DefaultReplacement((profile, argument) -> {
            Player player;
            if (profile == null || (player = profile.getPlayer()) == null) {
                return null;
            }
            return player.getDisplayName();
        }));
        this.defaultReplacementMap.put("uuid", new DefaultReplacement((profile, argument) -> {
            Player player;
            if (profile == null || (player = profile.getPlayer()) == null) {
                return null;
            }
            return player.getUniqueId().toString();
        }));

        // -- Global placeholders

        this.defaultReplacementMap.put("time", new DefaultReplacement(
                (profile, argument) -> DatetimeUtils.getTimeFormatted(),
                "-")
        );
        this.defaultReplacementMap.put("date", new DefaultReplacement(
                (profile, argument) -> DatetimeUtils.getDateFormatted(),
                "-")
        );

        // -- World placeholders

        this.defaultReplacementMap.put("world", new DefaultReplacement(
                (profile, argument) -> {
                    int online;
                    if (argument != null) {
                        // -- Given worlds
                        online = ReplacementsCommons.getFromWorldOrWorldsInt(
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
                    return online >= 0 ? String.valueOf(online) : null;
                }, "0")
        );

        // -- Server & Pinger placeholders

        this.defaultReplacementMap.put("online", new DefaultReplacement(
                (profile, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online;
                        Player player;
                        if (profile != null && (player = profile.getPlayer()) != null) {
                            online = ReplacementsCommons.getFromServerOrServersInt(
                                    player, argument, (server) -> server.getData().getPlayers().getOnline()
                            );
                        } else {
                            online = -1;
                        }
                        if (online >= 0) {
                            return String.valueOf(online);
                        }
                    } else {
                        // -- This server
                        return String.valueOf(Bukkit.getOnlinePlayers().size());
                    }
                    return null;
                }, "0")
        );
        this.defaultReplacementMap.put("max_players", new DefaultReplacement(
                (profile, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online;
                        Player player;
                        if (profile != null && (player = profile.getPlayer()) != null) {
                            online = ReplacementsCommons.getFromServerOrServersInt(
                                    player, argument, (server) -> server.getData().getPlayers().getMax()
                            );
                        } else {
                            online = -1;
                        }
                        if (online >= 0) {
                            return String.valueOf(online);
                        }
                    } else {
                        // -- This server
                        return String.valueOf(Bukkit.getServer().getMaxPlayers());
                    }
                    return null;
                }, "0")
        );
        this.defaultReplacementMap.put("motd", new DefaultReplacement(
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
                    return (Config.PINGER_TRIM_MOTD && motd != null) ? motd.trim() : motd;
                }, "")
        );
        this.defaultReplacementMap.put("status", new DefaultReplacement(
                (profile, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        Server server = PLUGIN.getServerRegistry().getServer(argument);
                        if (server != null && server.isOnline()) {
                            if (server.isFull()) {
                                return Config.PINGER_STATUS_FULL;
                            }
                            return Config.PINGER_STATUS_ONLINE;
                        }
                    } else {
                        // -- This server
                        return Config.PINGER_STATUS_ONLINE;
                    }
                    return null;
                }, Config.PINGER_STATUS_OFFLINE)
        );
    }

}
