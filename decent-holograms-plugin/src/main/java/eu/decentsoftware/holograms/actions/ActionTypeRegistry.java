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

package eu.decentsoftware.holograms.actions;

public class ActionTypeRegistry {
//
//    private final @NotNull Set<ActionType> types;
//
//    /**
//     * Create a new instance of {@link DefaultActionTypeRegistry}. This constructor
//     * also registers all default action types.
//     */
//    public DefaultActionTypeRegistry() {
//        this.types = new HashSet<>();
//
//        // Register default action types
//        register(ACTION_BAR);
//        register(CHAT);
//        register(COMMAND);
//        register(CONNECT);
//        register(CONSOLE);
//        register(MESSAGE);
//        register(MESSAGE_BROADCAST);
//        register(SOUND);
//        register(SOUND_BROADCAST);
//        register(SOUND_BROADCAST_WORLD);
//        register(TELEPORT);
//        register(TITLE);
//        register(WAIT);
//    }
//
//    @Override
//    public void register(@NotNull ActionType type) {
//        this.types.add(type);
//    }
//
//    @Override
//    public void remove(@NotNull ActionType type) {
//        this.types.remove(type);
//    }
//
//    @Override
//    public void remove(@NotNull String name) {
//        for (ActionType type : new HashSet<>(this.types)) {
//            if (type.isAlias(name)) {
//                this.types.remove(type);
//                return;
//            }
//        }
//    }
//
//    @Nullable
//    @Override
//    public ActionType get(@NotNull String name) {
//        for (ActionType type : this.types) {
//            if (type.isAlias(name)) {
//                return type;
//            }
//        }
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public Set<ActionType> getTypes() {
//        return this.types;
//    }
//
//    // =========================================================== //
//
//    public static final ActionType ACTION_BAR = new ActionType("ACTION_BAR", "actionbar") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            String message = Common.implode(data, " ");
//            return new ActionBarAction(message);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String message = object.get("message").getAsString();
//            return new ActionBarAction(message);
//        }
//    };
//
//    public static final ActionType CHAT = new ActionType("CHAT") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            String message = Common.implode(data, " ");
//            return new ChatAction(message);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String message = object.get("message").getAsString();
//            return new ChatAction(message);
//        }
//    };
//
//    public static final ActionType COMMAND = new ActionType("COMMAND", "cmd") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            String command = Common.implode(data, " ");
//            return new CommandAction(command);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String command = object.get("command").getAsString();
//            return new CommandAction(command);
//        }
//    };
//
//    public static final ActionType CONNECT = new ActionType("CONNECT") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            return new ConnectAction(data[0]);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String server = object.get("server").getAsString();
//            return new ConnectAction(server);
//        }
//    };
//
//    public static final ActionType CONSOLE = new ActionType("CONSOLE") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            String command = Common.implode(data, " ");
//            return new ConsoleAction(command);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String command = object.get("command").getAsString();
//            return new ConsoleAction(command);
//        }
//    };
//
//    public static final ActionType MESSAGE = new ActionType("MESSAGE") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            String message = Common.implode(data, " ");
//            return new MessageAction(message);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String message = object.get("message").getAsString();
//            return new MessageAction(message);
//        }
//    };
//
//    public static final ActionType MESSAGE_BROADCAST = new ActionType(
//            "MESSAGE_BROADCAST", "message broadcast", "broadcast", "broadcast message"
//    ) {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            String message = Common.implode(data, " ");
//            return new MessageBroadcastAction(message);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String message = object.get("message").getAsString();
//            return new MessageBroadcastAction(message);
//        }
//    };
//
//    public static final ActionType SOUND = new ActionType("SOUND", "play sound") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            if (data.length == 0) {
//                return null;
//            }
//            String sound = data[0];
//            if (data.length > 2) {
//                float volume = Float.parseFloat(data[1]);
//                float pitch = Float.parseFloat(data[2]);
//                return new SoundAction(sound, volume, pitch);
//            }
//            return new SoundAction(sound);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String sound = object.get("sound").getAsString();
//            if (sound == null) {
//                return null;
//            }
//            float volume = object.has("volume") ? object.get("volume").getAsFloat() : 1.0f;
//            float pitch = object.has("pitch") ? object.get("pitch").getAsFloat() : 1.0f;
//            return new SoundAction(sound, volume, pitch);
//        }
//    };
//
//    public static final ActionType SOUND_BROADCAST = new ActionType("SOUND_BROADCAST", "broadcast sound") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            if (data.length == 0) {
//                return null;
//            }
//            String sound = data[0];
//            if (data.length > 2) {
//                float volume = Float.parseFloat(data[1]);
//                float pitch = Float.parseFloat(data[2]);
//                return new SoundBroadcastAction(sound, volume, pitch);
//            }
//            return new SoundBroadcastAction(sound);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String sound = object.get("sound").getAsString();
//            if (sound == null) {
//                return null;
//            }
//            float volume = object.has("volume") ? object.get("volume").getAsFloat() : 1.0f;
//            float pitch = object.has("pitch") ? object.get("pitch").getAsFloat() : 1.0f;
//            return new SoundBroadcastAction(sound, volume, pitch);
//        }
//    };
//
//    public static final ActionType SOUND_BROADCAST_WORLD = new ActionType(
//            "SOUND_BROADCAST_WORLD", "sound broadcast world"
//    ) {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            if (data.length == 0) {
//                return null;
//            }
//            String sound = data[0];
//            if (data.length > 2) {
//                float volume = Float.parseFloat(data[1]);
//                float pitch = Float.parseFloat(data[2]);
//                return new SoundBroadcastWorldAction(sound, volume, pitch);
//            }
//            return new SoundBroadcastWorldAction(sound);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String sound = object.get("sound").getAsString();
//            if (sound == null) {
//                return null;
//            }
//            float volume = object.has("volume") ? object.get("volume").getAsFloat() : 1.0f;
//            float pitch = object.has("pitch") ? object.get("pitch").getAsFloat() : 1.0f;
//            return new SoundBroadcastWorldAction(sound, volume, pitch);
//        }
//    };
//
//    public static final ActionType TELEPORT = new ActionType("TELEPORT", "tele", "tp") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            try {
//                Location location = ConfigUtils.stringToLoc(Common.implode(data, ":"));
//                return new TeleportAction(location);
//            } catch (LocationParseException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String worldName = object.get("world").getAsString();
//            World world = Bukkit.getWorld(worldName);
//            if (world == null) {
//                return null;
//            }
//            double x = object.get("x").getAsDouble();
//            double y = object.get("y").getAsDouble();
//            double z = object.get("z").getAsDouble();
//            float yaw = object.has("yaw") ? object.get("yaw").getAsFloat() : 0.0f;
//            float pitch = object.has("pitch") ? object.get("pitch").getAsFloat() : 0.0f;
//            Location location = new Location(world, x, y, z, yaw, pitch);
//            return new TeleportAction(location);
//        }
//    };
//
//    public static final ActionType TITLE = new ActionType("TITLE") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            if (data.length > 0) {
//                String title = data[0];
//                if (data.length > 1) {
//                    String subtitle = data[1];
//                    if (data.length > 4) {
//                        int fadeIn = Integer.parseInt(data[2]);
//                        int stay = Integer.parseInt(data[3]);
//                        int fadeOut = Integer.parseInt(data[4]);
//                        return new TitleAction(title, subtitle, fadeIn, stay, fadeOut);
//                    }
//                    return new TitleAction(title, subtitle, 20, 60, 20);
//                }
//                return new TitleAction(title, "", 20, 60, 20);
//            }
//            return new TitleAction("", "", 20, 60, 20);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            String title = object.has("title") ? object.get("title").getAsString() : "";
//            String subtitle = object.has("subtitle") ? object.get("subtitle").getAsString() : "";
//            int fadeIn = object.has("fadeIn") ? object.get("fadeIn").getAsInt() : 20;
//            int stay = object.has("stay") ? object.get("stay").getAsInt() : 60;
//            int fadeOut = object.has("fadeOut") ? object.get("fadeOut").getAsInt() : 20;
//            return new TitleAction(title, subtitle, fadeIn, stay, fadeOut);
//        }
//    };
//
//    public static final ActionType WAIT = new ActionType("WAIT", "pause") {
//        @Override
//        public Action createAction(@NotNull String... data) {
//            int ticks = Integer.parseInt(data[0]);
//            return new WaitAction(ticks);
//        }
//
//        @Override
//        public Action createAction(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            int ticks = object.get("ticks").getAsInt();
//            return new WaitAction(ticks);
//        }
//    };
//
}
