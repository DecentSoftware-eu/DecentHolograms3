package eu.decentsoftware.holograms.actions;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import eu.decentsoftware.holograms.actions.impl.*;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ActionParser;
import eu.decentsoftware.holograms.api.utils.config.ConfigUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class DefaultActionParser implements ActionParser {

    @Override
    public Action parse(@NotNull Section config) {
        String typeName = config.getString("type");
        if (typeName == null) {
            return null;
        }
        DefaultActionType type = DefaultActionType.fromString(typeName);
        if (type == null) {
            return null;
        }
        long delay = config.getLong("delay", 0L);
        double chance = config.getDouble("chance", -1.0D);

        switch (type) {
            case MESSAGE:
                return new MultiMessageAction(delay, chance, config.getStringList("message"));
            case MESSAGE_BROADCAST:
                return new MultiMessageBroadcastAction(delay, chance, config.getStringList("message"));
            case ACTION_BAR:
                return new ActionBarAction(delay, chance, config.getString("message", ""));
            case CHAT:
                return new ChatAction(delay, chance, config.getString("message", ""));
            case COMMAND:
                return new CommandAction(delay, chance, config.getString("command", ""));
            case CONNECT:
                return new ConnectAction(delay, chance, config.getString("server", ""));
            case CONSOLE:
                return new ConsoleAction(delay, chance, config.getString("command", ""));
            case SOUND:
                String sound = config.getString("sound");
                float volume = config.getFloat("volume", 1.0F);
                float pitch = config.getFloat("pitch", 1.0F);
                return new SoundAction(delay, chance, sound, volume, pitch);
            case SOUND_BROADCAST:
                sound = config.getString("sound");
                volume = config.getFloat("volume", 1.0F);
                pitch = config.getFloat("pitch", 1.0F);
                return new SoundBroadcastAction(delay, chance, sound, volume, pitch);
            case SOUND_BROADCAST_WORLD:
                sound = config.getString("sound");
                volume = config.getFloat("volume", 1.0F);
                pitch = config.getFloat("pitch", 1.0F);
                return new SoundBroadcastWorldAction(delay, chance, sound, volume, pitch);
            case TELEPORT:
                Location location = ConfigUtils.getLocation(config.getSection("location"));
                if (location != null) {
                    return new TeleportAction(delay, chance, location);
                }
            case TITLE:
                String title = config.getString("title");
                String subtitle = config.getString("subtitle");
                int fadeIn = config.getInt("fade-in", -1);
                int stay = config.getInt("stay", -1);
                int fadeOut = config.getInt("fade-out", -1);
                return new TitleAction(delay, chance, title, subtitle, fadeIn, stay, fadeOut);
        }
        return null;
    }

}
