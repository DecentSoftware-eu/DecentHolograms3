package eu.decentsoftware.holograms.actions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This enum holds all possible Condition types.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings("SpellCheckingInspection")
public enum ActionType {
    ACTIONBAR("action-bar", "action_bar"),
    CHAT,
    COMMAND("cmd"),
    CONNECT("send"),
    CONSOLE,
    MESSAGE("msg"),
    MESSAGE_BROADCAST("broadcastmessage", "bcmessage", "messagebc", "bcmsg"),
    SOUND("playsound"),
    SOUND_BROADCAST("broadcastsound", "bcsound", "soundbc", "bcs"),
    SOUND_BROADCAST_WORLD("broadcastsoundworld", "bcsoundworld", "soundbcworld", "bcsoundw", "bcsw"),
    TELEPORT("tp"),
    TITLE("title"),
    WAIT("delay"),
    ;

    @Getter
    private final Set<String> aliases;

    ActionType(String... aliases) {
        this.aliases = new HashSet<>();
        if (aliases != null) {
            this.aliases.addAll(Arrays.asList(aliases));
        }
    }

    /**
     * Find a {@link ActionType} by the given string.
     *
     * @param string The string.
     * @return The ActionType or null if the string doesn't match any.
     */
    @Nullable
    public static ActionType fromString(@NotNull String string) {
        for (ActionType conditionType : values()) {
            for (String alias : conditionType.getAliases()) {
                if (alias.trim().equalsIgnoreCase(string.trim())) {
                    return conditionType;
                }
            }
        }
        return null;
    }

}
