package eu.decentsoftware.holograms.actions;

import eu.decentsoftware.holograms.actions.impl.*;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionType;
import eu.decentsoftware.holograms.api.actions.ActionTypeRegistry;
import eu.decentsoftware.holograms.api.exception.LocationParseException;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.config.ConfigUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DefaultActionTypeRegistry implements ActionTypeRegistry {

    private final @NotNull Set<ActionType> types;

    /**
     * Create a new instance of {@link DefaultActionTypeRegistry}. This constructor
     * also registers all default action types.
     */
    public DefaultActionTypeRegistry() {
        this.types = new HashSet<>();

        // Register default action types
        register(ACTION_BAR);
        register(CHAT);
        register(COMMAND);
        register(CONNECT);
        register(CONSOLE);
        register(MESSAGE);
        register(MESSAGE_BROADCAST);
        register(SOUND);
        register(SOUND_BROADCAST);
        register(SOUND_BROADCAST_WORLD);
        register(TELEPORT);
        register(TITLE);
        register(WAIT);
    }

    @Override
    public void register(@NotNull ActionType type) {
        this.types.add(type);
    }

    @Override
    public void remove(@NotNull ActionType type) {
        this.types.remove(type);
    }

    @Override
    public void remove(@NotNull String name) {
        for (ActionType type : this.types) {
            if (type.isAlias(name)) {
                this.types.remove(type);
                return;
            }
        }
    }

    @Nullable
    @Override
    public ActionType get(@NotNull String name) {
        for (ActionType type : this.types) {
            if (type.isAlias(name)) {
                return type;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Set<ActionType> getTypes() {
        return this.types;
    }
    
    // =========================================================== //

    public static final ActionType ACTION_BAR = new ActionType("ACTION_BAR", "actionbar") {
        @Override
        public Action createAction(@NotNull String... data) {
            String message = Common.implode(data, " ");
            return new ActionBarAction(message);
        }
    };

    public static final ActionType CHAT = new ActionType("CHAT") {
        @Override
        public Action createAction(@NotNull String... data) {
            String message = Common.implode(data, " ");
            return new ChatAction(message);
        }
    };

    public static final ActionType COMMAND = new ActionType("COMMAND", "cmd") {
        @Override
        public Action createAction(@NotNull String... data) {
            String command = Common.implode(data, " ");
            return new CommandAction(command);
        }
    };

    public static final ActionType CONNECT = new ActionType("CONNECT") {
        @Override
        public Action createAction(@NotNull String... data) {
            return new ConnectAction(data[0]);
        }
    };

    public static final ActionType CONSOLE = new ActionType("CONSOLE") {
        @Override
        public Action createAction(@NotNull String... data) {
            String command = Common.implode(data, " ");
            return new ConsoleAction(command);
        }
    };

    public static final ActionType MESSAGE = new ActionType("MESSAGE") {
        @Override
        public Action createAction(@NotNull String... data) {
            String message = Common.implode(data, " ");
            return new MessageAction(message);
        }
    };

    public static final ActionType MESSAGE_BROADCAST = new ActionType(
            "MESSAGE_BROADCAST", "message broadcast", "broadcast", "broadcast message"
    ) {
        @Override
        public Action createAction(@NotNull String... data) {
            String message = Common.implode(data, " ");
            return new MessageBroadcastAction(message);
        }
    };

    public static final ActionType SOUND = new ActionType("SOUND", "play sound") {
        @Override
        public Action createAction(@NotNull String... data) {
            if (data.length == 0) {
                return null;
            }
            String sound = data[0];
            if (data.length > 2) {
                float volume = Float.parseFloat(data[1]);
                float pitch = Float.parseFloat(data[2]);
                return new SoundAction(sound, volume, pitch);
            }
            return new SoundAction(sound);
        }
    };

    public static final ActionType SOUND_BROADCAST = new ActionType("SOUND_BROADCAST", "broadcast sound") {
        @Override
        public Action createAction(@NotNull String... data) {
            if (data.length == 0) {
                return null;
            }
            String sound = data[0];
            if (data.length > 2) {
                float volume = Float.parseFloat(data[1]);
                float pitch = Float.parseFloat(data[2]);
                return new SoundBroadcastAction(sound, volume, pitch);
            }
            return new SoundBroadcastAction(sound);
        }
    };

    public static final ActionType SOUND_BROADCAST_WORLD = new ActionType(
            "SOUND_BROADCAST_WORLD", "sound broadcast world"
    ) {
        @Override
        public Action createAction(@NotNull String... data) {
            if (data.length == 0) {
                return null;
            }
            String sound = data[0];
            if (data.length > 2) {
                float volume = Float.parseFloat(data[1]);
                float pitch = Float.parseFloat(data[2]);
                return new SoundBroadcastWorldAction(sound, volume, pitch);
            }
            return new SoundBroadcastWorldAction(sound);
        }
    };

    public static final ActionType TELEPORT = new ActionType("TELEPORT", "tele", "tp") {
        @Override
        public Action createAction(@NotNull String... data) {
            try {
                Location location = ConfigUtils.stringToLoc(Common.implode(data, ":"));
                return new TeleportAction(location);
            } catch (LocationParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    public static final ActionType TITLE = new ActionType("TITLE") {
        @Override
        public Action createAction(@NotNull String... data) {
            if (data.length > 0) {
                String title = data[0];
                if (data.length > 1) {
                    String subtitle = data[1];
                    if (data.length > 4) {
                        int fadeIn = Integer.parseInt(data[2]);
                        int stay = Integer.parseInt(data[3]);
                        int fadeOut = Integer.parseInt(data[4]);
                        return new TitleAction(title, subtitle, fadeIn, stay, fadeOut);
                    }
                    return new TitleAction(title, subtitle, 20, 60, 20);
                }
                return new TitleAction(title, "", 20, 60, 20);
            }
            return new TitleAction("", "", 20, 60, 20);
        }
    };

    public static final ActionType WAIT = new ActionType("WAIT", "pause") {
        @Override
        public Action createAction(@NotNull String... data) {
            int seconds = Integer.parseInt(data[0]);
            return new WaitAction(seconds);
        }
    };

}
