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

package eu.decentsoftware.holograms.api.addons;

import com.google.common.io.ByteStreams;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Interface that needs to be implemented when creating addons for DecentHolograms
 *
 * @author JesusChrist69
 * @since 3.0.0
 */
public interface DecentHologramsAddon {

    void onLoad();
    void onReload();
    void onUnload();

    // A default method that returns the plugin instance.
    default Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("DecentHolograms");
    };

    // A default method that registers a listener.
    default void registerListener(@NonNull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getPlugin());
    };

    // A default method that loads a file from the plugin's resources.
    default FileConfiguration loadFile(@NonNull String path, @NonNull String fileName) {
        Plugin instance = getPlugin();
        if (!instance.getDataFolder().exists()) {
            instance.getDataFolder().mkdirs();
        }
        File file = new File(path, fileName);
        if (!file.exists()) {
            try {
                if (fileName.contains("/")) {
                    String tempPath = file.getAbsolutePath();
                    tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
                    File dir = new File(tempPath);
                    dir.mkdirs();
                    file = new File(tempPath, fileName.substring(fileName.lastIndexOf("/")));
                }
                file.createNewFile();
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
                     OutputStream outputStream = Files.newOutputStream(file.toPath())) {
                    if (inputStream == null) {
                        throw new RuntimeException("File " + fileName + " is not in resources!");
                    }
                    ByteStreams.copy(inputStream, outputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("File " + path + "/" + fileName + " could not be loaded!");
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    };

}
