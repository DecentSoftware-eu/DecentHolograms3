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

package eu.decentsoftware.holograms.utils.watcher;

import eu.decentsoftware.holograms.DecentHolograms;
import eu.decentsoftware.holograms.api.event.DecentHologramsFileEvent;
import eu.decentsoftware.holograms.utils.Common;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Class used to watch certain folders for file changes.
 *
 * @author JesusChrist69
 * @since 3.0.0
 */
@UtilityClass
public class FileWatcher {

    @Getter
    private final Map<WatchKey, Path> WATCHED_FOLDERS = new HashMap<>();

    private final String PLUGIN_PATH = DecentHolograms.getInstance().getDataFolder().getAbsolutePath() + "/";

    private WatchService watchService = null;
    private Thread watchThread = null;

    /**
     * It takes a folder path, checks if it exists, and if it does, it adds it to the list of folders to watch
     *
     * @param folderPath The path to the folder you want to watch.
     */
    public void assignFolder(String folderPath) {
        String finalPath = folderPath;
        if (!finalPath.contains(PLUGIN_PATH)) {
            finalPath = PLUGIN_PATH + (finalPath.startsWith(File.separator) ? finalPath.substring(1) : finalPath);
        }
        if (!finalPath.endsWith(File.separator)) finalPath += File.separator;
        File f = new File(finalPath);
        f.mkdirs();

        if (!f.exists() || !f.isDirectory()) {
            throw new RuntimeException(Common.format("Folder could not be watched as it does not exist or does not belong to DecentHolograms. " +
                    "Initial Path: {0} Final Path: {1}", folderPath, finalPath));
        }

        if (watchService == null) {
            try {
                watchService = FileSystems.getDefault().newWatchService();
            } catch (Exception e) {
                throw new RuntimeException(Common.format("WatchService could not be initialised. Folder {0} will not be watched.", finalPath));
            }
        }

        try {
            Path p = Paths.get(finalPath);
            WatchKey key = p.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            WATCHED_FOLDERS.put(key, p);
        } catch (Exception e) {
            throw new RuntimeException(Common.format("Failed to start WatchService for folder {0}.", finalPath));
        }

        initThread();
    }

    /**
     * If the watch service is not null, close it. If the watch thread is not null and is alive, interrupt it.
     */
    public void close() {
        if (watchService != null) {
            try {
                watchService.close();
            } catch (Exception e) {
                throw new RuntimeException("Could not close WatchService.", e);
            }
        }
        if (watchThread != null && watchThread.isAlive()) {
            watchThread.interrupt();
        }
        WATCHED_FOLDERS.clear();
    }

    /**
     * It creates a new thread that watches the assigned folders for any changes to files
     */
    private void initThread() {
        if (watchThread == null || !watchThread.isAlive()) {
            watchThread = new Thread(() -> {
                try {
                    WatchKey key;
                    while (true) {
                        key = watchService.take();
                        if (key == null) continue;

                        Path directory = WATCHED_FOLDERS.get(key);
                        if (directory == null) {
                            // WatchKey was not recognized
                            continue;
                        }

                        for (WatchEvent<?> event : key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();

                            if (kind.equals(OVERFLOW)) {
                                continue;
                            }

                            @SuppressWarnings("unchecked")
                            WatchEvent<Path> wep = (WatchEvent<Path>) event;
                            String changedFile = wep.context().toFile().getAbsolutePath();
                            changedFile = changedFile.substring(changedFile.lastIndexOf(File.separator) + 1);

                            PluginManager pm = Bukkit.getPluginManager();

                            if (ENTRY_CREATE.equals(kind)) {
                                pm.callEvent(new DecentHologramsFileEvent(new File(PLUGIN_PATH + directory.toFile().getName() + File.separator + changedFile), DecentHologramsFileEvent.FileAction.CREATE));
                            } else if (ENTRY_DELETE.equals(kind)) {
                                pm.callEvent(new DecentHologramsFileEvent(new File(PLUGIN_PATH + directory.toFile().getName() + File.separator + changedFile), DecentHologramsFileEvent.FileAction.DELETE));
                            } else if (ENTRY_MODIFY.equals(kind)) {
                                pm.callEvent(new DecentHologramsFileEvent(new File(PLUGIN_PATH + directory.toFile().getName() + File.separator + changedFile), DecentHologramsFileEvent.FileAction.EDIT));
                            }
                        }

                        // remove if directory is no longer accessible
                        boolean valid = key.reset();
                        if (!valid) {
                            WATCHED_FOLDERS.remove(key);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("WatcherService thread was terminated due to unknown error.");
                }
            });
            watchThread.setDaemon(true);
            watchThread.setName("DecentHolograms-FileWatcher");
            watchThread.start();
        }
    }

}
