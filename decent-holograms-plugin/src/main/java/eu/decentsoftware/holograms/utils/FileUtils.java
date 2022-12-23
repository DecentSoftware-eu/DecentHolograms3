package eu.decentsoftware.holograms.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for working with files.
 *
 * @author d0by
 * @since 3.0.0
 */
@UtilityClass
public final class FileUtils {

    /**
     * Get all files in a tree, starting at the given root.
     *
     * @param root The root file.
     * @return The list of files.
     */
    @NotNull
    public static List<File> getFilesFromTree(@NotNull File root) {
        return getFilesFromTree(root, null);
    }

    /**
     * Get all files in a tree, starting at the given root.
     *
     * @param root The root file.
     * @return The list of files.
     */
    @NotNull
    public static List<File> getFilesFromTree(@NotNull File root, @Nullable FileFilter fileFilter) {
        if (!root.isDirectory()) {
            return fileFilter != null && fileFilter.accept(root) ? Collections.singletonList(root) : Collections.emptyList();
        }
        List<File> files = new ArrayList<>();
        File[] listFiles = root.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (fileFilter != null && !fileFilter.accept(file)) {
                    continue;
                }
                if (file.isDirectory()) {
                    files.addAll(getFilesFromTree(file));
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }

}
