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

package eu.decentsoftware.holograms;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a buffer for logging messages during the boot process.
 * <p>
 * The purpose of this class is to then send all the messages at once, after
 * the plugin has been enabled, to avoid having the messages mixed with other
 * plugin's messages.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class BootMessenger {

    private static final DecentHolograms PLUGIN = DecentHolograms.getInstance();
    private List<String> messages;

    /**
     * Add a message to the final output.
     *
     * @param message The message.
     * @since 3.0.0
     */
    public void log(@NotNull String message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }

    /**
     * Send the boot info and clear cache.
     *
     * @since 3.0.0
     */
    public void sendAndFinish() {
        PluginDescriptionFile desc = PLUGIN.getDescription();
        CommandSender console = Bukkit.getConsoleSender();
        console.sendMessage("――――――――――――――――――――――――――――――――――――――――――――――――――");
        console.sendMessage(Lang.formatString(" &fThank you for using &3{0}&f! &c❤", desc.getName()));
        console.sendMessage(Lang.formatString(" &f- Authors: &b[{0}]", String.join(", ", desc.getAuthors())));
        console.sendMessage(Lang.formatString(" &f- Version: &b{0}", desc.getVersion()));
        if (Config.isUpdateAvailable()) {
            console.sendMessage(Lang.formatString("   &a➥ A newer version ({0}) is available, update asap!", Config.getUpdateVersion()));
        }
        console.sendMessage(Lang.formatString(" &f- Spigot Page:&b https://www.spigotmc.org/resources/96927/"));
        console.sendMessage("――――――――――――――――――――――――――――――――――――――――――――――――――");
        for (String message : messages) {
            console.sendMessage(Lang.formatString(" &b" + message));
        }
        console.sendMessage("――――――――――――――――――――――――――――――――――――――――――――――――――");

        messages = null;
    }

}
