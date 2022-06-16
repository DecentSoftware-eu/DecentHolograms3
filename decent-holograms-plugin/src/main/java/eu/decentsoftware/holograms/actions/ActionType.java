package eu.decentsoftware.holograms.actions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum ActionType {

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

    // -- Menu Actions

    /**
     * Open the given menu.
     */
    OPEN_MENU("open menu", "open", "menu"),
    /**
     * Open the previous menu if possible.
     */
    PREVIOUS_MENU("previous menu", "previous", "prev", "prev menu"),
    /**
     * Refresh the players current menu if possible.
     */
    REFRESH_MENU("refresh menu", "update menu", "refresh", "update"),
    /**
     * Close the players current menu if possible.
     */
    CLOSE_MENU("close menu", "close"),

    // -- Other Actions

    /**
     * Teleport the player to the given location.
     */
    TELEPORT("teleport", "tele", "tp"),
    ;

    private final Set<String> aliases;

    ActionType(String... aliases) {
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
    public static ActionType fromString(@NotNull String string) {
        for (ActionType actionType : values()) {
            for (String alias : actionType.getAliases()) {
                if (alias.trim().equalsIgnoreCase(string.trim())) {
                    return actionType;
                }
            }
        }
        return null;
    }

}
