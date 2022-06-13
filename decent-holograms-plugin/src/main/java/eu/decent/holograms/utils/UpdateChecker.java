package eu.decent.holograms.utils;

import eu.decent.holograms.api.utils.Common;
import eu.decent.holograms.api.utils.S;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * This class is used to check for updates.
 */
public class UpdateChecker {

    private final String url;

    /**
     * Creates a new update checker.
     *
     * @param resourceId The resource id of the plugin.
     */
    public UpdateChecker(int resourceId) {
        this.url = "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId;
    }

    /**
     * Gets the latest version of the plugin from the Spigot API.
     *
     * @param consumer The consumer to call when the latest version is found.
     */
    public void check(final Consumer<String> consumer) {
        S.async(() -> {
            try (InputStream inputStream = new URL(url).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                Common.log(Level.WARNING, "Cannot look for updates: " + exception.getMessage());
            }
        });
    }

}