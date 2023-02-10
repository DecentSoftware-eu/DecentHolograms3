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

import eu.decentsoftware.holograms.actions.impl.*;
import eu.decentsoftware.holograms.profile.Profile;
import eu.decentsoftware.holograms.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a specific action. It can be instantiated,
 * then executed as many times as needed. This class holds all parameters
 * of the action and uses them to properly execute the action.
 *
 * @author d0by
 * @since 3.0.0
 */
public abstract class Action {

    protected final long delay;
    protected final double chance;

    /**
     * Create a new instance of {@link Action} with default values.
     */
    @Contract(pure = true)
    public Action() {
        this(0, -1.0);
    }

    /**
     * Create a new instance of {@link Action} with the given delay and chance.
     *
     * @param delay  The delay.
     * @param chance The chance.
     */
    @Contract(pure = true)
    public Action(long delay, double chance) {
        this.delay = delay;
        this.chance = chance;
    }

    /**
     * Execute this action for the given {@link Profile}.
     *
     * @param profile The profile.
     */
    public abstract void execute(@NotNull Profile profile);

    /**
     * Check if this action should be executed according to chance.
     *
     * @return True if this action should be executed.
     */
    public boolean checkChance() {
        if (chance < 0) {
            return true;
        }
        double rand = Math.random();
        return rand < chance;
    }

    /**
     * Get the delay of this action. This is the amount of ticks
     * that should be waited before executing this action.
     *
     * @return The delay of this action.
     */
    public long getDelay() {
        return delay;
    }

    /**
     * Get the chance of this action. This is the chance that this action
     * should be executed.
     *
     * @return The chance of this action.
     */
    public double getChance() {
        return chance;
    }

    /**
     * Create an instance of {@link Action} from the given string. The string
     * should be in the format of {@code <type>:[data]}. The type is the type of
     * the action, and the data is the data that should be used for the action.
     *
     * @param string The string.
     * @return The action or null if the string is invalid.
     */
    @Nullable
    public static Action fromString(@NotNull String string) throws Exception {
        String[] split = string.split(":", 2);
        if (split.length == 0) {
            return null;
        }
        String typeName = split[0];
        String data = split.length == 2 ? split[1] : null;
        ActionType type = ActionType.fromString(typeName);
        if (type == null) {
            return null;
        }

        switch (type) {
            case ACTIONBAR:
                if (data != null) {
                    return new ActionBarAction(data);
                }
                break;
            case CHAT:
                if (data != null) {
                    return new ChatAction(data);
                }
                break;
            case COMMAND:
                if (data != null) {
                    return new CommandAction(data);
                }
                break;
            case CONNECT:
                if (data != null) {
                    return new ConnectAction(data);
                }
                break;
            case CONSOLE:
                if (data != null) {
                    return new ConsoleAction(data);
                }
                break;
            case MESSAGE:
                if (data != null) {
                    return new MessageAction(data);
                }
                break;
            case MESSAGE_BROADCAST:
                if (data != null) {
                    return new MessageBroadcastAction(data);
                }
                break;
            case SOUND:
                if (data != null) {
                    String[] soundSplit = split(data, 3);
                    String sound = soundSplit[0];
                    if (soundSplit.length > 2) {
                        float volume = Float.parseFloat(soundSplit[1]);
                        float pitch = Float.parseFloat(soundSplit[2]);
                        return new SoundAction(sound, volume, pitch);
                    }
                    return new SoundAction(sound);
                }
                break;
            case SOUND_BROADCAST:
                if (data != null) {
                    String[] soundSplit = split(data, 3);
                    String sound = soundSplit[0];
                    if (soundSplit.length > 2) {
                        float volume = Float.parseFloat(soundSplit[1]);
                        float pitch = Float.parseFloat(soundSplit[2]);
                        return new SoundBroadcastAction(sound, volume, pitch);
                    }
                    return new SoundBroadcastAction(sound);
                }
                break;
            case SOUND_BROADCAST_WORLD:
                if (data == null) {
                    break;
                }
                String[] soundSplit = split(data, 3);
                String sound = soundSplit[0];
                if (soundSplit.length > 2) {
                    float volume = Float.parseFloat(soundSplit[1]);
                    float pitch = Float.parseFloat(soundSplit[2]);
                    return new SoundBroadcastWorldAction(sound, volume, pitch);
                }
                return new SoundBroadcastWorldAction(sound);
            case TELEPORT:
                if (data == null) {
                    break;
                }
                String[] teleportSplit = split(data, 6);
                Location location;
                if (teleportSplit.length > 3) {
                    String world = teleportSplit[0];
                    double x = Double.parseDouble(teleportSplit[1]);
                    double y = Double.parseDouble(teleportSplit[2]);
                    double z = Double.parseDouble(teleportSplit[3]);
                    location = new Location(Bukkit.getWorld(world), x, y, z);
                    if (teleportSplit.length > 5) {
                        float yaw = Float.parseFloat(teleportSplit[4]);
                        float pitch = Float.parseFloat(teleportSplit[5]);
                        location.setYaw(yaw);
                        location.setPitch(pitch);
                    }
                    return new TeleportAction(location);
                }
                break;
            case TITLE:
                if (data == null) {
                    break;
                }
                String[] titleSplit = split(data, 5);
                if (titleSplit.length > 0) {
                    String title = titleSplit[0];
                    if (titleSplit.length > 1) {
                        String subtitle = titleSplit[1];
                        if (titleSplit.length > 4) {
                            int fadeIn = Integer.parseInt(titleSplit[2]);
                            int stay = Integer.parseInt(titleSplit[3]);
                            int fadeOut = Integer.parseInt(titleSplit[4]);
                            return new TitleAction(title, subtitle, fadeIn, stay, fadeOut);
                        }
                        return new TitleAction(title, subtitle, 20, 60, 20);
                    }
                    return new TitleAction(title, "", 20, 60, 20);
                }
                return new TitleAction("", "", 20, 60, 20);
            case WAIT:
                if (data != null) {
                    return new WaitAction(Long.parseLong(data));
                }
                break;
        }
        return null;
    }

    private static String @NotNull [] split(@NotNull String data, int limit) {
        String[] split = data.split(Common.NON_ESCAPED_SEMICOLON_REGEX, limit);
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].replace("\\;", ";");
        }
        return split;
    }

}
