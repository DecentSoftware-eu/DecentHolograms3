package eu.decentsoftware.holograms;

import eu.decentsoftware.holograms.api.DecentHolograms;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class BootProcess {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private static List<String> messages;

    /**
     * Add a message to the final output.
     *
     * @param message The message.
     */
    public void log(@NotNull String message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }

    /**
     * Send the boot info and clear cache.
     */
    public void sendAndFinish() {
        PluginDescriptionFile desc = PLUGIN.getDescription();
        String latestVersion = ""; // TODO

        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage("――――――――――――――――――――――――――――――――――――――――――――――――――");
        console.sendMessage(Lang.formatString(" &fThank you for using &3%s&f! &c❤", null, desc.getName()));
        console.sendMessage("");
        console.sendMessage(Lang.formatString(" &f- Authors: &b[%s]", null, String.join(", ", desc.getAuthors())));
        console.sendMessage(Lang.formatString(" &f- Version: &b%s", null, desc.getVersion()));
        if (Config.isUpdateAvailable()) {
            console.sendMessage(Lang.formatString("   &a➥ A newer version (%s) is available, update asap!", null, latestVersion));
        }
        console.sendMessage(Lang.formatString(" &f- Spigot Page: &bhttps://www.spigotmc.org/resources/96927/"));
        console.sendMessage("――――――――――――――――――――――――――――――――――――――――――――――――――");
        for (String message : messages) {
            console.sendMessage(Lang.formatString(" &a" + message));
        }
        console.sendMessage("――――――――――――――――――――――――――――――――――――――――――――――――――");

        messages = null;
    }

}
