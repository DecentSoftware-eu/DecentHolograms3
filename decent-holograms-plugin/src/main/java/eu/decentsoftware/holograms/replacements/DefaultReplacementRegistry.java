package eu.decentsoftware.holograms.replacements;

import eu.decentsoftware.holograms.Config;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.replacements.Replacement;
import eu.decentsoftware.holograms.api.replacements.ReplacementRegistry;
import eu.decentsoftware.holograms.api.server.Server;
import eu.decentsoftware.holograms.utils.DatetimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultReplacementRegistry extends ReplacementRegistry {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\S+(:\\S+)?)}");

    /**
     * Create a new instance of this registry.
     */
    public DefaultReplacementRegistry() {
        this.registerDefaultPlaceholders();
    }

    @Override
    public void reload() {}

    @Override
    public String replacePlaceholders(Player player, @NotNull String string) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(string);
        while (matcher.find()) {
            String replacement = this.getReplacement(player, matcher.group(1).trim());
            if (replacement != null) {
                string = string.replace(matcher.group(), replacement);
            }
            matcher = PLACEHOLDER_PATTERN.matcher(string);
        }
        return string;
    }

    /**
     * Get the replacement for the given placeholder string.
     *
     * @param player The player to get the replacement for.
     * @param placeholderString The placeholder string.
     * @return The replacement for the placeholder string or null
     *  if the given placeholder string cannot be replaced.
     */
    @Nullable
    private String getReplacement(Player player, @NotNull String placeholderString) {
        String placeholderIdentifier;
        String placeholderArgument;
        if (placeholderString.contains(":")) {
            String[] spl = placeholderString.split(":", 1);
            placeholderIdentifier = spl[0];
            placeholderArgument = spl[1];
        } else {
            placeholderIdentifier = placeholderString;
            placeholderArgument = null;
        }

        Replacement replacement = this.get(placeholderIdentifier);
        if (replacement != null) {
            return replacement.getReplacement(player, placeholderArgument);
        }
        return null;
    }

    /**
     * Register the default built-in placeholders.
     */
    private void registerDefaultPlaceholders() {
        // -- Player placeholders

        this.register("player", new DefaultReplacement(
                (player, argument) -> player == null ? null : player.getName(),
                "You")
        );
        this.register("display_name", new DefaultReplacement(
                (player, argument) -> player == null ? null : player.getDisplayName(),
                "You")
        );

        // -- Global placeholders

        this.register("time", new DefaultReplacement(
                (player, argument) -> DatetimeUtils.getTimeFormatted(),
                "-")
        );
        this.register("date", new DefaultReplacement(
                (player, argument) -> DatetimeUtils.getDateFormatted(),
                "-")
        );

        // -- World placeholders

        this.register("world", new DefaultReplacement(
                (player, argument) -> {
                    int online;
                    if (argument != null) {
                        // -- Given worlds
                        online = ReplacementsCommons.getFromWorldOrWorldsInt(
                                argument, (world) -> world.getPlayers().size()
                        );
                    } else if (player != null) {
                        // -- Player world
                        online = player.getWorld().getPlayers().size();
                    } else {
                        online = -1;
                    }
                    return online >= 0 ? String.valueOf(online) : null;
                }, "0")
        );

        // -- Server & Pinger placeholders

        this.register("online", new DefaultReplacement(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online = ReplacementsCommons.getFromServerOrServersInt(
                                player, argument, (server) -> server.getData().getPlayers().getOnline()
                        );
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
        this.register("max_players", new DefaultReplacement(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online = ReplacementsCommons.getFromServerOrServersInt(
                                player, argument, (server) -> server.getData().getPlayers().getMax()
                        );
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
        this.register("motd", new DefaultReplacement(
                (player, argument) -> {
                    String motd = null;
                    if (argument != null) {
                        // -- Pinged server
                        Server server = PLUGIN.getServerRegistry().get(argument);
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
        this.register("status", new DefaultReplacement(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        Server server = PLUGIN.getServerRegistry().get(argument);
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
