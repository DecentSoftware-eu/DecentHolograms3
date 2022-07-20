package eu.decentsoftware.holograms.actions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.decentsoftware.holograms.actions.impl.*;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionType;
import eu.decentsoftware.holograms.api.actions.ActionTypeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
            if (type.isValid(name)) {
                this.types.remove(type);
                return;
            }
        }
    }

    @Nullable
    @Override
    public ActionType get(@NotNull String name) {
        for (ActionType type : this.types) {
            if (type.isValid(name)) {
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
        public Action createAction(@NotNull JsonObject json) {
            String message = json.get("message").getAsString();
            return new ActionBarAction(message);
        }
    };

    public static final ActionType CHAT = new ActionType("CHAT") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            String message = json.get("message").getAsString();
            return new ChatAction(message);
        }
    };

    public static final ActionType COMMAND = new ActionType("COMMAND", "cmd") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            String command = json.get("command").getAsString();
            return new CommandAction(command);
        }
    };

    public static final ActionType CONNECT = new ActionType("CONNECT") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            String server = json.get("server").getAsString();
            return new ConnectAction(server);
        }
    };

    public static final ActionType CONSOLE = new ActionType("CONSOLE") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            String command = json.get("command").getAsString();
            return new ConsoleAction(command);
        }
    };

    public static final ActionType MESSAGE = new ActionType("MESSAGE") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            JsonElement element = json.get("message");
            if (element.isJsonArray()) {
                String[] messages = new String[element.getAsJsonArray().size()];
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = element.getAsJsonArray().get(i).getAsString();
                }
                return new MessageAction(messages);
            }
            String message = element.getAsString();
            return new MessageAction(message);
        }
    };

    public static final ActionType MESSAGE_BROADCAST = new ActionType(
            "MESSAGE_BROADCAST", "message broadcast", "broadcast", "broadcast message"
    ) {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            JsonElement element = json.get("message");
            if (element.isJsonArray()) {
                String[] messages = new String[element.getAsJsonArray().size()];
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = element.getAsJsonArray().get(i).getAsString();
                }
                return new MessageBroadcastAction(messages);
            }
            String message = element.getAsString();
            return new MessageBroadcastAction(message);
        }
    };

    public static final ActionType SOUND = new ActionType("SOUND", "play sound") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            String sound = json.get("sound").getAsString();
            float volume = (float) json.get("volume").getAsDouble();
            float pitch = (float) json.get("pitch").getAsDouble();
            return new SoundAction(sound, volume, pitch);
        }
    };

    public static final ActionType SOUND_BROADCAST = new ActionType("SOUND_BROADCAST", "broadcast sound") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            String sound = json.get("sound").getAsString();
            float volume = (float) json.get("volume").getAsDouble();
            float pitch = (float) json.get("pitch").getAsDouble();
            return new SoundBroadcastAction(sound, volume, pitch);
        }
    };

    public static final ActionType SOUND_BROADCAST_WORLD = new ActionType(
            "SOUND_BROADCAST_WORLD", "sound broadcast world"
    ) {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            String sound = json.get("sound").getAsString();
            float volume = (float) json.get("volume").getAsDouble();
            float pitch = (float) json.get("pitch").getAsDouble();
            return new SoundBroadcastWorldAction(sound, volume, pitch);
        }
    };

    public static final ActionType TELEPORT = new ActionType("TELEPORT", "tele", "tp") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            World world = Bukkit.getWorld(json.get("world").getAsString());
            if (world == null) {
                return null;
            }
            double x = json.get("x").getAsDouble();
            double y = json.get("y").getAsDouble();
            double z = json.get("z").getAsDouble();
            return new TeleportAction(new Location(world, x, y, z));
        }
    };

    public static final ActionType TITLE = new ActionType("TITLE") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            String title = json.get("title").getAsString();
            String subtitle = json.get("subtitle").getAsString();
            int fadeIn = json.get("fadeIn").getAsInt();
            int stay = json.get("stay").getAsInt();
            int fadeOut = json.get("fadeOut").getAsInt();
            return new TitleAction(title, subtitle, fadeIn, stay, fadeOut);
        }
    };

    public static final ActionType WAIT = new ActionType("WAIT", "pause") {
        @Override
        public Action createAction(@NotNull JsonObject json) {
            int seconds = json.get("ticks").getAsInt();
            return new WaitAction(seconds);
        }
    };

}
