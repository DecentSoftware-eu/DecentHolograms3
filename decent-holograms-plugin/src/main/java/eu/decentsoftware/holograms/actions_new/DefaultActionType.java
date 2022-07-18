package eu.decentsoftware.holograms.actions_new;

import eu.decentsoftware.holograms.actions.ActionType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * This enum holds all default (built-in) action types.
 *
 * @author d0by
 * @since 3.0.0
 */
public enum DefaultActionType {

    // -- Message Actions

    /**
     * Send the given message to the player.
     */
    MESSAGE("message"),
    /**
     * Broadcast the given message across the server.
     */
    BROADCAST("broadcast", "broadcast message"),
    /**
     * Send the given title and subtitle to the player.
     */
    TITLE("title", "title message"),
    /**
     * Send the given action bar to the player.
     */
    ACTION_BAR("actionbar", "actionbar message"),

    // -- Sound Actions

    /**
     * Play the given sound to the player.
     */
    SOUND("sound", "play sound"),
    /**
     * Broadcast the given sound across the server.
     */
    BROADCAST_SOUND("broadcast sound"),
    /**
     * Broadcast the given sound across the world.
     */
    BROADCAST_SOUND_WORLD("broadcast sound world", "play sound world"),

    // -- Command Actions

    /**
     * Execute the given command as the player.
     */
    COMMAND("command", "player"),
    /**
     * Execute the given command as console.
     */
    CONSOLE("console"),
    /**
     * Send the given message into chat as the player.
     */
    CHAT("chat"),

    // -- Server Actions

    /**
     * Send player to the given server.
     */
    CONNECT("connect", "server"),

    // -- Other Actions

    /**
     * Teleport the player to the given location.
     */
    TELEPORT("teleport", "tele", "tp"),
    ;

    @Getter
    private final Set<String> aliases;

    DefaultActionType(String... aliases) {
        this.aliases = new HashSet<>();
        if (aliases != null) {
            for (String alias : aliases) {
                this.aliases.add(alias.toLowerCase());
            }
        }
    }

    /**
     * Get an {@link ActionType} from string if possible.
     *
     * @param string The string.
     * @return The matching ActionType or null if the string doesn't match any type.
     */
    @Nullable
    public static DefaultActionType fromString(@NotNull String string) {
        for (DefaultActionType type : values()) {
            for (String alias : type.getAliases()) {
                if (alias.trim().equalsIgnoreCase(string.trim())) {
                    return type;
                }
            }
        }
        return null;
    }

}
