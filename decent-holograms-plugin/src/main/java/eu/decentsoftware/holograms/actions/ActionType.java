package eu.decentsoftware.holograms.actions;

import eu.decentsoftware.holograms.actions.impl.*;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This enum holds all possible Action types and some useful data about them.
 *
 * @author d0by
 * @since 3.0.0
 */
@SuppressWarnings("SpellCheckingInspection")
public enum ActionType {
    ACTIONBAR(ActionBarAction.class, "action-bar", "action_bar"),
    CHAT(ChatAction.class, "say"),
    COMMAND(CommandAction.class, "cmd"),
    CONNECT(ConnectAction.class, "send"),
    CONSOLE(ConsoleAction.class, "consolecmd", "console-cmd", "console_cmd"),
    MESSAGE(MessageAction.class, "msg"),
    MESSAGE_BROADCAST(MessageBroadcastAction.class, "broadcastmessage", "bcmessage", "messagebc", "bcmsg"),
    SOUND(SoundAction.class, "playsound"),
    SOUND_BROADCAST(SoundBroadcastAction.class, "broadcastsound", "bcsound", "soundbc", "bcs"),
    SOUND_BROADCAST_WORLD(SoundBroadcastWorldAction.class, "broadcastsoundworld", "bcsoundworld", "soundbcworld", "bcsoundw", "bcsw"),
    TELEPORT(TeleportAction.class, "tp"),
    TITLE(TitleAction.class, "title"),
    WAIT(WaitAction.class, "delay"),
    ;

    private final Class<? extends Action> actionClass;
    private final Set<String> aliases = new HashSet<>();

    ActionType(@NonNull Class<? extends Action> actionClass, String... aliases) {
        this.actionClass = actionClass;
        if (aliases != null) {
            this.aliases.addAll(Arrays.asList(aliases));
        }
    }

    @NonNull
    @Contract(pure = true)
    public Class<? extends Action> getActionClass() {
        return actionClass;
    }

    @NonNull
    @Contract(pure = true)
    public Set<String> getAliases() {
        return aliases;
    }

    /**
     * Find an {@link ActionType} by the given name or alias.
     *
     * @param string The name or alias to search for.
     * @return The ActionType or null if the given name or alias doesn't match any type.
     */
    @Nullable
    public static ActionType byNameOrAlias(@NonNull String string) {
        for (ActionType conditionType : values()) {
            if (conditionType.name().equalsIgnoreCase(string.trim())) {
                return conditionType;
            }
            for (String alias : conditionType.getAliases()) {
                if (alias.trim().equalsIgnoreCase(string.trim())) {
                    return conditionType;
                }
            }
        }
        return null;
    }

    /**
     * Find an {@link ActionType} by the given class.
     *
     * @param clazz The class to search for.
     * @return The ActionType or null if the class doesn't match any.
     */
    @Nullable
    public static ActionType byClass(@NonNull Class<? extends Action> clazz) {
        for (ActionType conditionType : values()) {
            if (conditionType.getActionClass().equals(clazz)) {
                return conditionType;
            }
        }
        return null;
    }

}
