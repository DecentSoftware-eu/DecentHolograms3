package eu.decentsoftware.holograms.actions;

import eu.decentsoftware.holograms.actions.impl.SoundAction;
import eu.decentsoftware.holograms.actions.impl.StringAction;
import eu.decentsoftware.holograms.actions.impl.StringListAction;
import eu.decentsoftware.holograms.actions.impl.TeleportAction;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.config.ConfigUtils;
import eu.decentsoftware.holograms.api.exception.LocationParseException;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class represents an action, that can be executed for a specific profile.
 *
 * @author d0by
 * @since 3.0.0
 */
@Getter
public abstract class DefaultAction implements Action {

    protected final long delay;
    protected final double chance;

    /**
     * Create new {@link DefaultAction}.
     */
    public DefaultAction() {
        this(0, -1);
    }

    /**
     * Create new {@link DefaultAction}.
     *
     * @param delay Delay of this action.
     * @param chance Chance of this action.
     */
    public DefaultAction(long delay, double chance) {
        this.delay = delay;
        this.chance = chance;
    }

    /**
     * Check the chance of this action.
     *
     * @return Boolean whether this action should be executed.
     */
    public boolean checkChance() {
        final double chance = getChance();
        if (chance < 0 || chance > 100) {
            return true;
        }
        return (Math.random() * 100) < chance;
    }

    /**
     * Load an action from a configuration section.
     *
     * @param config The configuration section.
     * @return The action.
     */
    @Nullable
    public static Action load(@NotNull ConfigurationSection config) {
        if (!config.isString("type")) {
            return null;
        }

        // -- Get type of the action
        String typeName = config.getString("type", "");
        ActionType type = ActionType.fromString(typeName);
        if (type == null) {
            return null;
        }

        // -- Create the action if possible
        Action action = null;
        switch (type) {
            case SOUND:
            case BROADCAST_SOUND:
            case BROADCAST_SOUND_WORLD:
                String soundName = config.getString("sound");
                if (soundName != null) {
                    float volume = (float) config.getDouble("volume", 1.0f);
                    float pitch = (float) config.getDouble("pitch", 1.0f);
                    try {
                        Sound sound = Sound.valueOf(soundName.toUpperCase());
                        action = new SoundAction(type, sound, volume, pitch);
                    } catch (Exception ignored) {}
                }
                break;
            case MESSAGE:
            case BROADCAST:
                List<String> stringList = config.getStringList("message");
                if (!stringList.isEmpty()) {
                    action = new StringListAction(type, stringList);
                }
                break;
            case CHAT:
                String message = config.getString("message");
                if (message != null) {
                    action = new StringAction(type, message);
                }
                break;
            case COMMAND:
            case CONSOLE:
                String command = config.getString("command");
                if (command != null) {
                    action = new StringAction(type, command);
                }
                break;
            case CONNECT:
                String server = config.getString("server");
                if (server != null) {
                    action = new StringAction(type, server);
                }
                break;
            case TELEPORT:
                String locationString = config.getString("location");
                if (locationString != null) {
                    try {
                        Location location = ConfigUtils.stringToLoc(locationString);
                        action = new TeleportAction(location);
                    } catch (LocationParseException ignored) {}
                }
                break;
            default: break;
        }
        return action;
    }

}
