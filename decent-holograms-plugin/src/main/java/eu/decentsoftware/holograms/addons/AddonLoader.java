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

package eu.decentsoftware.holograms.addons;


import eu.decentsoftware.holograms.api.addons.AddonNameDefinition;
import eu.decentsoftware.holograms.api.addons.DecentHologramsAddon;
import lombok.Getter;
import lombok.NonNull;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for addon loading and unloading.
 *
 * @author JesusChrist69
 * @since 3.0.0
 */
public class AddonLoader {

    private final String addonDirectory;
    @Getter
    private final Map<String, Class<?>> loadedClasses;

    public AddonLoader(@NonNull String addonDirectory) {
        this.addonDirectory = addonDirectory;
        this.loadedClasses = new HashMap<>();
    }

    /**
     * It loads the addon class using URLClassLoader, checks if it implements the DecentHologramsAddon interface,
     * instantiates the addon and calls its onLoad() method, and finally adds the addon class to the loadedClasses map
     *
     * @param addonFileName The name of the addon file.
     */
    public <T extends DecentHologramsAddon> void loadAddon(@NonNull String addonFileName) throws Exception {
        // Load the addon class using URLCLassLoader
        URL addonURL = new URL("file://" + addonDirectory + "/" + addonFileName);
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{addonURL}, getClass().getClassLoader())) {
            Class<?> addonClass = classLoader.loadClass(getAddonClassName(addonFileName));
            // Check if addon implements DHAddon interface
            if (!DecentHologramsAddon.class.isAssignableFrom(addonClass)) {
                throw new IllegalArgumentException("Addon " + addonFileName + " does not implement DecentHologramsAddon interface!");
            }

            // Instantiate the addon and call its init method
            @SuppressWarnings("unchecked")
            T addon = (T) addonClass.newInstance();
            addon.onLoad();

            // Add the addon class to the loadedClasses map
            AddonNameDefinition addonNameDefinitionAnnotation = addonClass.getAnnotation(AddonNameDefinition.class);
            loadedClasses.put(addonNameDefinitionAnnotation.value(), addonClass);
        }
    }

    /**
     * It unloads the addon by removing it from the loadedClasses map, calling the onUnload method, and then unloading the
     * addon class using the ClassLoader
     *
     * @param addonName The name of the addon to unload.
     */
    public void unloadAddon(String addonName) throws Exception {
        // Check if the addon is loaded
        if (!loadedClasses.containsKey(addonName)) {
            throw new IllegalArgumentException("Addon " + addonName + " is not loaded!");
        }

        // Get the addon class and instantiate it
        Class<?> addonClass = loadedClasses.get(addonName);
        DecentHologramsAddon addon = (DecentHologramsAddon) addonClass.newInstance();

        // Call onUnload method
        addon.onUnload();

        // Remove from loadedClasses map
        loadedClasses.remove(addonName);

        // Unload addon class using ClassLoader
        String addonFileName = addonClass.getName().replace('.', '/') + ".class";
        ClassLoader addonClassLoader = addonClass.getClassLoader();
        try (URLClassLoader urlClassLoader = (URLClassLoader) addonClassLoader) {
            URL url = urlClassLoader.findResource(addonFileName);
            Object classLoader = ClassLoader.class.getDeclaredMethod("getClassLoader", (Class<?>) null).invoke(urlClassLoader);
            Method method = classLoader.getClass().getDeclaredMethod("removeURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        }
    }

    /**
     * It loads all the addons in the addons directory
     */
    public void loadAllAddons() {
        File addonsDir = new File(addonDirectory);
        File[] addonFiles = addonsDir.listFiles((dir, name) -> name.endsWith(".jar"));
        if (addonFiles != null) {
            for (File addonFile : addonFiles) {
                try {
                    loadAddon(addonFile.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * It unloads all addons
     */
    public void unloadAllAddons() {
        for (String addonName : loadedClasses.keySet()) {
            try {
                unloadAddon(addonName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Unload all addons, then load all addons.
     */
    public void reload() {
        unloadAllAddons();
        loadAllAddons();
    }

    /**
     * It takes a string that represents a file name, and returns a string that represents a class name
     *
     * @param addonFileName The name of the addon file.
     * @return The class name of the addon.
     */
    private String getAddonClassName(String addonFileName) {
        return addonFileName.substring(0, addonFileName.length() - 4).replace('/', '.');
    }

}
