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

package eu.decentsoftware.holograms.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public class FileUtils {

    /**
     * Get the files in a tree, starting at the given root.
     *
     * @param root      The root file.
     * @param regex     The regex to match the file names.
     * @param createDir Whether to create the root directory if it doesn't exist.
     * @return The list of file.
     * @since 3.0.0
     */
    @NotNull
    public static List<File> getFilesFromTree(@NotNull File root, @Nullable String regex, boolean createDir) {
        List<File> files = new ArrayList<>();
        if (root.exists() && root.isDirectory()) {
            File[] children = root.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isDirectory()) {
                        files.addAll(getFilesFromTree(child, regex, createDir));
                    } else if (regex == null || regex.trim().isEmpty() || child.getName().matches(regex)) {
                        files.add(child);
                    }
                }
            }
        } else if (createDir && root.mkdirs()) {
            Common.log("Created directory %s", root.getPath());
        }
        return files;
    }

    /**
     * Get the files in a tree, starting at the given root.
     *
     * @param rootPath  The root file path.
     * @param regex     The regex to match the file names.
     * @param createDir Whether to create the root directory if it doesn't exist.
     * @return The list of files.
     * @since 3.0.0
     */
    @NotNull
    public static List<File> getFilesFromTree(@NotNull String rootPath, @Nullable String regex, boolean createDir) {
        return getFilesFromTree(new File(rootPath), regex, createDir);
    }

    /**
     * Get the relative path of a file to a base directory.
     *
     * @param file The file.
     * @param base The base directory.
     * @return The relative path or null if the file is not in the base directory.
     * @since 3.0.0
     */
    @Nullable
    public static String getRelativePath(@NotNull File file, @NotNull File base) {
        String filePath = file.getAbsolutePath();
        String basePath = base.getAbsolutePath();
        if (filePath.startsWith(basePath)) {
            return filePath.substring(basePath.length() + 1);
        }
        return null;
    }

}
