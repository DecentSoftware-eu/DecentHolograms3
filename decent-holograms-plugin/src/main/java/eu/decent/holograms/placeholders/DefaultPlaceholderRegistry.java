package eu.decent.holograms.placeholders;

import eu.decent.holograms.Config;
import eu.decent.holograms.api.DecentHolograms;
import eu.decent.holograms.api.DecentHologramsAPI;
import eu.decent.holograms.api.placeholders.Placeholder;
import eu.decent.holograms.api.placeholders.PlaceholderRegistry;
import eu.decent.holograms.api.server.Server;
import eu.decent.holograms.utils.DatetimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This registry holds all custom placeholders.
 */
public class DefaultPlaceholderRegistry extends PlaceholderRegistry {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.getInstance();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\S+(:\\S+)?)}");

    /**
     * Create a new instance of this registry.
     */
    public DefaultPlaceholderRegistry() {
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

        Placeholder placeholder = this.get(placeholderIdentifier);
        if (placeholder != null) {
            return placeholder.getReplacement(player, placeholderArgument);
        }
        return null;
    }

    /**
     * Register the default built-in placeholders.
     */
    private void registerDefaultPlaceholders() {
        // -- Player placeholders

        this.register("player", new DefaultPlaceholder(
                (player, argument) -> player == null ? null : player.getName(),
                "You")
        );
        this.register("display_name", new DefaultPlaceholder(
                (player, argument) -> player == null ? null : player.getDisplayName(),
                "You")
        );

        // -- Global placeholders

        this.register("time", new DefaultPlaceholder(
                (player, argument) -> DatetimeUtils.getTimeFormatted(),
                "-")
        );
        this.register("date", new DefaultPlaceholder(
                (player, argument) -> DatetimeUtils.getDateFormatted(),
                "-")
        );

        // -- World placeholders

        this.register("world", new DefaultPlaceholder(
                (player, argument) -> {
                    int online;
                    if (argument != null) {
                        // -- Given worlds
                        online = PlaceholderCommons.getFromWorldOrWorldsInt(
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

        this.register("online", new DefaultPlaceholder(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online = PlaceholderCommons.getFromServerOrServersInt(
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
        this.register("max_players", new DefaultPlaceholder(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        int online = PlaceholderCommons.getFromServerOrServersInt(
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
        this.register("motd", new DefaultPlaceholder(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        Server server = PLUGIN.getServerRegistry().get(argument);
                        if (server != null && server.isOnline()) {
                            return server.getData().getDescription();
                        }
                    } else {
                        // -- This server
                        return Bukkit.getServer().getMotd();
                    }
                    return null;
                }, "")
        );
        this.register("status", new DefaultPlaceholder(
                (player, argument) -> {
                    if (argument != null) {
                        // -- Pinged server
                        Server server = PLUGIN.getServerRegistry().get(argument);
                        if (server != null && server.isOnline()) {
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
